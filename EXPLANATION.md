# Задачи 5–7: PostgreSQL + Spring Data JPA

## Задача 5 — Миграция с MongoDB на PostgreSQL

### Что изменилось

MongoDB хранит документы без схемы. PostgreSQL — реляционная база с таблицами и типами.
Вся структура данных осталась той же, поменялись аннотации и репозитории.

### Сущности: было → стало

**MongoDB:**
```java
@Document(collection = "doctors")
public class Doctor {
    @Id
    private String id;
    private String firstName;
    ...
}
```

**PostgreSQL:**
```java
@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String firstName;
    ...
}
```

- `@Document` → `@Entity` + `@Table` — теперь это JPA-сущность
- `@GeneratedValue(strategy = GenerationType.UUID)` — база сама генерирует UUID при вставке

### Репозитории: было → стало

**MongoDB:**
```java
public interface DoctorRepository extends MongoRepository<Doctor, String> { ... }
```

**PostgreSQL:**
```java
public interface DoctorRepository extends JpaRepository<Doctor, String> { ... }
```

`JpaRepository` предоставляет те же базовые методы (`findById`, `save`, `delete` и т.д.), но работает через Hibernate/JPA с SQL.

### Конфигурация (application.yaml)

```yaml
spring:
  datasource:
    url: ${POSTGRES_URL:jdbc:postgresql://localhost:5432/hospital}
    username: ${POSTGRES_USER:hospital}
    password: ${POSTGRES_PASSWORD:hospital}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

- `ddl-auto: update` — Hibernate автоматически создаёт/обновляет таблицы при старте
- `show-sql: true` — все SQL запросы видны в логах

### Docker Compose

```yaml
services:
  postgres:
    image: postgres:17
    environment:
      POSTGRES_DB: hospital
      POSTGRES_USER: hospital
      POSTGRES_PASSWORD: hospital
    ports:
      - "5432:5432"

  app:
    build: .
    environment:
      POSTGRES_URL: jdbc:postgresql://postgres:5432/hospital
      POSTGRES_USER: hospital
      POSTGRES_PASSWORD: hospital
    depends_on:
      postgres:
        condition: service_healthy
```

Приложение запускается только после того, как PostgreSQL прошёл healthcheck.

---

## Задача 6 — Массовая загрузка данных (Bulk Load)

### Цель

Загрузить 10 000 врачей / пациентов / болезней одним REST-запросом для тестирования производительности.

### REST API

```
POST /doctors/bulk
POST /patients/bulk
POST /illnesses/bulk
```

Тело запроса (одинаковое для всех):
```json
{
  "namePrefix": "bulk-doctor",
  "startIndex": 0,
  "count": 10000
}
```

### Реализация (пример для врачей)

**Контроллер:**
```java
@PostMapping("/bulk")
public BulkOperationResultDto createDoctors(@RequestBody BulkCreateRequest request) {
    long processed = doctorService.createDoctors(
        request.getNamePrefix(), request.getStartIndex(), request.getCount()
    );
    return BulkOperationResultDto.builder()
            .requested(request.getCount())
            .processed(processed)
            .build();
}
```

**Сервис хранения:**
```java
public long createDoctors(String namePrefix, long startIndex, int count) {
    List<Doctor> doctors = new ArrayList<>();
    for (long i = startIndex; i < startIndex + count; i++) {
        doctors.add(Doctor.builder()
                .firstName(namePrefix + "-" + i)
                .lastName("Lastname-" + i)
                .specialization("Spec-" + (i % 5))
                .build());
    }
    return doctorRepository.saveAll(doctors).size();
}
```

`saveAll()` отправляет один батч в базу вместо N отдельных INSERT — это намного быстрее.

### Внешний клиент HospitalRestLoadClient

Клиент вне Spring — обычный Java-класс с `main()`. Запускается через Gradle:

```bash
./gradlew runHospitalRestLoadClient --args="--entity=doctors --count=10000 --threads=4 --batch-size=1000"
```

Параметры:
- `--entity` — что загружать: `doctors`, `patients`, `illnesses`
- `--count` — сколько записей всего
- `--threads` — число потоков (параллельная загрузка)
- `--batch-size` — сколько записей в одном запросе

При `--threads > 1` используется `ExecutorService` и `AtomicLong` для счётчика без гонок:

```java
ExecutorService executor = Executors.newFixedThreadPool(threads);
AtomicLong totalProcessed = new AtomicLong(0);

for (int t = 0; t < threads; t++) {
    final long start = startIndex + (long) t * batchesPerThread * batchSize;
    executor.submit(() -> {
        // отправляет несколько батчей через HTTP
    });
}
executor.shutdown();
executor.awaitTermination(10, TimeUnit.MINUTES);
```

---

## Задача 7 — Оптимизированные запросы

### 7.1 Выборка по условию — фильтр по специализации

```java
// DoctorRepository
List<Doctor> findBySpecialization(String specialization);
```

Spring Data генерирует SQL автоматически из имени метода:
```sql
SELECT * FROM doctors WHERE specialization = ?
```

Использование:
```
GET /doctors?specialization=Терапевт
```

### 7.2 Выборка с сортировкой — врачи по фамилии А-Я

```java
// DoctorRepository
List<Doctor> findAllByOrderByLastNameAsc();
```

Генерирует:
```sql
SELECT * FROM doctors ORDER BY last_name ASC
```

Использование:
```
GET /doctors?sort=lastName
```

### 7.3 JOIN через нативный SQL — медкарты с деталями

Проблема: стандартный JPQL не поддерживает `COALESCE` в `nativeQuery = true` с маппингом в класс.
Решение: **interface-based projection** — интерфейс с геттерами, Spring Data сам его реализует.

```java
// Интерфейс проекции
public interface MedicalCardDetailsDto {
    String getId();
    String getDiagnosis();
    String getDoctorName();
    String getPatientName();
    String getIllnessName();
}
```

```java
// Репозиторий
@Query(value = """
        SELECT mc.id,
               mc.diagnosis,
               COALESCE(d.first_name || ' ' || d.last_name, 'Не назначен') AS doctorName,
               p.first_name || ' ' || p.last_name AS patientName,
               i.name AS illnessName
        FROM medical_cards mc
        LEFT JOIN doctors d ON mc.doctor_id = d.id
        LEFT JOIN patients p ON mc.patient_id = p.id
        LEFT JOIN illnesses i ON mc.illness_id = i.id
        """, nativeQuery = true)
List<MedicalCardDetailsDto> findAllWithDetails();
```

- `LEFT JOIN` — медкарта без врача не пропадёт, поле будет `"Не назначен"`
- `COALESCE` — если `doctor_id = null`, подставляет строку `"Не назначен"`
- Псевдонимы в SQL (`AS doctorName`) должны совпадать с именами геттеров интерфейса

Использование:
```
GET /medical-cards/details
```

### 7.4 Агрегация — количество врачей по специализации

```java
// DoctorStatsDto — обычный класс с @AllArgsConstructor
@Data
@AllArgsConstructor
public class DoctorStatsDto {
    private String specialization;
    private Long count;
}
```

```java
// DoctorRepository — JPQL с конструктором
@Query("SELECT new ru.vsu.hospital.model.dto.DoctorStatsDto(d.specialization, COUNT(d)) " +
       "FROM Doctor d GROUP BY d.specialization ORDER BY COUNT(d) DESC")
List<DoctorStatsDto> getStatsBySpecialization();
```

- `new ru.vsu.hospital.model.dto.DoctorStatsDto(...)` — JPQL вызывает конструктор напрямую
- `GROUP BY` + `COUNT` — считает, сколько врачей в каждой специализации
- `ORDER BY COUNT(d) DESC` — самые популярные специализации первыми

Использование:
```
GET /doctors/stats
```

---

## Архитектура слоёв

```
Controller
    ↓ (DoctorDto / request)
BusinessService (interface + impl)
    ↓ (DoctorDto / request)
StorageService (interface + impl)
    ↓ (Doctor entity)
Repository (JpaRepository)
    ↓ SQL
PostgreSQL
```

Каждый слой изолирован: контроллер не знает про репозиторий, репозиторий не знает про DTO.

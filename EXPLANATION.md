# Hospital Web — объяснение проекта

## Что делает приложение

REST API для управления больницей. Хранит данные в MongoDB, запущенной в Docker-контейнере.
Четыре коллекции: врачи, пациенты, болезни, медицинские карты.

---

## 1. Запуск через Docker

**docker-compose.yml** поднимает три контейнера:

```yaml
services:
  mongo:        # база данных MongoDB 8.0
  mongo-express: # визуальный интерфейс для просмотра данных (localhost:8081)
  app:          # само Spring Boot приложение (localhost:8080)
```

MongoDB стартует первой (healthcheck), приложение ждёт пока она будет готова (`depends_on: condition: service_healthy`).

---

## 2. Начальные данные — mongo-init/init.js

Скрипт запускается автоматически при первом старте MongoDB.

```js
db = db.getSiblingDB('hospital');

// Создание коллекций
db.createCollection('doctors');
db.createCollection('patients');
db.createCollection('illnesses');
db.createCollection('medical_cards');
```

Создаёт коллекции и сразу вставляет начальные данные: 3 врача, 3 болезни, 3 пациента, 2 медкарты.

---

## 3. Индексы

### Полнотекстовый индекс на Illness.description

```js
db.illnesses.createIndex(
    { description: "text" },
    { name: "illness_description_text" }
);
```

Позволяет искать болезни по ключевым словам в описании:
```
GET /illnesses/search?text=дыхательных
```
MongoDB ищет слово во всех описаниях и возвращает совпадения. Обычный индекс так не умеет.

В Java-сущности поле помечено аннотацией:
```java
@TextIndexed
private String description;
```

### TTL индекс на MedicalCard.expireAt

```js
db.medical_cards.createIndex(
    { expireAt: 1 },
    { expireAfterSeconds: 0, sparse: true }
);
```

TTL (Time To Live) — MongoDB автоматически удаляет документ когда наступает время в поле `expireAt`.

- `expireAt: null` — пациент ещё болеет, карточка живёт
- `expireAt: new Date()` — пациент выздоровел, MongoDB удалит карточку в течение ~60 секунд

Вызывается через эндпоинт:
```
POST /medical-cards/{id}/recover
```

```java
// MedicalCardStorageServiceImpl.java
public UpdateResult markAsRecovered(String medicalCardId) {
    return mongoTemplate.updateFirst(
        Query.query(Criteria.where("_id").is(medicalCardId)),
        Update.update("expireAt", new Date()),
        MedicalCard.class
    );
}
```

---

## 4. Сущности

### Doctor
```java
@Document("doctors")
public class Doctor {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String specialization;
}
```
Простая сущность. Хранится в коллекции `doctors`.

### Patient
```java
@Document("patients")
public class Patient {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String medicalCardId; // ссылка на медкарту
}
```
Содержит `medicalCardId` — обратная ссылка на медицинскую карту пациента.

### Illness
```java
@Document("illnesses")
public class Illness {
    @Id
    private String id;
    private String name;
    @TextIndexed
    private String description; // по этому полю работает полнотекстовый поиск
    private String severity;    // MILD / MODERATE / SEVERE
}
```

### MedicalCard
```java
@Document("medical_cards")
public class MedicalCard {
    @Id
    private String id;
    private String patientId;  // кто болеет
    private String doctorId;   // кто лечит
    private String illnessId;  // чем болеет
    private String diagnosis;
    private Date treatmentStart;
    private Date expireAt;     // null = болеет, new Date() = выздоровел → TTL удалит
}
```

MedicalCard — сущность-связка: объединяет пациента, врача и болезнь в один документ.

---

## 5. Архитектура

```
Controller
    ↓
Business Service (interface + impl)
    ↓
Storage Service (interface + impl)
    ↓
Repository (MongoRepository)
    ↓
MongoDB
```

Каждый слой имеет свою зону ответственности:
- **Controller** — принимает HTTP запросы, возвращает ответы
- **Business Service** — бизнес-логика (например: при создании медкарты назначить id карты пациенту)
- **Storage Service** — работа с базой данных, использует Repository и MongoTemplate
- **Repository** — стандартные CRUD операции через Spring Data

**DTO и маппинг через MapStruct:**
Контроллер никогда не отдаёт сущность напрямую — только DTO. MapStruct автоматически генерирует код преобразования.

```java
@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDto toDto(Doctor doctor);
    Doctor toEntity(DoctorDto doctorDto);
}
```

---

## 6. REST эндпоинты

### Doctor, Patient, Illness — стандартный CRUD
| Метод | URL | Действие |
|---|---|---|
| GET | /doctors | все врачи |
| GET | /doctors/{id} | врач по id |
| POST | /doctors | создать врача |
| PUT | /doctors | обновить врача |
| DELETE | /doctors/{id} | удалить врача |

То же самое для `/patients` и `/illnesses`.

### Illness — дополнительно
| Метод | URL | Действие |
|---|---|---|
| GET | /illnesses/search?text=... | полнотекстовый поиск |

### MedicalCard — дополнительно
| Метод | URL | Действие |
|---|---|---|
| POST | /medical-cards/{id}/doctors | назначить врача |
| DELETE | /medical-cards/{id}/doctors | убрать врача |
| POST | /medical-cards/{id}/illnesses | сменить болезнь |
| POST | /medical-cards/{id}/recover | пациент выздоровел (запускает TTL) |

---

## 7. Конфигурация MongoDB

```java
@Configuration
public class MongoConfig {
    @Bean
    public ApplicationRunner indexInitializer(MongoTemplate mongoTemplate) {
        return args -> {
            // Программно создаём TTL индекс при старте приложения
            mongoTemplate.indexOps(MedicalCard.class)
                    .ensureIndex(new Index().on("expireAt", Sort.Direction.ASC).expire(0));
        };
    }
}
```

`ensureIndex` — идемпотентный вызов: если индекс уже есть (создан через init.js), ничего не происходит. Текстовый индекс на `Illness` создаётся только через init.js, потому что MongoDB допускает лишь один текстовый индекс на коллекцию.

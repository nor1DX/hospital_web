# Сценарий демонстрации (postgres branch)

## Запуск

```bash
docker compose up --build
```

Ждёшь пока в логах появится `Started HospitalApplication`.

---

## Шаг 1 — Создать данные

Открываешь `requests.connekt.kts` и выполняешь по очереди:

1. `POST /doctors` — создать двух врачей (Петров Терапевт + Сидорова Кардиолог)
2. `POST /patients` — создать пациента (Смирнов)
3. `POST /illnesses` — создать болезнь (ОРВИ)
4. `POST /medical-cards` — создать медкарту (вставить id пациента и болезни из предыдущих ответов)
5. `POST /medical-cards/{id}/doctors` — назначить врача на карту

---

## Шаг 2 — Задача 5: CRUD через JPA

```
GET /doctors          → список всех врачей
GET /patients         → список всех пациентов
GET /illnesses        → список всех болезней
GET /medical-cards    → список всех медкарт
```

Показывает что данные сохраняются в PostgreSQL.

---

## Шаг 3 — Задача 6: Bulk Load

```
POST /doctors/bulk    → загрузить 10000 врачей
POST /patients/bulk   → загрузить 10000 пациентов
POST /illnesses/bulk  → загрузить 10000 болезней
```

Ответ покажет `{"requested": 10000, "processed": 10000}`.

После этого:
```
GET /doctors          → видно что их теперь много
```

---

## Шаг 4 — Задача 7: Оптимизированные запросы

```
GET /doctors?specialization=Терапевт   → фильтр по специализации
GET /doctors?sort=lastName              → сортировка А-Я по фамилии
GET /medical-cards/details             → JOIN: карта + врач + пациент + болезнь
GET /doctors/stats                     → агрегация: количество врачей по специализации
```

В `/medical-cards/details` для карты без врача появится `"doctorName": "Не назначен"`.
В `/doctors/stats` увидишь что `Spec-0..Spec-4` — по 2000 врачей каждая.

---

## Шаг 5 — Тесты

```bash
./gradlew test
```

В выводе видны `println`-логи каждого теста — что именно проверялось и какой результат.

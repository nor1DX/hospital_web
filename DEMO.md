# Сценарий демонстрации

## Шаг 1 — Запуск
```
docker-compose up --build
```
Показать что все 3 контейнера поднялись (mongo, mongo-express, app).

---

## Шаг 2 — Показать начальные данные
Открыть **http://localhost:8081** (Mongo Express).
Зайти в базу `hospital` → показать 4 коллекции с данными от init.js.

---

## Шаг 3 — CRUD запросы (requests.http)

**Врачи:**
1. `GET /doctors` — показать список
2. `POST /doctors` — создать нового врача → обновить Mongo Express → видно новый документ
3. `DELETE /doctors/{id}` → обновить Mongo Express → документ удалён

**Пациенты:**
4. `GET /patients` — показать список
5. `POST /patients` — создать → видно в Mongo Express

**Болезни:**
6. `GET /illnesses` — показать список
7. `GET /illnesses/search?text=дыхательных` — **полнотекстовый поиск**, вернёт ОРВИ и Пневмонию

**Медкарты:**
8. `GET /medical-cards` — показать список
9. `POST /medical-cards` — создать карту для пациента
10. `POST /medical-cards/{id}/doctors` — назначить врача

---

## Шаг 4 — TTL индекс (самое важное)

1. Открыть Mongo Express → коллекция `medical_cards` → показать карту (expireAt: null)
2. Выполнить `POST /medical-cards/{id}/recover`
3. Показать ответ — в поле `expireAt` теперь стоит дата
4. Подождать **~60 секунд**
5. Обновить Mongo Express — карточка **исчезла сама**

> MongoDB автоматически удалила документ по TTL индексу — это и есть механизм исключения выздоровевших пациентов.

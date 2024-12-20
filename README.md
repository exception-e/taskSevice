# TaskService

Сервис распределенного выполнения задач. 

## Принцип работы

### Получение задачи

Сервис получает сообщения из Kafka-топика в виде JSON:
```json
{
  "name": "task_name",
  "duration": 5000
}
```
где:
  - `name` - уникальное имя задачи (выполняет роль ключа идемпотентности);
  - `duration` - условное время выполнения задачи в мс;

Объект Dto десериализуется в `Task`-объект и валидируется по правилам:
  - имя задачи не может быть пустым;
  - время выполнения задача должно быть > 0 и < 10 секунд;

### Сохранение в базу данных

Задача сохраняется в БД со статусом `NEW`.
Возможные статусы:
  - `NEW` - новая созданная задача;
  - `PROCESSING` - задача обрабатывается одним из worker-ов;
  - `DONE` - задача успешно обработана;

### Передача задачи в worker pool

Приложение запускает TaskPoller-thread, который пробует получить из БД готовую к выполнению задачу.
Poller получает `NEW` / `PROCESSING` задачу с сортировкой по `created_at`.
В случае успеха, задача добавляется в синхронную очередь для ThreadPool, где её обрабатывает один из свободных worker-ов.

Статус задачи меняется на `PROCESSING`.
Если по какой-то причине задача зависнет в этом статусе, poller сможет её подобрать через 10 секунд (принятое максимальное время выполнения задачи).

### Обработка задачи worker-ом

Dummy-worker вызывает `Thread.sleep` и обновляет статус задачи в `DONE`.

### Получение статуса асинхронного выполнения задачи

Статус задачи можно получить через REST-интерфейс:

```
http://localhost:8080/rest/tasks/{taskName}
```

## Как запустить

Ввиду временных ограничений, не получилось полностью автоматизировать настройку всех зависимостей.

1. Docker compose

```bash
docker compose up
```

Будут подняты контейнеры: Kafka, Kafka-UI и PostgreSQL.

2. Для Kafka внести измененениия в файл /etc/hosts

Необходимо добавить в /etc/hosts:
`kafka 127.0.0.1`

3. Создание топика Kafka

Через [Kafka UI](http://localhost:9999) создать топик `tasks` (partitions=1, replicationFactor=1).

4. Создание схемы БД

Подключиться к `tasks-db` БД (уже создана Docker) по localhost:5433 (postgres/postgres) и создать схему:

```SQL
CREATE TYPE status AS ENUM ('NEW', 'PROCESSING', 'DONE');

CREATE TABLE tasks
(
    name        VARCHAR(128) PRIMARY KEY,
    duration_ms INTEGER   NOT NULL,
    status      status  NOT NULL DEFAULT 'NEW'::status,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX new_or_handled_tasks ON tasks (status) WHERE status IN ('NEW', 'PROCESSING');
```

5. Запустить приложение

## Технологии
- [SpringBoot](https://spring.io/projects/spring-boot/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa/)
- [PostgreSQL](https://www.postgresql.org/)
- [Kafka](https://kafka.apache.org/)
- REST API
- [Swagger](https://swagger.io/)

## TODO

1. Сборка и запуск приложения через Docker, включить сервис в Compose.
2. Автоматическое создание Kafka топика.
3. Добавить функционал миграций PSQL через Liquibase.
4. Отправка невалидных сообщений из Kafka в DLQ (dead letter queue) для последующего ручного разбора.
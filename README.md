# TaskService
Сервис распределенного выполнения задач. 

Сервис получает сообщения из топика Kafka и сохраняет в базу данных. При появлении в базе задач со статусом "New" 
или находящихся в статусе "Processing" больше 10 секунд, запускается обработка таких задач пулом воркеров. 
Статус задачи можно получить по REST-endpoint:

 [http://localhost:8080/rest/tasks/Task_name](http://localhost:8080/rest/tasks/Task_name)


## Технологии
- [SpringBoot](https://spring.io/projects/spring-boot/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa/)
- [PostgreSQL](https://www.postgresql.org/)
- [Kafka](https://kafka.apache.org/)
- REST API
- [Swagger](https://swagger.io/)

## Использование
В приложении использует БД postgreSQL. Для создания схемы данных запустите, пожалуйста, скрипт init.sql.
Чтобы задать настроить количество воркеров, нужно задать параметр poolSize в файле application.yaml.
Там же находятся настройки соединения с базой данных и брокером сообщений.



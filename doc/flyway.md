# Flyway

Since the database is live now, we need a tool to manage database changes.

Flyway is a tool to manage these kind of changes (see
[Spring.io](https://docs.spring.io/spring-boot/how-to/data-initialization.html))
for more information.

Support for generating scripts can be found [here](https://blog.jetbrains.com/idea/2024/11/how-to-use-flyway-for-database-migrations-in-spring-boot-applications/).

## Project Setup
* Update scripts can be found here: [src/main/resources/db/migration](../src/main/resources/db/migration)
* also check if there is confugration in application.yml

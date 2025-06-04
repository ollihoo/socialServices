# Flyway

Since the database is live now, we need a tool to manage database changes.

Flyway is a tool to manage these kind of changes (see
[Spring.io](https://docs.spring.io/spring-boot/how-to/data-initialization.html))
for more information.

Support for generating scripts can be found [here](https://blog.jetbrains.com/idea/2024/11/how-to-use-flyway-for-database-migrations-in-spring-boot-applications/).

## Notice
There seems to be a problem for IntelliJ to work with sqlite databases. Means: if we want to work with
FLyway migration tools by IntelliJ we need to change databse, i.e. to postgres.

## Project Setup
* Update scripts can be found here: [src/main/resources/db/migration](../src/main/resources/db/migration)
* also check if there is confugration in application.yml

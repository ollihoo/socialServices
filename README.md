# Social Services online

Build an application that shows services in Berlin (and anywhere else) as easy and supportive as possible

## Database
I use postgres in a docker container. First step:

    cp env.template .env

Second step: fill in correct data for your personal database. Do…

    docker-compose up -d

to start database.

## Backend
* Spring Boot with RestService
* Data are stored statically at the moment, it's the easiest way…
* … but I use a database to simplify searches
* /social delivers a list of saved locations
* /catefory delivers a list of categories used for locations
* I am using actuator for some simple monitoring issues

To get application running:

    cp application.properties.template application.properties

Do not forget to check, if your env vars are set correctly.

I centralize important properties in .env file. This can be used by docker,
frontend and backend.

Unfortunately, IntelliJ doesn't include .env automatically. I decided to use a plug-in for
this problem:

    https://plugins.jetbrains.com/plugin/7861-envfile

TO use this plugin, go to "Edit Configurations…" and to the start of your application. See this
example:

![IntelliJ configuration with env](./doc/applicationConfigurationIntelliJ.png)

## Frontend:
* next.js - see [Documentation](doc/nextjsapp.md)
* React - see [Documentation](doc/react.md)

## Monitoring

    http://localhost:8080/actuator/health
    http://localhost:8080/actuator/metrics
Is activated, it's the easiest way to see, if metric does work and how long this app needs. I don't know when this
application fails with this in-memory-approach, so it's better to measure as soon as possible.

There is no problem with 50 entries ;-)
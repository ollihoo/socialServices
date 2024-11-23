# Social Services online

Build an application that shows services in Berlin (and anywhere else) as easy and supportive as possible

## Backend:
* Spring Boot with RestService
* Data are stored statically at the moment, it's the easiest way…
* … but I use a database to simplify searches
* /social delivers a list of saved locations
* I am using actuator for some simple monitoring issues

### Database installation
I use postgres i a docker container. First step:

    cp env.template .env

Second step: fill in correct data for your personal database
Do
    docker-compose up -d

to start database.

## Frontend:
* next.js - see [Documentation](doc/nextjsapp.md)
* React - see [Documentation](doc/react.md)

### Monitoring

    http://localhost:8080/actuator/health

    http://localhost:8080/actuator/metrics
Is activated, it's the easiest way to see, if metric does work and how long this app needs. I don't know when this
application fails with this in-memory-approach, so it's better to measure as soon as possible.

There is no problem with 50 entries ;-)
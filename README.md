# Social Services online

Build an application that shows services in Berlin (and anywhere else) as easy and supportive as possible

## First steps
Make a copy of env.template with the most important env vars:

    cp env.template .env

To get backend application running:

    cp application.properties.template application.properties

With

    docker-compose up -d

you can see interaction between back- and frontend:

    http://localhost:3100

I centralize  properties in .env file. This can be used by docker and backend.

Unfortunately, IntelliJ doesn't include .env automatically. I decided to use a plug-in for
this problem:

    https://plugins.jetbrains.com/plugin/7861-envfile

To use this plugin, go to "Edit Configurations…" and to the start of your application. See this
example:

![IntelliJ configuration with env](./doc/applicationConfigurationIntelliJ.png)

## Backend
* Spring Boot with RestService
* Data are stored statically at the moment, it's the easiest way…
* … but I use a database to simplify searches
* /social delivers a list of saved locations
* /category delivers a list of categories used for locations
* I am using actuator for some simple monitoring issues

### Monitoring

    http://localhost:8088/actuator/metrics

Is activated, it's the easiest way to see, if metric does work and how long this app needs. I don't know when this
application fails with this in-memory-approach, so it's better to measure as soon as possible.

There is no problem with 50 entries ;-)
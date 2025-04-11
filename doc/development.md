# Development environment

## Enivronment
* Spring Boot with RestService
* Data input is a csv that is filled with Google Forms. The output can be downloaded as tsv.
* At the moment, this project uses SQLite as database
* I am using actuator for some simple monitoring issues
* swagger is installed

## The API

* see Swagger-UI: http://localhost:8080/swagger-ui/index.html
* /social delivers a list of saved locations
* /category delivers a list of categories used for locations

## Working with .env
This project uses centralized properties in .env file. This can be used by docker and backend.

Unfortunately, IntelliJ doesn't include .env automatically. I decided to use a plug-in for
this problem:

    https://plugins.jetbrains.com/plugin/7861-envfile

To use this plugin, go to "Edit Configurations…" and to the start of your application. See this
example:

![IntelliJ configuration with env](./applicationConfigurationIntelliJ.png)

## CI/CD
The project uses github actions for automatic deployment.

In the github project, there are some secret variables defined in section

    Settings -> Secrets and vartiables -> Actions

The workflows themselves are saved [here](../.github/workflows)

### How to filter the latest tag

    curl https://hub.docker.com/v2/repositories/ollihoo/socialservice_backend/tags/ | jq '.results[] | .name' | sort -r

### docker
This workflow is automated in script [create_release_with_docker.sh](../create_release_with_docker.sh).

It builds to images on main, that are compatible with linux and MacOS. Look at this script for details. 

## Database
To update the database, this prject uses [Flyway](./flyway.md).

## Monitoring

    http://localhost:8088/actuator/metrics

is activated, it's the easiest way to see, if metric does work and how long this app needs. I don't know when this
application fails with this in-memory-approach, so it's better to measure as soon as possible.

To verify that API doc works: http://localhost:8080/v3/api-docs

In the future, we can add an influxdb instance to make metrics visible. 

## More Information

### OAS 3.0 and API documentation
* https://springdoc.org/
* [Compatability Mode](https://springdoc.org/#what-is-the-compatibility-matrix-of-springdoc-openapi-with-spring-boot)






KRkpSQuUfskkUOSG3X6VGEJnJfauVrPx5Wn9YU1_qGglJsIhGuxME8hNWJ3VdoCLn-XWlxgeDbq4IPPwLPbl-w==
# Development environment

* Spring Boot with RestService
* Data are stored statically at the moment, it's the easiest way…
* … but I use a database to simplify searches
* /social delivers a list of saved locations
* /category delivers a list of categories used for locations
* I am using actuator for some simple monitoring issues

## Working with .env
I've centralized properties in .env file. This can be used by docker and backend.

Unfortunately, IntelliJ doesn't include .env automatically. I decided to use a plug-in for
this problem:

    https://plugins.jetbrains.com/plugin/7861-envfile

To use this plugin, go to "Edit Configurations…" and to the start of your application. See this
example:

![IntelliJ configuration with env](./applicationConfigurationIntelliJ.png)

## CI/CD
I use github actions for automatic deployment.

In the github project online , all variables are defined in section

    Settings -> Secrets and vartiables -> Actions

The workflows themselves are saved [here](../.github/workflows)

### How to filter the latest tag

    curl https://hub.docker.com/v2/repositories/ollihoo/socialservice_backend/tags/ | jq '.results[] | .name' | sort -r

## docker
This workflow is automated in script [create_release_with_docker.sh](../create_release_with_docker.sh).

On MacOS:
Don't forget to use the right platform when you work on MacOS:

    docker buildx build --platform=linux/amd64 -t ${DOCKER_USER}/socialservice_backend:${IMAGE_TAG} .


## Database
See...
* [Flyway](./flyway.md)

## Monitoring

    http://localhost:8088/actuator/metrics

is activated, it's the easiest way to see, if metric does work and how long this app needs. I don't know when this
application fails with this in-memory-approach, so it's better to measure as soon as possible.


## More Information

### OAS 3.0 and API documentation
* https://springdoc.org/
* [Compatability Mode](https://springdoc.org/#what-is-the-compatibility-matrix-of-springdoc-openapi-with-spring-boot)
* to verify that API doc works: http://localhost:8080/v3/api-docs
* Swagger-UI: http://localhost:8080/swagger-ui/index.html
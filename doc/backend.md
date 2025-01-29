# Technical Documentation

* Spring Boot with RestService
* Data are stored statically at the moment, it's the easiest way…
* … but I use a database to simplify searches
* /social delivers a list of saved locations
* /category delivers a list of categories used for locations
* I am using actuator for some simple monitoring issues

# Monitoring

    http://localhost:8088/actuator/metrics

is activated, it's the easiest way to see, if metric does work and how long this app needs. I don't know when this
application fails with this in-memory-approach, so it's better to measure as soon as possible.

# docker build

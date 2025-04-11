# Monitoring
This project provides actuator metrics:

    http://localhost:8088/actuator/metrics

They have to activated in the application.yml. It's the easiest way to see, if metric does work 
and how long this app needs. I don't know when this  application fails with this in-memory-approach,
so it's better to measure as soon as possible.

To verify that API doc works: http://localhost:8080/v3/api-docs

## influxdb
In the production system, there is a influxdb already running. That's why there is a influxdb section
in application.yml.

See also https://www.influxdata.com/
Documentation: https://docs.influxdata.com/influxdb/v2/

For local development, it makes sens to link to a local influxdb. In the future, we can add an influxdb 
instance to make metrics visible.

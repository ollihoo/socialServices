# Monitoring

https://grafana.com/docs/grafana/latest/setup-grafana/installation/docker/
https://medium.com/@dulanjayasandaruwan1998/monitoring-spring-boot-applications-with-grafana-932d22f67865 (2024)
https://piotrminkowski.com/2018/05/11/exporting-metrics-to-influxdb-and-prometheus-using-spring-boot-actuator/ (2018)

grafana: http://localhost:3000/
influxdb: http://localhost:8086/


## influxdb

See also https://www.influxdata.com/
Documentation: https://docs.influxdata.com/influxdb/v2/

For local development, it makes sens to link to your local influxdb.
This can be done with this command:

    influx config create --config-name CONFIG_NAME \
        --host-url http://localhost:8086 \
        --org "Die Linke" \
        --token D1WHA8v4brNUbGu8OWlqfUrrYTlZQxlfJ4Fk5QTlzT0o-B0Bc1h4_PKfI-dkTBbsHVWxVkFKYQw_6yfqCrWdSg== \
        --active


## grafana
Connection to influx: https://docs.influxdata.com/influxdb/v2/tools/grafana/
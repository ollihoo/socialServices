FROM eclipse-temurin:23-alpine
ARG JAR_FILE=./build/libs/socialServices-0.1.jar

WORKDIR /app
COPY ${JAR_FILE} ./app.jar
RUN mkdir conf
COPY src/main/resources/application.yml.template ./conf/application.yml

ENTRYPOINT ["java", "-Dspring.config.location=/app/conf/application.properties","-jar", "/app/app.jar"]

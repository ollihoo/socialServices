FROM eclipse-temurin:23-alpine
ARG JAR_FILE=./build/libs/socialServices-0.0.1-SNAPSHOT.jar

WORKDIR /app
COPY ${JAR_FILE} ./app.jar
RUN mkdir config

ENTRYPOINT ["java", "-Dspring.config.location=/app/config/application.properties","-jar", "/app/app.jar"]

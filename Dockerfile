FROM eclipse-temurin:23-alpine
ARG IMAGE_TAG
ARG JAR_FILE=./build/libs/socialServices-${IMAGE_TAG}.jar

WORKDIR /app
COPY ${JAR_FILE} ./app.jar
RUN mkdir conf
COPY src/main/resources/application.properties.template ./conf/application.properties

ENTRYPOINT ["java", "-Dspring.config.location=/app/conf/application.properties","-jar", "/app/app.jar"]

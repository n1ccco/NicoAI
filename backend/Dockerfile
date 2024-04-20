FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn -e -B dependency:resolve
COPY src ./src
RUN mvn -e -B package -Dmaven.test.skip

FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=/app/target/*.jar
RUN apk add dumb-init
COPY --from=builder $JAR_FILE app.jar
RUN mkdir -p /images
CMD "dumb-init" "java" "-jar" "app.jar"
FROM eclipse-temurin:21-jdk-alpine AS base
WORKDIR /app/backend

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

RUN mkdir -p /images
COPY src ./src

FROM base AS dev
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=dev
CMD ["./mvnw", "spring-boot:run"]

FROM base AS builder
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21-jdk-alpine AS prod
COPY --from=builder /app/backend/target/*.jar app.jar
COPY --from=base /images /images
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"]
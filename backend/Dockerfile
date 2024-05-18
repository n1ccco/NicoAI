FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app/backend
 
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
 
COPY src ./src
RUN mkdir -p /images
CMD ["./mvnw", "spring-boot:run"]

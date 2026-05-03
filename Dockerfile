# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app/target/nlp-service-java-1.0-SNAPSHOT.jar ./app.jar

EXPOSE 8192
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

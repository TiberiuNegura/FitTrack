# Stage 1: Build the AuthService application with Maven
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy the Maven project files and source code
COPY pom.xml ./
COPY src ./src

# Package the application (skip tests for faster build)
RUN mvn clean package -DskipTests

# Stage 2: Create a minimal image for running the app
FROM eclipse-temurin:21-jdk-alpine AS runtime
WORKDIR /app

# Copy the fat jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose AuthService port
EXPOSE 8081

# Entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]

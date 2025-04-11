# Stage 1: Build the WorkoutService application
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build


WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Prepare runtime image
FROM eclipse-temurin:21-jdk-alpine AS runtime
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# Step 1: Build the application using Maven and Temurin Java 17
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run the application using Temurin Runtime Java 17
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/bugtracker-0.0.1-SNAPSHOT.jar bugtracker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "bugtracker.jar"]
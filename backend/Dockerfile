# Step 1: Build the application using Maven
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run the application using OpenJDK
FROM openjdk:17-jdk-slim
COPY --from=build /target/bugtracker-0.0.1-SNAPSHOT.jar bugtracker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "bugtracker.jar"]
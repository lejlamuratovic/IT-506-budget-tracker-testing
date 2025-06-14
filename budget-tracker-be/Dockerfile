FROM maven:3.9.4-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/budgettracker-0.0.1-SNAPSHOT.jar budget-tracker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "budget-tracker.jar"]

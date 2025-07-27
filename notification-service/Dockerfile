FROM ubuntu:latest
LABEL authors="Юлия"

ENTRYPOINT ["top", "-b"]

FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/user-service-*.jar ./user-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "user-service.jar"]
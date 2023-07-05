
FROM maven:3.8.5-openjdk-17 AS builder
EXPOSE 8084

# FROM maven:3.6.3-jdk-8 AS builder
COPY src/main/resources/application.properties application.properties
COPY target/*.jar application.jar
ENTRYPOINT ["java","-jar","application.jar"]
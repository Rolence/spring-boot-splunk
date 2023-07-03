# You can change this base image to anything else
# But make sure to use the correct version of Java docker pull openjdk:21-ea-17-jdk
FROM openjdk:21-ea-17-jdk

# Simply the artifact path
# ARG artifact=target/biz-0.0.1-SNAPSHOT.jar

# WORKDIR /opt/app

# COPY ${artifact} .
EXPOSE 8084
# This should not be changed
# ENTRYPOINT ["java","-jar","biz-0.0.1-SNAPSHOT.jar"]

# FROM maven:3.6.3-jdk-8 AS builder
COPY src/main/resources/application.properties application.properties
COPY target/*.jar application.jar
ENTRYPOINT ["java","-jar","application.jar"]
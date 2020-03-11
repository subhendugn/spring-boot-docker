FROM openjdk:8-jdk-alpine

COPY target/*.jar app.jar

RUN mvn test

ENTRYPOINT ["java","-jar","/app.jar"]
#FROM openjdk:8-jdk-alpine
FROM openjdk:11-jre-slim
ADD target/Docker-SpringCloud-Hystrix-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

FROM --platform=linux/amd64 eclipse-temurin:22-jdk-alpine
VOLUME /tmp
ARG CACHEBUST=1
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

FROM --platform=linux/amd64 eclipse-temurin:22-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

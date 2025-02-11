FROM openjdk:17-alpine
LABEL authors="dmitry voicu 2025"
WORKDIR /app
COPY target/task-service-0.0.1-SNAPSHOT.jar /app/task-service.jar
RUN rm -rf /var/cache/apk/*
ENTRYPOINT ["java", "-jar", "task-service.jar"]
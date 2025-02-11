FROM openjdk:17-alpine
LABEL authors="dmitry voicu 2025"
WORKDIR /app
COPY target/user-service-0.0.1-SNAPSHOT.jar /app/user-service.jar
RUN rm -rf /var/cache/apk/*
ENTRYPOINT ["java", "-jar", "user-service.jar"]
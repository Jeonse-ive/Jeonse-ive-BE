FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/realty-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]

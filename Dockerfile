FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/realty-0.0.1-SNAPSHOT.jar app.jar

# Healthcheck 추가
HEALTHCHECK --interval=30s --timeout=10s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]

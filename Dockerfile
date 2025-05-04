FROM openjdk:17-jdk-slim

WORKDIR /app

# 개발 중에는 jar 이름을 고정하지 않고 자유롭게 바꿀 수 있도록 soft link를 써도 좋음
COPY build/libs/realty-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]

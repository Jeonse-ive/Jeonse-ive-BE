#!/bin/bash

./gradlew clean build -x test

#docker-compose -f docker-compose.dev.yml down
docker-compose stop
docker-compose -f docker-compose.dev.yml up --build -d

# 스프링 로그 보는 명령어
# docker logs -f jeonse-ive-spring-app

# 실행 중인 컨테이너 확인 명령어
# docker ps

# 컨테이너 중지 명령어
# docker stop jeonse-ive-spring-app

# 다시 빌드 + 실행
#./gradlew build
#docker-compose -f docker-compose.dev.yml up --build -d backend
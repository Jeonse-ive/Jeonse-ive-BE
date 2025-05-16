#!/bin/bash

./gradlew clean build -x test

#docker-compose -f docker-compose.dev.yml down
docker-compose stop
docker-compose -f docker-compose.dev.yml up --build -d

# 스프링 로그 보는 명령어
# docker logs -f jeonse-ive-spring-app


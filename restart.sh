#!/bin/bash


./gradlew clean build -x test

docker-compose -f docker-compose.dev.yml down
docker-compose -f docker-compose.dev.yml up --build


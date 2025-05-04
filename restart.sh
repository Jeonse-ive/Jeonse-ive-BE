#!/bin/bash


echo "🧹 기존 백엔드 컨테이너 정리 중..."
docker compose rm -sf backend db

echo "🚀 백엔드 빌드 및 실행..."
docker compose up backend db


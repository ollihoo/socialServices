#!/bin/bash

if [ "$1" = "deploy" ]; then
  DEPLOY=true
fi

if [ -f .env ]; then
    source .env
else
    echo ".env file not found!"
    exit 1
fi

starttime=$(date +%s)

IMAGE_TAG=$(date +%y%m%d%H%M)
APP_NAME=socialservice_backend

if [ "$DEPLOY" = "true" ]; then
  docker buildx create --use --name arm-builder
  docker buildx inspect --bootstrap

  ./gradlew build && docker buildx build --platform linux/amd64 -t ${DOCKER_USER}/${APP_NAME}:${IMAGE_TAG} .
  docker buildx build --platform linux/arm64 -t ${DOCKER_USER}/${APP_NAME}:arm_${IMAGE_TAG} . --load
else
  ./gradlew build && docker build -t ${DOCKER_USER}/${APP_NAME}:${IMAGE_TAG} .
fi

if [ $? -eq 0 ]; then
    echo OK
else
    echo FAIL: build failed
    exit 1
fi

if [ "$DEPLOY" = "true" ]; then
  docker push ${DOCKER_USER}/${APP_NAME}:${IMAGE_TAG}
  docker push ${DOCKER_USER}/${APP_NAME}:arm_${IMAGE_TAG}
  if [ $? -eq 0 ]; then
      endtime=$(date +%s)
      delta=$((endtime - starttime))
      echo "Ok. Latest IMAGE: ${DOCKER_USER}/${APP_NAME}:${IMAGE_TAG} Duration: $delta seconds"
      exit 0
  else
      echo FAIL: push failed
      exit 1
  fi
else
  echo "File hasn't been pushed to docker hub"
  exit 1
fi


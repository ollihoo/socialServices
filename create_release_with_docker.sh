#!/bin/zsh

if [ -f .env ]; then
    source .env
else
    echo ".env file not found!"
    exit 1
fi

starttime=$(date +%s)

IMAGE_TAG=$(date +%y%m%d%H%M)
APP_NAME=socialservice_backend

./gradlew build && \
docker buildx build --platform linux/amd64 -t ${DOCKER_USER}/${APP_NAME}:${IMAGE_TAG} . && \
docker push ${DOCKER_USER}/${APP_NAME}:${IMAGE_TAG}

endtime=$(date +%s)
delta=$((endtime - starttime))
echo "Ok. Latest IMAGE: ${DOCKER_USER}/${APP_NAME}:${IMAGE_TAG} Duration: $delta seconds"
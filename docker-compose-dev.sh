#!/bin/bash
source ./docker.properties
export PROFILE="${PROFILE:=docker}"

echo '### Java version ###'
java --version

front_path=""
front_image=""
if [[ "$1" = "gql" ]]; then
  front_path="./${FRONT_IMAGE_NAME_GQL}/";
  front_image="${IMAGE_PREFIX}/${FRONT_IMAGE_NAME_GQL}-${PROFILE}:latest";
else
  front_path="./${FRONT_IMAGE_NAME}/";
  front_image="${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}-${PROFILE}:latest";
fi

FRONT_IMAGE="$front_image" PREFIX="${IMAGE_PREFIX}" PROFILE="${PROFILE}" docker-compose down

docker_containers="$(docker ps -a -q)"
docker_images="$(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'niffler')"

if [ ! -z "$docker_containers" ]; then
  echo "### Stop containers: $docker_containers ###"
  docker stop $(docker ps -a -q)
  docker rm $(docker ps -a -q)
fi
if [ ! -z "$docker_images" ]; then
  echo "### Remove images: $docker_images ###"
  docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'niffler')
fi

if [ "$1" = "push" ] || [ "$2" = "push" ]; then
  echo "### Build & push images (front_path: $front_path) ###"
  bash ./gradlew -Pskipjaxb jib -x :niffler-e-2-e-tests:test
  cd "$front_path" || exit
  bash ./docker-build.sh ${PROFILE} push
else
  echo "### Build images (front_path: $front_path) ###"
  bash ./gradlew -Pskipjaxb jibDockerBuild -x :niffler-e-2-e-tests:test
  cd "$front_path" || exit
  bash ./docker-build.sh ${PROFILE}
fi

cd ../
docker images
FRONT_IMAGE="$front_image" PREFIX="${IMAGE_PREFIX}" PROFILE="${PROFILE}" docker-compose up -d
docker ps -a

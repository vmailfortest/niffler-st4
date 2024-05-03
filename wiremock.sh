#!/bin/bash

docker pull wiremock/wiremock:2.35.0
docker run --name mock -p 8089:8089 -v ./wiremock/rest:/home/wiremock -d wiremock/wiremock:2.35.0 --global-response-templating --enable-stub-cors

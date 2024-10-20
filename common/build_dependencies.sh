#!/bin/bash

PROGRESS="auto"
if [ "$1" == "debug" ]; then
    PROGRESS="plain"
fi

# Build docker image for dependencies
echo "Building docker image for dependencies... \n"
cd ..
docker build -t disecomm-build-dependencies:local -f common/Dockerfile-dependencies --progress=$PROGRESS .

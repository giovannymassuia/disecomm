#!/bin/bash

# App will will be available at http://localhost:8083
echo "🌐 http://localhost:8083/graphiql"

# Run app with [docker run] or [mvn spring-boot:run]
# if docker use spring profile "docker"
if [ "$1" == "docker" ]; then
    echo "Running app with docker... \n"
    docker run \
        --rm \
        -p 8083:8083 \
        -e SPRING_PROFILES_ACTIVE=docker \
        disecomm-api-gateway:local
elif [ "$1" == "mvn" ]; then
    # check if maven is installed
    echo "Running app with mvn... \n"
    mvn spring-boot:run
else
    echo "Invalid argument. Please use 'docker' or 'mvn' \n"
fi

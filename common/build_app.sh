#!/bin/bash

set -e

# Valid list of apps
VALID_APPS=("api-gateway" "inventory-management" "order-management" "product-catalog")

# Help
if [ "$1" == "--help" ]; then
	echo "Usage: ./build.sh <app-name> [--no-otel-agent] [--debug]"
	echo "  app-name: The name of the app to build. Valid apps: ${VALID_APPS[@]}"
	echo "  --no-otel-agent: Optional. If present, the OpenTelemetry agent will not be used."
	echo "  --push-local: Optional. If present, the built image will be pushed to the local registry."
	echo "  --debug: Optional. If present, the build will output more information."
	exit 0
fi

# Inputs
APP_NAME=""
if [ -z "$1" ]; then
  echo ""
	echo "🤕 Missing app name argument. Usage: \`./build.sh <app-name>\`"
  echo ""
	exit 1
else
	APP_NAME=$1
fi
# Check if app name is valid
if [[ ! " ${VALID_APPS[@]} " =~ " ${APP_NAME} " ]]; then
  echo ""
	echo "🤕 Invalid app name. Valid apps: ${VALID_APPS[@]}"
	echo ""
	exit 1
fi

USE_OTEL_AGENT="true"
if [ "$2" == "--no-otel-agent" ]; then
	USE_OTEL_AGENT="false"
fi

# if api-gateway disable otel agent
if [ "$APP_NAME" == "api-gateway" ]; then
	USE_OTEL_AGENT="false"
fi

PUSH_TO_LOCAL_REGISTRY="false"
if [ "$2" == "--push-local" ] || [ "$3" == "--push-local" ]; then
  PUSH_TO_LOCAL_REGISTRY="true"
fi

PROGRESS="auto"
if [ "$2" == "--debug" ] || [ "$3" == "--debug" ] || [ "$4" == "--debug" ]; then
	PROGRESS="plain"
fi

JAVA_TOOL_OPTIONS_VALUE="-javaagent:/app/otel-java-agent.jar"
if [ "$USE_OTEL_AGENT" == "false" ]; then
	JAVA_TOOL_OPTIONS_VALUE=""
fi

# Build docker image
echo "Building docker image... \n"
echo "-----------------------------------"
echo "OTEL AGENT: $USE_OTEL_AGENT"
echo "JAVA_TOOL_OPTIONS: $JAVA_TOOL_OPTIONS_VALUE"
echo "APP NAME: $APP_NAME"
echo "PROGRESS: $PROGRESS"
echo "PUSH TO LOCAL REGISTRY: $PUSH_TO_LOCAL_REGISTRY"
echo "-----------------------------------"
cd ..
docker build \
	-t disecomm-$APP_NAME:local \
	--build-arg APP_NAME=$APP_NAME \
	--build-arg JAVA_TOOL_OPTIONS_VALUE="$JAVA_TOOL_OPTIONS_VALUE" \
	--build-arg USE_OTEL_AGENT=$USE_OTEL_AGENT \
	-f common/Dockerfile-app \
	--progress=$PROGRESS \
	.

# tag image and push to local registry
if [ "$PUSH_TO_LOCAL_REGISTRY" == "true" ]; then
  docker tag disecomm-$APP_NAME:local localhost:5001/disecomm-$APP_NAME:local
  docker push localhost:5001/disecomm-$APP_NAME:local
fi

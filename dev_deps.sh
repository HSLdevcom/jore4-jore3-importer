#!/usr/bin/env bash

set -eu

cd "$(dirname "$0")" # Setting the working directory as the script directory

COMMAND=${1:-}
PARAMETER=${2:-none}

# Define a Docker Compose project name to distinguish
# the docker environment of this project from others
export COMPOSE_PROJECT_NAME=JOREIMPORTER

instruct_and_exit() {
  echo "Usage: ${0} <command>"
  echo ""
  echo "Available commands:"
  echo "start               Start the dependencies"
  echo "stop                Stop the dependencies"
  echo "recreate            Remove and recreate the dependencies, removing all data"
  echo "list                List running dependencies"
  echo "logs (service-name) Attach to log output of all or specified service"
  exit 1
}

### Control flow

if [[ -z ${COMMAND} ]]; then
  instruct_and_exit
fi

if [[ ${COMMAND} == "start" ]]; then
  docker-compose -f ./docker/docker-compose.dev.yml -f ./docker/docker-compose.test.yml up -d
  exit 0
fi

if [[ ${COMMAND} == "stop" ]]; then
  docker-compose -f ./docker/docker-compose.dev.yml -f ./docker/docker-compose.test.yml down
  exit 0
fi

if [[ ${COMMAND} == "remove" ]]; then
  docker-compose -f ./docker/docker-compose.dev.yml -f ./docker/docker-compose.test.yml rm -f
  exit 0
fi

if [[ ${COMMAND} == "recreate" ]]; then
  docker-compose -f ./docker/docker-compose.dev.yml -f ./docker/docker-compose.test.yml stop
  docker-compose -f ./docker/docker-compose.dev.yml -f ./docker/docker-compose.test.yml rm -f
  docker-compose -f ./docker/docker-compose.dev.yml -f ./docker/docker-compose.test.yml up --build -d
  exit 0
fi

if [[ ${COMMAND} == "list" ]]; then
  docker-compose -f ./docker/docker-compose.dev.yml -f ./docker/docker-compose.test.yml config --services
  exit 0
fi

if [[ ${COMMAND} == "logs" ]]; then
  docker-compose -f ./docker/docker-compose.dev.yml -f ./docker/docker-compose.test.yml logs -f ${PARAMETER}
  exit 0
fi

### Unknown argument was passed.

echo "Unknown command '${COMMAND}' !"
echo ""
instruct_and_exit

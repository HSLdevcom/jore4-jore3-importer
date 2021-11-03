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
  echo "start               Start the dependencies and the dockerized application"
  echo "start:deps          Start the dependencies only"
  echo "generate:jooq       Start the dependencies and generate JOOQ classes"
  echo "stop                Stop the dependencies and the dockerized application"
  echo "recreate            Remove and recreate the dependencies, removing all data"
  echo "list                List running dependencies"
  echo "logs (service-name) Attach to log output of all or specified service"
  exit 1
}

download_docker_bundle() {
  # initialize package folder
  mkdir -p ./docker

  # compare versions
  GITHUB_VERSION=$(curl -L https://github.com/HSLdevcom/jore4-flux/releases/download/e2e-docker-compose/RELEASE_VERSION.txt --silent)
  LOCAL_VERSION=$(cat ./docker/RELEASE_VERSION.txt || echo "unknown")

  # download latest version of the docker-compose package in case it has changed
  if [ "$GITHUB_VERSION" != "$LOCAL_VERSION" ]; then
    echo "E2E docker-compose package is not up to date, downloading a new version."
    curl -L https://github.com/HSLdevcom/jore4-flux/releases/download/e2e-docker-compose/e2e-docker-compose.tar.gz --silent | tar -xzf - -C ./docker/
  else
    echo "E2E docker-compose package is up to date, no need to download new version."
  fi

  # start up all services (and build on-demand)
  # docker-compose -f ./docker/docker-compose.yml -f ./docker/docker-compose.custom.yml up --build jore4-testdb jore4-hasura jore4-mssqltestdb
  docker-compose -f ./docker/docker-compose.yml -f ./docker/docker-compose.custom.yml up --build jore4-hasura jore4-testdb

  # start up only some services (and build on-demand)
  # docker-compose -f ./docker/docker-compose.yml up --build jore4-ui jore4-proxy
}

start_all() {
  docker-compose up --build -d
}

start_deps() {
  docker-compose up --build -d importer-jooq-database importer-test-destination-database importer-test-source-database
}

generate_jooq() {
  mvn clean generate-sources -Pci
}

### Control flow

if [[ -z ${COMMAND} ]]; then
  instruct_and_exit
fi

if [[ ${COMMAND} == "bundle" ]]; then
  download_docker_bundle
  exit 0
fi

if [[ ${COMMAND} == "start" ]]; then
  start_all
  exit 0
fi

if [[ ${COMMAND} == "start:deps" ]]; then
  start_deps
  exit 0
fi

if [[ ${COMMAND} == "generate:jooq" ]]; then
  start_deps
  while ! pg_isready -h localhost -p 17000
  do
    echo "waiting for db to spin up"
    sleep 2;
  done
  generate_jooq
  exit 0
fi

if [[ ${COMMAND} == "stop" ]]; then
  docker-compose down
  exit 0
fi

if [[ ${COMMAND} == "remove" ]]; then
  docker-compose rm -f
  exit 0
fi

if [[ ${COMMAND} == "recreate" ]]; then
  docker-compose stop
  docker-compose rm -f
  docker-compose up --build -d
  exit 0
fi

if [[ ${COMMAND} == "list" ]]; then
  docker-compose config --services
  exit 0
fi

if [[ ${COMMAND} == "logs" ]]; then
  docker-compose logs -f ${PARAMETER}
  exit 0
fi

### Unknown argument was passed.

echo "Unknown command '${COMMAND}' !"
echo ""
instruct_and_exit

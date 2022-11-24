#!/usr/bin/env bash

set -eu

cd "$(dirname "$0")" # Setting the working directory as the script directory

COMMAND=${1:-}

# Define a Docker Compose project name to distinguish
# the docker environment of this project from others
export COMPOSE_PROJECT_NAME=JOREIMPORTER

DOCKER_COMPOSE_CMD="docker-compose -f ./docker/docker-compose.yml -f ./docker/docker-compose.testdb-volume.yml -f ./docker/docker-compose.custom.yml"

# if the --no-volume parameter is set, the testdb volume will not be mounted
for i in "$@" ; do
    if [[ $i == "--no-volume" ]] ; then
        DOCKER_COMPOSE_CMD="docker-compose -f ./docker/docker-compose.yml -f ./docker/docker-compose.custom.yml"
        break
    fi
done

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
  exit 1
}

download_docker_bundle() {
  # based on https://github.com/HSLdevcom/jore4-tools#download-docker-bundlesh

  echo "Downloading latest version of E2E docker-compose package..."
  curl https://raw.githubusercontent.com/HSLdevcom/jore4-tools/main/docker/download-docker-bundle.sh | bash
}

start_all() {
  download_docker_bundle
  $DOCKER_COMPOSE_CMD up --build -d importer-jooq-database importer-test-database jore4-mssqltestdb jore4-hasura jore4-testdb jore4-jore3importer jore4-mapmatchingdb jore4-mapmatching
}

start_deps() {
  download_docker_bundle
  # Runs the following services:
  # importer-jooq-database - The database which contains the information imported and transformed from Jore 3
  # importer-test-destination-database - The test database which contains the information imported and transformed from Jore 3
  # jore4-mssqltestdb - The Jore 3 MSSQL database which contains the source data which is read by the importer
  # jore4-hasura - Hasura. We have to start Hasura because it ensures that db migrations are run to the Jore 4 database.
  # jore4-testdb - Jore 4 database. This is the destination database of the import process.
  $DOCKER_COMPOSE_CMD up --build -d importer-jooq-database importer-test-database jore4-mssqltestdb jore4-hasura jore4-testdb jore4-mapmatchingdb jore4-mapmatching
}

generate_jooq() {
  mvn clean generate-sources -Pci
}

### Control flow

if [[ -z ${COMMAND} ]]; then
  instruct_and_exit
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
    echo "waiting for importer db to spin up"
    sleep 2;
  done
  while ! pg_isready -h localhost -p 6432
  do
    echo "waiting for Jore 4 db to spin up"
    sleep 2;
  done
  while ! curl --fail http://localhost:3201/healthz --output /dev/null --silent
  do
    echo "waiting for hasura db migrations to execute"
    sleep 2;
  done
  generate_jooq
  exit 0
fi

if [[ ${COMMAND} == "stop" ]]; then
  $DOCKER_COMPOSE_CMD down
  exit 0
fi

if [[ ${COMMAND} == "remove" ]]; then
  $DOCKER_COMPOSE_CMD rm -f
  exit 0
fi

if [[ ${COMMAND} == "recreate" ]]; then
  docker-compose stop
  docker-compose rm -f
  $DOCKER_COMPOSE_CMD up --build -d importer-jooq-database importer-test-database jore4-mssqltestdb jore4-hasura jore4-testdb
  exit 0
fi

if [[ ${COMMAND} == "list" ]]; then
  $DOCKER_COMPOSE_CMD config --services
  exit 0
fi

### Unknown argument was passed.

echo "Unknown command '${COMMAND}' !"
echo ""
instruct_and_exit

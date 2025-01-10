#!/usr/bin/env bash

set -euo pipefail

cd "$(dirname "$0")" # Setting the working directory as the script directory

# Define a Docker Compose project name to distinguish
# the docker environment of this project from others
export COMPOSE_PROJECT_NAME=jore3-importer

DOCKER_COMPOSE_CMD="docker compose -f ./docker/docker-compose.yml -f ./docker/docker-compose.testdb-volume.yml -f ./docker/docker-compose.custom.yml"

# if the --no-volume parameter is set, the testdb volume will not be mounted
for i in "$@" ; do
    if [[ $i == "--no-volume" ]] ; then
        DOCKER_COMPOSE_CMD="docker compose -f ./docker/docker-compose.yml -f ./docker/docker-compose.custom.yml"
        break
    fi
done

print_usage() {
  echo "
  Usage: $(basename "$0") <command>

  Available commands:

  start               Start the dependencies and the dockerized application

  start:deps          Start the dependencies only

  generate:jooq       Generate JOOQ classes

  stop                Stop the dependencies and the dockerized application

  remove              Remove the dependencies and the dockerized application

  recreate            Stop, remove and recreate the dependencies, removing all data

  list                List running dependencies
  "
}

download_docker_bundle() {
  # based on https://github.com/HSLdevcom/jore4-tools#download-docker-bundlesh

  echo "Downloading latest version of E2E docker-compose package..."
  curl https://raw.githubusercontent.com/HSLdevcom/jore4-tools/main/docker/download-docker-bundle.sh | bash
}

start_all() {
  $DOCKER_COMPOSE_CMD up --build -d importer-jooq-database importer-test-database jore4-mssqltestdb jore4-hasura jore4-testdb jore4-jore3importer jore4-mapmatchingdb jore4-mapmatching
}

start_deps() {
  # Runs the following services:
  # importer-jooq-database - The database which contains the information imported and transformed from Jore 3
  # importer-test-destination-database - The test database which contains the information imported and transformed from Jore 3
  # jore4-mssqltestdb - The Jore 3 MSSQL database which contains the source data which is read by the importer
  # jore4-hasura - Hasura. We have to start Hasura because it ensures that db migrations are run to the Jore 4 database.
  # jore4-testdb - Jore 4 database. This is the destination database of the import process.
  $DOCKER_COMPOSE_CMD up --build -d importer-jooq-database importer-test-database jore4-mssqltestdb jore4-hasura jore4-testdb jore4-mapmatchingdb jore4-mapmatching
}

wait_for_test_databases_to_be_ready() {
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
}

generate_jooq() {
  mvn clean generate-sources -Pci
}

### Control flow

COMMAND=${1:-}

if [[ -z $COMMAND ]]; then
  print_usage
  exit 1
fi

case $COMMAND in
  start)
    download_docker_bundle
    start_all
    ;;

  start:deps)
    download_docker_bundle
    start_deps
    ;;

  generate:jooq)
    wait_for_test_databases_to_be_ready
    generate_jooq
    ;;

  stop)
    docker compose stop
    ;;

  remove)
    docker compose rm -f
    ;;

  recreate)
    docker compose stop
    docker compose rm -f
    start_deps
    ;;

  list)
    $DOCKER_COMPOSE_CMD config --services
    ;;

  *)
    echo ""
    echo "Unknown command: '${COMMAND}'"
    print_usage
    exit 1
    ;;
esac

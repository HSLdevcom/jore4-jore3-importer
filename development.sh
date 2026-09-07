#!/usr/bin/env bash

set -euo pipefail

cd "$(dirname "$0")" # Setting the working directory as the script directory

# By default, the tip of the main branch of the jore4-docker-compose-bundle
# repository is used as the commit reference, which determines the version of
# the Docker Compose bundle to download. For debugging purposes, this default
# can be overridden by some other commit reference (e.g., commit SHA or its
# initial substring), which you can pass via the `BUNDLE_REF` environment
# variable.
DOCKER_COMPOSE_BUNDLE_REF=${BUNDLE_REF:-main}

STOP_REGISTRY_IMPORTER_DIR="./stop-registry-importer"
STOP_REGISTRY_VENV_DIR="${STOP_REGISTRY_IMPORTER_DIR}/.venv-stop-registry"
STOP_REGISTRY_REQUIREMENTS_FILE="${STOP_REGISTRY_IMPORTER_DIR}/requirements.txt"
STOP_REGISTRY_REQUIREMENTS_IN_FILE="${STOP_REGISTRY_IMPORTER_DIR}/requirements.in"

# Python/pip executables inside the stop-registry virtualenv. These are
# populated by `ensure_python_venv` so that every Python-related command uses
# the correct interpreter and isolated environment.
STOP_REGISTRY_VENV_PYTHON="${STOP_REGISTRY_VENV_DIR}/bin/python"

# Define a Docker Compose project name to distinguish
# the docker environment of this project from others
export COMPOSE_PROJECT_NAME=jore3-importer

INFRALINKS_URL="https://stjore4dev001.blob.core.windows.net/jore4-ui/2025-09-24-infraLinks.sql"
TRAM_INFRALINKS_URL="https://stjore4dev001.blob.core.windows.net/jore4-ui/tram_infraLinks_2026-01-28.sql"
BASE_DB_CONNECTION_STRING=postgresql://dbadmin:adminpassword@localhost:5432
JORE4_DB_CONNECTION_STRING=$BASE_DB_CONNECTION_STRING/jore4e2e

DOCKER_COMPOSE_CMD="docker compose -f ./docker/docker-compose.yml -f ./docker/docker-compose.custom.yml"
DOCKER_COMPOSE_TIAMAT_FLYWAY_CMD="docker compose -f ./docker/docker-compose.yml -f ./docker/docker-compose.tiamat-flyway-baseline.yml -f ./docker/docker-compose.custom.yml"

# if the --volume parameter is set, the testdb volume will be mounted
for i in "$@" ; do
  if [[ $i == "--volume" ]] ; then
    DOCKER_COMPOSE_CMD="docker compose -f ./docker/docker-compose.yml -f ./docker/docker-compose.testdb-volume.yml -f ./docker/docker-compose.custom.yml"
    break
  fi
done

print_usage() {
  echo "
  Usage: $(basename "$0") <command>

  Available commands:

  start
    Start the dependencies and the dockerized application.

    See also start:deps.

  start:deps
    Start the dependencies only.

    You can control which version of the Docker Compose bundle is downloaded by
    passing a commit reference to the jore4-docker-compose-bundle repository via
    the BUNDLE_REF environment variable. By default, the latest version is
    downloaded.

    jore4-testdb is built by default without persistent database volume in docker/testdb.
    Enable volume with --volume.

  generate:jooq
    Generate JOOQ classes.

  python:setup
    Creates/updates Python virtualenv for stop-registry importer and installs dependencies.

  python:update-reqs
    Recompiles stop-registry-importer/requirements.txt from requirements.in using
    pip-tools (pip-compile). Run this after changing requirements.in. You must run python:setup
    to install the updated dependencies after review.

  stop
    Stop the dependencies and the dockerized application.

  remove
    Stop and remove the dependencies and the dockerized application.

  recreate
    Stop, remove and recreate the dependencies, removing all data.

  list
    List running dependencies.

  infralinks:download
    Downloads the infrastructure links seed data SQL file (infraLinks.sql) from Azure
    Blob Storage.

  infralinks:seed
    Downloads the infrastructure links seed data SQL file (infraLinks.sql) from Azure
    Blob Storage. Applies the links to testdb.

  jore3:import_sql_files
    Imports every jore3dump/jr_*.sql file into the running jore3testdb MSSQL
    database. Files are discovered and ordered by filename on every run.

  infralinks:import_infra_links_from_csv:wipe_database <MML_TRAM_IMPORT_DATE>
    Completely clears the database, runs migrations and imports infrastructure from the digiroad material.
    Expects the workdir of jore4-digiroad-import to be copied or linked to the project root:
    ln -s ../jore4-digiroad-import/workdir workdir
    MML_TRAM_IMPORT_DATE is the date of the tram import in YYYY-MM-DD format.
  "
}

sleepf() {
  local seconds="$1"
  echo -n "Sleeping for $seconds seconds"

  for ((i=1; i <= seconds ; i++))
  do
    echo -n "."
  done

  for ((i=1; i <= seconds ; i++))
  do
    sleep 1
    echo -ne "\b \b"
  done
  echo -ne "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"
  if [[ $seconds -gt 9 ]]; then
    echo -ne "\b"
  fi
  if [[ $seconds -gt 99 ]]; then
    echo -ne "\b"
  fi
}

docker_stop_service() {
  local service_name="$1"
  echo "Stopping $service_name..."
  $DOCKER_COMPOSE_CMD --project-name "$COMPOSE_PROJECT_NAME" stop "$service_name"
  sleepf 1
}

docker_start_service() {
  local service_name="$1"
  local service_env="";
  local se=${2:-}
  if [[ -n $se ]]; then
    service_env="-e $2"
  fi
  echo "Starting $service_name..."
  $DOCKER_COMPOSE_CMD --project-name "$COMPOSE_PROJECT_NAME" up --build -d "$service_name" ${service_env}
  sleepf 2
}

import_infrastructure_wipe_database() {
  MML_TRAM_IMPORT_DATE="$1"
  if [[ ! "$MML_TRAM_IMPORT_DATE" =~ ^20[2-9][0-9]-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$ ]]; then
    echo "Invalid date: $MML_TRAM_IMPORT_DATE"
    usage
    exit 1
  fi

  export DIGIROAD_IRROTUS_NRO=""
  if [[ -f "workdir/zip/digiroad_irrotus_nro.txt" ]]; then
    DIGIROAD_IRROTUS_NRO=$(cat "workdir/zip/digiroad_irrotus_nro.txt")
  fi
  if [[ -z "$DIGIROAD_IRROTUS_NRO" ]]; then
    echo "digiroad_irrotus_nro.txt not found in workdir/zip or empty. Is jore4-digiroad-importer's workdir copied/linked and digiroad-importer ran?"
    exit 1
  fi

  INPUT_FILENAME="infra_network_digiroad_r_${DIGIROAD_IRROTUS_NRO}_mml_${MML_TRAM_IMPORT_DATE}.csv"
  if [[ ! -f "workdir/csv/${INPUT_FILENAME}" ]]; then
    echo "Input file workdir/csv/${INPUT_FILENAME} does not exist. Is jore4-digiroad-importer's workdir copied/linked and digiroad-importer ran?"
    exit 1
  fi

  read -p "Delete all tables and data in jore4e2e, stopdb, and timetablesdb on BASE_DB_CONNECTION_STRING? [y/N]: " TRUNCATE_ALL_DATABASES
  if [[ ! "$TRUNCATE_ALL_DATABASES" =~ ^[Yy]$ ]]; then
    echo "Aborting."
    exit 1
  fi

  docker_stop_service jore4-hasura
  docker_stop_service jore4-tiamat
  docker_stop_service jore4-mapmatching
  docker compose rm -f jore4-tiamat

  sleepf 5

  echo "Deleting all tables and data in jore4e2e, stopdb, and timetablesdb on $BASE_DB_CONNECTION_STRING"
  for DATABASE in jore4e2e stopdb timetablesdb; do
    docker exec -i testdb psql "$BASE_DB_CONNECTION_STRING/$DATABASE" \
      -v ON_ERROR_STOP=1 -f - < "scripts/drop-tables.sql"
  done
  docker exec -i testdb psql $BASE_DB_CONNECTION_STRING/stopdb < "scripts/drop-tiamat-views-sequences.sql";
  docker exec -i testdb psql $BASE_DB_CONNECTION_STRING/jore4e2e < "scripts/drop-hasura-resources.sql";
  docker exec -i testdb psql $BASE_DB_CONNECTION_STRING/jore4main < "scripts/drop-hasura-resources.sql";
  docker exec -i testdb psql $BASE_DB_CONNECTION_STRING/timetablesdb < "scripts/drop-hasura-resources.sql";

  sleepf 5

  echo "Restarting tiamat..."
  #docker run jore4-tiamat -e SPRING_FLYWAY_BASELINE_ON_MIGRATE=false
  $DOCKER_COMPOSE_TIAMAT_FLYWAY_CMD --project-name "$COMPOSE_PROJECT_NAME" up --build -d jore4-tiamat
  while ! curl --fail http://localhost:3010/actuator/health --silent | grep --fixed-strings --quiet '{"status":"UP"}'
  do
    echo "waiting for tiamat db migrations to execute"
    sleepf 2;
  done

  echo "Restarting mapmatching..."
  docker_start_service jore4-mapmatching
  while ! curl --fail http://localhost:3005/actuator/health --silent | grep --fixed-strings --quiet '{"groups":["liveness","readiness"],"status":"UP"'
  do
    echo "waiting for mapmatching db migrations to execute"
    sleepf 2;
  done

  echo "Restarting hasura..."
  docker_start_service jore4-hasura
  while ! curl --fail http://localhost:3201/healthz --output /dev/null --silent
  do
    echo "waiting for hasura db migrations to execute"
    sleepf 2;
  done

  echo "Stopping services again to drive in infrastructure network"
  docker_stop_service jore4-hasura
  docker_stop_service jore4-tiamat
  docker rm -f jore4-tiamat
  docker_stop_service jore4-mapmatching

  sleepf 5

  echo "Importing infrastructure data from CSV file to jore4e2e on $BASE_DB_CONNECTION_STRING..."
  # Import dump from csv file.
  docker exec -i testdb psql "$BASE_DB_CONNECTION_STRING/jore4e2e" \
    -v ON_ERROR_STOP=1 -f /mnt/jore3importer/sql/import_infra_links_from_csv.sql \
    -v csvfile="/mnt/jore3importer/workdir/csv/${INPUT_FILENAME}"
  echo "Importing infrastructure data from CSV file to jore4main on $BASE_DB_CONNECTION_STRING..."
  # Import dump from csv file.
  docker exec -i testdb psql "$BASE_DB_CONNECTION_STRING/jore4main" \
    -v ON_ERROR_STOP=1 -f /mnt/jore3importer/sql/import_infra_links_from_csv.sql \
    -v csvfile="/mnt/jore3importer/workdir/csv/${INPUT_FILENAME}"

  sleepf 5

  echo "Restarting tiamat..."
  docker_start_service jore4-tiamat
  while ! curl --fail http://localhost:3010/actuator/health --silent | grep --fixed-strings --quiet '{"status":"UP"}'
  do
    echo "waiting for tiamat db migrations to execute"
    sleepf 2;
  done

  echo "Restarting mapmatching..."
  docker_start_service jore4-mapmatching
  while ! curl --fail http://localhost:3005/actuator/health --silent | grep --fixed-strings --quiet '{"groups":["liveness","readiness"],"status":"UP"'
  do
    echo "waiting for mapmatching db migrations to execute"
    sleepf 2;
  done

  echo "Restarting hasura..."
  docker_start_service jore4-hasura
  while ! curl --fail http://localhost:3201/healthz --output /dev/null --silent
  do
    echo "waiting for hasura db migrations to execute"
    sleepf 2;
  done

  echo "All done."
}

# Download Docker Compose bundle from the "jore4-docker-compose-bundle"
# repository. GitHub CLI is required to be installed.
#
# A commit reference is read from global `DOCKER_COMPOSE_BUNDLE_REF` variable,
# which should be set based on the script execution arguments.
download_docker_compose_bundle() {
  local commit_ref="$DOCKER_COMPOSE_BUNDLE_REF"

  local repo_name="jore4-docker-compose-bundle"
  local repo_owner="HSLdevcom"

  # Check GitHub CLI availability.
  if ! command -v gh &> /dev/null; then
    echo "Please install the GitHub CLI (gh) on your machine."
    exit 1
  fi

  # Make sure the user is authenticated to GitHub.
  gh auth status || gh auth login

  echo "Using the commit reference '${commit_ref}' to fetch a Docker Compose bundle..."

  # First, try to find a commit on GitHub that matches the given reference.
  # This function exits with an error code if no matching commit is found.
  local commit_sha
  commit_sha=$(
    gh api \
      -H "Accept: application/vnd.github+json" \
      -H "X-GitHub-Api-Version: 2022-11-28" \
      "repos/${repo_owner}/${repo_name}/commits/${commit_ref}" \
      --jq '.sha'
  )

  echo "Commit with the following SHA digest was found: ${commit_sha}"

  local zip_file="/tmp/${repo_name}.zip"
  local unzip_target_dir_prefix="/tmp/${repo_owner}-${repo_name}"

  # Remove old temporary directories if any remain.
  rm -fr "$unzip_target_dir_prefix"-*

  echo "Downloading the JORE4 Docker Compose bundle..."

  # Download Docker Compose bundle from the jore4-docker-compose-bundle
  # repository as a ZIP file.
  gh api "repos/${repo_owner}/${repo_name}/zipball/${commit_sha}" > "$zip_file"

  # Extract ZIP file contents to a temporary directory.
  unzip -q "$zip_file" -d /tmp

  # Clean untracked files from the `docker` directory even if they are
  # git-ignored. Exclude the `testdb` directory, which we want to keep.
  git clean -fx -e testdb ./docker

  echo "Copying JORE4 Docker Compose bundle files to ./docker directory..."

  # Copy files from the `docker-compose` directory of the ZIP file to your
  # local `docker` directory.
  mv "$unzip_target_dir_prefix"-*/docker-compose/* ./docker

  # Remove the temporary files and directories created above.
  rm -fr "$zip_file" "$unzip_target_dir_prefix"-*

  echo "Generating a release version file for the downloaded bundle..."

  # Create a release version file containing the SHA digest of the referenced
  # commit.
  echo "$commit_sha" > ./docker/RELEASE_VERSION.txt
}

download_infralinks() {
  download_bus_infralinks
  download_tram_infralinks
}

download_bus_infralinks() {
  if [ -f "infraLinks.sql" ]; then
    echo "infraLinks.sql already exists, skipping download."
  else
    echo "Downloading infraLinks.sql..."
    curl "$INFRALINKS_URL" -o "infraLinks.sql"
  fi
}

download_tram_infralinks() {
  if [ -f "tram_infraLinks.sql" ]; then
    echo "tram_infraLinks.sql already exists, skipping download."
  else
    echo "Downloading tram_infraLinks.sql..."
    curl "$TRAM_INFRALINKS_URL" -o "tram_infraLinks.sql"
  fi
}

seed_infra_links() {
  seed_bus_infra_links $1
  seed_tram_infra_links $1
}

seed_bus_infra_links() {
  download_bus_infralinks

  echo "$1: Seeding Bus infrastructure links..."

  wait_for_test_databases_to_be_ready

  echo "$1: infraLinks.sql..."
  docker exec -i "$1" psql $JORE4_DB_CONNECTION_STRING < "infraLinks.sql";

  echo "$1: Done Bus seeding infrastructure links."
}

seed_tram_infra_links() {
  download_tram_infralinks

  echo "$1: Seeding Tram infrastructure links..."

  wait_for_test_databases_to_be_ready

  echo "$1: tram_infraLinks.sql..."
  docker exec -i "$1" psql $JORE4_DB_CONNECTION_STRING < "tram_infraLinks.sql";

  echo "$1: Done Tram seeding infrastructure links."
}

import_jore3_sql_files() {
  local sql_file
  local -a sql_files

  shopt -s nullglob
  sql_files=(jore3dump/jr_*.sql)
  shopt -u nullglob

  if (( ${#sql_files[@]} == 0 )); then
    echo "No SQL files matching jore3dump/jr_*.sql were found." >&2
    exit 1
  fi

  if ! command -v sqlcmd > /dev/null 2>&1; then
    echo "sqlcmd must be installed on the host to import Jore 3 SQL files." >&2
    exit 1
  fi

  echo "Ensuring the dbo schema exists in jore3testdb..."
  SQLCMDPASSWORD="${SA_PASSWORD:-P@ssw0rd}" \
    sqlcmd -b -r 1 -C -S localhost,1433 -U SA -d jore3testdb \
    -Q "IF SCHEMA_ID(N'dbo') IS NULL EXEC(N'CREATE SCHEMA [dbo]');"

  for sql_file in "${sql_files[@]}"; do
    if [[ ! -f "$sql_file" ]]; then
      echo "Expected a regular file but found: $sql_file" >&2
      exit 1
    fi

    echo "Importing $sql_file into jore3testdb..."
    SQLCMDPASSWORD="${SA_PASSWORD:-P@ssw0rd}" \
      sqlcmd -b -r 1 -C -S localhost,1433 -U SA -d jore3testdb \
      -i "$sql_file"
  done

  echo "Imported ${#sql_files[@]} Jore 3 SQL file(s) into jore3testdb."
}

start_all() {
  start_deps
  $DOCKER_COMPOSE_CMD up --build -d  jore4-jore3importer
}

start_deps() {
  if [[ ! -d "workdir" ]]; then
    echo "ERROR: workdir must be a copy of or a symlink to jore4-digiroad-importer's workdir" >&2
    exit 1
  fi

  # Runs the following services:
  # importer-jooq-database - The database which contains the information imported and transformed from Jore 3
  # importer-test-destination-database - The test database which contains the information imported and transformed from Jore 3
  # jore4-mssqltestdb - The Jore 3 MSSQL database which contains the source data which is read by the importer
  # jore4-hasura - Hasura. We have to start Hasura because it ensures that db migrations are run to the Jore 4 database.
  # jore4-testdb - Jore 4 database. This is the destination database of the import process.
  $DOCKER_COMPOSE_CMD up --build -d importer-jooq-database importer-test-database jore4-mssqltestdb jore4-hasura jore4-testdb jore4-mapmatchingdb jore4-mapmatching jore4-tiamat jore4-auth jore4-idp jore4-mbtiles jore4-hastus jore4-timetablesapi jore4-ui
}

stop() {
  docker compose --project-name "$COMPOSE_PROJECT_NAME" stop
}

remove() {
  docker compose --project-name "$COMPOSE_PROJECT_NAME" down
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

upload_zones() {
  echo "Uploading municipality and fare zones to Tiamat"

  curl --silent --output /dev/null --show-error --fail -X POST -H"Content-Type: application/xml" -d @netex/hsl-zones-netex.xml localhost:3010/services/stop_places/netex
}

setup_python() {
  ensure_python_venv

  echo "Installing dependencies from ${STOP_REGISTRY_REQUIREMENTS_FILE}..."
  "$STOP_REGISTRY_VENV_PYTHON" -m pip install -r "$STOP_REGISTRY_REQUIREMENTS_FILE"
}

# Ensures the stop-registry virtualenv exists and that pip is up to date.
#
# This is the common entry point for all Python commands: it guarantees that
# subsequent calls to "$STOP_REGISTRY_VENV_PYTHON" use the correct interpreter
# and isolated environment regardless of the host Python setup.
ensure_python_venv() {
  if [ ! -d "$STOP_REGISTRY_VENV_DIR" ]; then
    echo "Creating Python virtualenv in ${STOP_REGISTRY_VENV_DIR}..."
    python3 -m venv "$STOP_REGISTRY_VENV_DIR"
  fi

  if [ ! -x "$STOP_REGISTRY_VENV_PYTHON" ]; then
    echo "ERROR: Python executable not found in virtualenv: ${STOP_REGISTRY_VENV_PYTHON}" >&2
    echo "Try removing ${STOP_REGISTRY_VENV_DIR} and running 'python:setup' again." >&2
    exit 1
  fi

  "$STOP_REGISTRY_VENV_PYTHON" -m pip install --upgrade pip
}

# Recompiles requirements.txt from requirements.in using pip-tools and installs
# the resolved dependency set into the virtualenv.
update_python_requirements() {
  ensure_python_venv

  if [ ! -f "$STOP_REGISTRY_REQUIREMENTS_IN_FILE" ]; then
    echo "ERROR: requirements input file not found: ${STOP_REGISTRY_REQUIREMENTS_IN_FILE}" >&2
    exit 1
  fi

  "$STOP_REGISTRY_VENV_PYTHON" -m pip install --upgrade pip-tools

  echo "Compiling ${STOP_REGISTRY_REQUIREMENTS_FILE} from ${STOP_REGISTRY_REQUIREMENTS_IN_FILE}..."
  "$STOP_REGISTRY_VENV_PYTHON" -m piptools compile \
    --strip-extras \
    --output-file "$STOP_REGISTRY_REQUIREMENTS_FILE" \
    "$STOP_REGISTRY_REQUIREMENTS_IN_FILE"
}

### Control flow

COMMAND=${1:-}

if [[ -z $COMMAND ]]; then
  print_usage
  exit 1
fi

case $COMMAND in
  start)
    download_docker_compose_bundle
    setup_python
    start_all
    upload_zones
    ;;

  start:deps)
    download_docker_compose_bundle
    setup_python
    start_deps
    upload_zones
    ;;

  generate:jooq)
    wait_for_test_databases_to_be_ready
    generate_jooq
    ;;

  python:setup)
    setup_python
    ;;

  python:update-reqs)
    update_python_requirements
    ;;

  stop)
    stop
    ;;

  remove)
    remove
    ;;

  recreate)
    remove
    setup_python
    start_deps
    upload_zones
    ;;

  list)
    $DOCKER_COMPOSE_CMD config --services
    ;;

  infralinks:download)
    download_infralinks
    ;;

  infralinks:seed)
    download_infralinks
    seed_infra_links testdb
    ;;

  jore3:import_sql_files)
    import_jore3_sql_files
    ;;

  infralinks:import_infra_links_from_csv:wipe_database)
    import_infrastructure_wipe_database "$2"
    ;;

  *)
    echo ""
    echo "Unknown command: '${COMMAND}'"
    print_usage
    exit 1
    ;;
esac

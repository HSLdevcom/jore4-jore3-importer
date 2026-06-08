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
ROUTES_DB_CONNECTION_STRING=postgresql://dbadmin:adminpassword@localhost:5432/jore4e2e

DOCKER_COMPOSE_CMD="docker compose -f ./docker/docker-compose.yml -f ./docker/docker-compose.custom.yml"

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
  "
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
  docker exec -i "$1" psql $ROUTES_DB_CONNECTION_STRING < "infraLinks.sql";

  echo "$1: Done Bus seeding infrastructure links."
}

seed_tram_infra_links() {
  download_tram_infralinks

  echo "$1: Seeding Tram infrastructure links..."

  wait_for_test_databases_to_be_ready

  echo "$1: tram_infraLinks.sql..."
  docker exec -i "$1" psql $ROUTES_DB_CONNECTION_STRING < "tram_infraLinks.sql";

  echo "$1: Done Tram seeding infrastructure links."
}

start_all() {
  start_deps
  $DOCKER_COMPOSE_CMD up --build -d  jore4-jore3importer
}

start_deps() {
  # Runs the following services:
  # importer-jooq-database - The database which contains the information imported and transformed from Jore 3
  # importer-test-destination-database - The test database which contains the information imported and transformed from Jore 3
  # jore4-mssqltestdb - The Jore 3 MSSQL database which contains the source data which is read by the importer
  # jore4-hasura - Hasura. We have to start Hasura because it ensures that db migrations are run to the Jore 4 database.
  # jore4-testdb - Jore 4 database. This is the destination database of the import process.
  $DOCKER_COMPOSE_CMD up --build -d importer-jooq-database importer-test-database jore4-mssqltestdb jore4-hasura jore4-testdb jore4-mapmatchingdb jore4-mapmatching jore4-tiamat jore4-auth jore4-idp
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

  *)
    echo ""
    echo "Unknown command: '${COMMAND}'"
    print_usage
    exit 1
    ;;
esac

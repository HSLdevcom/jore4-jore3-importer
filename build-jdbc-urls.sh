#!/bin/sh

set -eu

# we assume here that all secrets are already read to environment variables

# if SOURCE_DB_URL was not set, build it from parts
if [ -z "${SOURCE_DB_URL+x}" ]; then
  export SOURCE_DB_URL="jdbc:sqlserver://${SOURCE_DB_HOSTNAME}:${SOURCE_DB_PORT};database=${SOURCE_DB_DATABASE};applicationIntent=ReadOnly"
fi

# if IMPORTER_DB_URL was not set, build it from parts
if [ -z "${IMPORTER_DB_URL+x}" ]; then
  export IMPORTER_DB_PORT=${IMPORTER_DB_PORT:-"5432"}
  export IMPORTER_DB_URL="jdbc:postgresql://${IMPORTER_DB_HOSTNAME}:${IMPORTER_DB_PORT}/${IMPORTER_DB_DATABASE}?stringtype=unspecified"
fi

#!/bin/sh

set -eu

# we assume here that all secrets are already read to environment variables

# if SOURCE_DB_URL was not set, build it from parts
if [ -z "${SOURCE_DB_URL+x}" ]; then
  export SOURCE_DB_URL="jdbc:sqlserver://${SOURCE_DB_HOSTNAME}:${SOURCE_DB_PORT};database=${SOURCE_DB_DATABASE};applicationIntent=ReadOnly;encrypt=false"
fi

# if IMPORTER_DB_URL was not set, build it from parts
if [ -z "${IMPORTER_DB_URL+x}" ]; then
  export IMPORTER_DB_PORT=${IMPORTER_DB_PORT:-"5432"}
  export IMPORTER_DB_URL="jdbc:postgresql://${IMPORTER_DB_HOSTNAME}:${IMPORTER_DB_PORT}/${IMPORTER_DB_DATABASE}?stringtype=unspecified"
fi

# if JORE4_DB_URL was not set, build it from parts
if [ -z "${JORE4_DB_URL+x}" ]; then
  export JORE4_DB_PORT=${JORE4_DB_PORT:-"5432"}
  export JORE4_DB_URL="jdbc:postgresql://${JORE4_DB_HOSTNAME}:${JORE4_DB_PORT}/${JORE4_DB_DATABASE}?stringtype=unspecified"
fi

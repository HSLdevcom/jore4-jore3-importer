# Jore 3 => Jore 4 -importer

This tool implements a batch job for importing data from a Jore 3 database to a Jore 4 database.

## Running the app locally

The `dev_deps.sh` is a simple utility script for starting the development and test dependencies (e.g. databases). The dependencies must be up-and-running for both running this app locally or running the tests.

Most of the runtime environment is configured in `profiles/dev/config.properties`. However, some values must be supplied manually:

- `JORE_IMPORTER_MIGRATE`: At the moment this app requires that the destination database is already up-to-date (e.g. database migrations are handled by another party). If `JORE_IMPORTER_MIGRATE` is set to `true`, this app will perform the migrations. This should only be enabled in local development when using the dockerized local database!
- `SOURCE_DB_USERNAME`: Username for the Jore 3 database.
- `SOURCE_DB_PASSWORD`: Password for the Jore 3 database.

You can create a local `run.sh` script for supplying these arguments:

```shell
#!/usr/bin/env bash

set -euo pipefail

JORE_IMPORTER_MIGRATE=true \
  SOURCE_DB_USERNAME=change_this \
  SOURCE_DB_PASSWORD=change_this \
  mvn clean spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx128m"

```

## Triggering the batch job

At the moment the batch job triggers automatically once the application starts. Later, mechanisms for manually triggering the batch job over e.g. HTTP will be added.

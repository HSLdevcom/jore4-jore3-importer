# Jore 3 => Jore 4 importer

This tool implements a batch job for importing data from a Jore 3 database to a Jore 4 database.

## Running the app locally

### Setup

1. Install preliminaries
  - OpenJDK 11
1. Make a copy of the maven `dev`-profile for your user:
    ```
    cp profiles/dev/config.properties profiles/dev/config.<my-username>.properties
    ```
1. Adjust the `source.db.*` properties in `profiles/dev/config.<my-username>.properties` to your needs. Other configuration for destination database and test database may also be found from here.
1. If you wish to connect to the original JORE3 database, follow the instructions [here](https://github.com/HSLdevcom/jore4/blob/main/wiki/onboarding.md#creating-an-ssh-configuration-entry) on how to create a tunnel and connect to the database. After the tunnel is created, the jore3 database will be available on localhost:56239. Ask for the username and password from the project team. 

### Run 

The `development.sh` is a simple utility script for starting the development and test dependencies (e.g. databases).

The dependencies must be up and running for both running this app locally or for running tests. Note that for generating the jooq classes for build, the test database must be running. Run `./development.sh start:deps`

Most of the runtime environment is configured in `profiles/dev/config.<my-username>.properties`. However, some values must be supplied manually:

- `JORE_IMPORTER_MIGRATE`: At the moment this app requires that the destination database is already up-to-date (e.g. database migrations are handled by another party). If `JORE_IMPORTER_MIGRATE` is set to `true`, this app will perform the migrations. This should only be enabled in local development when using the dockerized local database!

You can create a local `run.sh` script for supplying these arguments:

```shell
#!/usr/bin/env bash

set -euo pipefail

JORE_IMPORTER_MIGRATE=true \
  mvn clean spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx128m"
```

The importer will be available through http://localhost:8080. See instructions below on how to trigger importing through the HTTP API.

## Running the app in docker-compose

You may wish to test whether the application works as a container in a docker network. Spin up the dependencies and 
the app itself with `development.sh start`

The importer will be available through http://localhost:3200. See instructions below on how to trigger importing through the HTTP API.

## Building the app

The application is using jooq for ORM. To (re)generate the mapping classes, you need to have the ORM source database to
be up and running and jooq connecting to it. `./development.sh generate:jooq` spins up the database and generates the
ORM classes.

Note that the `dev` profile is only meant for use in your local development environment. To create a build to be used for deployment, compile and create a package using the `prod` profile:
```
mvn clean package spring-boot:repackage -Pprod
```



## Running tests

## Triggering the batch job

The import can be triggered using a HTTP API. The `POST /job/import/start` endpoint will start the job and return its status. If a previous job instance was already running, a new job is not started and the return value will reflect the status of the running job.

```shell
$ curl -X POST http://localhost:8080/job/import/start/
{"id":0,"batchStatus":"STARTING","exitCode":"UNKNOWN","exitDescription":null,"startTime":null,"endTime":null}
```

### Querying the status of the latest import

The `GET /job/import/status` endpoint returns information about the latest import. If no import has been performed, a HTTP 204 status is returned.

While the import is running:

```shell
$ curl http://localhost:8080/job/import/status/
{"id":5,"batchStatus":"STARTED","exitCode":"UNKNOWN","exitDescription":null,"startTime":"2021-04-09T08:35:51.560Z","endTime":null}
```

After the import is complete:

```shell
$ curl http://localhost:8080/job/import/status/
{"id":5,"batchStatus":"COMPLETED","exitCode":"COMPLETED","exitDescription":null,"startTime":"2021-04-09T08:35:51.560Z","endTime":"2021-04-09T08:36:11.308Z"}
```

If an error occurs:

```shell
$ curl http://localhost:8080/job/import/status/
{"id":5,"batchStatus":"FAILED","exitCode":"FAILED","exitDescription":"<here's a really long Java stack trace>","startTime":"2021-04-09T08:39:47.698Z","endTime":"2021-04-09T08:41:17.761Z"}
```

## Import job(s)

### Importing Jore 3 data (`importJoreJob`)

![Overview](images/import_jore_job.svg "Job overview")

### Overview of a Generic Job

![objectDiagram](images/job_diagram.svg "Object diagram of a generic job")

#### Importing nodes (`jr_solmu`)

![Overview](images/import_nodes_step.svg "Step overview")

#### Importing links (`jr_linkki`)

![Overview](images/import_links_step.svg "Step overview")

#### Importing points (`jr_piste`)

![Overview](images/import_points_step.svg "Step overview")

#### Importing lines (`jr_linja`)

![Overview](images/import_lines_step.svg "Step overview")

## TODO

### Destination database

- The schema/table/column names in the destination database might need some renaming
- We might do some postprocessing after the import by using _materialized views_ to calculate derived data. For example:
    - Construct _route direction_ geometries by traversing the _route links_->_infrastructure links_->_infrastructure link shapes_ and concatenating the link-wise linestrings into a single linestring.

### Import job

- At the moment we fetch all the data, regardless of how old it is. Most of the time this is not a problem, but for some larger tables the amount of stale old data is significant (e.g. `jr_reitinlinkki` has 3M rows, of which 150k are actually active).
- The Spring Batch jobs/step data is not persisted. See `fi.hsl.jore.importer.config.jobs.BatchConfig`.
- Some of our constraints on the incoming Jore3 data are stricter than the constraints in Jore3 itself. For example, we do not allow multiple _line headers_ to be active for the same _line_ at the same time. So far there have been only a few isolated cases where incorrect data in Jore3 has triggered these constraints and halted the import. This may become an issue when Jore3 and Jore4 are running in production side-by-side and the import is run periodically.
- We use the derived _WGS84_ coordinates (`solomx`, `solstmx`,..) when importing data from Jore 3, instead of the original KKJ2 coordinates (`solx`, `solstx`,..). This might be ok, or it might be better to read the KKJ2 coordinates and convert them ourselves (either in Java or in Postgis). I tried implementing the conversion using `jts` and a few other GIS libraries, but the results were wildly inaccurate.

### Tests

- The integration tests for the import job steps (e.g. `fi.hsl.jore.importer.config.jobs.ImportLinesStepTest`) all create their own Spring test context => A lot of time is spent building and tearing down the context. At the moment the tests use the same starting conditions (the target tables are empty, the source tables are populated with identical data), so we could just run the full import job once and then assert the state of each target table.
- The integration tests for the import job/steps have very little data (`src/test/resources/sql/source/populate_??.sql`).

### Test repositories

- Most of the test repositories (e.g. `fi.hsl.jore.importer.feature.network.line.repository.LineRepository`) have quite a lot of similar code. Using an abstract base class might reduce the amount of duplication but also increase unwanted coupling between the repositories.
- It might be a good idea to implement simple _jOOQ converters_ for the different primary key fields (e.g. `fi.hsl.jore.importer.feature.network.line.dto._LinePK`), which convert the raw `UUID` fields to the corresponding key (`LinePK`). At the moment we do this conversion manually in the repositories (e.g. `.map(row -> LinePK.of(row.value1()))`), but there's always the risk we accidentally `SELECT` the wrong UUID column.

### Misc

- The _plantuml_ charts above do not cover all the import steps.

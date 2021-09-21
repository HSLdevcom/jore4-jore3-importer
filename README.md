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

## Docker reference

The application uses spring boot which allows overwriting configuration properties as described
[here](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.typesafe-configuration-properties.relaxed-binding.environment-variables).
The docker container is also able to
[read secrets](https://github.com/HSLdevcom/jore4-tools#read-secretssh) and expose
them as environment variables.

The following configuration properties are to be defined for each environment:

| Config property         | Environment variable    | Secret name             | Example                                                                          | Description                                                                      |
| ----------------------  | ----------------------- | ----------------------- | -------------------------------------------------------------------------------- | -------------------------------------------------------------------------------- |
| -                       | SECRET_STORE_BASE_PATH  | -                       | /mnt/secrets-store                                                               | Directory containing the docker secrets                                          |
| source.db.url           | SOURCE_DB_URL           | source-db-url           | jdbc:sqlserver://localhost:1433;database=testsourcedb;applicationIntent=ReadOnly | The jdbc url of the source JORE3 MSSQL database                                  |
|                         | SOURCE_DB_HOSTNAME      | source-db-hostname      | localhost                                                                        | The IP/hostname of the source database (if SOURCE_DB_URL is not set)             |
|                         | SOURCE_DB_PORT          | source-db-port          | 1433                                                                             | The port of the source database (if SOURCE_DB_URL is not set)                    |
|                         | SOURCE_DB_DATABASE      | source-db-database      | testsourcedb                                                                     | The name of the source database (if SOURCE_DB_URL is not set)                    |
| source.db.username      | SOURCE_DB_USERNAME      | source-db-username      | sa                                                                               | Username for the source database                                                 |
| source.db.password      | SOURCE_DB_PASSWORD      | source-db-password      | ****                                                                             | Password for the source database                                                 |
| destination.db.url      | DESTINATION_DB_URL      | destination-db-url      | jdbc:postgresql://localhost:5432/devdb?stringtype=unspecified                    | The jdbc url of the destination JORE4 PostgreSQL database                        |
|                         | DESTINATION_DB_HOSTNAME | destination-db-hostname | localhost                                                                        | The IP/hostname of the destination database (if DESTINATION_DB_URL is not set)   |
|                         | DESTINATION_DB_PORT     | destination-db-port     | 5432                                                                             | The port of the destination database (if DESTINATION_DB_URL is not set)          |
|                         | DESTINATION_DB_DATABASE | destination-db-database | devdb                                                                            | The name of the destination database (if DESTINATION_DB_URL is not set)          |
| destination.db.username | DESTINATION_DB_USERNAME | destination-db-username | postgres                                                                         | Username for the destination database                                            |
| destination.db.password | DESTINATION_DB_PASSWORD | destination-db-password | ****                                                                             | Password for the destination database                                            |
| jore.importer.migrate   | JORE_IMPORTER_MIGRATE   | jore-importer-migrate   | false                                                                            | Should the importer should run its own migrations (for local development only)   |

More properties can be found from `/profiles/prod/config.properties`

## Running tests

For running the tests, you need to have the test databases up an running:
```sh
./development.sh start:deps
```

Then start the tests with:
```sh
mvn --batch-mode clean verify
```

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

The import jobs are implemented by using Spring Batch. If you are not familiar with Spring Batch, you should take a
look a the [Spring Batch reference documentation](https://docs.spring.io/spring-batch/docs/4.3.x/reference/html/index.html).

### Importing Jore 3 data (`importJoreJob`)

An import job which imports data from the Jore 3 database has the following steps:

* The `prepareStep` cleans the data found from the staging tables.
* The `importStep` reads the imported data from the source MSSQL database and inserts the imported data into the 
  staging table found from the target PostgreSQL database.
* The `commitStep` moves the data from the staging table to the actual target table.

The following figure identifies the steps of the import jobs:

![Overview](images/import_jore_job.svg "Job overview")

### Overview of a Generic Job

A single Spring Batch job consists of the following components:

* A `Job` contains the steps which are invoked when a batch job is run.
* The `GenericCleanupTasklet` cleans the staging tables before the import process is run. This step 
  is called the prepare step.
* The `JdbcCursorItemReader<ROW>` class reads the imported data from the source MSSQL database by using an SQL
  script which is found from _src/main/resources/import_ directory. This component is run during the import step.
* An implementation of the `ItemProcessor<ROW, ENTITY>` interface transforms the source data into a format which can be inserted into the
  staging table found from the target PostgreSQL database. This component is run during the import step.
* The `GenericImportWriter<ENTITY, KEY>` class writes the imported data into the staging table which is found
  from the target PostgreSQL database. The actual insert logic is found from the implementation of the 
  `IImportRepository<ENTITY,KEY>` interface. This component is also run during the import step.
* The `GenericCommitTasklet` object is run during the import step, and it moves the data from the staging table to the 
  real target table. The logic which moves the imported data is found from the `commitStagingToTarget()` method of the 
  `IImportRepository<ENTITY,KEY>` interface. The implementations of this interface must extend the `AbstractImportRepository<ENTITY,KEY>`
  class which contains three abstract methods:
    * The `delete()` method contains the logic which deletes rows from the target table. A row is deleted from the target
      table if it's found from the target table and it's not found from the staging table.
    * The `insert()` method contains the logic which inserts new rows into the target table. A row is inserted to the target table
      if it's found from the staging table and it's not found from the target table.
    * The `update()` method contains the logic which checks if a row is found from the target and staging tables, and
      replaces the information found from the target table with the information found from the staging table.

The following figure illustrates the responsibilities of these components:

![objectDiagram](images/job_diagram.svg "Object diagram of a generic job")

#### Importing nodes (`jr_solmu`)

![Overview](images/import_nodes_step.svg "Step overview")

#### Importing links (`jr_linkki`)

![Overview](images/import_links_step.svg "Step overview")

#### Importing points (`jr_piste`)

![Overview](images/import_points_step.svg "Step overview")

#### Importing lines (`jr_linja`)

![Overview](images/import_lines_step.svg "Step overview")

## The Structure of the Project

The directory structure of this project follows the [Maven directory layout](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html).

The package structure of this application is described in the following:

* The `fi.hsl.jore.importer.config` package contains the configuration classes which configure the Spring context
  which is started when this application is run. It has the following sub packages:
  * The `fi.hsl.jore.importer.config.jackson` package configures the [Jackson datatype module for Vavr](https://github.com/vavr-io/vavr-jackson).
  * The `fi.hsl.jore.importer.config.jobs` package configures the Spring Batch jobs which import data from the Jore 3 database
    to the Jore 4 database.
  * The `fi.hsl.jore.importer.config.jooq` package configures the jOOQ integration of Spring Boot.
  * The `fi.hsl.jore.importer.config.migration` package configures Flyway which is used to the database migration scripts.
  * The `fi.hsl.jore.importer.config.profile` package specifies the different Spring profiles used by this application.
  * The `fi.hsl.jore.importer.config.properties` package contains configuration read from the properties files.
* The `fi.hsl.jore.importer.feature` package contains the implementation of the import jobs. It has the following
  sub packages:
  * The `fi.hsl.jore.importer.feature.api` package contains the implementation of the REST api which allows you to
    start the import job and query the status of the previous import job.
  * The `fi.hsl.jore.importer.feature.batch` package contains the custom components which are used by Spring Batch.
    These components include tasklets, row mappers, item processors, and item readers.
  * The `fi.hsl.jore.importer.feature.common` package contains general utility code which is used by several other classes.
  * The `fi.hsl.jore.importer.feature.infrastructure` package contains repositories which insert infrastructure data 
    into the target database.
  * The `fi.hsl.jore.importer.feature.jore3` package contains classes which contain the information that's read from the source 
   database.
  * The `fi.hsl.jore.importer.feature.network` package contains repositories which insert network data into the
    target database.
  * The `fi.hsl.jore.importer.feature.system.repository` package contains a repository which allows you to current
    date and time information from the database.
* The `fi.hsl.jore.importer.util` package provide factory methods which allow you to instantiate classes 
  provided by the [JTS topogy suite](https://github.com/locationtech/jts).

The content of the _src/main/resources_ directory is described in the following:

* The _configuration_ directory contains properties files which configure the used database connections 
  and the jOOQ integration of Spring Boot.
* The _db/migration_ directory contains the Flyway database migrations.
* The _import_ directory contains the SQL scripts which read the imported data from the
  source MSSQL database.

## Known Problems

### Test case fails because a database object isn't found

If a test case fails because the `com.microsoft.sqlserver.jdbc.SQLServerException` is thrown and
the error message says that it cannot find a database object, the problem is that the script which
creates the source MSSQL database (_docker/mssql_init/populate.sql_) was changed. You can solve this problem by running the command: 
`./development.sh recreate` at command prompt.

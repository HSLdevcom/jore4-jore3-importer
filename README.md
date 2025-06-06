# Jore 3 => Jore 4 Importer

This application imports scheduled stop points, lines, routes, and journey patterns from the Jore 3 database to the
Jore 4 database.

The import process has two steps:

1. Read the imported data from the Jore 3 database (aka source)
   and insert it into the database of this application (aka importer database) to the staging tables.
   Note that this step saves the version history of the imported data.
2. Read the latest version of the imported data from the staging tables from database of this application
   and insert it into the Jore 4 database (aka target).

## Using the REST API

The importer application has a REST API which allows you to start the import job and query the status of the last import
job. This section describes how you can use this REST API.

### Triggering the Import Job

If you want to start the import job, you have to send a POST request to the path: `/job/import/start`. The API endpoint
will start the job and return its status. If an import job is already running, a new job isn't started and the return
value will reflect the status of the currently running job.

The following example demonstrates how you can start the import job by using curl:

```shell
$ curl -X POST http://localhost:8080/job/import/start
{"id":0,"batchStatus":"STARTING","exitCode":"UNKNOWN","exitDescription":null,"startTime":null,"endTime":null}
```

Note: if using Docker environment, the port is `3004` (by default).

### Querying the Status of the Import Job

If you want to query the status of the latest import job, you have to send a GET request to the path: `/job/import/status`.
The API endpoint returns the status of an ongoing import job or the status of the latest finished import job in case
there is no job currently running. If no import job has been run, the API endpoint returns
the HTTP status code 204.

The following examples demonstrate how you can query the status of the latest import by using curl:

**Example 1: import is running:**

```shell
$ curl http://localhost:8080/job/import/status
{"id":5,"batchStatus":"STARTED","exitCode":"UNKNOWN","exitDescription":null,"startTime":"2021-04-09T08:35:51.560Z","endTime":null}
```

**Example 2: import was completed successfully:**

```shell
$ curl http://localhost:8080/job/import/status
{"id":5,"batchStatus":"COMPLETED","exitCode":"COMPLETED","exitDescription":null,"startTime":"2021-04-09T08:35:51.560Z","endTime":"2021-04-09T08:36:11.308Z"}
```

**Example 3: import failed:**

```shell
$ curl http://localhost:8080/job/import/status/
{"id":5,"batchStatus":"FAILED","exitCode":"FAILED","exitDescription":"<here's a really long Java stack trace>","startTime":"2021-04-09T08:39:47.698Z","endTime":"2021-04-09T08:41:17.761Z"}
```

## The Directory Structure of This Repository

This project uses [the standard directory layout of Maven](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html).
However, the root directory of this repository contains four additional directories which are described in the following:

* The _docker_ directory contains the `docker-compose.custom.yml` file which allows you to override the configuration
  defined in the Docker Compose bundle ([jore4-docker-compose-bundle](https://github.com/HSLdevcom/jore4-docker-compose-bundle)
  repository).
* The _images_ directory contains diagrams displayed on this README.
* The _profiles_ directory contains the Maven profile specific configuration files which are used to configure this application.
* The _testing_ directory contains testing tools (essentially Python scripts) which ensure that the import job is working
  as expected.

The _src/main/resources_ directory contains the resources of our application. To be more specific, it contains
the subdirectories:

* The _configuration_ directory contains the properties files which configure the used database connections
  and the jOOQ integration of Spring Boot.
* The _db/migration_ directory contains the Flyway database migration scripts.
* The _jore4_ directory contains the SQL scripts which read the imported data from importer's PostgreSQL database.
* The _jore3_ directory contains the SQL scripts which read the imported data from the Jore 3 MSSQL database.

### DTO terminology

There are several types of DTOs in this application.
Generally they follow this naming scheme:

- *Jore3* means that the item can be inserted into importer's own database (staging tables).
  Used in the`importStep` of [Jore3 import phase](#importing-data-from-the-jore-3-database-to-the-importers-database)
- *Importer* means that the information is read from the importer's own database and can be exported to an external system.
  Used in the`commitStep` of [Jore3 import phase](#importing-data-from-the-jore-3-database-to-the-importers-database)
- *Jore4* means that the information can be inserted into the Jore4 data model.
  Used in the [Jore4 export phase](#importing-data-from-the-importers-database-to-the-jore-4-database)

## Technical Documentation

This application is basically a Spring Boot application which reads data from the Jore 3 database, makes some
transformations to the data and finally writes it to the Jore 4 database by using Spring Batch library. This
application also provides a REST API which allows you to start the import job and query the status of the import job.

> If you are not familiar with Spring Batch, you should take a
look at the [Spring Batch reference documentation](https://docs.spring.io/spring-batch/docs/4.3.x/reference/html/index.html).

### Package Structure

The package structure of this application is described in the following:

* The `fi.hsl.jore.importer.config` package contains the configuration classes which configure the Spring context
  which is started when this application is run. It has the following sub packages:
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
    * The `fi.hsl.jore.importer.feature.infrastructure` package contains DTOs and repositories which insert infrastructure data
      into the target database.
    * The `fi.hsl.jore.importer.feature.jore3` package contains classes which contain the information that's read from the source
      database.
    * The `fi.hsl.jore.importer.feature.network` package contains DTOs and repositories which insert network data into the
      target database.
    * The `fi.hsl.jore.importer.feature.system.repository` package contains a repository which allows you to current
      date and time information from the database.
    * The `fi.hsl.jore.importer.feature.jore4` package contains entities, repositories, and utility classes which
      are used to insert data into the Jore 4 database.
* The `fi.hsl.jore.importer.util` package provides factory methods which allow you to instantiate classes
  provided by the [JTS topogy suite](https://github.com/locationtech/jts).

### Import Jobs

#### Importing Data From the Jore 3 Database to the Importer's Database

The first part of the import job, which imports data from the Jore 3 database to the importer's database, consists of these flows:

* The `importNodesFlow` flow imports infrastructure nodes from the Jore 3 database to the importer's database.
* The `importLinksFlow` flow imports infrastructure links from the Jore 3 database to the importer's database.
* The `importLinkPointsFlow` flow imports infrastructure link shapes from the Jore 3 database to the importer's database.
* The `importLinesFlow` flow imports lines from the Jore 3 database to the importer's database.
* The `importLineHeadersFlow` flow imports line headers from the Jore 3 database to the importer's database.
* The `importRoutesFlow` flow imports routes from the Jore 3 database to the importer's database.
* The `importRouteDirectionsFlow` flow imports route directions from the Jore 3 database to the importer's database.
* The `importRouteLinksFlow` flow imports route points, route's scheduled stop points, and route links from the Jore 3
  database to the importer's database.
* The `importScheduledStopPointsFlow` flow imports scheduled stop points from the Jore 3 database to the importer's database.

A typical import flow, which imports data from the Jore 3 database to the importer's database, has the following steps:

* The `prepareStep` cleans the data found from the staging tables.
* The `importStep` reads the imported data from the source MSSQL database and inserts the imported data into the
  staging table found from the target PostgreSQL database.
* The `commitStep` moves the data from the staging table to the actual target table.

It's important to understand that the job which import data from the Jore 3 database to the importer's database doesn't follow
[the chunk oriented processing "pattern" of Spring Batch](https://docs.spring.io/spring-batch/docs/current/reference/html/step.html#chunkOrientedProcessing).
Even though these jobs use chunk oriented processing for transferring data from Jore 3 database to importer's staging tables (`importStep`),
these jobs also use a tasklet which copy the imported data from the staging table to the target table. Because the final transfer is
performed inside one transaction (`commitStep`), no information is transferred to the target table if an error occurs during that transaction.

A single Spring Batch flow, which imports data from the Jore 3 database to the importer's database, consists of the following components:

* A `Flow` contains the steps which are invoked when a batch job is run.
* The `GenericCleanupTasklet` cleans the staging tables before the import process is run. This step
  is called the prepare step.
* The `JdbcCursorItemReader<ROW>` class reads the imported data from the source MSSQL database by using an SQL
  script which is found from the _src/main/resources/import_ directory. This component is run during the import step.
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
      Beware that changed rows cannot be properly identified in all tables in the Jore 3 database because of the lack of
      appropriate keys. This means that in some cases a row change is interpreted as a deletion and insertion. One example
      for this is the line header, and this phenomenon had to be taken into account in
      [the related tests](src/test/java/fi/hsl/jore/importer/feature/batch/line_header/support/LineHeaderImportRepositoryTest.java).

The following figure illustrates the relationships of these components:

![objectDiagram](images/job_diagram.svg "Object diagram of a generic job")

The following figure illustrates the steps of a single flow which imports data from the Jore 3 database to the
importer's database:

![Jore 3 Import Job](images/jore3-import-job.png)

#### Importing Data From the Importer's Database to the Jore 4 Database

The second part of the import job imports scheduled stop points, lines, routes, and journey patterns from the importer's
database to the Jore 4 database. The steps of this import flow (`jore4ExportFlow`) follow the
[chunk oriented processing "pattern" of Spring Batch](https://docs.spring.io/spring-batch/docs/current/reference/html/step.html#chunkOrientedProcessing).
The import flow consists of the following steps:

* The `prepareJore4ExportStep` step deletes the data found from the target tables.
* The `exportTimingPlacesStep` step extracts unique Hastus place IDs from the scheduled stop points of the importer's database to the Jore 4 database.
* The `exportScheduledStopPointsStep` step imports scheduled stop points from the importer's database to the Jore 4 database.
  See also: [the non-obvious assumptions](#scheduled-stop-points).
* The `exportLinesStep` step imports lines from the importer's database to the Jore 4 database.
  See also: [the non-obvious assumptions](#lines).
* The `exportRoutesStep` step imports route metadata from the importer's database to the Jore 4 database. This step
  imports a route metadata to the Jore 4 database only if the line which owns the processed route metadata was imported
  to the Jore 4 database by the `exportLinesStep` step. See also: [the non-obvious assumptions](#routes).
* The `exportRouteGeometriesStep` step imports route geometries from the importer's database to the Jore 4 database. Note
  that this step imports only the route geometries of route metadatas which were imported to the Jore 4 database by
  the `exportRoutesStep` step.
* The `exportJourneyPatternsStep` step imports journey pattern metadata (not including actual stop point sequences) from the
  importer's database to the Jore 4 database. This step creates one journey pattern per route metadata which was imported
  to the Jore 4 database by the `exportRoutesStep` step.
* The `exportJourneyPatternStopsStep`  step imports the sequence of scheduled stop point references for each journey pattern
  from the importer's database to the Jore 4 database. Note that this step processes the scheduled stop points of a journey
  pattern only if the journey pattern was imported to the Jore 4 database by the `exportJourneyPatternsStep` step.
  See also: [the non-obvious assumptions](#stop-points-of-journey-pattern).

A single Spring Batch `Step` which imports data from the importer's database to the Jore 4 database consists of
these three components:

* An `ItemReader<INPUT>` object reads the input data from the importer's database. This application uses the
  `JdbcCursorItemReader<ROW>` class which reads the input data by using an SQL script which is found from the
  _src/main/resources/export_ directory.
* An `ItemProcessor<INPUT, OUTPUT>` object transforms the input data into a format which can be inserted into the
  Jore 4 database. It also generates an UUID which is the primary key of the row inserted to the Jore 4 database
  in the next process of this step.
* An `ItemWriter<OUTPUT>` object inserts the imported data into the Jore 4 database. If the data was inserted into the
  Jore 4 database, the `ItemWriter<OUTPUT>` object sets the Jore 4 ids of exported rows (performs an `UPDATE`
  statement to the importer's database). Currently, not all database constraints of Jore4 database are implemented or
  taken into account in internal data processing logic of Importer. Hence, writing a data item into Jore4 database may
  fail because of a database constraint violation. As a consequence of constraint violations, subsequent steps of Spring
  Batch run may see a reduced number of data items remaining to be processed.

The following figure illustrates the responsibilities of these components:

![Jore 4 Import Job](images/jore4-import-job.png)

Every step which imports data to the Jore 4 database inserts the imported data into the Jore 4 database one row at
the time. This approach is slower than using a "larger" chunk size, but it also ensures that we can ignore erroneous
rows without losing any other data. If an error occurs, the erroneous row is written to the log and the import process starts to process the next row
found from the importer's database.

The following sections identify the non-obvious assumptions made by the import process.

##### Scheduled Stop Points

The process that imports scheduled stop points to Jore 4 follows these rules:

* If the ELY number of a scheduled stop point isn't found from the database of the importer, it won't be transferred
  to Jore 4.
* The import process ignores Digiroad stop points which have invalid information. Note that typically the CSV file
  which contains the information of Digiroad stop points doesn't contain invalid data. This check was added because
  we need to ensure that the importer can be run successfully even if the importer cannot read the information of a
  single scheduled stop point for some reason.
* The imported stop points are sorted in ascending the order by using the external id (Jore 3 id). If multiple scheduled
  stop points have the same short id, the exported information is selected by using these rules:
  * The external ids of these stop points are added to a comma separated string.
  * The ELY numbers of these stop points are added to a comma separated string.
  * The location, name, and short id of the first stop point are exported to Jore 4.
* When the import process queries the stop point information from Digiroad, it follows these rules:
  * It iterates all ELY numbers and uses the first stop point whose information is found from Digiroad. The information
    of the found Digiroad stop point is combined with the data read from the importer's database. The combined stop point
    data is imported to the Jore 4 database. For each stop point the collected data items from Digiroad are: (1) the
    Digiroad ID of the infrastructure link along which the stop point is located and (2) the direction of traffic with
    regard to direction of the linestring geometry of the associated infrastructure link (reversed or not). All the
    other data items originate from Jore3.
  * If the import process has iterated all ELY numbers and none of them was found from the Digiroad data, the importer
    ignores the processed stop point and won't transfer its information to the Jore 4 database.

See the _/src/resources/jore4-export/export_scheduled_stop_points.sql_ file for more details.

##### Lines

* If multiple lines with the same label and priority have overlapping validity periods, only one line is inserted into
  the Jore 4 database. The inserted line is the first line found from the query results of the SQL query which selects
  the source data from the importer's database. At the moment, the exported lines are ordered in descending order by
  using the value of the `network_line_header_valid_date_range` column as a sort criteria.

See the _/src/resources/jore4-export/export_lines.sql_ file for more details.

##### Routes

* When the importer queries the start and end stop points of an exported route from the importer's database, it
  will "group" stop points by using short id and selects the stop point that was exported previously to Jore 4 database.
* If multiple routes with same label and priority have overlapping validity periods, only one route is inserted into
  the Jore 4 database. The inserted route is the first route found from the query results of the SQL query which selects
  the source data from the importer's database. At the moment, the exported routes are ordered in descending order by
  using the value of the `network_line_header_valid_date_range` column as a sort criteria.
* The priority of a route must be higher or equal than the priority of the line which owns the route. If this isn't the
  case, the route in question cannot be inserted into the Jore 4 database.
* A route will be transferred to the Jore 4 database only if the processed route is valid at 1.1.2021 or it will be valid
  after that date. If you change the valid date range found from the SQL query (_/src/main/resources/jore4-export/export_routes.sql_),
  you should remember to make the required changes to the route data sets found from the _src/test/resources/sql/destination_ directory.

See the _/src/resources/jore4-export/export_routes.sql_ file for more details.

##### Stop Points of Journey Pattern

* When the importer queries the stop points of a journey pattern from the importer's database, it
  will "group" stop points by using short id and for each group selects the one that was exported previously
  to Jore 4 database.

See the _/src/resources/jore4-export/export_stops_of_journey_patterns.sql_ file for more details.

## Developer Guide

### Coding Conventions

This section identifies the coding conventions which you must follow when you are writing either production or test code
for this project. These coding conventions are described in the following:

* You must use the `final` keyword when you declare fields, local variables, constructor arguments, or method parameters
  whose value cannot be changed after it has been assigned for the first time.
* When you add new fields to entities or data transfer objects, you must follow these
  rules:
    * If the field value cannot be `null`, you must use primitive types when possible (e.g. `int`).
    * If the field value is optional, you must use `java.util.Optional` (e.g. `Optional<Integer>` or `Optional<String>`)
* Tag nullable method parameters and return values with the `@Nullable` annotation. You don't have to annotate non-null
  parameters and return values because every method parameter and return value is non-null by default (see
  the next coding convention).
* Every package must include the `package-info.java` file which declares that every field, method parameter, or
  return value is non-null by default (see the code example 1 for more details).
* Use only immutable DTO's by introducing interfaces annotated with `org.immutables` annotations.
* You must, by default, not mutate any Collections, unless you have created it yourself and perform the mutations within
  a single function/scope. Stream's map & filter functions should handle most cases and more utils can be found from
  `JoreCollectionUtils` file, as well as from Guava. One should also prefer Collection constructors that return
  unmodifiable collections such as: List.of(), Set.of(), Map.of(), Stream::toList(), …; If you need to construct a
  collection with mutable version, you can make it unmodifiable with `Collections.unmodifiable[List,Set,Map]` methods.
  Guavas ImmutableList/ImmutableSet/… Collections are also valid.

**Code example 1: package-info.java**

```
@NonNullApi
@NonNullFields
fi.hsl.jore.foo.bar

import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;
```

### Naming Conventions

DTO interfaces are named according to which database they are used with.

* `Jore3` prefix is a DTO which represents data from the Jore3 database
* `Importer` prefix is a DTO which represents data in the importer's own database
* `Jore4` prefix is a DTO which represents data which can be inserted into the Jore4 database.

### Configuration

The importer application is configured by using profile specific configuration files. These configuration files are
found from the profile specific directories which are found from the _profiles_ directory. At the moment this application
has the following profiles:

* `ci`. This profile is used by the Github Actions workflows which run our CI jobs.
* `dev`. This profile should be used in the local development environment. This is also the
  default profile which is used by Maven if the active profile isn't specified.
* `prod`. This profile is used by the Docker image which runs the importer application.

Each one of these directories contains one _config.properties_ file which contains the profile
specific configuration of the importer application. The different configuration options are described
in the _profiles/dev/config.properties_ file.

### Docker reference

The application uses spring boot which allows overwriting configuration properties as described
[here](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.typesafe-configuration-properties.relaxed-binding.environment-variables).
The docker container is also able to
[read secrets](https://github.com/HSLdevcom/jore4-tools#read-secretssh) and expose
them as environment variables.

The following configuration properties are to be defined for each environment:

| Config property            | Environment variable       | Secret name                | Example                                                                                 | Description                                                                    |
|----------------------------|----------------------------|----------------------------|-----------------------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| -                          | SECRET_STORE_BASE_PATH     | -                          | /mnt/secrets-store                                                                      | Directory containing the docker secrets                                        |
| source.db.url              | SOURCE_DB_URL              | source-db-url              | jdbc:sqlserver://localhost:1433;database=testsourcedb;applicationIntent=ReadOnly        | The jdbc url of the source JORE3 MSSQL database                                |
|                            | SOURCE_DB_HOSTNAME         | source-db-hostname         | localhost                                                                               | The IP/hostname of the source database (if SOURCE_DB_URL is not set)           |
|                            | SOURCE_DB_PORT             | source-db-port             | 1433                                                                                    | The port of the source database (if SOURCE_DB_URL is not set)                  |
|                            | SOURCE_DB_DATABASE         | source-db-database         | testsourcedb                                                                            | The name of the source database (if SOURCE_DB_URL is not set)                  |
| source.db.username         | SOURCE_DB_USERNAME         | source-db-username         | sa                                                                                      | Username for the source database                                               |
| source.db.password         | SOURCE_DB_PASSWORD         | source-db-password         | \*\*\*\*                                                                                | Password for the source database                                               |
| importer.db.url            | IMPORTER_DB_URL            | importer-db-url            | jdbc:postgresql://localhost:5432/devdb?stringtype=unspecified                           | The jdbc url of the importer's PostgreSQL database                             |
|                            | IMPORTER_DB_HOSTNAME       | importer-db-hostname       | localhost                                                                               | The IP/hostname of the importer's database (if IMPORTER_DB_URL is not set)     |
|                            | IMPORTER_DB_PORT           | importer-db-port           | 5432                                                                                    | The port of the importer's database (if IMPORTER_DB_URL is not set)            |
|                            | IMPORTER_DB_DATABASE       | importer-db-database       | devdb                                                                                   | The name of the importer's database (if IMPORTER_DB_URL is not set)            |
| importer.db.username       | IMPORTER_DB_USERNAME       | importer-db-username       | postgres                                                                                | Username for the importer's database                                           |
| importer.db.password       | IMPORTER_DB_PASSWORD       | importer-db-password       | \*\*\*\*                                                                                | Password for the importer's database                                           |
| jore4.db.url               | JORE4_DB_URL               | jore4-db-url               | jdbc:postgresql://localhost:5432/jore4e2e?stringtype=unspecified                        | The jdbc url of the jore4 target PostgreSQL database                           |
|                            | JORE4_DB_HOSTNAME          | jore4-db-hostname          | localhost                                                                               | The IP/hostname of the jore4 target database (if JORE4_DB_URL is not set)      |
|                            | JORE4_DB_PORT              | jore4-db-port              | 5432                                                                                    | The port of the jore4 target (if JORE4_DB_URL is not set)                      |
|                            | JORE4_DB_DATABASE          | jore4-db-database          | jore4e2e                                                                                | The name of the jore4 target (if JORE4_DB_URL is not set)                      |
| jore4.db.username          | JORE4_DB_USERNAME          | jore4-db-username          | dbimporter                                                                              | Username for the jore4 target                                                  |
| jore4.db.password          | JORE4_DB_PASSWORD          | jore4-db-password          | \*\*\*\*                                                                                | Password for the jore4 target                                                  |
| digiroad.stop.csv.file.url | DIGIROAD_STOP_CSV_FILE_URL | digiroad-stop-csv-file-url | https://jore4storage.blob.core.windows.net/jore4-digiroad/digiroad_stops_2022_06_08.csv | Url of the digiroad stops csv file to be downloaded                            |
| jore.importer.migrate      | JORE_IMPORTER_MIGRATE      | jore-importer-migrate      | false                                                                                   | Should the importer should run its own migrations (for local development only) |
| map.matching.api.baseUrl   | MAP_MATCHING_API_BASEURL   | map-matching-api-baseurl   | https://localhost:3005                                                                  | The base url of the map matching API.                                          |

More properties can be found from `/profiles/prod/config.properties`

### Setting Up the Local Development Environment

Before you can run the application in your local development environment, you have to set it up by following these steps:

1. Install software required to compile and run the importer application and its dependencies.
   Before you can run this application, you must install these tools:
    - Docker Desktop
    - OpenJDK 17
    - Maven
2. Make a copy of the `.properties` file under the Maven `dev` profile for your user account:
    ```shell
    cp profiles/dev/config.properties profiles/dev/config.<my-username>.properties
    ```
   Look up the Jore 3 database credentials from Azure Key Vault (`hsl-jore3-db-username` and `hsl-jore3-db-password`
   in `kv-jore4-dev-001` under the `rg-jore4-dev-001` resource group) and apply them to the `source.db.XXX` properties
   in your personal `.properties` file.
3. Adjust the other `source.db.*` properties in `profiles/dev/config.<my-username>.properties` to your needs.
   Other configuration for destination database and test database may also be found from here.
4. If you wish to connect to the original Jore 3 database, follow the general Jore4 Azure instructions in Azure wiki
   (non-public) on how to create an SSH tunnel and connect to the database. After the tunnel is created, the Jore 3
   database will be available on localhost:56239. During the importer run, the shell on the bastion host needs to be
   "touched" in regular intervals to keep it from timing out. (The `TMOUT` environment variable on the bastion host
   cannot be modified.) "Touching" can be done manually by issuing key presses into the shell every few minutes. 
   Alternatively, you can start a new subshell with the timeout disabled to keep the session open:
    ```shell
    env TMOUT=0 bash
    ```
5. Set up infrastructure links and stop points data:
   1. Import data from Digiroad:
      1. Clone the [jore4-digiroad-import](https://github.com/HSLdevcom/jore4-digiroad-import) repository.
      2. Run the Digiroad import (`import_digiroad_shapefiles.sh`).
         See [the related README section](https://github.com/HSLdevcom/jore4-digiroad-import#importing-data-from-digiroad).
      3. Export infrastructure links from the data imported from Digiroad (`export_infra_network_csv.sh`).
         See [the related README section](https://github.com/HSLdevcom/jore4-digiroad-import#exporting-infrastructure-links-for-jore4).
         Then, with `jore3-jore4-importer` dependencies set up,
         import these infrastructure links to the Jore 4 database (`import_infra_network_csv.sh`).
      4. Stop points are fetched from a remote URL during the import process.
         If needed, run the scheduled stop point CSV export (`export_stops_csv.sh`)
         and upload the result CSV file eg. to `jore4storage` in Azure.
   2. In the profile-specific configuration file, set `digiroad.stop.csv.file.url` to the Digiroad stops CSV to use
      (eg. from `hsl-jore4-common / jore4storage / jore4-digiroad / digiroad_stops_XXX.csv`)

### Running the Tests

When you want to run the test, you have to follow these steps:

1. Run the dependencies of this application by using the command: `./development.sh start:deps`.
2. Run the tests by running the command: `mvn --batch-mode clean verify`.

### Running the Application

When you want to run this application, you can use one of these two options:

**First**, you can run the dependencies of this application with Docker and run the importer by using the
Spring Boot Maven plugin. This is useful if you want to get an easy access to the log files written by importer.
If you want to use this option, you have to follow these steps:

1. Run the dependencies of this application by running the command: `./development.sh start:deps`.
   (This will bind a volume for the testdb container so that the imported data won't be lost during sessions. If you wish not to do so, run with with `--no-volume` parameter)
2. Run the application by running the command: `./run-local.sh`.

**Second**, you can run everything with Docker. If you want to use this option, you have to run the command:
`./development.sh start`.

### Packaging the Application

If you want to create a package that can be used for deployment, you have run the command: `mvn clean package spring-boot:repackage -P prod`

### Restoring a Jore 3 Database Dump to the Testing Database

If you want to restore a database dump from Jore 3 to the testing database, there is a script provided for it.
Put the jore3 .bak file to the `jore3dump` directory and run `./apply-jore3-database-backup.sh jore3dump.bak`,
where `jore3dump.bak` should be replaced with the name of the .bak file you want to apply.

Some dump files can be found in [Google Drive](https://drive.google.com/drive/folders/1oTfv8vgM7nqg9Hkg5DkSoLTsc8MUAf-s).

### Taking a Database Dump from the Jore 4 Database

When you want to take a database dump from the Jore 4 database, you can use one of these two options:

**First**, If you want the database dump by using the graphical pgAdmin tool, you should use the custom format and ensure
that pgAdmin uses the default settings when it takes the database dump.

**Second**, if you want to use the pg_dump command line utility, you should run the following command at command prompt:

    pg_dump --file [file path]  --host [host] --port [port] --username [username] --format=c --blobs [database name]

### Restoring a Jore 4 Database Dump

#### Local Development Environment

If you want to restore a database dump into your local development database, you can run the following command:

```shell
docker exec -i testdb bash -c "
  set -eux
  dropdb --username=dbadmin --force jore4e2e
  pg_restore --username=dbadmin --dbname=postgres --format=custom --create
" < jore4e2e.pgdump
```

In the above example, we are recreating the `jore4e2e` database with a full custom-format database dump.

Typically, dumps are created either manually using the command in the [previous section](#taking-a-database-dump-from-the-jore-4-database),
or you can fetch one from Azure Blob storage.

## Known Problems

### Test case fails because a database object isn't found

If a test case fails because the `com.microsoft.sqlserver.jdbc.SQLServerException` is thrown and
the error message says that it cannot find a database object, the problem is that the script which
creates the source MSSQL database (_docker/mssql_init/populate.sql_) was changed. You can solve this problem by running the command:
`./development.sh recreate` at command prompt.

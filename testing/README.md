# Testing Tools

This directory contains a bunch of Python scripts which help you to ensure that the 
import job is working as expected. The testing process has four steps:

1. [Run the import batch job](https://github.com/HSLdevcom/jore4-jore3-importer/blob/main/README.md).
2. Export the source data from the Microsoft SQL server database to a CSV file. If you want to use the Jore3 dev
   as a source database, you must ensure that you can [connect to the Azure environment via bastion host](https://github.com/HSLdevcom/jore4/blob/main/wiki/onboarding.md#connecting-to-the-azure-environment-via-bastion-host).
3. Export the target data from the PostgreSQL database to a CSV file. If you want to use the dockerized PostgreSQL database
   as a target database, you must start it by running the command: `./development.sh start:deps` at command prompt. 
4. Run the test script which compares the source CSV file with the target CSV file.

This README describes how you can complete these steps.

## Installing the Required Prerequisites

Before you can run these testing scripts, you have to install the required prerequisites 
by following these steps:

1. [Install Docker Desktop](https://www.docker.com/products/docker-desktop)
2. [Install SQL server command line tools](https://docs.microsoft.com/en-us/sql/linux/sql-server-linux-setup-tools?view=sql-server-ver15)
3. Install the PostgreSQL command line client (psql) and ensure that it's found from the PATH.
4. Install Python 3 ([MacOS](https://opensource.com/article/19/5/python-3-default-mac), [Windows](https://docs.python.org/3/using/windows.html#using-python-on-windows)).

## Running the test scripts

The test scripts found from this directory are tools which must be run manually when you want to
ensure that the import job has finished successfully. In other words, these test scripts assume that:

* You have access to a Microsoft SQL server database which contains the source data (Jore3 data) and you have configured the import
  job to read the imported data from this database.
* You have access to a PostgreSQL database which contains  the Jore4 data model and you have configured the import job
  to write the processed data to this database.
* [You have invoked the batch job](https://github.com/HSLdevcom/jore4-jore3-importer/blob/main/README.md) which reads data from the Jore3 database and writes data to the Jore4 database.

The actual test process has four steps:

1. You have to go to the _testing_ directory.
2. You have to export the valid source data from the source database to a CSV file. If you want to use Jore3 dev database as a source database, you must [connect to the Azure environment via bastion host](https://github.com/HSLdevcom/jore4/blob/main/wiki/onboarding.md#connecting-to-the-azure-environment-via-bastion-host)
   before you export the source data.
3. You have to export the imported data from the target database to a CSV file.
4. You have to run the test script which ensures that the expected data was imported to the target database. This
   script compares the contents of the CSV file exported from the source database with the
   contents of the CSV file exported from the target database and reports the errors found from the target CSV file.

The placeholders found from the next examples are described in the following:

* The `[host]` contains the host of the database.
* The `[port]` contains the port of the database.
* The `[username]` contains the name of the database user.
* The `[database]` contains the name of the database.

### Infrastructure Nodes

This test script (_infrastructure_node_test.py_) ensures that:

* The valid node objects which are read from the `jr_solmu` table of the Jore3 database are processed correctly.
* The processed data is written to the `infrastructure_network.infrastructure_nodes` database table which is found from
  the Jore4 database.

You can run this test script by following these steps:

**First**, you have to get the valid source data by running the following command at command prompt:

```
sqlcmd -S [host],[port] -d [database] -U [username] -Q "SELECT n.soltunnus AS soltunnus,n.soltyyppi AS soltyyppi,
n.solomx AS solomx,n.solomy AS solomy,n.solstmx AS solstmx,n.solstmy AS solstmy FROM jr_solmu n 
WHERE n.solomx IS NOT NULL AND n.solomy IS NOT NULL AND n.solstmx IS NOT NULL AND n.solstmy IS NOT NULL 
ORDER BY n.soltunnus ASC" -o "infrastructure_nodes_source.csv" -s"," -w 700 -W
```

**Second**, you have to get the imported data by running the following command at command prompt:

```
psql -h [host] -p [port] -U [username] [database] -A -F"," -P null='NULL' -c "SELECT infrastructure_node_ext_id AS soltunnus, 
infrastructure_node_type AS soltyyppi, ST_Y(infrastructure_node_location) AS solomx, 
ST_X(infrastructure_node_location) AS solomy, ST_Y(infrastructure_node_projected_location) AS solstmx, 
ST_X(infrastructure_node_projected_location) AS solstmy FROM infrastructure_network.infrastructure_nodes 
ORDER BY infrastructure_node_ext_id ASC" > infrastructure_nodes_target.csv
```

**Third**, You have to run the test script by running the command: `python infrastructure_node_test.py` at command prompt.

### Infrastructure Links

This test script (_infrastructure_link_test.py_) ensures that:

* The valid link objects which are read from the `jr_linkki` and `jr_solmu` tables of the Jore3 database are processed correctly.
* The processed data is written to the `infrastructure_network.infrastructure_links` database table which is found from
  the Jore4 database.

You can run this test script by following these steps:

**First**, you have to get the valid source data by running the following command at command prompt:

```
sqlcmd -S [host],[port] -d [database] -U [username] -Q "SELECT CONCAT(l.lnkverkko, '-', sa.soltunnus, '-', sb.soltunnus) AS lnk_id, 
l.lnkverkko, sa.soltunnus AS alku_soltunnus, sa.solomx AS alku_solomx, sa.solomy AS alku_solomy, sb.soltunnus AS loppu_soltunnus,
sb.solomx AS loppu_solomx, sb.solomy AS loppu_solomy FROM jr_linkki l LEFT JOIN jr_solmu sa ON sa.soltunnus = l.lnkalkusolmu 
LEFT JOIN jr_solmu sb ON sb.soltunnus = l.lnkloppusolmu WHERE sa.solomx IS NOT NULL AND sa.solomy IS NOT NULL AND sa.solstmx IS NOT NULL 
AND sa.solstmy IS NOT NULL AND sb.solomx IS NOT NULL AND sb.solomy IS NOT NULL AND sb.solstmx IS NOT NULL AND sb.solstmy IS NOT NULL 
ORDER BY lnk_id ASC;" -o "infrastructure_links_source.csv" -s"," -w 700 -W
```

**Second**, you have to get the imported data by running the following command at command prompt:

```
psql -h [host] -p [port] -U [username] [database] -A -F"," -P null='NULL' -c "SELECT l.infrastructure_link_ext_id AS lnk_id, 
l.infrastructure_network_type AS lnkverkko, nb.infrastructure_node_ext_id AS alku_soltunnus, 
ST_Y(ST_StartPoint(l.infrastructure_link_geog::geometry)) AS alku_solomx, 
ST_X(ST_StartPoint(l.infrastructure_link_geog::geometry)) AS alku_solomy, 
ne.infrastructure_node_ext_id AS loppu_soltunnus, ST_Y(ST_EndPoint(l.infrastructure_link_geog::geometry)) AS loppu_solomx, 
ST_X(ST_EndPoint(l.infrastructure_link_geog::geometry)) AS loppu_solomy FROM infrastructure_network.infrastructure_links l 
JOIN infrastructure_network.infrastructure_nodes nb ON (nb.infrastructure_node_id=l.infrastructure_link_start_node) 
JOIN infrastructure_network.infrastructure_nodes ne ON (ne.infrastructure_node_id=l.infrastructure_link_end_node) 
ORDER BY infrastructure_link_ext_id ASC" > infrastructure_links_target.csv
```

**Third**, You have to run the test script by running the command: `python infrastructure_link_test.py` at command prompt.

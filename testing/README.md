# Testing Tools

This directory contains a bunch of Python scripts which help you to ensure that the 
import job is working as expected. The testing process has three steps:

1. Export the source data from the Microsoft SQL server database to a CSV file.
2. Export the target data from the PostgreSQL database to a CSV file.
3. Run the test script which compares the source CSV file with the target CSV file.

This README describes how you can complete all three steps.

## Installing the Required Prerequisites

Before you can run these testing scripts, you have to install the required prerequisites 
by following these steps:

1. [Install SQL server command line tools](https://docs.microsoft.com/en-us/sql/linux/sql-server-linux-setup-tools?view=sql-server-ver15)
2. Install the PostgreSQL command line client (psql) and ensure that it's found from the PATH.
3. Install Python 3 ([MacOS](https://opensource.com/article/19/5/python-3-default-mac), [Windows](https://docs.python.org/3/using/windows.html#using-python-on-windows)).

## Running the test scripts

### Infrastructure Nodes

This test script (_infstructure_node_test.py_) ensures that:

* The data which is read from the `jr_solmu` database table is processed correctly.
* The processed data is written to the `infrastructure_network.infrastructure_nodes` database table.

You can run this test script by following these steps:

**First**, you have to export the source data to a CSV file. You can do this by running the following
command at command prompt when you are in the _testing_ directory:

```
sqlcmd -S localhost,56239 -d joretest -U joretest_reader -Q "SELECT n.soltunnus AS soltunnus,n.soltyyppi AS soltyyppi,
n.solomx AS solomx,n.solomy AS solomy,n.solstmx AS solstmx,n.solstmy AS solstmy FROM jr_solmu n 
WHERE n.solomx IS NOT NULL AND n.solomy IS NOT NULL AND n.solstmx IS NOT NULL AND n.solstmy IS NOT NULL 
ORDER BY n.soltunnus ASC" -o "infrastructure_nodes_source.csv" -s"," -w 700 -W
```

**Second**, you have to export the target data to a CSV file. You can do this by running the following
command at command prompt when you are in the _testing_ directory:

```
psql -h localhost -p 16000 -U devdb devdb -A -F"," -P null='NULL' -c "SELECT infrastructure_node_ext_id AS soltunnus, 
infrastructure_node_type AS soltyyppi, ST_Y(infrastructure_node_location) AS solomx, 
ST_X(infrastructure_node_location) AS solomy, ST_Y(infrastructure_node_projected_location) AS solstmx, 
ST_X(infrastructure_node_projected_location) AS solstmy FROM infrastructure_network.infrastructure_nodes 
ORDER BY infrastructure_node_ext_id ASC" > infrastructure_nodes_target.csv
```

**Third**, you have to run the script which ensures that source data was imported to the target database. You can do
this by running the command: ` python infrastructure_node_test.py` at command prompt when you are in the _testing_ directory.
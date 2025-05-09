Generate input data for bulk-map-matching all JORE3 bus routes to JORE4
===

The `generate_mapmatching_input_for_active_route_directions.sh` script file is used to create a CSV
file containing input data for bulk-map-matching all JORE3 bus routes to JORE4 road infrastructure.

Before running this script, the Docker container "importer-database" must be up and running and all
jore3-importer import steps must be completed so that the jore3-importer's internal (intermediary)
database has data from which to generate the map-matching input data.

The actual code that uses this CSV data is in the `bulk-test` branch of
[jore4-map-matching](https://github.com/HSLdevcom/jore4-map-matching) repository.

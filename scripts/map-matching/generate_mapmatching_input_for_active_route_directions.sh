#!/usr/bin/env bash

# Execute the SQL command inside the "importer-database" container.
cat generate_mapmatching_input_for_active_route_directions.sql | docker exec -i importer-database sh -c \
  'psql -U devdb -d devdb > mapmatching_input_for_active_route_directions.csv'

# Copy the result file to host file system.
docker cp importer-database:mapmatching_input_for_active_route_directions.csv ./mapmatching_input_for_active_route_directions.csv

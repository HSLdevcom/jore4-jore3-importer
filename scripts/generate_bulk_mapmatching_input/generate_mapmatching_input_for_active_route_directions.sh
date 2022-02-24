#!/usr/bin/env bash

psql \
  -h localhost \
  -p 16000 \
  -U devdb \
  -d devdb \
  -v ON_ERROR_STOP=1 \
  -f ./generate_mapmatching_input_for_active_route_directions.sql \
  -o ./mapmatching_input_for_active_route_directions-$(date "+%Y-%m-%d").csv

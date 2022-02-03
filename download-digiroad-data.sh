#!/bin/sh

set -eu

# download Digiroad routing dump at startup
echo 'Downloading digiroad stops data'
curl -o /tmp/digiroad_stops.csv \
    https://jore4storage.blob.core.windows.net/jore4-digiroad/digiroad_stops_${DIGIROAD_STOPS_CSV_VERSION}.csv

#!/usr/bin/env bash

docker build -t stop-registry-importer .
docker run --network="host" stop-registry-importer

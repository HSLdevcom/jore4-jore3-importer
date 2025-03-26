#!/usr/bin/env bash

docker build -t stop-importer .
docker run --network="host" stop-importer
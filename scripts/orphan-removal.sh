#!/usr/bin/env bash

set -eu

SCRIPT_DIR="$(dirname "$(realpath "$0")")"
psql -h localhost -p 6432 -d jore4e2e -U dbimporter -f "$SCRIPT_DIR/orphan-removal.sql"

#!/bin/bash

set -eu

/usr/src/app/wait_and_populate.sh &
/opt/mssql/bin/sqlservr

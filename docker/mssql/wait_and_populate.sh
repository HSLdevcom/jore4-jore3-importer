#!/bin/bash

set -eu

echo "Waiting for MS SQL to start.."

/usr/src/app/wait-for-it.sh --host=localhost --port=1433 --timeout=30

# We must wait a few additional seconds, otherwise login might fail
# with "Error: 18456, Severity: 14, State: 7."
sleep 5s

echo "Initialize the MS SQL database contents.."

/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P $SA_PASSWORD -d master -i populate.sql

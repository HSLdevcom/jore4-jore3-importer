#!/bin/bash

set -eu

echo "Waiting 15s for MS SQL to start.."

sleep 15s

echo "Initialize the MS SQL database contents.."

/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P $SA_PASSWORD -d master -i populate.sql

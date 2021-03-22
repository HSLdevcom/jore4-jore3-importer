#!/bin/bash

set -eu

echo "Waiting 15s for MS SQL to start.."

sleep 15s

echo "Populating DB with sample data.."

/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P $SA_PASSWORD -d master -i populate.sql

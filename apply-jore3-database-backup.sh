#!/usr/bin/env bash

if [[ -z ${1} ]]; then
  echo "Usage: ${0} FILE.bak"
  echo "Applies the given .bak file to the running jore3testdb."
  echo "Where FILE.bak is a .bak in jore3dump directory, while which contains the relevant database dump."
  echo "User needs to supply SA user password when restoring the backup."
  exit 1
fi

docker exec -it mssqltestdb /opt/mssql-tools/bin/sqlcmd -S localhost -U SA -Q "RESTORE DATABASE [jore3testdb] FROM DISK = N'/mnt/jore3dump/${1}' WITH FILE = 1, NOUNLOAD, REPLACE, RECOVERY, STATS = 5"
#!/usr/bin/env bash

set -eu

# Default target directory for dump files
# It is jore-data/database_dumps, where jore-data is next to the project directory.
# The three dirname calls each remove a segment from the end of the path; first the script name, then 'scripts'
# directory, and finally the project directory.
TARGET_DIR="$(dirname "$(dirname "$(dirname "$(realpath "$0")")")")/jore-data/database_dumps"

# Function to print usage information
print_usage() {
  echo "Usage: $0 --database|--db DATABASE_NAME [--target-dir|-t TARGET_DIRECTORY]"
  echo "  --database, -db      Database name to dump (required)"
  echo "  --target-dir, -t     Target directory for dump file (optional, default: ${TARGET_DIR})"
  echo "  --user, -u           Database user (optional, default: dbadmin)"
  echo "  --container, -c      Docker container name (optional, default: testdb)"
  echo "  -h, --help           Show this help message"
}

DATABASE=""
USER="dbadmin"
CONTAINER="testdb"

# Parse command line arguments
while [[ $# -gt 0 ]]; do
  case $1 in
    --database|-db)
      DATABASE="$2"
      shift 2
      ;;

    --user|-u)
      USER="$2"
      shift 2
      ;;

    --container|-c)
      CONTAINER="$2"
      shift 2
      ;;

    --target-dir|-t)
      TARGET_DIR="$2"
      shift 2
      ;;

    -h|--help)
      print_usage
      exit 0
      ;;

    *)
      echo "Unknown option $1"
      print_usage
      exit 1
      ;;
  esac
done

# Check if database argument was provided
if [[ -z "$DATABASE" ]]; then
  echo "Error: Database name is required"
  print_usage
  exit 1
fi

TARGET_FILE_BASENAME=$(date "+%Y-%m-%d")-jore4-local-"$DATABASE"

echo Target directory: "$TARGET_DIR"
mkdir -p "$TARGET_DIR"

echo Target file basename: "$TARGET_FILE_BASENAME"

# We invoke the pg_dump from within the Docker container to ensure that the dump
# file is of the same version as the database it is taken from. If you are using
# pg_dump from a newer major version of PostgreSQL, restoring a custom-format
# dump file will fail if you try to restore it back to the original database or
# to a database with an even older version.
docker exec "$CONTAINER" pg_dump \
  -f /tmp/"$DATABASE".pgdump \
  -h localhost \
  -p 5432 \
  -U dbadmin \
  -d "$DATABASE" \
  -F custom \
  -b

docker cp "$CONTAINER":/tmp/"$DATABASE".pgdump "${TARGET_DIR}/${TARGET_FILE_BASENAME}.pgdump"

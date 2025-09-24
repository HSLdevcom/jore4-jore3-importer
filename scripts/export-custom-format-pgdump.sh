#!/usr/bin/env bash

set -eu

TARGET_DIR=~/src/hsl/jore-data/database_dumps/

# Function to print usage information
print_usage() {
  echo "Usage: $0 --database|--db DATABASE_NAME [--target-dir|-t TARGET_DIRECTORY]"
  echo "  --database, -db      Database name to dump (required)"
  echo "  --target-dir, -t     Target directory for dump file (optional, default: ${TARGET_DIR})"
  echo "  -h, --help           Show this help message"
}

DATABASE=""

# Parse command line arguments
while [[ $# -gt 0 ]]; do
  case $1 in
    --database|-db)
      DATABASE="$2"
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
docker exec testdb pg_dump \
  -f /tmp/"$DATABASE".pgdump \
  -h localhost \
  -p 5432 \
  -U dbadmin \
  -d "$DATABASE" \
  -F custom \
  -b

docker cp testdb:/tmp/"$DATABASE".pgdump "${TARGET_DIR}/${TARGET_FILE_BASENAME}.pgdump"

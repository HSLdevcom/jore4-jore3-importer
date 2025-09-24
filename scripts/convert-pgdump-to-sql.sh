#!/usr/bin/env bash

set -eu

# Default target directory for dump files
# It is jore-data/database_dumps, where jore-data is next to the project directory.
# The three dirname calls each remove a segment from the end of the path; first the script name, then 'scripts'
# directory, and finally the project directory.
TARGET_DIR="$(dirname "$(dirname "$(dirname "$(realpath "$0")")")")/jore-data/database_dumps"
FILE_DATE=$(date "+%Y-%m-%d")
SUFFIX=""

# Function to print usage information
print_usage() {
  echo "Usage: $0 --database|--db DATABASE_NAME [--target-dir|-t TARGET_DIRECTORY] [--suffix|-s dev|test]"
  echo "  --database, -db      Name of dumped database to convert (required)"
  echo "  --target-dir, -t     Target directory for dump file (optional, default: ${TARGET_DIR})"
  echo "  --suffix, -s         Suffix to append to database environment names (optional, dev | test, default: empty)"
  echo "  --date, -d           Date to use in the filename (optional, default: ${FILE_DATE})"
  echo "  -h, --help           Show this help message"
}

DATABASE=""
# By excluding the public schema, access privileges (grant/revoke) for the
# public schema will be omitted from the resulting dump file, as well as the
# public schema extensions that should already exist in the Azure PostgreSQL
# database after proper initialisation.
PG_RESTORE_FLAGS="-N public"

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

    --date|-d)
      FILE_DATE="$2"
      shift 2
      ;;

    --suffix|-s)
      SUFFIX="$2"
      shift 2

      case $SUFFIX in
        dev|test|"")
          ;;

        empty)
          SUFFIX=""
          ;;

        *)
          echo "Error: Unsupported suffix '$SUFFIX'. Supported suffixes are: dev, test, or empty."
          print_usage
          exit 1
          ;;
      esac
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

case $DATABASE in
  "")
    echo "Error: Database name is required"
    print_usage
    exit 1
    ;;

  stopdb)
    # By explicitly defining schemas, we only create the objects they contain, but
    # not the schemas themselves, which should already exist in the Azure PostgreSQL
    # database after proper database initialisation.
    PG_RESTORE_FLAGS="-n public -n topology"
    ;;
esac

FILE_BASENAME=${FILE_DATE}-jore4-local-"$DATABASE"
SOURCE_FILE_NAME="${FILE_BASENAME}.pgdump"
SOURCE_FILE="${TARGET_DIR}/${SOURCE_FILE_NAME}"
TARGET_FILE_NAME="${FILE_BASENAME}-azure${SUFFIX}.sql"
TARGET_FILE="${TARGET_DIR}/${TARGET_FILE_NAME}"

echo Target directory: "$TARGET_DIR"
echo Source file: "$SOURCE_FILE_NAME"
echo Target file: "$TARGET_FILE_NAME"
echo PG_RESTORE_FLAGS: "$PG_RESTORE_FLAGS"

mkdir -p "$TARGET_DIR"

# This command will output a plain-text SQL dump file. With pg_restore, it is
# not possible to output another custom-format dump file.
pg_restore \
  ${PG_RESTORE_FLAGS} \
  -f "$TARGET_FILE" \
  "$SOURCE_FILE"

# Replace the names of database roles.
sed -i '' "s/dbadmin/dbadmin${SUFFIX}/g" "$TARGET_FILE"
sed -i '' "s/dbhasura/dbhasura${SUFFIX}/g" "$TARGET_FILE"
sed -i '' "s/dbimporter/dbjore3importer${SUFFIX}/g" "$TARGET_FILE"

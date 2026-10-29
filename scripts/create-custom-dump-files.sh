#!/usr/bin/env bash

set -eu

# Default target directory for dump files
# It is jore-data/database_dumps, where jore-data is next to the project directory.
# The three dirname calls each remove a segment from the end of the path; first the script name, then 'scripts'
# directory, and finally the project directory.
SCRIPT_DIR="$(dirname "$(realpath "$0")")"
TARGET_BASE_DIR="$(dirname "$(dirname "${SCRIPT_DIR}")")/jore-data/database_dumps"

# Function to print usage information
print_usage() {
  echo "Usage: $0 [--date|-d DATE_STRING] [-h] [--target-dir|-t TARGET_DIRECTORY]"
  echo "  --date, -d           Date to use in directory and dump file names. Default: today"
  echo "  --target-dir, -t     Target directory for dump file (optional, default: ${TARGET_DIR})"
  echo "  --help, -h           Show this help message"
}

DATE=$(date +%Y-%m-%d)
TARGET_DIR="${TARGET_BASE_DIR}/${DATE}"

# Parse command line arguments
while [[ $# -gt 0 ]]; do
  case $1 in
    --date|-d)
      DATE="$2"
      TARGET_DIR="${TARGET_BASE_DIR}/${DATE}"
      shift 2
      ;;

    --target-dir|-t)
      TARGET_BASE_DIR="$2"
      TARGET_DIR="${TARGET_BASE_DIR}/${DATE}"
      shift 2
      ;;

    --help|-h)
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

run_cmd() {
  echo ""
  echo "Running command: $*"
  "$@"
}

echo Target date: "$DATE"
echo Target directory: "$TARGET_DIR"
mkdir -p "$TARGET_DIR"

run_cmd ${SCRIPT_DIR}/export-custom-format-pgdump.sh --target-dir "${TARGET_DIR}" --date "${DATE}" --database jore4e2e
run_cmd ${SCRIPT_DIR}/export-custom-format-pgdump.sh --target-dir "${TARGET_DIR}" --date "${DATE}" --database timetablesdb
run_cmd ${SCRIPT_DIR}/export-custom-format-pgdump.sh --target-dir "${TARGET_DIR}" --date "${DATE}" --database stopdb

run_cmd ${SCRIPT_DIR}/convert-pgdump-to-sql.sh --target-dir "${TARGET_DIR}" --date "${DATE}" --zip --database jore4e2e
run_cmd ${SCRIPT_DIR}/convert-pgdump-to-sql.sh --target-dir "${TARGET_DIR}" --date "${DATE}" --database timetablesdb
run_cmd ${SCRIPT_DIR}/convert-pgdump-to-sql.sh --target-dir "${TARGET_DIR}" --date "${DATE}" --database stopdb

run_cmd ${SCRIPT_DIR}/convert-pgdump-to-sql.sh --target-dir "${TARGET_DIR}" --date "${DATE}" --zip --database jore4e2e -s dev
run_cmd ${SCRIPT_DIR}/convert-pgdump-to-sql.sh --target-dir "${TARGET_DIR}" --date "${DATE}" --database timetablesdb -s dev
run_cmd ${SCRIPT_DIR}/convert-pgdump-to-sql.sh --target-dir "${TARGET_DIR}" --date "${DATE}" --database stopdb -s dev

run_cmd ${SCRIPT_DIR}/convert-pgdump-to-sql.sh --target-dir "${TARGET_DIR}" --date "${DATE}" --zip --database jore4e2e -s test
run_cmd ${SCRIPT_DIR}/convert-pgdump-to-sql.sh --target-dir "${TARGET_DIR}" --date "${DATE}" --database timetablesdb -s test
run_cmd ${SCRIPT_DIR}/convert-pgdump-to-sql.sh --target-dir "${TARGET_DIR}" --date "${DATE}" --database stopdb -s test

#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/../.." && pwd)"
STOP_REGISTRY_DIR="${REPO_ROOT}/stop-registry-importer"
STOP_REGISTRY_VENV_PYTHON="${STOP_REGISTRY_DIR}/.venv-stop-registry/bin/python"
TEST_FILE="stop-registry-importer/tests/test_stop_registry_graphql_capture.py"

if [[ ! -x "${STOP_REGISTRY_VENV_PYTHON}" ]]; then
	echo "Missing stop-registry virtualenv python at ${STOP_REGISTRY_VENV_PYTHON}" >&2
	echo "Create it first, for example:" >&2
	echo "  python3 -m venv stop-registry-importer/.venv-stop-registry" >&2
	echo "  stop-registry-importer/.venv-stop-registry/bin/pip install -r stop-registry-importer/requirements.txt" >&2
	exit 1
fi

cd "${REPO_ROOT}"
exec "${STOP_REGISTRY_VENV_PYTHON}" -m unittest -v "${TEST_FILE}"

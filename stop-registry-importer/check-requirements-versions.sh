#!/usr/bin/env bash
set -euo pipefail

#
# A convenience script for checking if the pinned versions of the dependencies in the requirements.txt file are outdated
# compared to the latest versions available on PyPI.
#

REQ_FILE="${1:-requirements.txt}"

if [[ ! -f "$REQ_FILE" ]]; then
  echo "ERROR: requirements file not found: $REQ_FILE" >&2
  exit 1
fi

have_cmd() {
  command -v "$1" >/dev/null 2>&1
}

python_version_of() {
  local cmd="$1"
  "$cmd" -c 'import sys; print(f"{sys.version_info.major}.{sys.version_info.minor}.{sys.version_info.micro}")' 2>/dev/null
}

python_mm_of() {
  local cmd="$1"
  "$cmd" -c 'import sys; print(f"{sys.version_info.major}.{sys.version_info.minor}")' 2>/dev/null
}

version_ge() {
  # returns 0 if $1 >= $2
  [[ "$(printf '%s\n%s\n' "$2" "$1" | sort -V | tail -n1)" == "$1" ]]
}

pip_python_mm_of() {
  local cmd="$1"
  "$cmd" --version 2>/dev/null | sed -E 's/.*\(python ([0-9]+\.[0-9]+)\).*/\1/'
}

choose_python() {
  local chosen=""
  local py_ver=""

  if have_cmd python; then
    py_ver="$(python_mm_of python || true)"
    if [[ -n "$py_ver" ]] && version_ge "$py_ver" "3.14"; then
      chosen="python"
      echo "$chosen"
      return 0
    fi
  fi

  if have_cmd python3; then
    py_ver="$(python_mm_of python3 || true)"
    if [[ -n "$py_ver" ]] && version_ge "$py_ver" "3.14"; then
      chosen="python3"
      echo "$chosen"
      return 0
    fi
  fi

  return 1
}

choose_pip() {
  local chosen=""
  local pip_py_ver=""

  if have_cmd pip; then
    pip_py_ver="$(pip_python_mm_of pip || true)"
    if [[ -n "$pip_py_ver" ]] && version_ge "$pip_py_ver" "3.14"; then
      chosen="pip"
      echo "$chosen"
      return 0
    fi
  fi

  if have_cmd pip3; then
    pip_py_ver="$(pip_python_mm_of pip3 || true)"
    if [[ -n "$pip_py_ver" ]] && version_ge "$pip_py_ver" "3.14"; then
      chosen="pip3"
      echo "$chosen"
      return 0
    fi
  fi

  return 1
}

PY_CMD=""
PIP_CMD=""
PIP_INVOKE=()

if PY_CMD="$(choose_python)"; then
  :
else
  echo "ERROR: could not find a usable 'python' or 'python3' command with Python >= 3.14" >&2
  exit 1
fi

if PIP_CMD="$(choose_pip)"; then
  PIP_INVOKE=("$PIP_CMD")
else
  # fallback to python -m pip using the selected Python
  if "$PY_CMD" -m pip --version >/dev/null 2>&1; then
    PIP_INVOKE=("$PY_CMD" "-m" "pip")
  else
    echo "ERROR: could not find a usable pip command ('pip', 'pip3', or '$PY_CMD -m pip')" >&2
    exit 1
  fi
fi

PY_FULL_VER="$("$PY_CMD" -c 'import sys; print(f"{sys.version_info.major}.{sys.version_info.minor}.{sys.version_info.micro}")')"
PIP_VER_STR="$("${PIP_INVOKE[@]}" --version 2>/dev/null || true)"

echo "Using Python command: $PY_CMD ($PY_FULL_VER)"
if [[ ${#PIP_INVOKE[@]} -eq 1 ]]; then
  echo "Using pip command: ${PIP_INVOKE[0]}"
else
  echo "Using pip command: ${PIP_INVOKE[*]}"
fi
echo "pip reports: $PIP_VER_STR"
echo

latest_version_for() {
  local package="$1"
  local output latest

  if ! output="$("${PIP_INVOKE[@]}" index versions "$package" 2>/dev/null)"; then
    return 1
  fi

  latest="$(printf '%s\n' "$output" | sed -nE 's/^'"$package"' \(([^)]+)\)$/\1/p' | head -n1)"

  if [[ -z "$latest" ]]; then
    latest="$(printf '%s\n' "$output" | sed -nE 's/^Available versions: ([^, ]+).*/\1/p' | head -n1)"
  fi

  if [[ -z "$latest" ]]; then
    return 1
  fi

  printf '%s\n' "$latest"
}

printf '%-24s %-15s %-15s %-10s\n' "PACKAGE" "PINNED" "LATEST" "STATUS"
printf '%-24s %-15s %-15s %-10s\n' "------------------------" "---------------" "---------------" "----------"

while IFS= read -r raw_line || [[ -n "$raw_line" ]]; do
  line="${raw_line#"${raw_line%%[![:space:]]*}"}"
  line="${line%"${line##*[![:space:]]}"}"

  [[ -z "$line" ]] && continue
  [[ "$line" == \#* ]] && continue

  if [[ "$line" =~ ^([A-Za-z0-9._-]+)==([^[:space:]]+)$ ]]; then
    pkg="${BASH_REMATCH[1]}"
    pinned="${BASH_REMATCH[2]}"
  else
    printf '%-24s %-15s %-15s %-10s\n' "$line" "-" "-" "SKIPPED"
    continue
  fi

  if latest="$(latest_version_for "$pkg")"; then
    if version_ge "$pinned" "$latest"; then
      status="OK"
    else
      status="OUTDATED"
    fi
    printf '%-24s %-15s %-15s %-10s\n' "$pkg" "$pinned" "$latest" "$status"
  else
    printf '%-24s %-15s %-15s %-10s\n' "$pkg" "$pinned" "?" "ERROR"
  fi
done < "$REQ_FILE"

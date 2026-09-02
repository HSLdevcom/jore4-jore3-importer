#!/usr/bin/env python3
"""Report Jore4 route data for traversal-path conflicts in a Spring Batch log.

The script reads only the exportJourneyPatternStopsStep section, extracts all
route_id/journey_pattern_id pairs emitted by verify_infra_link_stop_refs(), and
queries PostgreSQL directly through psql (not Hasura).
"""

from __future__ import annotations

import argparse
import os
import re
import subprocess
import sys
from pathlib import Path
from typing import Iterable, Optional

DEFAULT_DATABASE_URL = "postgresql://dbadmin:adminpassword@localhost:6432/jore4e2e"
START_MARKER = "INFO  org.springframework.batch.core.step.AbstractStep - Executing step: [exportJourneyPatternStopsStep]"
END_MARKER = "INFO  org.springframework.batch.core.step.AbstractStep - Step: [exportJourneyPatternStopsStep] executed in"
CONFLICT_PATTERN = re.compile(
    r"route_id:\s*([0-9a-f]{8}(?:-[0-9a-f]{4}){3}-[0-9a-f]{12}),\s*"
    r"journey_pattern_id:\s*([0-9a-f]{8}(?:-[0-9a-f]{4}){3}-[0-9a-f]{12})",
    re.IGNORECASE,
)


def step_log_section(log_file: Path) -> str:
    """Return the requested step section, accepting EOF for a truncated log."""
    in_step = False
    lines: list[str] = []

    with log_file.open(encoding="utf-8", errors="replace") as file:
        for line in file:
            if not in_step:
                if START_MARKER in line:
                    in_step = True
                    lines.append(line)
                continue

            lines.append(line)
            if END_MARKER in line:
                return "".join(lines)

    if not in_step:
        raise ValueError(f"Start marker for exportJourneyPatternStopsStep was not found in {log_file}")

    print(
        f"Warning: end marker was not found in {log_file}; analyzing from the start marker to EOF.",
        file=sys.stderr,
    )
    return "".join(lines)


def conflicts_in(log_section: str) -> list[tuple[str, str]]:
    """Extract unique conflict pairs, retaining first-seen order."""
    return list(dict.fromkeys(CONFLICT_PATTERN.findall(log_section)))


def sql_for(conflicts: Iterable[tuple[str, str]]) -> str:
    """Build SQL from UUIDs parsed by CONFLICT_PATTERN, not untrusted free text."""
    values = ",\n        ".join(
        f"('{route_id}'::uuid, '{journey_pattern_id}'::uuid)" for route_id, journey_pattern_id in conflicts
    )
    return f"""
WITH conflicts(route_id, journey_pattern_id) AS (
    VALUES
        {values}
)
SELECT
    r.route_id,
    r.on_line_id,
    r.validity_start,
    r.validity_end,
    r.priority,
    r.label,
    r.direction,
    r.description_i18n,
    r.name_i18n,
    r.origin_name_i18n,
    r.origin_short_name_i18n,
    r.destination_name_i18n,
    r.destination_short_name_i18n,
    r.variant,
    r.unique_label,
    r.legacy_hsl_municipality_code,
    r.version_comment,
    l.primary_vehicle_mode,
    COALESCE(route_link_count.count, 0) AS infrastructure_link_along_route_count
FROM conflicts c
LEFT JOIN route.route r ON r.route_id = c.route_id
LEFT JOIN route.line l ON l.line_id = r.on_line_id
LEFT JOIN LATERAL (
    SELECT count(*)::bigint AS count
    FROM route.infrastructure_link_along_route ilar
    WHERE ilar.route_id = c.route_id
) route_link_count ON true
ORDER BY c.route_id, c.journey_pattern_id;
"""


def query_database(
    database_url: str, conflicts: list[tuple[str, str]], output_file: Optional[Path]
) -> None:
    command = ["psql", "--no-psqlrc", "--csv", "--set", "ON_ERROR_STOP=1", "--dbname", database_url]
    if output_file is not None:
        command.extend(["--output", str(output_file)])

    try:
        subprocess.run(command, input=sql_for(conflicts), text=True, check=True)
    except FileNotFoundError as error:
        raise RuntimeError("psql was not found on PATH. Install the PostgreSQL client tools and retry.") from error
    except subprocess.CalledProcessError as error:
        raise RuntimeError(f"psql failed with exit code {error.returncode}.") from error


def parse_arguments() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("log_file", type=Path, help="Spring Batch application log to analyze")
    parser.add_argument(
        "--database-url",
        default=os.environ.get("JORE4_DATABASE_URL", DEFAULT_DATABASE_URL),
        help="PostgreSQL URL (default: JORE4_DATABASE_URL or local jore4e2e database)",
    )
    parser.add_argument("--output", type=Path, help="Write PostgreSQL CSV output to this file")
    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Print extracted conflict pairs without connecting to PostgreSQL",
    )
    return parser.parse_args()


def main() -> int:
    arguments = parse_arguments()
    try:
        conflicts = conflicts_in(step_log_section(arguments.log_file))
    except (OSError, ValueError) as error:
        print(f"Error: {error}", file=sys.stderr)
        return 2

    if not conflicts:
        print("No route/journey-pattern traversal conflicts found in exportJourneyPatternStopsStep.", file=sys.stderr)
        return 0

    print(f"Found {len(conflicts)} unique route/journey-pattern conflict pair(s).", file=sys.stderr)
    if arguments.dry_run:
        print("route_id,journey_pattern_id")
        for route_id, journey_pattern_id in conflicts:
            print(f"{route_id},{journey_pattern_id}")
        return 0

    try:
        query_database(arguments.database_url, conflicts, arguments.output)
    except RuntimeError as error:
        print(f"Error: {error}", file=sys.stderr)
        return 1
    return 0


if __name__ == "__main__":
    raise SystemExit(main())




#!/usr/bin/env python3
"""Diagnose route/journey-pattern traversal conflicts reported by an importer log.

Creates <log>.conflicts.txt with analyze_journey_pattern_stop_conflicts.py when
needed, then writes route-level and stop-level diagnostic CSV files alongside it.
The stop report includes logged rejected stops that were rolled back or skipped.
"""

from __future__ import annotations

import argparse
import csv
import os
import re
import subprocess
import sys
from pathlib import Path
from typing import Iterable

DEFAULT_DATABASE_URL = "postgresql://dbadmin:adminpassword@localhost:6432/jore4e2e"
START_MARKER = "INFO  org.springframework.batch.core.step.AbstractStep - Executing step: [exportJourneyPatternStopsStep]"
END_MARKER = "INFO  org.springframework.batch.core.step.AbstractStep - Step: [exportJourneyPatternStopsStep] executed in"
CONFLICT_PATTERN = re.compile(
    r"route_id:\s*([0-9a-f]{8}(?:-[0-9a-f]{4}){3}-[0-9a-f]{12}),\s*"
    r"journey_pattern_id:\s*([0-9a-f]{8}(?:-[0-9a-f]{4}){3}-[0-9a-f]{12})",
    re.IGNORECASE,
)
PAYLOAD_PATTERN = re.compile(
    r"Jore4JourneyPatternStop\{journeyPatternId=([0-9a-f]{8}(?:-[0-9a-f]{4}){3}-[0-9a-f]{12}),\s*"
    r"scheduledStopPointSequence=(\d+),\s*scheduledStopPointLabel=([^,}]+)",
    re.IGNORECASE,
)


def step_log_section(log_file: Path) -> str:
    """Read the target step, permitting a log snippet that ends before the step."""
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
                break
    if not in_step:
        raise ValueError(f"Start marker for exportJourneyPatternStopsStep was not found in {log_file}")
    return "".join(lines)


def values_sql(rows: Iterable[tuple[str, ...]], casts: tuple[str, ...]) -> str:
    """Build VALUES SQL from regex-validated UUIDs and numeric sequences only."""
    return ",\n        ".join(
        "(" + ", ".join(f"'{value}'::{cast}" for value, cast in zip(row, casts)) + ")" for row in rows
    )


def parse_log(log_file: Path) -> tuple[list[tuple[str, str]], list[tuple[str, str, str]]]:
    section = step_log_section(log_file)
    conflicts = list(dict.fromkeys((route.lower(), pattern.lower()) for route, pattern in CONFLICT_PATTERN.findall(section)))
    conflict_patterns = {pattern for _, pattern in conflicts}
    payloads = list(
        dict.fromkeys(
            (pattern.lower(), sequence, label.strip())
            for pattern, sequence, label in PAYLOAD_PATTERN.findall(section)
            if pattern.lower() in conflict_patterns
        )
    )
    return conflicts, payloads


def ensure_conflict_report(log_file: Path, database_url: str, report_file: Path) -> None:
    """Create the existing route report if it is absent, retaining its standard format."""
    if report_file.exists():
        return
    reporter = Path(__file__).with_name("analyze_journey_pattern_stop_conflicts.py")
    if not reporter.is_file():
        raise RuntimeError(f"Companion report script was not found: {reporter}")
    command = [sys.executable, str(reporter), str(log_file), "--database-url", database_url, "--output", str(report_file)]
    try:
        subprocess.run(command, text=True, check=True)
    except subprocess.CalledProcessError as error:
        raise RuntimeError(f"Could not create {report_file} (exit code {error.returncode}).") from error


def reported_route_ids(report_file: Path) -> set[str]:
    """Read route IDs from the companion report for a consistency check."""
    try:
        with report_file.open(newline="", encoding="utf-8") as file:
            return {row["route_id"].lower() for row in csv.DictReader(file) if row.get("route_id")}
    except (OSError, KeyError) as error:
        raise RuntimeError(f"Could not read route_id values from {report_file}: {error}") from error


def sql_for(conflicts: list[tuple[str, str]], payloads: list[tuple[str, str, str]], detail: bool) -> str:
    conflict_values = values_sql(conflicts, ("uuid", "uuid"))
    payload_values = values_sql(payloads, ("uuid", "integer", "text")) if payloads else "(NULL::uuid, NULL::integer, NULL::text)"
    if not detail:
        return f"""
WITH conflicts(route_id, journey_pattern_id) AS (VALUES {conflict_values})
SELECT
    c.route_id, c.journey_pattern_id, r.on_line_id, r.label, r.direction,
    r.validity_start, r.validity_end, r.priority, l.primary_vehicle_mode,
    count(DISTINCT ilar.infrastructure_link_sequence) AS infrastructure_link_along_route_count,
    count(DISTINCT sspijp.scheduled_stop_point_sequence) AS persisted_journey_pattern_stop_count
FROM conflicts c
LEFT JOIN route.route r ON r.route_id = c.route_id
LEFT JOIN route.line l ON l.line_id = r.on_line_id
LEFT JOIN route.infrastructure_link_along_route ilar ON ilar.route_id = c.route_id
LEFT JOIN journey_pattern.scheduled_stop_point_in_journey_pattern sspijp
    ON sspijp.journey_pattern_id = c.journey_pattern_id
GROUP BY c.route_id, c.journey_pattern_id, r.route_id, l.line_id
ORDER BY c.route_id, c.journey_pattern_id;
"""
    return f"""
WITH conflicts(route_id, journey_pattern_id) AS (VALUES {conflict_values}),
logged_payloads(journey_pattern_id, scheduled_stop_point_sequence, scheduled_stop_point_label) AS (
    VALUES {payload_values}
),
input_stops AS (
    SELECT c.route_id, c.journey_pattern_id, s.scheduled_stop_point_sequence,
           s.scheduled_stop_point_label, 'persisted'::text AS source
    FROM conflicts c
    JOIN journey_pattern.scheduled_stop_point_in_journey_pattern s
      ON s.journey_pattern_id = c.journey_pattern_id
    UNION ALL
    SELECT c.route_id, c.journey_pattern_id, p.scheduled_stop_point_sequence,
           p.scheduled_stop_point_label, 'logged_payload'::text AS source
    FROM conflicts c
    JOIN logged_payloads p ON p.journey_pattern_id = c.journey_pattern_id
    WHERE NOT EXISTS (
        SELECT 1 FROM journey_pattern.scheduled_stop_point_in_journey_pattern s
        WHERE s.journey_pattern_id = p.journey_pattern_id
          AND s.scheduled_stop_point_sequence = p.scheduled_stop_point_sequence
          AND s.scheduled_stop_point_label = p.scheduled_stop_point_label
    )
)
SELECT
    i.route_id, i.journey_pattern_id, i.source,
    i.scheduled_stop_point_sequence, i.scheduled_stop_point_label,
    ssp.scheduled_stop_point_id, ssp.validity_start AS stop_validity_start,
    ssp.validity_end AS stop_validity_end, ssp.priority AS stop_priority,
    ssp.located_on_infrastructure_link_id, ssp.direction AS stop_direction,
    ssp.relative_distance_from_infrastructure_link_start,
    ilar.infrastructure_link_sequence, ilar.is_traversal_forwards,
    CASE
      WHEN ssp.scheduled_stop_point_id IS NULL THEN 'NO_STOP_WITH_OVERLAPPING_VALIDITY'
      WHEN ilar.infrastructure_link_id IS NULL THEN 'STOP_LINK_NOT_ON_ROUTE'
      WHEN (ssp.direction = 'forward' AND NOT ilar.is_traversal_forwards)
        OR (ssp.direction = 'backward' AND ilar.is_traversal_forwards) THEN 'DIRECTION_MISMATCH'
      ELSE 'COMPATIBLE_ROUTE_VISIT'
    END AS diagnostic_status
FROM input_stops i
JOIN route.route r ON r.route_id = i.route_id
LEFT JOIN service_pattern.scheduled_stop_points_with_infra_link_data ssp
  ON ssp.label = i.scheduled_stop_point_label
 AND internal_utils.daterange_closed_upper(ssp.validity_start, ssp.validity_end)
     && internal_utils.daterange_closed_upper(r.validity_start, r.validity_end)
LEFT JOIN route.infrastructure_link_along_route ilar
  ON ilar.route_id = i.route_id
 AND ilar.infrastructure_link_id = ssp.located_on_infrastructure_link_id
ORDER BY i.route_id, i.journey_pattern_id, i.scheduled_stop_point_sequence,
         i.source, ssp.priority DESC NULLS LAST, ilar.infrastructure_link_sequence;
"""


def assessment_sql_for(conflicts: list[tuple[str, str]], payloads: list[tuple[str, str, str]]) -> str:
    """Classify the direct candidate failures before investigating stop order."""
    conflict_values = values_sql(conflicts, ("uuid", "uuid"))
    payload_values = values_sql(payloads, ("uuid", "integer", "text")) if payloads else "(NULL::uuid, NULL::integer, NULL::text)"
    return f"""
WITH conflicts(route_id, journey_pattern_id) AS (VALUES {conflict_values}),
logged_payloads(journey_pattern_id, scheduled_stop_point_sequence, scheduled_stop_point_label) AS (
    VALUES {payload_values}
),
input_stops AS (
    SELECT c.route_id, c.journey_pattern_id, s.scheduled_stop_point_sequence, s.scheduled_stop_point_label
    FROM conflicts c
    JOIN journey_pattern.scheduled_stop_point_in_journey_pattern s
      ON s.journey_pattern_id = c.journey_pattern_id
    UNION ALL
    SELECT c.route_id, c.journey_pattern_id, p.scheduled_stop_point_sequence, p.scheduled_stop_point_label
    FROM conflicts c
    JOIN logged_payloads p ON p.journey_pattern_id = c.journey_pattern_id
    WHERE NOT EXISTS (
        SELECT 1 FROM journey_pattern.scheduled_stop_point_in_journey_pattern s
        WHERE s.journey_pattern_id = p.journey_pattern_id
          AND s.scheduled_stop_point_sequence = p.scheduled_stop_point_sequence
          AND s.scheduled_stop_point_label = p.scheduled_stop_point_label
    )
),
stop_candidates AS (
    SELECT DISTINCT
        i.route_id, i.journey_pattern_id, i.scheduled_stop_point_sequence,
        i.scheduled_stop_point_label, ssp.scheduled_stop_point_id,
        ilar.infrastructure_link_id AS route_link_id,
        (ssp.direction = 'bidirectional'
         OR (ssp.direction = 'forward' AND ilar.is_traversal_forwards)
         OR (ssp.direction = 'backward' AND NOT ilar.is_traversal_forwards))
            AS direction_matches
    FROM input_stops i
    JOIN route.route r ON r.route_id = i.route_id
    LEFT JOIN service_pattern.scheduled_stop_points_with_infra_link_data ssp
      ON ssp.label = i.scheduled_stop_point_label
     AND internal_utils.daterange_closed_upper(ssp.validity_start, ssp.validity_end)
         && internal_utils.daterange_closed_upper(r.validity_start, r.validity_end)
    LEFT JOIN route.infrastructure_link_along_route ilar
      ON ilar.route_id = i.route_id
     AND ilar.infrastructure_link_id = ssp.located_on_infrastructure_link_id
),
per_stop AS (
    SELECT route_id, journey_pattern_id, scheduled_stop_point_sequence,
           scheduled_stop_point_label,
           bool_or(scheduled_stop_point_id IS NOT NULL) AS has_overlapping_stop,
           bool_or(route_link_id IS NOT NULL) AS link_is_on_route,
           bool_or(direction_matches) AS has_compatible_visit
    FROM stop_candidates
    GROUP BY route_id, journey_pattern_id, scheduled_stop_point_sequence,
             scheduled_stop_point_label
),
per_pattern AS (
    SELECT route_id, journey_pattern_id,
           count(*) FILTER (WHERE NOT has_overlapping_stop) AS no_stop_count,
           count(*) FILTER (WHERE has_overlapping_stop AND NOT link_is_on_route) AS missing_link_count,
           count(*) FILTER (WHERE link_is_on_route AND NOT has_compatible_visit) AS direction_mismatch_count,
           string_agg(scheduled_stop_point_label, ', ' ORDER BY scheduled_stop_point_sequence)
             FILTER (WHERE NOT has_overlapping_stop) AS stops_without_overlapping_data,
           string_agg(scheduled_stop_point_label, ', ' ORDER BY scheduled_stop_point_sequence)
             FILTER (WHERE has_overlapping_stop AND NOT link_is_on_route) AS stops_on_links_not_in_route,
           string_agg(scheduled_stop_point_label, ', ' ORDER BY scheduled_stop_point_sequence)
             FILTER (WHERE link_is_on_route AND NOT has_compatible_visit) AS stops_without_compatible_direction
    FROM per_stop
    GROUP BY route_id, journey_pattern_id
)
SELECT *, CASE
    WHEN no_stop_count > 0 THEN 'NO_STOP_WITH_OVERLAPPING_VALIDITY'
    WHEN missing_link_count > 0 THEN 'STOP_LINK_NOT_ON_ROUTE'
    WHEN direction_mismatch_count > 0 THEN 'DIRECTION_MISMATCH'
    ELSE 'REVIEW_STOP_ORDER_OR_SAME_LINK_POSITION'
END AS primary_diagnosis
FROM per_pattern
ORDER BY route_id, journey_pattern_id;
"""


def run_query(database_url: str, sql: str, output: Path) -> None:
    command = ["psql", "--no-psqlrc", "--csv", "--set", "ON_ERROR_STOP=1", "--dbname", database_url, "--output", str(output)]
    try:
        subprocess.run(command, input=sql, text=True, check=True)
    except FileNotFoundError as error:
        raise RuntimeError("psql was not found on PATH. Install PostgreSQL client tools and retry.") from error
    except subprocess.CalledProcessError as error:
        raise RuntimeError(f"psql failed while writing {output} (exit code {error.returncode}).") from error


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("log_file", type=Path)
    parser.add_argument("--database-url", default=os.environ.get("JORE4_DATABASE_URL", DEFAULT_DATABASE_URL))
    parser.add_argument("--dry-run", action="store_true", help="Parse inputs but do not create/query reports")
    args = parser.parse_args()
    report_file = args.log_file.with_name(args.log_file.name + ".conflicts.txt")
    try:
        conflicts, payloads = parse_log(args.log_file)
        if not conflicts:
            print("No traversal conflicts were found in exportJourneyPatternStopsStep.", file=sys.stderr)
            return 0
        if args.dry_run:
            print(f"conflicts={len(conflicts)}, matching_logged_payloads={len(payloads)}, report={report_file}")
            return 0
        ensure_conflict_report(args.log_file, args.database_url, report_file)
        missing = {route for route, _ in conflicts} - reported_route_ids(report_file)
        if missing:
            print(f"Warning: {len(missing)} log conflict route(s) are absent from {report_file}.", file=sys.stderr)
        summary = report_file.with_name(report_file.name + ".diagnostics.csv")
        stops = report_file.with_name(report_file.name + ".stop-diagnostics.csv")
        assessment = report_file.with_name(report_file.name + ".assessment.csv")
        run_query(args.database_url, sql_for(conflicts, payloads, detail=False), summary)
        run_query(args.database_url, sql_for(conflicts, payloads, detail=True), stops)
        run_query(args.database_url, assessment_sql_for(conflicts, payloads), assessment)
    except (OSError, ValueError, RuntimeError) as error:
        print(f"Error: {error}", file=sys.stderr)
        return 1
    print(f"Analyzed {len(conflicts)} conflict pair(s); matched {len(payloads)} logged stop payload(s).", file=sys.stderr)
    print(f"Route summary: {summary}", file=sys.stderr)
    print(f"Stop diagnostics: {stops}", file=sys.stderr)
    print(f"Pattern assessment: {assessment}", file=sys.stderr)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())




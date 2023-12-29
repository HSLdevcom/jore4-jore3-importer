-- Since few years ago the last three digits of the line/route number were not
-- unique, and since in Jore4 the line/route number consists of only those last
-- three digits (stripping the preceding digit for the legacy municipality
-- code), the lines of the 'LEGACY_NOT_USED' legacy municipality code are
-- filtered out to prevent unique constraint violations in the Jore4 database
-- during export.

-- Currently, only bus and ferry routes are exported to Jore4. Also, we only take
-- routes that are valid on or after 1.1.2021.

-- See the `export_lines.sql` file for more information on line and route export
-- issues.

SELECT
    r.network_route_number AS route_number,
    r.network_route_hidden_variant AS hidden_variant,
    rd.network_route_direction_id AS direction_id,
    rd.network_route_direction_type AS direction_type,
    rd.network_route_direction_name AS name,
    lh.jore4_line_id AS jore4_id_of_line,
    rd.network_route_direction_valid_date_range AS valid_date_range,
    r.network_route_legacy_hsl_municipality_code AS legacy_hsl_municipality_code
FROM network.network_routes r
JOIN network.network_route_directions rd USING (network_route_id)
JOIN network.network_lines l USING (network_line_id)
JOIN LATERAL (
    -- Select the most recent overlapping line header. It is the one for which
    -- the Jore4 line ID has been allocated during the line export stage.
    SELECT
        network_line_id,
        jore4_line_id
    FROM network.network_line_headers
    WHERE
        network_line_id = l.network_line_id
        AND network_line_header_valid_date_range && rd.network_route_direction_valid_date_range
        AND jore4_line_id IS NOT NULL
    ORDER BY network_line_header_valid_date_range DESC
    LIMIT 1
) lh USING (network_line_id)
WHERE
    l.infrastructure_network_type IN ('road', 'waterway')
    AND l.network_line_legacy_hsl_municipality_code NOT IN ('LEGACY_NOT_USED', 'TESTING_NOT_USED')
    AND rd.network_route_direction_valid_date_range && '[2021-01-01, 2050-01-01)'::daterange
ORDER BY rd.network_route_direction_valid_date_range DESC;

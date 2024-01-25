-- Since a few years ago the last three digits of the line/route number were not
-- unique, and since in Jore4 the line/route number consists of only those last
-- three digits (stripping the first digit), the lines starting with the legacy
-- municipality code of '0' (roughly meaning "legacy, not used") are filtered
-- out to prevent unique constraint violations in the Jore4 database during
-- export. In addition, the lines starting with the municipality code '8'
-- ("testing, not used") are also filtered out, because they are not real lines
-- and can cause the mentioned constraint violations.

-- Currently, only bus and ferry routes are exported to Jore4. Also, we only take
-- routes that are valid on or after 1.1.2021.

SELECT
    r.network_route_number AS route_number,
    r.network_route_hidden_variant AS hidden_variant,
    rd.network_route_direction_id AS direction_id,
    rd.network_route_direction_type AS direction_type,
    rd.network_route_direction_name AS name,
    l.network_line_jore4_id AS jore4_id_of_line,
    rd.network_route_direction_valid_date_range AS valid_date_range,
    r.network_route_legacy_hsl_municipality_code AS legacy_hsl_municipality_code
FROM network.network_routes r
JOIN network.network_route_directions rd USING (network_route_id)
JOIN network.network_lines l USING (network_line_id)
WHERE
    l.infrastructure_network_type IN ('road', 'waterway')
    AND l.network_line_legacy_hsl_municipality_code NOT IN ('LEGACY_NOT_USED', 'TESTING_NOT_USED')
    AND rd.network_route_direction_valid_date_range && '[2021-01-01, 2050-01-01)'::daterange
ORDER BY rd.network_route_direction_valid_date_range DESC;

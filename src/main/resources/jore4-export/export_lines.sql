-- Since a few years ago the last three digits of the line number were not
-- unique, and since in Jore4 the line number consists of only those last three
-- digits (stripping the first digit), the lines starting with the legacy
-- municipality code of '0' (roughly meaning "legacy, not used") are filtered
-- out to prevent unique constraint violations in the Jore4 database during
-- export. In addition, the lines starting with the municipality code '8'
-- ("testing, not used") are also filtered out, because they are not real lines
-- and can cause the mentioned constraint violations.

-- Currently, only bus and ferry lines are exported to Jore4.

SELECT
    l.network_line_ext_id AS external_id,
    l.network_line_number AS line_number,
    l.infrastructure_network_type AS network_type,
    l.network_line_type_of_line AS type_of_line,
    l.network_line_legacy_hsl_municipality_code AS legacy_hsl_municipality_code,
    lh.network_line_header_name AS line_header_name,
    lh.network_line_header_name_short AS line_header_short_name,
    lh.network_line_header_valid_date_range AS line_header_valid_date_range
FROM network.network_lines l
JOIN network.network_line_headers lh USING (network_line_id)
WHERE
    l.infrastructure_network_type IN ('road', 'waterway')
    AND l.network_line_legacy_hsl_municipality_code NOT IN ('LEGACY_NOT_USED', 'TESTING_NOT_USED')
ORDER BY lh.network_line_header_valid_date_range DESC;

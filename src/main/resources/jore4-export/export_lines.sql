-- Since few years ago the last three digits of the line number were not
-- unique, and since in Jore4 the line number uses only those last three digits
-- (stripping the preceding digit for the legacy municipality code), the lines
-- of the 'LEGACY_NOT_USED' legacy municipality code are filtered out to avoid
-- uniqueness constraint problems in Jore4 database.

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

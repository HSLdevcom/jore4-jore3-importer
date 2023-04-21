SELECT
        l.network_line_ext_id AS external_id,
        l.network_line_export_id AS export_id,
        l.network_line_number AS line_number,
        l.infrastructure_network_type AS network_type,
        l.network_line_type_of_line AS type_of_line,
        l.network_line_legacy_hsl_municipality_code AS legacy_hsl_municipality_code,
        lh.network_line_header_name AS name,
        lh.network_line_header_name_short AS short_name,
        lh.network_line_header_valid_date_range AS valid_date_range
FROM network.network_lines l
JOIN network.network_line_headers lh ON (lh.network_line_id = l.network_line_id)
ORDER BY lh.network_line_header_valid_date_range DESC;

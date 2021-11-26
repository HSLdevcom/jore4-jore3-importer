SELECT
        l.infrastructure_network_type AS network_type,
        lh.network_line_header_name AS name,
        lh.network_line_header_name_short AS short_name
FROM network.network_lines l
JOIN network.network_line_headers lh ON (lh.network_line_id = l.network_line_id);
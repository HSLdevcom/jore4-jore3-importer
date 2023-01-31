SELECT r.network_route_name AS name,
       r.network_route_number AS route_number,
       r.network_route_hidden_variant AS hidden_variant,
       rd.network_route_direction_id AS direction_id,
       rd.network_route_direction_type AS direction_type,
       l.network_line_transmodel_id AS line_transmodel_id,
       rd.network_route_direction_valid_date_range AS valid_date_range,
       r.network_route_legacy_hsl_municipality_code AS legacy_hsl_municipality_code
    FROM network.network_routes r
    JOIN network.network_route_directions rd ON (rd.network_route_id = r.network_route_id)
    JOIN network.network_lines l ON (l.network_line_id = r.network_line_id)
    WHERE isempty(rd.network_route_direction_valid_date_range * '[2021-01-01, 2050-01-01)'::daterange) = false
    ORDER BY rd.network_route_direction_valid_date_range DESC

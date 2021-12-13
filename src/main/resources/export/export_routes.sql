SELECT r.network_route_ext_id AS external_id,
       r.network_route_name AS name,
       r.network_route_number AS route_number,
       rd.network_route_direction_type AS direction_type,
       l.network_line_transmodel_id AS line_transmodel_id,
       (
           SELECT ssp.scheduled_stop_point_transmodel_id FROM network.scheduled_stop_points ssp
           JOIN network.network_route_stop_points rsp ON (split_part(rsp.network_route_stop_point_ext_id, '-', 2) = ssp.scheduled_stop_point_ext_id)
           JOIN network.network_route_points rp ON (rp.network_route_point_id = rsp.network_route_point_id)
           JOIN network.network_route_directions rd1 ON (rd1.network_route_direction_id = rp.network_route_direction_id)
           WHERE rd1.network_route_id = r.network_route_id AND rp.network_route_direction_id = rd1.network_route_direction_id
           ORDER BY rp.network_route_point_order ASC LIMIT 1
       ) AS start_scheduled_stop_point_transmodel_id,
       (
           SELECT ssp.scheduled_stop_point_transmodel_id FROM network.scheduled_stop_points ssp
           JOIN network.network_route_stop_points rsp ON (split_part(rsp.network_route_stop_point_ext_id, '-', 2) = ssp.scheduled_stop_point_ext_id)
           JOIN network.network_route_points rp ON (rp.network_route_point_id = rsp.network_route_point_id)
           JOIN network.network_route_directions rd1 ON (rd1.network_route_direction_id = rp.network_route_direction_id)
           WHERE rd1.network_route_id = r.network_route_id AND rp.network_route_direction_id = rd1.network_route_direction_id
           ORDER BY rp.network_route_point_order DESC LIMIT 1
       ) AS end_scheduled_stop_point_transmodel_id,
       rd.network_route_direction_valid_date_range AS valid_date_range
    FROM network.network_routes r
    JOIN network.network_route_directions rd ON (rd.network_route_id = r.network_route_id)
    JOIN network.network_lines l ON (l.network_line_id = r.network_line_id)
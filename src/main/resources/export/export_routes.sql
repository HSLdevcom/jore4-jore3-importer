SELECT r.network_route_name AS name,
       r.network_route_number AS route_number,
       rd.network_route_direction_id AS direction_id,
       rd.network_route_direction_type AS direction_type,
       l.network_line_transmodel_id AS line_transmodel_id,
       /*
            Finds the first scheduled stop point of the exported route. Note that we must use
            the split_part() function when we join the network.scheduled_stop_points and
            network.network_route_stop_points tables because the value of the network_route_stop_point_ext_id
            column of the network.network_route_stop_points table uses this format:

            [the id of the route link]-[the external id of the scheduled stop point].
        */
       (
           SELECT ssp.scheduled_stop_point_transmodel_id FROM network.scheduled_stop_points ssp
           JOIN network.network_route_stop_points rsp ON (split_part(rsp.network_route_stop_point_ext_id, '-', 2) = ssp.scheduled_stop_point_ext_id)
           JOIN network.network_route_points rp ON (rp.network_route_point_id = rsp.network_route_point_id)
           JOIN network.network_route_directions rd1 ON (rd1.network_route_direction_id = rp.network_route_direction_id)
           WHERE rd1.network_route_id = r.network_route_id AND rd1.network_route_direction_id = rd.network_route_direction_id
           ORDER BY rsp.network_route_stop_point_order ASC LIMIT 1
       ) AS start_scheduled_stop_point_transmodel_id,
       /*
            Finds the last scheduled stop point of the exported route. Note that we must use
            the split_part() function when we join the network.scheduled_stop_points and
            network.network_route_stop_points tables because the value of the network_route_stop_point_ext_id
            column of the network.network_route_stop_points table uses this format:

            [the id of the route link]-[the external id of the scheduled stop point].
        */
       (
           SELECT ssp.scheduled_stop_point_transmodel_id FROM network.scheduled_stop_points ssp
           JOIN network.network_route_stop_points rsp ON (split_part(rsp.network_route_stop_point_ext_id, '-', 2) = ssp.scheduled_stop_point_ext_id)
           JOIN network.network_route_points rp ON (rp.network_route_point_id = rsp.network_route_point_id)
           JOIN network.network_route_directions rd2 ON (rd2.network_route_direction_id = rp.network_route_direction_id)
           WHERE rd2.network_route_id = r.network_route_id AND rd2.network_route_direction_id = rd.network_route_direction_id
           ORDER BY rsp.network_route_stop_point_order DESC LIMIT 1
       ) AS end_scheduled_stop_point_transmodel_id,
       rd.network_route_direction_valid_date_range AS valid_date_range
    FROM network.network_routes r
    JOIN network.network_route_directions rd ON (rd.network_route_id = r.network_route_id)
    JOIN network.network_lines l ON (l.network_line_id = r.network_line_id)
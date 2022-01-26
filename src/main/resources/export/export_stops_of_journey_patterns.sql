SELECT r.journey_pattern_transmodel_id AS journey_pattern_transmodel_id,
       rsp.network_route_stop_point_hastus_point AS is_hastus_point,
       rsp.network_route_stop_point_via_point AS is_via_point,
       rsp.network_route_stop_point_order AS order_number,
       ssp.scheduled_stop_point_transmodel_id AS scheduled_stop_point_transmodel_id
FROM network.network_routes r
JOIN network.network_route_directions rd ON (rd.network_route_id = r.network_route_id)
JOIN network.network_route_points rp ON (rp.network_route_direction_id = rd.network_route_direction_id)
JOIN network.network_route_stop_points rsp ON (rsp.network_route_point_id = rp.network_route_point_id)
/*
    We must use the split_part() function when we join the network.scheduled_stop_points and
    network.network_route_stop_points tables because the value of the network_route_stop_point_ext_id
    column of the network.network_route_stop_points table uses this format:

    [the id of the route link]-[the external id of the scheduled stop point].
*/
JOIN network.scheduled_stop_points ssp ON (ssp.scheduled_stop_point_ext_id = split_part(rsp.network_route_stop_point_ext_id, '-', 2))
WHERE r.journey_pattern_transmodel_id IS NOT NULL
AND ssp.scheduled_stop_point_transmodel_id IS NOT NULL
ORDER BY rsp.network_route_stop_point_order ASC;
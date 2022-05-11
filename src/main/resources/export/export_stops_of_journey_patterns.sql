SELECT rd.journey_pattern_transmodel_id AS journey_pattern_transmodel_id,
       rsp.network_route_stop_point_hastus_point AS is_hastus_point,
       rsp.network_route_stop_point_via_point AS is_via_point,
       rsp.network_route_stop_point_order AS order_number,
       (
            SELECT sq1.scheduled_stop_point_transmodel_id FROM network.scheduled_stop_points sq1
            WHERE sq1.scheduled_stop_point_short_id = ssp.scheduled_stop_point_short_id
            AND sq1.scheduled_stop_point_transmodel_id IS NOT NULL
       ) AS scheduled_stop_point_transmodel_id
FROM network.network_route_directions rd
         JOIN network.network_route_points rp ON (rp.network_route_direction_id = rd.network_route_direction_id)
         JOIN infrastructure_network.infrastructure_nodes n ON (n.infrastructure_node_id = rp.infrastructure_node)
         JOIN network.network_route_stop_points rsp ON (rsp.network_route_point_id = rp.network_route_point_id)
         JOIN network.scheduled_stop_points ssp ON (ssp.infrastructure_node_id = n.infrastructure_node_id)
WHERE rd.journey_pattern_transmodel_id IS NOT NULL
  AND ssp.scheduled_stop_point_transmodel_id IS NOT NULL
ORDER BY rsp.network_route_stop_point_order ASC;
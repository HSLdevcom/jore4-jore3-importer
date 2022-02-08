SELECT rd.network_route_direction_id AS route_direction_id,
       rd.network_route_transmodel_id AS route_transmodel_id
FROM network.network_route_directions rd
WHERE rd.network_route_transmodel_id IS NOT NULL;
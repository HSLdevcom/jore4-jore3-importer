SELECT rd.network_route_direction_id AS route_direction_id,
       rd.network_route_jore4_id AS route_jore4_id
FROM network.network_route_directions rd
WHERE rd.network_route_jore4_id IS NOT NULL;

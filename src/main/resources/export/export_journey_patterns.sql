SELECT r.network_route_transmodel_id AS route_transmodel_id
FROM network.network_routes r
WHERE r.network_route_transmodel_id IS NOT NULL;
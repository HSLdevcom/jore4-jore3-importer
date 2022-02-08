DROP VIEW network.network_routes_with_history;

ALTER TABLE network.network_routes_history DROP COLUMN journey_pattern_transmodel_id;
ALTER TABLE network.network_routes DROP COLUMN journey_pattern_transmodel_id;

ALTER TABLE network.network_routes_history DROP COLUMN network_route_transmodel_id;
ALTER TABLE network.network_routes DROP COLUMN network_route_transmodel_id;

CREATE VIEW network.network_routes_with_history AS
SELECT *
FROM network.network_routes
UNION ALL
SELECT *
FROM network.network_routes_history;
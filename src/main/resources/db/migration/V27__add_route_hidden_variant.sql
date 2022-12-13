ALTER TABLE network.network_routes ADD COLUMN network_route_hidden_variant SMALLINT DEFAULT NULL;
ALTER TABLE network.network_routes_staging ADD COLUMN network_route_hidden_variant SMALLINT DEFAULT NULL;
ALTER TABLE network.network_routes_history ADD COLUMN network_route_hidden_variant SMALLINT DEFAULT NULL;

CREATE OR REPLACE VIEW network.network_routes_with_history AS
SELECT *
FROM network.network_routes
UNION ALL
SELECT *
FROM network.network_routes_history;

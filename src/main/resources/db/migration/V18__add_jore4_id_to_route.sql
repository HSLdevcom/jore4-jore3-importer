ALTER TABLE network.network_routes ADD COLUMN network_route_jore4_id uuid;
ALTER TABLE network.network_routes_history ADD COLUMN network_route_jore4_id uuid;

CREATE OR REPLACE VIEW network.network_routes_with_history AS
SELECT *
FROM network.network_routes
UNION ALL
SELECT *
FROM network.network_routes_history;

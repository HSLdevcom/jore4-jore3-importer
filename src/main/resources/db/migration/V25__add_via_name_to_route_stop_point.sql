ALTER TABLE network.network_route_stop_points ADD COLUMN network_route_stop_point_via_name JSONB;
ALTER TABLE network.network_route_stop_points_history ADD COLUMN network_route_stop_point_via_name JSONB;
ALTER TABLE network.network_route_stop_points_staging ADD COLUMN network_route_stop_point_via_name JSONB;

CREATE OR REPLACE VIEW network.network_route_stop_points_with_history AS
SELECT *
FROM network.network_route_stop_points
UNION ALL
SELECT *
FROM network.network_route_stop_points_history;
ALTER TABLE network.network_route_directions ADD COLUMN network_route_jore4_id uuid;
ALTER TABLE network.network_route_directions_history ADD COLUMN network_route_jore4_id uuid;

ALTER TABLE network.network_route_directions ADD COLUMN journey_pattern_jore4_id uuid;
ALTER TABLE network.network_route_directions_history ADD COLUMN journey_pattern_jore4_id uuid;

CREATE OR REPLACE VIEW network.network_route_directions_with_history AS
SELECT *
FROM network.network_route_directions
UNION ALL
SELECT *
FROM network.network_route_directions_history;

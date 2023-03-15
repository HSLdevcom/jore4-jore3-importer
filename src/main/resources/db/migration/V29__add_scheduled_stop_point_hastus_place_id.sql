ALTER TABLE network.scheduled_stop_points ADD COLUMN hastus_place_id text;
ALTER TABLE network.scheduled_stop_points_staging ADD COLUMN hastus_place_id text;
ALTER TABLE network.scheduled_stop_points_history ADD COLUMN hastus_place_id text;

CREATE OR REPLACE VIEW network.scheduled_stop_points_with_history AS
SELECT *
FROM network.scheduled_stop_points
UNION ALL
SELECT *
FROM network.scheduled_stop_points_history;

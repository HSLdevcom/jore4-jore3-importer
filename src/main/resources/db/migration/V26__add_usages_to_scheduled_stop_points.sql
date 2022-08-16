ALTER TABLE network.scheduled_stop_points ADD COLUMN usage_in_routes INTEGER;
ALTER TABLE network.scheduled_stop_points_staging ADD COLUMN usage_in_routes INTEGER;
ALTER TABLE network.scheduled_stop_points_history ADD COLUMN usage_in_routes INTEGER;

CREATE OR REPLACE VIEW network.scheduled_stop_points_with_history AS
SELECT *
FROM network.scheduled_stop_points
UNION ALL
SELECT *
FROM network.scheduled_stop_points_history;
ALTER TABLE network.scheduled_stop_points ADD COLUMN usage_in_routes INTEGER NOT NULL DEFAULT 0;
ALTER TABLE network.scheduled_stop_points_staging ADD COLUMN usage_in_routes INTEGER NOT NULL DEFAULT 0;
ALTER TABLE network.scheduled_stop_points_history ADD COLUMN usage_in_routes INTEGER NOT NULL DEFAULT 0;

CREATE OR REPLACE VIEW network.scheduled_stop_points_with_history AS
SELECT *
FROM network.scheduled_stop_points
UNION ALL
SELECT *
FROM network.scheduled_stop_points_history;
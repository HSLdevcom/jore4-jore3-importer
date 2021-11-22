ALTER TABLE network.scheduled_stop_points ADD COLUMN scheduled_stop_point_short_id VARCHAR(6);
ALTER TABLE network.scheduled_stop_points_staging ADD COLUMN scheduled_stop_point_short_id VARCHAR(6);
ALTER TABLE network.scheduled_stop_points_history ADD COLUMN scheduled_stop_point_short_id VARCHAR(6);

CREATE OR REPLACE VIEW network.scheduled_stop_points_with_history AS
SELECT *
FROM network.scheduled_stop_points
UNION ALL
SELECT *
FROM network.scheduled_stop_points_history;
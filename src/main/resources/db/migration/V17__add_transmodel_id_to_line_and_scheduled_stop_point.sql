ALTER TABLE network.network_lines ADD COLUMN network_line_transmodel_id uuid;
ALTER TABLE network.network_lines_history ADD COLUMN network_line_transmodel_id uuid;

CREATE OR REPLACE VIEW network.network_lines_with_history AS
SELECT *
FROM network.network_lines
UNION ALL
SELECT *
FROM network.network_lines_history;

ALTER TABLE network.scheduled_stop_points ADD COLUMN scheduled_stop_point_transmodel_id uuid;
ALTER TABLE network.scheduled_stop_points_history ADD COLUMN scheduled_stop_point_transmodel_id uuid;

CREATE OR REPLACE VIEW network.scheduled_stop_points_with_history AS
SELECT *
FROM network.scheduled_stop_points
UNION ALL
SELECT *
FROM network.scheduled_stop_points_history;

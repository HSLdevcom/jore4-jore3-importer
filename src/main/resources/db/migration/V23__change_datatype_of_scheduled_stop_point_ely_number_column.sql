DROP VIEW network.scheduled_stop_points_with_history;

ALTER TABLE network.scheduled_stop_points
    ALTER COLUMN scheduled_stop_point_ely_number TYPE bigint USING (trim(scheduled_stop_point_ely_number)::integer);

ALTER TABLE network.scheduled_stop_points_history
    ALTER COLUMN scheduled_stop_point_ely_number TYPE bigint USING (trim(scheduled_stop_point_ely_number)::integer);

ALTER TABLE network.scheduled_stop_points_staging
    ALTER COLUMN scheduled_stop_point_ely_number TYPE bigint USING (trim(scheduled_stop_point_ely_number)::integer);

CREATE OR REPLACE VIEW network.scheduled_stop_points_with_history AS
SELECT *
FROM network.scheduled_stop_points
UNION ALL
SELECT *
FROM network.scheduled_stop_points_history;
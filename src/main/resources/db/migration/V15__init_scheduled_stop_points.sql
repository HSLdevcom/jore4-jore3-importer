-- SCHEDULED STOP POINTS

CREATE TABLE network.scheduled_stop_points (
    scheduled_stop_point_id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    scheduled_stop_point_ext_id VARCHAR(7) NOT NULL,
    infrastructure_node_id uuid NOT NULL REFERENCES infrastructure_network.infrastructure_nodes(infrastructure_node_id),
    scheduled_stop_point_ely_number VARCHAR(10),
    scheduled_stop_point_name JSONB NOT NULL,
    scheduled_stop_point_sys_period tstzrange DEFAULT tstzrange(current_timestamp, null) NOT NULL
);

CREATE UNIQUE INDEX scheduled_stop_points_ext_id_idx
    ON network.scheduled_stop_points (scheduled_stop_point_ext_id);

-- Staging table used by the scheduled stop points import job

CREATE TABLE network.scheduled_stop_points_staging (
    scheduled_stop_point_ext_id VARCHAR(7) PRIMARY KEY,
    scheduled_stop_point_ely_number VARCHAR(10),
    scheduled_stop_point_name JSONB NOT NULL
);

-- VERSIONED SCHEDULED STOP POINTS

CREATE TABLE network.scheduled_stop_points_history (
    LIKE network.scheduled_stop_points
);

CREATE TRIGGER versioning_trigger
    BEFORE INSERT OR UPDATE OR DELETE
    ON network.scheduled_stop_points
    FOR EACH ROW
EXECUTE PROCEDURE temporal.versioning('scheduled_stop_point_sys_period', 'network.scheduled_stop_points_history', true, true);

CREATE OR REPLACE VIEW network.scheduled_stop_points_with_history AS
SELECT *
FROM network.scheduled_stop_points
UNION ALL
SELECT *
FROM network.scheduled_stop_points_history;

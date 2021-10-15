-- ROUTE POINTS

CREATE TABLE network.network_route_points
(
    network_route_point_id         uuid      DEFAULT gen_random_uuid() PRIMARY KEY,
    network_route_direction_id     uuid                                                 NOT NULL REFERENCES network.network_route_directions (network_route_direction_id) ON DELETE CASCADE,
    infrastructure_node            uuid                                                 NOT NULL REFERENCES infrastructure_network.infrastructure_nodes (infrastructure_node_id) ON DELETE CASCADE,
    network_route_point_ext_id     TEXT                                                 NOT NULL,
    network_route_point_order      integer                                              NOT NULL,
    network_route_point_sys_period tstzrange DEFAULT tstzrange(current_timestamp, null) NOT NULL
);

CREATE UNIQUE INDEX network_route_points_ext_id_idx
    ON network.network_route_points (network_route_point_ext_id);

CREATE INDEX network_route_points_route_direction_fkey
    ON network.network_route_points (network_route_direction_id);

CREATE INDEX network_route_points_node_fkey
    ON network.network_route_points (infrastructure_node);

-- Staging table used for route point import

CREATE TABLE network.network_route_points_staging
(
    network_route_point_ext_id     TEXT    NOT NULL PRIMARY KEY,
    network_route_direction_ext_id TEXT    NOT NULL,
    infrastructure_node_ext_id     TEXT    NOT NULL,
    network_route_point_order      integer NOT NULL
);

CREATE INDEX network_route_points_staging_route_direction_fkey
    ON network.network_route_points_staging (network_route_direction_ext_id);

CREATE INDEX network_route_points_staging_node_fkey
    ON network.network_route_points_staging (infrastructure_node_ext_id);

-- VERSIONED ROUTE POINTS

CREATE TABLE network.network_route_points_history
(
    LIKE network.network_route_points,
    -- There should be no overlap between system times for the same entity:
    EXCLUDE USING GIST (
        network_route_point_id WITH =,
        network_route_point_sys_period WITH &&)
);

CREATE TRIGGER versioning_trigger
    BEFORE INSERT OR UPDATE OR DELETE
    ON network.network_route_points
    FOR EACH ROW
EXECUTE PROCEDURE temporal.versioning('network_route_point_sys_period',
                                      'network.network_route_points_history', true, true);

CREATE OR REPLACE VIEW network.network_route_points_with_history AS
SELECT *
FROM network.network_route_points
UNION ALL
SELECT *
FROM network.network_route_points_history;

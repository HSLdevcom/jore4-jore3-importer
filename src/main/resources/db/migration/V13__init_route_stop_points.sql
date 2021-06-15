-- ROUTE STOP POINTS

CREATE TABLE network.network_route_stop_points
(
    network_route_point_id                    uuid                                                 NOT NULL REFERENCES network.network_route_points (network_route_point_id) ON DELETE CASCADE,
    network_route_stop_point_ext_id           TEXT                                                 NOT NULL,
    network_route_stop_point_order            integer                                              NOT NULL,
    network_route_stop_point_hastus_point     boolean                                              NOT NULL,
    network_route_stop_point_timetable_column integer                                              NULL,
    network_route_stop_point_sys_period       tstzrange DEFAULT tstzrange(current_timestamp, null) NOT NULL,

    -- Note how the point_id is both our primary key and a foreign key to the corresponding route point!
    PRIMARY KEY (network_route_point_id)
);

CREATE UNIQUE INDEX network_route_stop_points_ext_id_idx
    ON network.network_route_stop_points (network_route_stop_point_ext_id);

-- Staging table used for route stop point import

CREATE TABLE network.network_route_stop_points_staging
(
    network_route_stop_point_ext_id           TEXT    NOT NULL PRIMARY KEY,
    network_route_stop_point_order            integer NOT NULL,
    network_route_stop_point_hastus_point     boolean NOT NULL,
    network_route_stop_point_timetable_column integer NULL
);

-- VERSIONED ROUTE POINTS

CREATE TABLE network.network_route_stop_points_history
(
    LIKE network.network_route_stop_points,
    -- There should be no overlap between system times for the same entity:
    EXCLUDE USING GIST (
        network_route_point_id WITH =,
        network_route_stop_point_sys_period WITH &&)
);

CREATE TRIGGER versioning_trigger
    BEFORE INSERT OR UPDATE OR DELETE
    ON network.network_route_stop_points
    FOR EACH ROW
EXECUTE PROCEDURE temporal.versioning('network_route_stop_point_sys_period',
                                      'network.network_route_stop_points_history', true, true);

CREATE OR REPLACE VIEW network.network_route_stop_points_with_history AS
SELECT *
FROM network.network_route_stop_points
UNION ALL
SELECT *
FROM network.network_route_stop_points_history;

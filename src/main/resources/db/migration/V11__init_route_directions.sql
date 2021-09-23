-- ROUTE DIRECTIONS

CREATE TABLE network.network_route_directions
(
    network_route_direction_id               uuid      DEFAULT gen_random_uuid() PRIMARY KEY,
    network_route_id                         uuid                                                 NOT NULL REFERENCES network.network_routes (network_route_id) ON DELETE CASCADE,
    network_route_direction_type             text                                                 NOT NULL REFERENCES network.network_direction_types (network_direction_type),
    network_route_direction_ext_id           TEXT                                                 NOT NULL,
    network_route_direction_length           INTEGER,
    network_route_direction_name             JSONB                                                NOT NULL,
    network_route_direction_name_short       JSONB                                                NOT NULL,
    network_route_direction_origin           JSONB                                                NOT NULL,
    network_route_direction_destination      JSONB                                                NOT NULL,
    network_route_direction_valid_date_range daterange                                            NOT NULL,
    network_route_direction_sys_period       tstzrange DEFAULT tstzrange(current_timestamp, null) NOT NULL,

    -- There should be no overlap between valid dates for the same parent route and direction:
    EXCLUDE USING GIST (
        network_route_id WITH =,
        network_route_direction_type WITH =,
        network_route_direction_valid_date_range WITH &&)
);

CREATE UNIQUE INDEX network_route_directions_ext_id_idx
    ON network.network_route_directions (network_route_direction_ext_id);

-- Staging table used for route direction import

CREATE TABLE network.network_route_directions_staging
(
    network_route_direction_ext_id           TEXT      NOT NULL PRIMARY KEY,
    network_route_ext_id                     TEXT      NOT NULL,
    network_route_direction_type             text      NOT NULL REFERENCES network.network_direction_types (network_direction_type),
    network_route_direction_length           INTEGER,
    network_route_direction_name             JSONB     NOT NULL,
    network_route_direction_name_short       JSONB     NOT NULL,
    network_route_direction_origin           JSONB     NOT NULL,
    network_route_direction_destination      JSONB     NOT NULL,
    network_route_direction_valid_date_range daterange NOT NULL,

    -- There should be no overlap between valid dates for the same route direction:
    EXCLUDE USING GIST (
        network_route_direction_ext_id WITH =,
        network_route_direction_valid_date_range WITH &&)
);

-- VERSIONED ROUTE DIRECTIONS

CREATE TABLE network.network_route_directions_history
(
    LIKE network.network_route_directions,
    -- There should be no overlap between system times for the same entity:
    EXCLUDE USING GIST (
        network_route_direction_ext_id WITH =,
        network_route_direction_sys_period WITH &&)
);

CREATE TRIGGER versioning_trigger
    BEFORE INSERT OR UPDATE OR DELETE
    ON network.network_route_directions
    FOR EACH ROW
EXECUTE PROCEDURE temporal.versioning('network_route_direction_sys_period',
                                      'network.network_route_directions_history', true, true);

CREATE OR REPLACE VIEW network.network_route_directions_with_history AS
SELECT *
FROM network.network_route_directions
UNION ALL
SELECT *
FROM network.network_route_directions_history;

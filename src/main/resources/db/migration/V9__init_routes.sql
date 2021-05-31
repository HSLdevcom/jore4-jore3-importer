-- ROUTES

CREATE TABLE network.network_routes
(
    network_route_id         uuid      DEFAULT gen_random_uuid() PRIMARY KEY,
    network_line_id          uuid                                                 NOT NULL REFERENCES network.network_lines (network_line_id) ON DELETE CASCADE,
    network_route_ext_id     TEXT                                                 NOT NULL,
    network_route_number     TEXT                                                 NOT NULL,
    network_route_name       JSONB                                                NOT NULL,
    network_route_sys_period tstzrange DEFAULT tstzrange(current_timestamp, null) NOT NULL
);

CREATE UNIQUE INDEX network_routes_ext_id_idx
    ON network.network_routes (network_route_ext_id);

-- Staging table used for route import

CREATE TABLE network.network_routes_staging
(
    network_route_ext_id TEXT  NOT NULL PRIMARY KEY,
    network_line_ext_id  TEXT  NOT NULL,
    network_route_number TEXT  NOT NULL,
    network_route_name   JSONB NOT NULL
);

-- VERSIONED ROUTES

CREATE TABLE network.network_routes_history
(
    LIKE network.network_routes,
    -- There should be no overlap between system times for the same entity:
    EXCLUDE USING GIST (network_route_id WITH =, network_route_sys_period WITH &&)
);

CREATE TRIGGER versioning_trigger
    BEFORE INSERT OR UPDATE OR DELETE
    ON network.network_routes
    FOR EACH ROW
EXECUTE PROCEDURE temporal.versioning('network_route_sys_period',
                                      'network.network_routes_history', true, true);

CREATE OR REPLACE VIEW network.network_routes_with_history AS
SELECT *
FROM network.network_routes
UNION ALL
SELECT *
FROM network.network_routes_history;

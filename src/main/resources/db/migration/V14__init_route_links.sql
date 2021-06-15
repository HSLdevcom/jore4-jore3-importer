-- ROUTE LINKS

CREATE TABLE network.network_route_links
(
    network_route_link_id         uuid      DEFAULT gen_random_uuid() PRIMARY KEY,
    network_route_direction_id    uuid                                                 NOT NULL REFERENCES network.network_route_directions (network_route_direction_id) ON DELETE CASCADE,
    infrastructure_link_id        uuid                                                 NOT NULL REFERENCES infrastructure_network.infrastructure_links (infrastructure_link_id) ON DELETE CASCADE,
    network_route_link_ext_id     TEXT                                                 NOT NULL,
    network_route_link_order      integer                                              NOT NULL,
    network_route_link_sys_period tstzrange DEFAULT tstzrange(current_timestamp, null) NOT NULL
);

CREATE UNIQUE INDEX network_route_links_ext_id_idx
    ON network.network_route_links (network_route_link_ext_id);

CREATE INDEX network_route_links_route_direction_fkey
    ON network.network_route_links (network_route_direction_id);

CREATE INDEX network_route_links_link_fkey
    ON network.network_route_links (infrastructure_link_id);

-- Staging table used for route link import

CREATE TABLE network.network_route_links_staging
(
    network_route_link_ext_id      TEXT    NOT NULL PRIMARY KEY,
    network_route_direction_ext_id TEXT    NOT NULL,
    infrastructure_link_ext_id     TEXT    NOT NULL,
    network_route_link_order       integer NOT NULL
);

CREATE INDEX network_route_links_staging_route_direction_fkey
    ON network.network_route_links_staging (network_route_direction_ext_id);

CREATE INDEX network_route_points_staging_link_fkey
    ON network.network_route_links_staging (infrastructure_link_ext_id);

-- VERSIONED ROUTE POINTS

CREATE TABLE network.network_route_links_history
(
    LIKE network.network_route_links,
    -- There should be no overlap between system times for the same entity:
    EXCLUDE USING GIST (
        network_route_link_id WITH =,
        network_route_link_sys_period WITH &&)
);

CREATE TRIGGER versioning_trigger
    BEFORE INSERT OR UPDATE OR DELETE
    ON network.network_route_links
    FOR EACH ROW
EXECUTE PROCEDURE temporal.versioning('network_route_link_sys_period',
                                      'network.network_route_links_history', true, true);

CREATE OR REPLACE VIEW network.network_route_links_with_history AS
SELECT *
FROM network.network_route_links
UNION ALL
SELECT *
FROM network.network_route_links_history;

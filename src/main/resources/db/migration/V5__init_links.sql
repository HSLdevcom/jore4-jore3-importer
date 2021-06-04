-- TODO: We should probably not store Jore 3 stuff directly in the "infrastructure" schema
--       Instead, we should have a "jore3" schema, which hosts all the raw data from Jore 3

-- LINKS

CREATE TABLE infrastructure_network.infrastructure_links
(
    infrastructure_link_id         uuid      DEFAULT gen_random_uuid() PRIMARY KEY,
    infrastructure_link_ext_id     TEXT                                                 NOT NULL,
    -- Link shape directly from start->end
    infrastructure_link_geog       geography(LinestringZ, 4326)                         NOT NULL,
    infrastructure_network_type    text                                                 NOT NULL REFERENCES infrastructure_network.infrastructure_network_types (infrastructure_network_type),
    infrastructure_link_sys_period tstzrange DEFAULT tstzrange(current_timestamp, null) NOT NULL,
    infrastructure_link_start_node uuid                                                 NOT NULL REFERENCES infrastructure_network.infrastructure_nodes (infrastructure_node_id) ON DELETE CASCADE,
    infrastructure_link_end_node   uuid                                                 NOT NULL REFERENCES infrastructure_network.infrastructure_nodes (infrastructure_node_id) ON DELETE CASCADE
);

CREATE INDEX infrastructure_links_infrastructure_network_type_fkey
    ON infrastructure_network.infrastructure_links (infrastructure_network_type);

CREATE UNIQUE INDEX idx_infrastructure_links_ext_id
    ON infrastructure_network.infrastructure_links (infrastructure_link_ext_id);

-- Staging table used for link import

CREATE TABLE infrastructure_network.infrastructure_links_staging
(
    -- id and sys_period are omitted
    infrastructure_link_ext_id            TEXT                         NOT NULL,
    infrastructure_link_geog              geography(LinestringZ, 4326) NOT NULL,
    infrastructure_network_type           text                         NOT NULL REFERENCES infrastructure_network.infrastructure_network_types (infrastructure_network_type),
    infrastructure_link_start_node_ext_id TEXT                         NOT NULL,
    infrastructure_link_end_node_ext_id   TEXT                         NOT NULL,

    PRIMARY KEY (infrastructure_link_ext_id)
);

-- VERSIONED LINKS

CREATE TABLE infrastructure_network.infrastructure_links_history
(
    LIKE infrastructure_network.infrastructure_links,
    -- There should be no overlap between system times for the same entity:
    EXCLUDE USING GIST (infrastructure_link_id WITH =, infrastructure_link_sys_period WITH &&)
);

CREATE TRIGGER versioning_trigger
    BEFORE INSERT OR UPDATE OR DELETE
    ON infrastructure_network.infrastructure_links
    FOR EACH ROW
EXECUTE PROCEDURE temporal.versioning('infrastructure_link_sys_period',
                                      'infrastructure_network.infrastructure_links_history', true, true);

CREATE OR REPLACE VIEW infrastructure_network.infrastructure_links_with_history AS
SELECT *
FROM infrastructure_network.infrastructure_links
UNION ALL
SELECT *
FROM infrastructure_network.infrastructure_links_history;

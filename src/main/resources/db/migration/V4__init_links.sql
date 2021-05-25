-- TODO: We should probably not store Jore 3 stuff directly in the "infrastructure" schema
--       Instead, we should have a "jore3" schema, which hosts all the raw data from Jore 3

-- LINKS

CREATE TABLE infrastructure_network.infrastructure_links
(
    infrastructure_link_id      uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    infrastructure_link_ext_id  TEXT                         NOT NULL,
    -- Link shape directly from start->end
    infrastructure_link_geog    geography(LinestringZ, 4326) NOT NULL,
    -- Link shape from start->...->end (including intermediate points).
    infrastructure_link_points  geography(LinestringZ, 4326),
    infrastructure_network_type text                         NOT NULL REFERENCES infrastructure_network.infrastructure_network_types (infrastructure_network_type)
);

CREATE INDEX infrastructure_links_infrastructure_network_type_fkey
    ON infrastructure_network.infrastructure_links (infrastructure_network_type);

CREATE UNIQUE INDEX idx_infrastructure_links_ext_id_type
    ON infrastructure_network.infrastructure_links (infrastructure_link_ext_id, infrastructure_network_type);

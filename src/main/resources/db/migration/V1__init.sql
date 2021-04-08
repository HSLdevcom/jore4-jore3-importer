CREATE SCHEMA infrastructure_network;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- NETWORK TYPES

CREATE TABLE infrastructure_network.infrastructure_network_types
(
    infrastructure_network_type text NOT NULL PRIMARY KEY
);

INSERT INTO infrastructure_network.infrastructure_network_types
    (infrastructure_network_type)
VALUES ('road'),
       ('railway'),
       ('tram_track'),
       ('metro_track'),
       ('waterway');

-- TODO: We should probably not store Jore 3 stuff directly in the "infrastructure" schema
--       Instead, we should have a "jore3" schema, which hosts all the raw data from Jore 3

-- LINKS

CREATE TABLE infrastructure_network.infrastructure_links
(
    infrastructure_link_id      uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    infrastructure_link_ext_id  TEXT                         NOT NULL,
    -- Geometry from start->end
    infrastructure_link_geog    geography(LinestringZ, 4326) NOT NULL,
    -- Geometry from start->...->end (including intermediate points).
    infrastructure_link_points  geography(LinestringZ, 4326),
    infrastructure_network_type text                         NOT NULL REFERENCES infrastructure_network.infrastructure_network_types (infrastructure_network_type)
);

CREATE INDEX infrastructure_links_infrastructure_network_type_fkey
    ON infrastructure_network.infrastructure_links (infrastructure_network_type);

CREATE UNIQUE INDEX idx_infrastructure_links_ext_id_type
    ON infrastructure_network.infrastructure_links (infrastructure_link_ext_id, infrastructure_network_type);

-- NODE TYPES

CREATE TABLE infrastructure_network.infrastructure_node_types
(
    infrastructure_node_type_value   TEXT PRIMARY KEY,
    infrastructure_node_type_comment TEXT NOT NULL
);

INSERT INTO infrastructure_network.infrastructure_node_types
    (infrastructure_node_type_value, infrastructure_node_type_comment)
VALUES ('S', 'Stop'),
       ('X', 'Crossroads/Junction'),
       ('B', 'Border between municipalities'),
       ('U', 'Unknown');

-- NODES

CREATE TABLE infrastructure_network.infrastructure_nodes
(
    infrastructure_node_id                 uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    infrastructure_node_ext_id             TEXT                   NOT NULL,
    infrastructure_node_type               TEXT                   NOT NULL REFERENCES infrastructure_network.infrastructure_node_types (infrastructure_node_type_value),
    infrastructure_node_location           geometry(PointZ, 4326) NOT NULL,
    infrastructure_node_projected_location geometry(PointZ, 4326)
);

CREATE INDEX ON infrastructure_network.infrastructure_nodes (infrastructure_node_type);

CREATE UNIQUE INDEX idx_infrastructure_nodes_ext_id_type
    ON infrastructure_network.infrastructure_nodes (infrastructure_node_ext_id, infrastructure_node_type);

-- TODO: We should probably not store Jore 3 stuff directly in the "infrastructure" schema
--       Instead, we should have a "jore3" schema, which hosts all the raw data from Jore 3

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

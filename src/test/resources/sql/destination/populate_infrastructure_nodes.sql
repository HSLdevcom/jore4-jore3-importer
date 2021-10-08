INSERT INTO infrastructure_network.infrastructure_nodes
    (infrastructure_node_id, infrastructure_node_ext_id, infrastructure_node_type, infrastructure_node_location, infrastructure_node_projected_location)
VALUES (
    'cc11a5db-2ae7-4220-adfe-aca5d6620909',
    'c',
    'S',
    ST_Force3D(point(6, 5)::geometry),
    ST_Force3D(point(13, 12)::geometry)
);
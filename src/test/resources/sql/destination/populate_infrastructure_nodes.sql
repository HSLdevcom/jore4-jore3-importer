INSERT INTO infrastructure_network.infrastructure_nodes (
        infrastructure_node_id,
        infrastructure_node_ext_id,
        infrastructure_node_type,
        infrastructure_node_location,
        infrastructure_node_projected_location
)
VALUES (
        '00002c7a-bd85-43ed-afb9-389b498aaa06',
        '6080216',
        'S',
        ST_Force3D(point(24.808003, 60.247783)::geometry),
        ST_Force3D(point(24.808003, 60.247783)::geometry)
);
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
        ST_Force3D(point(24.457948, 60.088725)::geometry),
        ST_Force3D(point(24.457981, 60.088986)::geometry)
);

INSERT INTO infrastructure_network.infrastructure_nodes (
        infrastructure_node_id,
        infrastructure_node_ext_id,
        infrastructure_node_type,
        infrastructure_node_location,
        infrastructure_node_projected_location
)
VALUES (
        '0009b559-0e7c-4549-99eb-5391727d1016',
        '"6010203"',
        'S',
        ST_Force3D(point(24.468175, 60.15286)::geometry),
        ST_Force3D(point(24.468122, 60.152911)::geometry)
);
INSERT INTO infrastructure_network.infrastructure_links (
        infrastructure_link_id,
        infrastructure_link_ext_id,
        infrastructure_link_geog,
        infrastructure_network_type,
        infrastructure_link_start_node,
        infrastructure_link_end_node
)
VALUES (
        '00018613-e3a1-41be-9ba0-a95f61d70aec',
        '1-9410205-9410203',
        ST_MakeLine(ST_Force3D(point(24.457948, 60.088725)::geometry), ST_Force3D(point(24.468175, 60.15286)::geometry)),
        'road',
        '00002c7a-bd85-43ed-afb9-389b498aaa06',
        '0009b559-0e7c-4549-99eb-5391727d1016'
);
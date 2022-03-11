INSERT INTO infrastructure_network.infrastructure_link_shapes (
        infrastructure_link_shape_id,
        infrastructure_link_ext_id,
        infrastructure_link_id,
        infrastructure_link_shape
)
VALUES (
        '2bc109a6-be4d-4119-a763-d15d7d46a934',
        '1-9410205-9410203',
        '00018613-e3a1-41be-9ba0-a95f61d70aec',
        ST_MakeLine(ST_SetSRID(ST_MakePoint(24.457948, 60.088725, 0), 4326), ST_SetSRID(ST_MakePoint(24.468175, 60.15286, 0), 4326))
);
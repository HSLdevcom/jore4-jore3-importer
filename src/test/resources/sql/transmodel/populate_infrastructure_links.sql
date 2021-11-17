INSERT INTO infrastructure_network.infrastructure_link (
        infrastructure_link_id,
        direction,
        shape,
        estimated_length_in_metres,
        external_link_id,
        external_link_source
)
VALUES (
        '554c63e6-87b2-4dc8-a032-b6b0e2607696',
        'bidirectional',
        ST_MakeLine(ST_Force3D(point(24.457948, 60.088725)::geometry), ST_Force3D(point(24.468175, 60.15286)::geometry)),
        1000,
        '1234567890',
        'digiroad_r'
);
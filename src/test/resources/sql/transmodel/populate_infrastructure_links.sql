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
        ST_MakeLine(ST_SetSRID(ST_MakePoint(24.457948, 60.088725, 0), 4326), ST_SetSRID(ST_MakePoint(24.468175, 60.15286, 0), 4326)),
        1000,
        '1234567890',
        'digiroad_r'
);
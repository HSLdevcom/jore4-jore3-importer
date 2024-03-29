BEGIN;

INSERT INTO service_pattern.scheduled_stop_point_invariant (label)
VALUES
    ('H1234'),
    ('H4321'),
    ('H5678')
ON CONFLICT DO NOTHING;

INSERT INTO service_pattern.scheduled_stop_point (
    scheduled_stop_point_id,
    measured_location,
    located_on_infrastructure_link_id,
    direction,
    label,
    timing_place_id,
    validity_start,
    validity_end,
    priority
)
VALUES
    (
        '45e83727-41fb-4e75-ad71-7e54d58f23ac',
        ST_SetSRID(ST_MakePoint(24.46050475, 60.10475875, 0), 4326),
        '554c63e6-87b2-4dc8-a032-b6b0e2607696',
        'forward',
        'H1234',
        '4cfb005b-eef8-4f5c-b0f1-43c8784a1f47',
        '2020-01-01 02:30:00+00',
        '2051-01-01 02:29:59+00',
        10
    ),
    (
        '48a88a16-7b8c-4a97-ac2b-c9bf2ac3a08d',
        ST_SetSRID(ST_MakePoint(24.4630615, 60.1207925, 0), 4326),
        '554c63e6-87b2-4dc8-a032-b6b0e2607696',
        'forward',
        'H4321',
        null,
        '2020-01-01 02:30:00+00',
        '2051-01-01 02:29:59+00',
        10
    ),
    (
        'd3fa4777-0479-418c-ba35-779a4f4d8941',
        ST_SetSRID(ST_MakePoint(24.46561825, 60.13682625, 0), 4326),
        '554c63e6-87b2-4dc8-a032-b6b0e2607696',
        'forward',
        'H5678',
        '30fc2925-8e81-4710-8a84-6aea2eece655',
        '2020-01-01 02:30:00+00',
        '2051-01-01 02:29:59+00',
        10
    );

INSERT INTO service_pattern.vehicle_mode_on_scheduled_stop_point (
    scheduled_stop_point_id,
    vehicle_mode
)
VALUES
    (
        '45e83727-41fb-4e75-ad71-7e54d58f23ac',
        'bus'
    ),
    (
        '48a88a16-7b8c-4a97-ac2b-c9bf2ac3a08d',
        'bus'
    ),
    (
        'd3fa4777-0479-418c-ba35-779a4f4d8941',
        'bus'
    );

COMMIT;

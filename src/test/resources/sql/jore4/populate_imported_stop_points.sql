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
        '135bfbf6-6912-4a1e-854b-f52a9287a38a',
        ST_SetSRID(ST_MakePoint(24.46050475, 60.10475875, 0), 4326),
        '554c63e6-87b2-4dc8-a032-b6b0e2607696',
        'forward',
        'H1234',
        '4cfb005b-eef8-4f5c-b0f1-43c8784a1f47',
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
        '135bfbf6-6912-4a1e-854b-f52a9287a38a',
        'bus'
    );

COMMIT;

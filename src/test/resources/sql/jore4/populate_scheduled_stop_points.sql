BEGIN;

INSERT INTO service_pattern.scheduled_stop_point_invariant (label)
VALUES ('H1234')
ON CONFLICT DO NOTHING;

INSERT INTO service_pattern.scheduled_stop_point_invariant (label)
VALUES ('H4321')
ON CONFLICT DO NOTHING;

INSERT INTO service_pattern.scheduled_stop_point (
        scheduled_stop_point_id,
        measured_location,
        located_on_infrastructure_link_id,
        direction,
        label,
        validity_start,
        validity_end,
        priority
)
VALUES (
        '45e83727-41fb-4e75-ad71-7e54d58f23ac',
        ST_SetSRID(ST_MakePoint(6, 5, 0), 4326),
        '554c63e6-87b2-4dc8-a032-b6b0e2607696',
        'forward',
        'H1234',
        '2020-01-01 02:30:00+00',
        '2051-01-01 02:29:59+00',
        10
);

INSERT INTO service_pattern.scheduled_stop_point (
        scheduled_stop_point_id,
        measured_location,
        located_on_infrastructure_link_id,
        direction,
        label,
        validity_start,
        validity_end,
        priority
)
VALUES (
        '48a88a16-7b8c-4a97-ac2b-c9bf2ac3a08d',
        ST_SetSRID(ST_MakePoint(8, 8, 0), 4326),
        '554c63e6-87b2-4dc8-a032-b6b0e2607696',
        'forward',
        'H4321',
        '2020-01-01 02:30:00+00',
        '2051-01-01 02:29:59+00',
        10
);

INSERT INTO service_pattern.vehicle_mode_on_scheduled_stop_point (
        scheduled_stop_point_id,
        vehicle_mode
)
VALUES (
        '45e83727-41fb-4e75-ad71-7e54d58f23ac',
        'bus'
);
INSERT INTO service_pattern.vehicle_mode_on_scheduled_stop_point (
        scheduled_stop_point_id,
        vehicle_mode
)
VALUES (
        '48a88a16-7b8c-4a97-ac2b-c9bf2ac3a08d',
        'bus'
);

COMMIT;
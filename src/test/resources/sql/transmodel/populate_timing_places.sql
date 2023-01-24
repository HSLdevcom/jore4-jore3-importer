BEGIN;

INSERT INTO timing_pattern.timing_place (
        timing_place_id,
        label,
        description
)
VALUES (
        '4cfb005b-eef8-4f5c-b0f1-43c8784a1f47',
        'Test Place 1',
        null
);

INSERT INTO timing_pattern.timing_place (
        timing_place_id,
        label,
        description
)
VALUES (
        '30fc2925-8e81-4710-8a84-6aea2eece655',
        'Test Place 2',
        null
);

UPDATE service_pattern.scheduled_stop_point
    SET timing_place_id = '4cfb005b-eef8-4f5c-b0f1-43c8784a1f47'
    WHERE scheduled_stop_point_id = '45e83727-41fb-4e75-ad71-7e54d58f23ac';


UPDATE service_pattern.scheduled_stop_point
    SET timing_place_id = '30fc2925-8e81-4710-8a84-6aea2eece655'
    WHERE scheduled_stop_point_id = '48a88a16-7b8c-4a97-ac2b-c9bf2ac3a08d';

COMMIT;

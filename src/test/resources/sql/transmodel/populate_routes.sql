BEGIN;

INSERT INTO route.route (
        route_id,
        name_i18n,
        description_i18n,
        direction,
        label,
        on_line_id,
        starts_from_scheduled_stop_point_id,
        ends_at_scheduled_stop_point_id,
        validity_start,
        validity_end,
        priority
)
VALUES (
        '5bfa9a65-c80f-4af8-be95-8370cb12df50',
        '{"fi_FI":"Keskustori - Etelä-Hervanta","sv_SE":"Central torget - Södra Hervanta"}',
        '{"fi_FI":"reitti Keskustori - Etelä-Hervanta","sv_SE":"route Central torget - Södra Hervanta"}',
        'inbound',
        '30',
        '5aa7d9fc-2cf9-466d-8ac0-f442d60c261f',
        '45e83727-41fb-4e75-ad71-7e54d58f23ac',
        '48a88a16-7b8c-4a97-ac2b-c9bf2ac3a08d',
        '2020-01-01 02:30:00+00',
        '2022-01-01 02:29:59+00',
        10
);

INSERT INTO route.infrastructure_link_along_route (
        route_id,
        infrastructure_link_id,
        infrastructure_link_sequence,
        is_traversal_forwards
)
VALUES (
        '5bfa9a65-c80f-4af8-be95-8370cb12df50',
        '554c63e6-87b2-4dc8-a032-b6b0e2607696',
        1,
        true
);

COMMIT;
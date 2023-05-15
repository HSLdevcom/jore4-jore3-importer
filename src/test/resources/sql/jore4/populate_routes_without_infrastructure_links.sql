INSERT INTO route.route (
    route_id,
    name_i18n,
    description_i18n,
    direction,
    label,
    on_line_id,
    validity_start,
    validity_end,
    legacy_hsl_municipality_code,
    priority
)
VALUES (
    '5bfa9a65-c80f-4af8-be95-8370cb12df50',
    '{"fi_FI":"Keskustori - Etelä-Hervanta","sv_SE":"Central torget - Södra Hervanta"}',
    '{"fi_FI":"reitti Keskustori - Etelä-Hervanta","sv_SE":"route Central torget - Södra Hervanta"}',
    'inbound',
    '30',
    '5aa7d9fc-2cf9-466d-8ac0-f442d60c261f',
    '2020-01-01 02:30:00+00',
    '2022-01-01 02:29:59+00',
    'helsinki',
    10
);

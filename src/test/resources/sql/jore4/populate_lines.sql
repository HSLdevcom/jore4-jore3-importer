INSERT INTO route.line (
    line_id,
    name_i18n,
    short_name_i18n,
    label,
    primary_vehicle_mode,
    validity_start,
    validity_end,
    type_of_line,
    transport_target,
    legacy_hsl_municipality_code,
    priority
)
VALUES (
    '5aa7d9fc-2cf9-466d-8ac0-f442d60c261f',
    '{"fi_FI": "Keskustori - Duo - Etelä-Hervanta","sv_SE": "Central torget - Duo - Södra Hervanta"}',
    '{"fi_FI": "Keskustori - Etelä-Hervanta","sv_SE": "Central torget - Södra Hervanta"}',
    '30',
    'bus',
    '2020-01-01 02:30:00+00',
    '2022-01-01 02:29:59+00',
    'stopping_bus_service',
    'helsinki_internal_traffic',
    'helsinki',
    10
);

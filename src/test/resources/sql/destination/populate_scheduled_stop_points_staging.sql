INSERT INTO network.scheduled_stop_points_staging
    (scheduled_stop_point_ext_id, scheduled_stop_point_location, scheduled_stop_point_name)
VALUES (
    'c',
    ST_Force3D(point(6, 5)::geometry),
    '{"fi_FI": "Yliopisto","sv_SE": "Universitetet"}'
);
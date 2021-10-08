INSERT INTO network.scheduled_stop_points
    (scheduled_stop_point_id, scheduled_stop_point_ext_id, infrastructure_node_id, scheduled_stop_point_location, scheduled_stop_point_name)
VALUES (
    '058a63b3-365b-4676-af51-809bef577cdd',
    'c',
    'cc11a5db-2ae7-4220-adfe-aca5d6620909',
    ST_Force3D(point(1, 1)::geometry),
    '{"fi_FI": "Yliopisto vanha","sv_SE": "Universitetet gamla"}'
);
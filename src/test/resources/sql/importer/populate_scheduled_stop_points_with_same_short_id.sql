INSERT INTO network.scheduled_stop_points (
    scheduled_stop_point_id,
    scheduled_stop_point_ext_id,
    infrastructure_node_id,
    scheduled_stop_point_ely_number,
    scheduled_stop_point_name,
    scheduled_stop_point_short_id,
    usage_in_routes
)
VALUES
    (
        '058a63b3-365b-4676-af51-809bef577cdd',
        'c',
        'cc11a5db-2ae7-4220-adfe-aca5d6620909',
        1234567890,
        '{"fi_FI": "Yliopisto vanha","sv_SE": "Universitetet gamla"}',
        'H1234',
        1
    ),
    (
        'c5390e54-1135-4b85-aab9-cf4a51c559bd',
        'd',
        'cc11a5db-2ae7-4220-adfe-aca5d6620909',
        9876543211,
        '{"fi_FI": "Yliopisto vanha","sv_SE": "Universitetet gamla"}',
        'H1234',
        3
    );
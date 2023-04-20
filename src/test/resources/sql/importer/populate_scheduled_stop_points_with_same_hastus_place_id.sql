INSERT INTO network.scheduled_stop_points (
    scheduled_stop_point_id,
    scheduled_stop_point_ext_id,
    infrastructure_node_id,
    scheduled_stop_point_ely_number,
    scheduled_stop_point_name,
    scheduled_stop_point_short_id,
    hastus_place_id,
    usage_in_routes
)
VALUES
    (
        '058a63b3-365b-4676-af51-809bef577cdd',
        '1000003',
        'cc11a5db-2ae7-4220-adfe-aca5d6620909',
        1234567890,
        '{"fi_FI": "Yliopisto uusi","sv_SE": "Universitetet nya"}',
        'H1234',
        '1ELIEL',
        1
    ),
    (
        'c5390e54-1135-4b85-aab9-cf4a51c559bd',
        '1000004',
        'a799ab85-bfc3-4b6b-8ea2-590f4f3e96cf',
        9876543211,
        '{"fi_FI": "Yliopisto vanha","sv_SE": "Universitetet gamla"}',
        'H5678',
        '1ELIEL',
        3
    );

INSERT INTO network.scheduled_stop_points (
    scheduled_stop_point_id,
    scheduled_stop_point_ext_id,
    infrastructure_node_id,
    scheduled_stop_point_ely_number,
    scheduled_stop_point_name,
    scheduled_stop_point_short_id,
    scheduled_stop_point_jore4_id,
    network_place_id,
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
        '45e83727-41fb-4e75-ad71-7e54d58f23ac',
        'dfdf8d1d-8649-43bf-9fda-6c23a500a189',
        1
    ),
    (
        '20d94223-ed84-4fd6-be3c-55650901f75b',
        'd',
        '0009b559-0e7c-4549-99eb-5391727d1016',
        987654321,
        '{"fi_FI": "Etelä-Hervanta vanha","sv_SE": "Södra Hervanta gamla"}',
        'H4321',
        '48a88a16-7b8c-4a97-ac2b-c9bf2ac3a08d',
        null,
        1
    ),
    (
        'c0330e4a-5285-4bd8-94bf-ce53bfca21fb',
        '6080216',
        '00002c7a-bd85-43ed-afb9-389b498aaa06',
        23456789,
        '{"fi_FI": "Pasila","sv_SE": "Böle"}',
        'H5678',
        'd3fa4777-0479-418c-ba35-779a4f4d8941',
        '5992337c-6f19-4458-9234-090ef399537b',
        1
    );

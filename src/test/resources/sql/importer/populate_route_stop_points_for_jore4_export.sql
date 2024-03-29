INSERT INTO network.network_route_stop_points (
        network_route_point_id,
        network_route_stop_point_ext_id,
        network_route_stop_point_order,
        network_route_stop_point_hastus_point,
        network_route_stop_point_regulated_timing_point_status,
        network_route_stop_point_timetable_column,
        network_route_stop_point_via_point,
        network_route_stop_point_via_name
)
VALUES
    (
        '00000cc9-d691-492e-b55c-294b903fca33',
        '1111111-c',
        1,
        true,
        1,
        1,
        true,
        '{"fi_FI": "ViaSuomi","sv_SE": "ViaSverige"}'
    ),
    (
        '67f69468-882b-49ec-8e3b-925cc1c0de6e',
        '2222222-d',
        2,
        false,
        0,
        0,
        true,
        '{"fi_FI": "","sv_SE": ""}'
    ),
    (
        'ed4e22ff-42f9-4a21-83fd-1fd99429320c',
        '4444444-6080216',
        3,
        true,
        2,
        1,
        false,
        null
    );

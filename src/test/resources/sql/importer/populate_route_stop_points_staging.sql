INSERT INTO network.network_route_stop_points_staging (
        network_route_stop_point_ext_id,
        network_route_stop_point_order,
        network_route_stop_point_hastus_point,
        network_route_stop_point_regulated_timing_point_status,
        network_route_stop_point_timetable_column,
        network_route_stop_point_via_point,
        network_route_stop_point_via_name
)
VALUES (
        '1234528-1113227',
        6,
        true,
        1,
        12,
        true,
        '{"fi_FI": "ViaSuomi","sv_SE": "ViaSverige"}'
);

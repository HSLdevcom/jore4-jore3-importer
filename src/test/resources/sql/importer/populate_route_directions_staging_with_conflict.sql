INSERT INTO network.network_route_directions_staging (
        network_route_direction_ext_id,
        network_route_ext_id,
        network_route_direction_type,
        network_route_direction_length,
        network_route_direction_name,
        network_route_direction_name_short,
        network_route_direction_origin,
        network_route_direction_destination,
        network_route_direction_valid_date_range
) VALUES
        (
                '1001-2-20211004',
                '1001 3',
                'inbound',
                12400,
                '{"fi_FI": "Existing direction", "sv_SE": "Existing direction"}',
                '{"fi_FI": "Existing", "sv_SE": "Existing"}',
                '{"fi_FI": "Origin", "sv_SE": "Origin"}',
                '{"fi_FI": "Destination", "sv_SE": "Destination"}',
                '[2021-01-01,2022-01-01)'
        ),
        (
                'conflicting-direction',
                '1001 3',
                'inbound',
                100,
                '{"fi_FI": "Conflicting direction", "sv_SE": "Conflicting direction"}',
                '{"fi_FI": "Conflicting", "sv_SE": "Conflicting"}',
                '{"fi_FI": "Origin", "sv_SE": "Origin"}',
                '{"fi_FI": "Destination", "sv_SE": "Destination"}',
                '[2021-06-01,2021-07-01)'
        ),
        (
                'non-conflicting-direction',
                '1001 3',
                'inbound',
                100,
                '{"fi_FI": "Non-conflicting direction", "sv_SE": "Non-conflicting direction"}',
                '{"fi_FI": "Non-conflicting", "sv_SE": "Non-conflicting"}',
                '{"fi_FI": "Origin", "sv_SE": "Origin"}',
                '{"fi_FI": "Destination", "sv_SE": "Destination"}',
                '[2022-01-01,2023-01-01)'
        );


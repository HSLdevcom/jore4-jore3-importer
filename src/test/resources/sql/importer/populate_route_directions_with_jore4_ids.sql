INSERT INTO network.network_route_directions (
        network_route_direction_id,
        network_route_id,
        network_route_direction_type,
        network_route_direction_ext_id,
        network_route_direction_length,
        network_route_direction_name,
        network_route_direction_name_short,
        network_route_direction_origin,
        network_route_direction_destination,
        network_route_direction_valid_date_range,
        network_route_jore4_id
) VALUES (
          '6f93fa6b-8a19-4b98-bd84-b8409e670c70',
          '484d89ae-f365-4c9b-bb1a-8f7b783e95f3',
          'inbound',
          '1001-2-20211004',
          12400,
          '{"fi_FI": "Keskustori - Kaleva - Etelä-Hervanta vanha", "sv_SE": "Central torget - Kaleva - Södra Hervanta gamla"}',
          '{"fi_FI": "Keskustori-Etelä-Hervanta vanha", "sv_SE": "Central torget-Södra Hervanta gamla"}',
          '{"fi_FI": "Keskustori vanha", "sv_SE": "Central torget gamla"}',
          '{"fi_FI": "Etelä-Hervanta vanha", "sv_SE": "Södra Hervanta gamla"}',
          '[2021-01-01,2022-01-01)',
          '5bfa9a65-c80f-4af8-be95-8370cb12df50'
);

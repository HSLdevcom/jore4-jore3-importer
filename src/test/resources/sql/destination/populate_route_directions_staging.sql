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
) VALUES (
          '1001-2-20211004',
          '1001',
          'inbound',
          10700,
          '{"fi_FI": "Keskustori - Kaleva - Etelä-Hervanta", "sv_SE": "Central torget - Kaleva - Södra Hervanta"}',
          '{"fi_FI": "Keskustori-Etelä-Hervanta", "sv_SE": "Central torget-Södra Hervanta"}',
          '{"fi_FI": "Keskustori", "sv_SE": "Central torget"}',
          '{"fi_FI": "Etelä-Hervanta", "sv_SE": "Södra Hervanta"}',
          '[2021-01-01,2022-01-01)'
);
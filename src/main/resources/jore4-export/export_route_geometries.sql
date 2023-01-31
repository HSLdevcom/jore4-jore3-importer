SELECT
    rdo.network_route_direction_id AS route_direction_id,
    rdo.network_route_direction_ext_id as route_direction_ext_id,
    rdo.network_route_jore4_id AS route_jore4_id,
    network_route_direction_shape
FROM network.network_route_directions rdo
CROSS JOIN LATERAL (
    SELECT ST_MakeLine(ST_Force2D(compound_geog::geometry)) FROM (
        SELECT coalesce(ls.infrastructure_link_shape, l.infrastructure_link_geog) AS compound_geog
        FROM network.network_route_directions rd
        INNER JOIN network.network_route_links rl ON rl.network_route_direction_id = rd.network_route_direction_id
        INNER JOIN infrastructure_network.infrastructure_links l ON l.infrastructure_link_id = rl.infrastructure_link_id
        LEFT JOIN infrastructure_network.infrastructure_link_shapes ls ON ls.infrastructure_link_id = l.infrastructure_link_id
        WHERE
            rd.network_route_direction_id = rdo.network_route_direction_id AND l.infrastructure_network_type = 'road'
        ORDER BY rl.network_route_link_order
    ) links ) network_route_direction_shape
WHERE network_route_direction_shape IS NOT NULL AND rdo.network_route_jore4_id IS NOT NULL
ORDER BY rdo.network_route_direction_valid_date_range DESC;

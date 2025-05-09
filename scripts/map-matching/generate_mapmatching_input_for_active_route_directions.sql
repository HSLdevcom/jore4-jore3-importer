COPY (
    SELECT *
    FROM (
        SELECT
            r.network_route_ext_id,
            rd.network_route_direction_type,
            rd.network_route_direction_ext_id,
            rd.network_route_direction_valid_date_range,
            network_route_direction_shape.geojson,
            network_route_point_summary.route_points_json,
            network_route_point_summary.route_points_json::json->0->>'type' AS first_route_point_type,
            network_route_point_summary.route_points_json::json->last_route_point_index->>'type' AS last_route_point_type
        FROM network.network_route_directions rd
        INNER JOIN network.network_routes r ON r.network_route_id = rd.network_route_id
        INNER JOIN network.network_lines l ON l.network_line_id = r.network_line_id
        CROSS JOIN LATERAL (
            SELECT ST_AsGeoJSON(ST_MakeLine(ST_Force2D(compound_geog::geometry))) AS geojson
            FROM (
                SELECT coalesce(infrastructure_link_shape, infrastructure_link_geog) AS compound_geog
                FROM network.network_route_links rl
                INNER JOIN infrastructure_network.infrastructure_links l ON l.infrastructure_link_id = rl.infrastructure_link_id
                LEFT JOIN infrastructure_network.infrastructure_link_shapes ls ON ls.infrastructure_link_id = l.infrastructure_link_id
                WHERE rl.network_route_direction_id = rd.network_route_direction_id
                ORDER BY rl.network_route_link_order
            ) links
        ) network_route_direction_shape
        CROSS JOIN LATERAL (
            SELECT
                json_agg(json_object) AS route_points_json,
                max(network_route_point_order) AS last_route_point_index
            FROM (
                SELECT
                    rp.network_route_point_order,
                    json_strip_nulls(
                        CASE
                            WHEN n.infrastructure_node_type = 'S' THEN json_build_object(
                                'type', 'PUBLIC_TRANSPORT_STOP',
                                'location', ST_AsGeoJSON(ST_Force2D(infrastructure_node_location))::jsonb,
                                'projectedLocation', ST_AsGeoJSON(ST_Force2D(n.infrastructure_node_projected_location))::jsonb,
                                'nationalId', CASE
                                    WHEN ssp.scheduled_stop_point_ely_number IS NULL THEN NULL
                                    -- WHEN trim(ssp.scheduled_stop_point_ely_number) = '' THEN NULL
                                    ELSE ssp.scheduled_stop_point_ely_number::integer
                                END,
                                'passengerId', scheduled_stop_point_short_id
                            )
                            ELSE json_build_object(
                                'type', CASE
                                    WHEN n.infrastructure_node_type = 'X' THEN 'ROAD_JUNCTION'
                                    ELSE 'OTHER'
                                END,
                                'location', ST_AsGeoJSON(ST_Force2D(infrastructure_node_location))::jsonb
                            )
                        END
                    )::jsonb AS json_object
                FROM network.network_route_points rp
                INNER JOIN infrastructure_network.infrastructure_nodes n ON n.infrastructure_node_id = rp.infrastructure_node
                LEFT JOIN network.scheduled_stop_points ssp ON ssp.infrastructure_node_id = n.infrastructure_node_id
                WHERE
                    rp.network_route_direction_id = rd.network_route_direction_id
                    AND n.infrastructure_node_type IN ('B', 'S', 'X')
                ORDER BY rp.network_route_point_order
            ) route_points
        ) network_route_point_summary
        WHERE
            l.infrastructure_network_type = 'road'

            -- AND rd.network_route_direction_valid_date_range && daterange(CURRENT_DATE, CURRENT_DATE + integer '720')
            AND rd.network_route_direction_valid_date_range && '[2025-04-25, 2028-04-25)'::daterange

        ORDER BY network_route_ext_id ASC, network_route_direction_ext_id ASC, network_route_direction_valid_date_range ASC
    ) results
) TO STDOUT WITH (FORMAT CSV, HEADER)

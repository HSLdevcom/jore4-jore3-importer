WITH jore3_route_stop_point AS (
    SELECT
        rd.network_route_direction_ext_id,
        rd.journey_pattern_jore4_id,
        ssp.scheduled_stop_point_jore4_id,
        rsp.network_route_stop_point_order,
        ssp.scheduled_stop_point_short_id,
        rsp.network_route_stop_point_hastus_point,
        np.network_place_ext_id,
        rsp.network_route_stop_point_regulated_timing_point_status,
        rsp.network_route_stop_point_via_point,
        rsp.network_route_stop_point_via_name
    FROM network.network_route_directions rd
    JOIN network.network_route_points rp USING (network_route_direction_id)
    JOIN network.network_route_stop_points rsp USING (network_route_point_id)
    JOIN network.scheduled_stop_points ssp ON ssp.infrastructure_node_id = rp.infrastructure_node
    JOIN network.network_places np USING (network_place_id)
    WHERE rd.journey_pattern_jore4_id IS NOT NULL
),
jore4_via_point AS (
    SELECT *
    FROM (
        SELECT
            network_route_direction_ext_id,
            network_route_stop_point_order,
            -- Take via point name from the previous entry.
            -- The first entry (of route direction) will get NULL value
            -- (and it is not a real via point).
            lag(network_route_stop_point_via_name) OVER (
                PARTITION BY network_route_direction_ext_id
                ORDER BY network_route_stop_point_order ASC
            ) AS network_route_stop_point_via_name
        FROM jore3_route_stop_point
        WHERE network_route_stop_point_via_point
    ) subq
    -- Effectively, discard the first Jore3 via point on each route direction
    -- as they are not real via points. Such via points just indicate from
    -- which stop point the first via information starts to be displayed.
    WHERE network_route_stop_point_via_name IS NOT NULL
),
jore4_journey_pattern_stop_point AS (
    SELECT
        rsp.journey_pattern_jore4_id,
        rsp.network_route_direction_ext_id AS route_direction_jore3_id,
        rsp.network_route_stop_point_order AS order_number,
        rsp.scheduled_stop_point_short_id AS short_id,
        rsp.network_route_stop_point_hastus_point AS is_used_as_timing_point,
        rsp.network_place_ext_id AS timing_place_id,
        rsp.network_route_stop_point_regulated_timing_point_status AS regulated_timing_point_status,
        (vp.network_route_stop_point_via_name IS NOT NULL) AS is_via_point,
        vp.network_route_stop_point_via_name AS via_names
    FROM jore3_route_stop_point rsp
    LEFT JOIN jore4_via_point vp USING (network_route_direction_ext_id, network_route_stop_point_order)
    -- This condition is left for the last step, because if it were evaluated in
    -- earlier steps, it could adversely affect the results - possibly mixing
    -- via information between wrong stop points.
    WHERE rsp.scheduled_stop_point_jore4_id IS NOT NULL
)
SELECT *
FROM jore4_journey_pattern_stop_point
ORDER BY order_number ASC, route_direction_jore3_id ASC

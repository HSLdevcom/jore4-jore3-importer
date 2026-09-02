SELECT
    (
        SELECT string_agg(
            COALESCE(sq1.scheduled_stop_point_ext_id, ''),
            ',' ORDER BY sq1.usage_in_routes DESC, sq1.scheduled_stop_point_id)
        FROM network.scheduled_stop_points sq1
        WHERE sq1.scheduled_stop_point_short_id = s.scheduled_stop_point_short_id
    ) AS external_id,
    (
        SELECT string_agg(
            COALESCE(sq1.scheduled_stop_point_ely_number::text, ''),
            ',' ORDER BY sq1.usage_in_routes DESC, sq1.scheduled_stop_point_id)
        FROM network.scheduled_stop_points sq1
        WHERE sq1.scheduled_stop_point_short_id = s.scheduled_stop_point_short_id
    ) AS ely_number,
    n.infrastructure_node_location AS location,
    s.scheduled_stop_point_name AS name,
    s.scheduled_stop_point_short_id AS short_id,
    np.network_place_ext_id AS timing_place_label
FROM network.scheduled_stop_points s
JOIN infrastructure_network.infrastructure_nodes n USING (infrastructure_node_id)
LEFT JOIN network.network_places np USING (network_place_id)
WHERE LENGTH(s.scheduled_stop_point_short_id) > 4
    AND EXISTS (
        SELECT 1
        FROM network.scheduled_stop_points sq1
        WHERE sq1.scheduled_stop_point_short_id = s.scheduled_stop_point_short_id
            AND sq1.scheduled_stop_point_ely_number IS NOT NULL
    )
    AND s.scheduled_stop_point_ext_id=(
        SELECT MIN(sp.scheduled_stop_point_ext_id)
        FROM network.scheduled_stop_points sp
        WHERE sp.scheduled_stop_point_short_id=s.scheduled_stop_point_short_id
)

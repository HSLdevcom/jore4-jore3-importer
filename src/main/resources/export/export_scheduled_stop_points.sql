SELECT (
           SELECT string_agg(sq1.scheduled_stop_point_ext_id, ',') FROM network.scheduled_stop_points sq1
           WHERE sq1.scheduled_stop_point_short_id = s.scheduled_stop_point_short_id
       ) AS external_id,
       (
           SELECT string_agg(sq1.scheduled_stop_point_ely_number::text, ',') FROM network.scheduled_stop_points sq1
           WHERE sq1.scheduled_stop_point_short_id = s.scheduled_stop_point_short_id
       ) AS ely_number,
       n.infrastructure_node_location AS location,
       s.scheduled_stop_point_name AS name,
       s.scheduled_stop_point_short_id AS short_id
FROM network.scheduled_stop_points s
         JOIN infrastructure_network.infrastructure_nodes n ON (n.infrastructure_node_id = s.infrastructure_node_id)
WHERE s.scheduled_stop_point_ely_number IS NOT NULL
  AND LENGTH(s.scheduled_stop_point_short_id) > 4
  AND s.scheduled_stop_point_ext_id=(
    SELECT MIN(sp.scheduled_stop_point_ext_id)
    FROM network.scheduled_stop_points sp
    WHERE sp.scheduled_stop_point_short_id=s.scheduled_stop_point_short_id
)
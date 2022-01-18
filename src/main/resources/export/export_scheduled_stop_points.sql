SELECT s.scheduled_stop_point_ext_id AS external_id,
       s.scheduled_stop_point_ely_number AS ely_number,
       n.infrastructure_node_location AS location,
       s.scheduled_stop_point_name AS name,
       s.scheduled_stop_point_short_id AS short_id
FROM network.scheduled_stop_points s
    JOIN infrastructure_network.infrastructure_nodes n ON (n.infrastructure_node_id = s.infrastructure_node_id)
WHERE s.scheduled_stop_point_ely_number IS NOT NULL
AND s.scheduled_stop_point_ext_id=(
    SELECT MIN(sp.scheduled_stop_point_ext_id)
    FROM network.scheduled_stop_points sp
    where sp.scheduled_stop_point_short_id=s.scheduled_stop_point_short_id
)
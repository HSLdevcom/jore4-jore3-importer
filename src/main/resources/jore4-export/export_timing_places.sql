SELECT DISTINCT ssp.hastus_place_id AS hastus_place_id
FROM network.scheduled_stop_points ssp
WHERE ssp.hastus_place_id IS NOT NULL
ORDER BY hastus_place_id ASC;

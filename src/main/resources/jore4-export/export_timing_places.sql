SELECT DISTINCT ssp.hastus_place_id AS timing_place_label
FROM network.scheduled_stop_points ssp
WHERE ssp.hastus_place_id IS NOT NULL
ORDER BY timing_place_label ASC;

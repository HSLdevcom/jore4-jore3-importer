SELECT
    network_place_ext_id AS timing_place_label,
    network_place_name AS timing_place_name
FROM network.network_places
ORDER BY timing_place_label ASC;

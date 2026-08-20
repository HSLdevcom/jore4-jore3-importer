DELETE FROM network.journey_pattern jp
WHERE NOT EXISTS (
    SELECT 1
    FROM network.scheduled_stop_point_in_journey_pattern
    WHERE journey_pattern_id = jp.journey_pattern_id
);

DELETE FROM network.route r
WHERE NOT EXISTS (
    SELECT 1
    FROM network.infrastructure_link_along_route
    WHERE route_id = r.route_id
);

DELETE FROM network.route r
WHERE NOT EXISTS (
    SELECT 1
    FROM network.journey_pattern
    WHERE on_route_id = r.route_id
);

DELETE FROM network.line l
WHERE NOT EXISTS (
    SELECT 1
    FROM network.route
    WHERE on_line_id = l.line_id
);

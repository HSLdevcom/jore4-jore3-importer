DELETE FROM journey_pattern.journey_pattern jp
WHERE NOT EXISTS (
    SELECT 1
    FROM journey_pattern.scheduled_stop_point_in_journey_pattern
    WHERE journey_pattern_id = jp.journey_pattern_id
);

DELETE FROM route.route r
WHERE NOT EXISTS (
    SELECT 1
    FROM route.infrastructure_link_along_route
    WHERE route_id = r.route_id
);

DELETE FROM route.route r
WHERE NOT EXISTS (
    SELECT 1
    FROM journey_pattern.journey_pattern
    WHERE on_route_id = r.route_id
);

DELETE FROM route.line l
WHERE NOT EXISTS (
    SELECT 1
    FROM route.route
    WHERE on_line_id = l.line_id
);

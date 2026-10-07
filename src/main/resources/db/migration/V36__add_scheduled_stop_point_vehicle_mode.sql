-- Existing rows use bus until their mode is refreshed from Jore 3 on the next import.
ALTER TABLE network.scheduled_stop_points
    ADD COLUMN vehicle_mode VARCHAR(7) NOT NULL DEFAULT 'bus'
    CHECK (vehicle_mode IN ('bus', 'tram', 'unknown'));

ALTER TABLE network.scheduled_stop_points_staging
    ADD COLUMN vehicle_mode VARCHAR(7) NOT NULL DEFAULT 'bus'
    CHECK (vehicle_mode IN ('bus', 'tram', 'unknown'));

ALTER TABLE network.scheduled_stop_points_history
    ADD COLUMN vehicle_mode VARCHAR(7) NOT NULL DEFAULT 'bus'
    CHECK (vehicle_mode IN ('bus', 'tram', 'unknown'));

CREATE OR REPLACE VIEW network.scheduled_stop_points_with_history AS
SELECT *
FROM network.scheduled_stop_points
UNION ALL
SELECT *
FROM network.scheduled_stop_points_history;

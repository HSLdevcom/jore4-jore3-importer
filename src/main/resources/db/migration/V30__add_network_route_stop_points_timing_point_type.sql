ALTER TABLE network.network_route_stop_points         ADD COLUMN network_route_stop_point_regulated_timing_point_status integer NOT NULL DEFAULT -9999;
ALTER TABLE network.network_route_stop_points_staging ADD COLUMN network_route_stop_point_regulated_timing_point_status integer NOT NULL DEFAULT -9999;
ALTER TABLE network.network_route_stop_points_history ADD COLUMN network_route_stop_point_regulated_timing_point_status integer NOT NULL DEFAULT -9999;

ALTER TABLE network.network_route_stop_points
    ADD CONSTRAINT is_valid_regulated_timing_point_status
        CHECK (
            network_route_stop_point_regulated_timing_point_status IN (0, 1, 2, -9999)
        );

ALTER TABLE network.network_route_stop_points_staging
    ADD CONSTRAINT is_valid_regulated_timing_point_status
        CHECK (
            network_route_stop_point_regulated_timing_point_status IN (0, 1, 2, -9999)
        );

ALTER TABLE network.network_route_stop_points_history
    ADD CONSTRAINT is_valid_regulated_timing_point_status
        CHECK (
            network_route_stop_point_regulated_timing_point_status IN (0, 1, 2, -9999)
        );

CREATE OR REPLACE VIEW network.network_route_stop_points_with_history AS
SELECT *
FROM network.network_route_stop_points
UNION ALL
SELECT *
FROM network.network_route_stop_points;

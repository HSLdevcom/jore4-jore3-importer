BEGIN; TRUNCATE TABLE service_pattern.vehicle_mode_on_scheduled_stop_point CASCADE; COMMIT;
BEGIN; TRUNCATE TABLE route.infrastructure_link_along_route CASCADE; COMMIT;
BEGIN; TRUNCATE TABLE infrastructure_network.vehicle_submode_on_infrastructure_link CASCADE; COMMIT;
BEGIN; TRUNCATE TABLE infrastructure_network.infrastructure_link CASCADE; COMMIT;
BEGIN; TRUNCATE TABLE internal_service_pattern.scheduled_stop_point CASCADE; COMMIT;
BEGIN; TRUNCATE TABLE route.line CASCADE; COMMIT;
BEGIN; TRUNCATE TABLE route.route CASCADE; COMMIT;
BEGIN; TRUNCATE TABLE journey_pattern.journey_pattern CASCADE; COMMIT;
BEGIN; TRUNCATE TABLE journey_pattern.scheduled_stop_point_in_journey_pattern CASCADE; COMMIT;

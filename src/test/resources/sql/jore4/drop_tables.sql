BEGIN;
    -- Changes the mode of all deferrable constraints.
    -- Done to prevent pending trigger events aborting transaction.
    -- Affects only current transaction.
    SET CONSTRAINTS ALL IMMEDIATE;

    -- Table truncation is done in a single statement to avoid cascading.
    -- Tables are ordered based on foreign key references (referred last).
    TRUNCATE TABLE
        journey_pattern.scheduled_stop_point_in_journey_pattern,
        journey_pattern.journey_pattern,

        route.infrastructure_link_along_route,
        route.route,
        route.line,

        service_pattern.vehicle_mode_on_scheduled_stop_point,
        service_pattern.scheduled_stop_point,
        service_pattern.scheduled_stop_point_invariant,

        timing_pattern.timing_place,

        infrastructure_network.vehicle_submode_on_infrastructure_link,
        infrastructure_network.infrastructure_link;
COMMIT;

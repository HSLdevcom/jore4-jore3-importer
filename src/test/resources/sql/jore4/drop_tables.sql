BEGIN;
    -- Changes the mode of all deferrable constraints.
    -- Done to prevent pending trigger events aborting transaction.
    -- Affects only current transaction.
    SET CONSTRAINTS ALL IMMEDIATE;

    -- Table truncation is done in a single statement to avoid cascading.
    -- Tables are ordered based on foreign key references (referred last).
    TRUNCATE TABLE
        network.scheduled_stop_point_in_journey_pattern,
        network.journey_pattern,

        network.infrastructure_link_along_route,
        network.route,
        network.line,

        network.vehicle_mode_on_scheduled_stop_point,
        network.scheduled_stop_point,
        network.scheduled_stop_point_invariant,

        network.timing_place,

        infrastructure_network.vehicle_submode_on_infrastructure_link,
        infrastructure_network.infrastructure_link;
COMMIT;

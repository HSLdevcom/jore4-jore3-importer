-- Add transport mode (network type) to stop places

-- Drop the view first since it depends on the tables
DROP VIEW IF EXISTS stops.stop_places_with_history;

-- Drop the versioning trigger
DROP TRIGGER IF EXISTS versioning_trigger ON stops.stop_places;

-- Add column to main table
ALTER TABLE stops.stop_places
    ADD COLUMN stops_stop_place_transport_mode TEXT NOT NULL DEFAULT 'unknown'
        REFERENCES infrastructure_network.infrastructure_network_types (infrastructure_network_type);

-- Remove the default after backfill
ALTER TABLE stops.stop_places
    ALTER COLUMN stops_stop_place_transport_mode DROP DEFAULT;

-- Add column to staging table
ALTER TABLE stops.stop_places_staging
    ADD COLUMN stops_stop_place_transport_mode TEXT NOT NULL DEFAULT 'unknown'
        REFERENCES infrastructure_network.infrastructure_network_types (infrastructure_network_type);

ALTER TABLE stops.stop_places_staging
    ALTER COLUMN stops_stop_place_transport_mode DROP DEFAULT;

-- Add column to history table
ALTER TABLE stops.stop_places_history
    ADD COLUMN stops_stop_place_transport_mode TEXT NOT NULL DEFAULT 'unknown';

ALTER TABLE stops.stop_places_history
    ALTER COLUMN stops_stop_place_transport_mode DROP DEFAULT;

-- Recreate the versioning trigger
CREATE TRIGGER versioning_trigger
    BEFORE INSERT OR UPDATE OR DELETE
    ON stops.stop_places
    FOR EACH ROW
EXECUTE PROCEDURE temporal.versioning('stops_stop_place_sys_period',
                                      'stops.stop_places_history', true, true);

-- Recreate the view
CREATE OR REPLACE VIEW stops.stop_places_with_history AS
SELECT *
FROM stops.stop_places
UNION ALL
SELECT *
FROM stops.stop_places_history;


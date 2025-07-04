CREATE SCHEMA stops;

-- STOP PLACES

CREATE TABLE stops.stop_places
(
    stops_stop_place_id         uuid      DEFAULT gen_random_uuid() PRIMARY KEY,
    stops_stop_place_ext_id     TEXT                                                 NOT NULL,
    stops_stop_place_name       JSONB                                                NOT NULL,
    stops_stop_place_long_name  JSONB                                                NOT NULL,
    stops_stop_place_location   JSONB                                                NOT NULL,
    stops_stop_place_sys_period tstzrange DEFAULT tstzrange(current_timestamp, null) NOT NULL
);

CREATE UNIQUE INDEX stops_stop_place_ext_id_idx
    ON stops.stop_places (stops_stop_place_ext_id);

-- Staging table used for stop place import

CREATE TABLE stops.stop_places_staging
(
    stops_stop_place_ext_id     TEXT                                                 NOT NULL,
    stops_stop_place_name       JSONB                                                NOT NULL,
    stops_stop_place_long_name  JSONB                                                NOT NULL,
    stops_stop_place_location   JSONB                                                NOT NULL
);

-- VERSIONED STOP PLACES

CREATE TABLE stops.stop_places_history
(
    LIKE stops.stop_places,
    -- There should be no overlap between system times for the same entity:
    EXCLUDE USING GIST (stops_stop_place_id WITH =, stops_stop_place_sys_period WITH &&)
);

CREATE TRIGGER versioning_trigger
    BEFORE INSERT OR UPDATE OR DELETE
    ON stops.stop_places
    FOR EACH ROW
EXECUTE PROCEDURE temporal.versioning('stops_stop_place_sys_period',
                                      'stops.stop_places_history', true, true);

CREATE OR REPLACE VIEW stops.stop_places_with_history AS
SELECT *
FROM stops.stop_places
UNION ALL
SELECT *
FROM stops.stop_places_history;

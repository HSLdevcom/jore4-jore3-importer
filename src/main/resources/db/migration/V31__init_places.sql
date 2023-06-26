-- PLACES

CREATE TABLE network.network_places
(
    network_place_id         uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    network_place_ext_id     text                                                 NOT NULL,
    network_place_name       text                                                 NOT NULL,
    network_place_sys_period tstzrange DEFAULT tstzrange(current_timestamp, NULL) NOT NULL
);

CREATE UNIQUE INDEX network_places_ext_id_idx
    ON network.network_places (network_place_ext_id);

-- Staging table used for place import

CREATE TABLE network.network_places_staging
(
    network_place_ext_id text NOT NULL PRIMARY KEY,
    network_place_name   text NOT NULL
);

-- VERSIONED PLACES

CREATE TABLE network.network_places_history
(
    LIKE network.network_places,
    -- There should be no overlap between system times for the same entity:
    EXCLUDE USING GIST (network_place_id WITH =, network_place_sys_period WITH &&)
);

CREATE TRIGGER versioning_trigger
    BEFORE INSERT OR UPDATE OR DELETE
    ON network.network_places
    FOR EACH ROW EXECUTE PROCEDURE temporal.versioning('network_place_sys_period',
                                                       'network.network_places_history',
                                                       true,
                                                       true);

CREATE OR REPLACE VIEW network.network_places_with_history AS
SELECT *
FROM network.network_places
UNION ALL
SELECT *
FROM network.network_places_history;

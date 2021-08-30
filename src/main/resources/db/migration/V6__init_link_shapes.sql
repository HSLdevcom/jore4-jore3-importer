-- TODO: We should probably not store Jore 3 stuff directly in the "infrastructure" schema
--       Instead, we should have a "jore3" schema, which hosts all the raw data from Jore 3

-- LINK SHAPES

CREATE TABLE infrastructure_network.infrastructure_link_shapes
(
    infrastructure_link_shape_id         uuid      DEFAULT gen_random_uuid() PRIMARY KEY,
    infrastructure_link_ext_id           TEXT                                                 NOT NULL,
    infrastructure_link_id          uuid                                                 NOT NULL REFERENCES infrastructure_network.infrastructure_links (infrastructure_link_id) ON DELETE CASCADE,
    -- Link shape from start->...->end (including intermediate points).
    infrastructure_link_shape            geography(LinestringZ, 4326)                         NOT NULL,
    infrastructure_link_shape_sys_period tstzrange DEFAULT tstzrange(current_timestamp, null) NOT NULL
);

CREATE UNIQUE INDEX idx_infrastructure_link_shapes_ext_id
    ON infrastructure_network.infrastructure_link_shapes (infrastructure_link_ext_id);

-- Staging table used for link point import

CREATE TABLE infrastructure_network.infrastructure_link_shapes_staging
(
    infrastructure_link_ext_id TEXT PRIMARY KEY,
    infrastructure_link_shape  geography(LinestringZ, 4326) NOT NULL
);

-- VERSIONED LINK SHAPES

CREATE TABLE infrastructure_network.infrastructure_link_shapes_history
(
    LIKE infrastructure_network.infrastructure_link_shapes,
    -- There should be no overlap between system times for the same entity:
    EXCLUDE USING GIST (infrastructure_link_shape_id WITH =, infrastructure_link_shape_sys_period WITH &&)
);

CREATE TRIGGER versioning_trigger
    BEFORE INSERT OR UPDATE OR DELETE
    ON infrastructure_network.infrastructure_link_shapes
    FOR EACH ROW
EXECUTE PROCEDURE temporal.versioning('infrastructure_link_shape_sys_period',
                                      'infrastructure_network.infrastructure_link_shapes_history', true, true);

CREATE OR REPLACE VIEW infrastructure_network.infrastructure_link_shapes_with_history AS
SELECT *
FROM infrastructure_network.infrastructure_link_shapes
UNION ALL
SELECT *
FROM infrastructure_network.infrastructure_link_shapes_history;

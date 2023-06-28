-- Drop view because a column will be replaced.
DROP VIEW network.scheduled_stop_points_with_history;

-- network.scheduled_stop_points

ALTER TABLE ONLY network.scheduled_stop_points
    ADD COLUMN network_place_id uuid NULL
    REFERENCES network.network_places(network_place_id);

UPDATE network.scheduled_stop_points
SET network_place_id = np.network_place_id
FROM network.network_places np
WHERE hastus_place_id = np.network_place_ext_id;

ALTER TABLE ONLY network.scheduled_stop_points
    DROP COLUMN hastus_place_id;

CREATE INDEX scheduled_stop_points_network_place_id_idx
    ON network.scheduled_stop_points USING btree (network_place_id);

-- network.scheduled_stop_points_history

ALTER TABLE ONLY network.scheduled_stop_points_history
    ADD COLUMN network_place_id uuid NULL;

-- Use with_history table in populating values.
UPDATE network.scheduled_stop_points_history
SET network_place_id = np.network_place_id
FROM network.network_places_with_history np
WHERE hastus_place_id = np.network_place_ext_id;

ALTER TABLE ONLY network.scheduled_stop_points_history
    DROP COLUMN hastus_place_id;

-- network.scheduled_stop_points_staging

ALTER TABLE ONLY network.scheduled_stop_points_staging
    RENAME COLUMN hastus_place_id TO network_place_ext_id;

-- To ensure that adding foreign key works. Staging table is filled only temporarily during import run.
-- Currently, commented out because it is expected that migrations are not run when there is an on-going
-- import run.
-- DELETE FROM network.scheduled_stop_points_staging;

ALTER TABLE ONLY network.scheduled_stop_points_staging
    ADD CONSTRAINT scheduled_stop_points_staging_network_place_ext_id_fkey
    FOREIGN KEY (network_place_ext_id) REFERENCES network.network_places(network_place_ext_id);

CREATE INDEX scheduled_stop_points_stating_network_place_ext_id_idx
    ON network.scheduled_stop_points_staging USING btree (network_place_ext_id);

-- Recreate the with-history view.
CREATE OR REPLACE VIEW network.scheduled_stop_points_with_history AS
SELECT *
FROM network.scheduled_stop_points
UNION ALL
SELECT *
FROM network.scheduled_stop_points_history;

-- LINES

CREATE TABLE network.network_lines
(
    network_line_id             uuid      DEFAULT gen_random_uuid() PRIMARY KEY,
    network_line_ext_id         TEXT                                                 NOT NULL,
    network_line_number         TEXT                                                 NOT NULL,
    infrastructure_network_type text                                                 NOT NULL REFERENCES infrastructure_network.infrastructure_network_types (infrastructure_network_type),
    network_line_sys_period     tstzrange DEFAULT tstzrange(current_timestamp, null) NOT NULL
);

CREATE UNIQUE INDEX network_lines_ext_id_idx
    ON network.network_lines (network_line_ext_id);

-- Staging table used for line import

CREATE TABLE network.network_lines_staging
(
    network_line_ext_id         TEXT NOT NULL PRIMARY KEY,
    network_line_number         TEXT NOT NULL,
    infrastructure_network_type text NOT NULL REFERENCES infrastructure_network.infrastructure_network_types (infrastructure_network_type)
);

-- VERSIONED LINES

CREATE TABLE network.network_lines_history
(
    LIKE network.network_lines,
    -- There should be no overlap between system times for the same entity:
    EXCLUDE USING GIST (network_line_id WITH =, network_line_sys_period WITH &&)
);

CREATE TRIGGER versioning_trigger
    BEFORE INSERT OR UPDATE OR DELETE
    ON network.network_lines
    FOR EACH ROW
EXECUTE PROCEDURE temporal.versioning('network_line_sys_period',
                                      'network.network_lines_history', true, true);

CREATE OR REPLACE VIEW network.network_lines_with_history AS
SELECT *
FROM network.network_lines
UNION ALL
SELECT *
FROM network.network_lines_history;

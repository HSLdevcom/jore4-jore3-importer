-- LINE HEADERS

CREATE TABLE network.network_line_headers
(
    network_line_header_id               uuid      DEFAULT gen_random_uuid() PRIMARY KEY,
    network_line_id                      uuid                                                 NOT NULL REFERENCES network.network_lines (network_line_id) ON DELETE CASCADE,
    network_line_header_ext_id           TEXT                                                 NOT NULL,
    network_line_header_name             JSONB                                                NOT NULL,
    network_line_header_name_short       JSONB                                                NOT NULL,
    network_line_header_origin_1         JSONB                                                NOT NULL,
    network_line_header_origin_2         JSONB                                                NOT NULL,
    network_line_header_valid_date_range daterange                                            NOT NULL,
    network_line_header_sys_period       tstzrange DEFAULT tstzrange(current_timestamp, null) NOT NULL,

    -- There should be no overlap between valid dates for the same parent line:
    EXCLUDE USING GIST (network_line_id WITH =, network_line_header_valid_date_range WITH &&)
);

CREATE UNIQUE INDEX network_line_headers_ext_id_idx
    ON network.network_line_headers (network_line_header_ext_id);

-- Staging table used for line header import

CREATE TABLE network.network_line_headers_staging
(
    network_line_header_ext_id           TEXT      NOT NULL PRIMARY KEY,
    network_line_ext_id                  TEXT      NOT NULL,
    network_line_header_name             JSONB     NOT NULL,
    network_line_header_name_short       JSONB     NOT NULL,
    network_line_header_origin_1         JSONB     NOT NULL,
    network_line_header_origin_2         JSONB     NOT NULL,
    network_line_header_valid_date_range daterange NOT NULL,

    -- There should be no overlap between valid dates for the same lane:
    EXCLUDE USING GIST (network_line_ext_id WITH =, network_line_header_valid_date_range WITH &&)
);

-- VERSIONED LINE HEADERS

CREATE TABLE network.network_line_headers_history
(
    LIKE network.network_line_headers,
    -- There should be no overlap between system times for the same entity:
    EXCLUDE USING GIST (network_line_header_id WITH =, network_line_header_sys_period WITH &&)
);

CREATE TRIGGER versioning_trigger
    BEFORE INSERT OR UPDATE OR DELETE
    ON network.network_line_headers
    FOR EACH ROW
EXECUTE PROCEDURE temporal.versioning('network_line_header_sys_period',
                                      'network.network_line_headers_history', true, true);

CREATE OR REPLACE VIEW network.network_line_headers_with_history AS
SELECT *
FROM network.network_line_headers
UNION ALL
SELECT *
FROM network.network_line_headers_history;

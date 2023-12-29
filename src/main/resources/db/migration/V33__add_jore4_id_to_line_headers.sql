ALTER TABLE network.network_line_headers ADD COLUMN jore4_line_id uuid;
ALTER TABLE network.network_line_headers_history ADD COLUMN jore4_line_id uuid;

CREATE OR REPLACE VIEW network.network_line_headers_with_history AS
SELECT *
FROM network.network_line_headers
UNION ALL
SELECT *
FROM network.network_line_headers_history;

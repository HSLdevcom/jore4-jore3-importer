ALTER TABLE network.network_lines
ADD COLUMN network_line_export_id VARCHAR(4) NOT NULL DEFAULT '9999';

ALTER TABLE network.network_lines_history
ADD COLUMN network_line_export_id VARCHAR(4) NOT NULL DEFAULT '9999';

ALTER TABLE network.network_lines_staging
ADD COLUMN network_line_export_id VARCHAR(4) NOT NULL DEFAULT '9999';

-- Default values are just to migrate possible existing data.
ALTER TABLE network.network_lines ALTER COLUMN network_line_export_id DROP DEFAULT;
ALTER TABLE network.network_lines_history ALTER COLUMN network_line_export_id DROP DEFAULT;
ALTER TABLE network.network_lines_staging ALTER COLUMN network_line_export_id DROP DEFAULT;

CREATE OR REPLACE VIEW network.network_lines_with_history AS
SELECT *
FROM network.network_lines
UNION ALL
SELECT *
FROM network.network_lines_history;

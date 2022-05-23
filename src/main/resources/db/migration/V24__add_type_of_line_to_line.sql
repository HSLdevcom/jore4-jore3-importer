
ALTER TABLE network.network_lines
ADD COLUMN network_line_type_of_line TEXT NOT NULL;

ALTER TABLE network.network_lines_history
ADD COLUMN network_line_type_of_line TEXT NOT NULL;

ALTER TABLE network.network_lines_staging
ADD COLUMN network_line_type_of_line TEXT NOT NULL;

CREATE OR REPLACE VIEW network.network_lines_with_history AS
SELECT *
FROM network.network_lines
UNION ALL
SELECT *
FROM network.network_lines_history;

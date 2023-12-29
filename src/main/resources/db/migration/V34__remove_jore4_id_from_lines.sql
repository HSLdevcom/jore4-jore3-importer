DROP VIEW network.network_lines_with_history;

ALTER TABLE network.network_lines DROP COLUMN network_line_jore4_id;
ALTER TABLE network.network_lines_history DROP COLUMN network_line_jore4_id;

CREATE VIEW network.network_lines_with_history AS
SELECT *
FROM network.network_lines
UNION ALL
SELECT *
FROM network.network_lines_history;

ALTER TABLE network.network_routes ADD COLUMN network_route_legacy_hsl_municipality_code text;
ALTER TABLE network.network_routes_staging ADD COLUMN network_route_legacy_hsl_municipality_code text;
ALTER TABLE network.network_routes_history ADD COLUMN network_route_legacy_hsl_municipality_code text;

CREATE OR REPLACE VIEW network.network_routes_with_history AS
SELECT *
FROM network.network_routes
UNION ALL
SELECT *
FROM network.network_routes_history;

ALTER TABLE network.network_lines ADD COLUMN network_line_legacy_hsl_municipality_code text;
ALTER TABLE network.network_lines_staging ADD COLUMN network_line_legacy_hsl_municipality_code text;
ALTER TABLE network.network_lines_history ADD COLUMN network_line_legacy_hsl_municipality_code text;

CREATE OR REPLACE VIEW network.network_lines_with_history AS
SELECT *
FROM network.network_lines
UNION ALL
SELECT *
FROM network.network_lines_history;

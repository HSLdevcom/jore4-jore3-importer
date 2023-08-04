-- For each line, possibly extend the date range of last header to cover the
-- route directions date range. This is done because in Jore4 database it
-- results in a constraint violation if route's date range extends beyond its
-- parent line's date range.
WITH
ids_of_last_line_headers AS (
    SELECT DISTINCT
        first_value(lh.network_line_header_id) OVER (
        PARTITION BY l.network_line_ext_id
        ORDER BY lh.network_line_header_valid_date_range DESC
    ) AS last_header_id
    FROM network.network_lines l
    JOIN network.network_line_headers lh USING (network_line_id)
),
overridden_line_headers AS (
    SELECT
        lh.network_line_header_id AS overridden_network_line_header_id,
        daterange(
            lower(lh.network_line_header_valid_date_range),
            max(upper(rd.network_route_direction_valid_date_range))
        ) AS overridden_network_line_header_valid_date_range
    FROM ids_of_last_line_headers last
    JOIN network.network_line_headers lh ON lh.network_line_header_id = last.last_header_id
    JOIN network.network_lines l USING (network_line_id)
    JOIN network.network_routes r USING (network_line_id)
    JOIN network.network_route_directions rd USING (network_route_id)
    WHERE upper(rd.network_route_direction_valid_date_range) > upper(lh.network_line_header_valid_date_range)
    GROUP BY lh.network_line_header_id
),
range_extended_lines AS (
    SELECT
        l.network_line_ext_id AS external_id,
        l.network_line_number AS line_number,
        l.infrastructure_network_type AS network_type,
        l.network_line_type_of_line AS type_of_line,
        l.network_line_legacy_hsl_municipality_code AS legacy_hsl_municipality_code,
        lh.network_line_header_name AS name,
        lh.network_line_header_name_short AS short_name,
        CASE
            WHEN (olh.overridden_network_line_header_id IS NOT NULL)
                THEN olh.overridden_network_line_header_valid_date_range
            ELSE lh.network_line_header_valid_date_range
        END AS valid_date_range
    FROM network.network_lines l
    JOIN network.network_line_headers lh USING (network_line_id)
    LEFT JOIN overridden_line_headers olh ON olh.overridden_network_line_header_id = lh.network_line_header_id
)
SELECT *
FROM range_extended_lines
WHERE valid_date_range && '[2021-01-01, 2050-01-01)'::daterange
ORDER BY external_id ASC, valid_date_range ASC;

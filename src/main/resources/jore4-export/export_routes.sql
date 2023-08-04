-- Contract route's begin date to correspond to the last line header validity
-- range when the route (direction) spans multiple line headers temporally.
WITH
filtered_route_directions AS (
    SELECT *
    FROM network.network_route_directions
    WHERE network_route_direction_valid_date_range && '[2021-01-01, 2050-01-01)'::daterange
),
ids_of_route_directions_overlapping_multiple_line_headers AS (
    SELECT
        rd.network_route_direction_id,
        count(lh.network_line_header_id) AS overlapping_line_header_count
    FROM filtered_route_directions rd
    JOIN network.network_routes r USING (network_route_id)
    JOIN network.network_lines l USING (network_line_id)
    JOIN network.network_line_headers lh USING (network_line_id)
    WHERE rd.network_route_direction_valid_date_range && lh.network_line_header_valid_date_range
    GROUP BY rd.network_route_direction_id
    HAVING count(lh.network_line_header_id) >= 2
),
route_directions_with_begin_date_contracted_by_last_line_header AS (
    SELECT
        rd.network_route_direction_id,
        max(lower(lh.network_line_header_valid_date_range)) AS last_line_header_begin_date
    FROM ids_of_route_directions_overlapping_multiple_line_headers rdi
    JOIN network.network_route_directions rd USING (network_route_direction_id)
    JOIN network.network_routes r USING (network_route_id)
    JOIN network.network_lines l USING (network_line_id)
    JOIN network.network_line_headers lh USING (network_line_id)
    WHERE rd.network_route_direction_valid_date_range && lh.network_line_header_valid_date_range
    GROUP BY rd.network_route_direction_id
),
range_contracted_route_directions AS (
    SELECT
        r.network_route_number AS route_number,
        r.network_route_hidden_variant AS hidden_variant,
        rd.network_route_direction_id AS direction_id,
        rd.network_route_direction_type AS direction_type,
        rd.network_route_direction_name AS name,
        l.network_line_jore4_id AS line_jore4_id,
        CASE
            WHEN override_rd.last_line_header_begin_date IS NOT NULL
                THEN daterange(
                    override_rd.last_line_header_begin_date,
                    upper(rd.network_route_direction_valid_date_range)
                )
            ELSE rd.network_route_direction_valid_date_range
        END AS valid_date_range,
        r.network_route_legacy_hsl_municipality_code AS legacy_hsl_municipality_code
    FROM network.network_routes r
    JOIN filtered_route_directions rd USING (network_route_id)
    JOIN network.network_lines l USING (network_line_id)
    LEFT JOIN route_directions_with_begin_date_contracted_by_last_line_header override_rd USING (network_route_direction_id)
)
SELECT *
FROM range_contracted_route_directions
ORDER BY valid_date_range DESC;

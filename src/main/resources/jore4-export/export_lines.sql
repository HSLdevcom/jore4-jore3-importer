-- Since few years ago the last three digits of the line number were not
-- unique, and since in Jore4 the line number consists of only those last three
-- digits (stripping the preceding digit for the legacy municipality code), the
-- lines of the 'LEGACY_NOT_USED' legacy municipality code are filtered out to
-- prevent unique constraint violations in the Jore4 database during export.

-- Currently, only bus and ferry lines are exported to Jore4.

-- In the Jore3 data model, the line itself (the `network_lines` table in the
-- internal database of jore3-importer) has no validity period. Instead, the
-- line-specific headers (`network_line_headers` table) have validity periods
-- that do not overlap. Originally, it was thought that a separate line
-- instance would be created for each line header into the Jore4 database.
-- However, it appears that Jore3 route directions (`network_route_directions`
-- table) may overlap with multiple line headers. This causes a transformation
-- problem, because in Jore4 the validity period of a route MUST NOT extend
-- further into the past or future than the line to which the route belongs.

-- Basically, there are two options to solve the problem:
-- (1) For each line in Jore3 (and all related line headers), create only single
--     line in Jore4.
-- (2) Group and join line headers together by validity periods in such a way
--     that no route overlaps multiple line instances transformed to Jore4.

-- ATM we have chosen the option (2) to import as much (version) data as
-- possible into Jore4. In the SQL, we group the line headers into temporally
-- continuous clusters. The last line header of each cluster is given a Jore4 ID
-- that can be referenced by routes.

WITH
line_header_route_directions AS (
    -- Assign line-specific sequence numbers to the line headers and collect IDs of route
    -- directions that overlap in time.

    SELECT
        l.network_line_id,
        l.network_line_ext_id,
        lh.network_line_header_id,

        -- Set a time-ordered sequence number for each header on the same line.
        row_number() OVER (
            PARTITION BY lh.network_line_id
            ORDER BY lh.network_line_header_valid_date_range ASC
        ) AS line_header_order,

        -- Collecting all route directions that overlap in time with the line header.
        array_agg(rd.network_route_direction_ext_id ORDER BY network_route_direction_ext_id) AS route_direction_ext_ids

    FROM network.network_line_headers lh
    JOIN network.network_lines l USING (network_line_id)
    LEFT JOIN network.network_routes r USING (network_line_id)  -- left-join because the lines without routes are also taken
    LEFT JOIN network.network_route_directions rd USING (network_route_id)
    WHERE
        rd.network_route_direction_valid_date_range IS NULL  -- null check due to left-join
        OR rd.network_route_direction_valid_date_range && lh.network_line_header_valid_date_range -- check overlap
    GROUP BY l.network_line_id, l.network_line_ext_id, lh.network_line_header_id
),
line_header_cluster_start_markers AS (
    -- Set the cluster start points, which will be used in the subsequent SQL query to group
    -- the line headers into clusters. By definition, two consecutive clusters do not have
    -- overlapping sets of route direction IDs.

    SELECT
        network_line_id,
        network_line_ext_id,
        network_line_header_id,
        line_header_order,
        CASE
            WHEN prev_route_direction_ext_ids IS NULL THEN 1  -- the first row in a partition always starts a new cluster
            WHEN route_direction_ext_ids && prev_route_direction_ext_ids THEN 0  -- two sets of route (direction) IDs overlap
            ELSE 1  -- mark the start of cluster, because the set of route (direction) IDs does not overlap with the IDs of the previous line header
        END AS cluster_start
    FROM line_header_route_directions lhrd
    JOIN (
        SELECT
            network_line_id,
            network_line_header_id,
            lag(route_direction_ext_ids) OVER (
                PARTITION BY network_line_id
                ORDER BY line_header_order ASC
            ) AS prev_route_direction_ext_ids
        FROM line_header_route_directions
    ) sub USING (network_line_id, network_line_header_id)
),
clustered_line_header_ids AS (
    -- Assign cluster IDs and cluster-specific sequence numbers for line headers.

    SELECT
        network_line_id,
        network_line_ext_id,
        network_line_header_id,
        cluster_index AS line_header_cluster,

        -- a time-ordered sequence number for each line header in a cluster
        row_number() OVER (
            PARTITION BY network_line_id, cluster_index
            ORDER BY line_header_order ASC
        ) AS line_header_cluster_order

    FROM (
        SELECT
            *,

            -- This marks all line headers with the cluster ID to which the headers belong.
            -- The cluster ID is a line-specific sequence number in ascending chronological order.

            -- This is the cumulative sum of an expanding set towards the end. That's why ordering
            -- is important here.
            sum(cluster_start) OVER (
                PARTITION BY network_line_id
                ORDER BY line_header_order ASC
            ) AS cluster_index
        FROM line_header_cluster_start_markers
    ) sub
),
line_header_clusters AS (
    -- Produce line-specific line header clusters that are internally time-ordered.

    -- The start date of the first header of the cluster is the start date of the cluster.
    -- Correspondingly, the end date of the last header of the cluster is the end date of the
    -- cluster.

    -- Additionally, collect the ID of the last line header (in a cluster), because the ID of
    -- the exported Jore4 line is attached to it.

    SELECT
        sub.network_line_id,
        sub.network_line_ext_id,
        sub.line_header_cluster,
        lower(first_header_in_cluster.network_line_header_valid_date_range) AS line_header_cluster_start_date,
        upper(last_header_in_cluster.network_line_header_valid_date_range) AS line_header_cluster_end_date_excl,
        last_header_in_cluster.network_line_header_id AS last_network_line_header_id
    FROM (
        SELECT
            network_line_id,
            network_line_ext_id,
            line_header_cluster,
            min(line_header_cluster_order) AS line_header_cluster_order_min,
            max(line_header_cluster_order) AS line_header_cluster_order_max
        FROM clustered_line_header_ids
        GROUP BY network_line_id, network_line_ext_id, line_header_cluster
    ) sub
    JOIN clustered_line_header_ids cluster_header_ids_min ON
        cluster_header_ids_min.network_line_id = sub.network_line_id
        AND cluster_header_ids_min.line_header_cluster = sub.line_header_cluster
        AND cluster_header_ids_min.line_header_cluster_order = sub.line_header_cluster_order_min
    JOIN clustered_line_header_ids cluster_header_ids_max ON
        cluster_header_ids_max.network_line_id = sub.network_line_id
        AND cluster_header_ids_max.line_header_cluster = sub.line_header_cluster
        AND cluster_header_ids_max.line_header_cluster_order = sub.line_header_cluster_order_max
    JOIN network.network_line_headers first_header_in_cluster ON
        first_header_in_cluster.network_line_header_id = cluster_header_ids_min.network_line_header_id
    JOIN network.network_line_headers last_header_in_cluster ON
        last_header_in_cluster.network_line_header_id = cluster_header_ids_max.network_line_header_id
),
max_route_direction_end_dates_for_line_header_clusters AS (
    -- It has been found that in some cases the validity of a line header does not extend as far
    -- into the future as an associated route direction, causing database constraint violations
    -- during the route export stage if no date corrections are made.

    -- Therefore, for each line, find the latest validity date from the route directions associated
    -- with the line's last line header cluster.

    SELECT
        last_cluster.network_line_id,
        last_cluster.network_line_ext_id,
        last_cluster.line_header_cluster,
        max(upper(network_route_direction_valid_date_range)) AS max_route_direction_end_date_excl
    FROM (
        -- Find the last line header cluster for each line.
        SELECT
            network_line_id,
            max(line_header_cluster) AS line_header_cluster_max
        FROM line_header_clusters
        GROUP BY network_line_id
    ) sub
    JOIN line_header_clusters last_cluster ON
        last_cluster.network_line_id = sub.network_line_id
        AND last_cluster.line_header_cluster = sub.line_header_cluster_max
    JOIN line_header_route_directions lhrd ON lhrd.network_line_header_id = last_cluster.last_network_line_header_id
    JOIN network.network_route_directions rd ON rd.network_route_direction_ext_id = ANY(lhrd.route_direction_ext_ids)
    GROUP BY last_cluster.network_line_id, last_cluster.network_line_ext_id, last_cluster.line_header_cluster
),
line_header_clusters_with_end_date_mod AS (
    -- Extend the validity period of the line header cluster so far that all associated route
    -- directions can fit within it. This only needs to be done for the last line header cluster in
    -- a line. By doing this, we prevent database constraint violations during the route export
    -- stage.

    SELECT
        cluster.*,

        -- The greatest() function is not fooled by NULL values.
        greatest(line_header_cluster_end_date_excl, max_route_direction_end_date_excl) AS modified_line_header_cluster_end_date_excl

    FROM line_header_clusters cluster
    -- Left-join because max-end-date-for-route-direction is resolved only for the last header
    -- cluster for each line.
    LEFT JOIN max_route_direction_end_dates_for_line_header_clusters USING (network_line_id, line_header_cluster)
)

-- uncomment for debugging
-- SELECT * FROM line_header_clusters_with_end_date_mod ORDER BY network_line_ext_id ASC, line_header_cluster ASC LIMIT 50;

-- the final export query
SELECT
    l.network_line_number AS line_number,
    l.infrastructure_network_type AS network_type,
    l.network_line_type_of_line AS type_of_line,
    l.network_line_legacy_hsl_municipality_code AS legacy_hsl_municipality_code,
    lh.network_line_header_ext_id AS line_header_external_id,
    lh.network_line_header_name AS line_header_name,
    lh.network_line_header_name_short AS line_header_short_name,
    daterange(cluster.line_header_cluster_start_date, cluster.modified_line_header_cluster_end_date_excl) AS line_header_cluster_date_range
FROM line_header_clusters_with_end_date_mod cluster
JOIN network.network_lines l USING (network_line_id)
JOIN network.network_line_headers lh ON lh.network_line_header_id = cluster.last_network_line_header_id
WHERE
    l.infrastructure_network_type IN ('road', 'waterway')
    AND l.network_line_legacy_hsl_municipality_code NOT IN ('LEGACY_NOT_USED', 'TESTING_NOT_USED')
ORDER BY line_number ASC, line_header_cluster_date_range DESC;

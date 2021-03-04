CREATE SCHEMA infrastructure_network;

-- !! ADDED MANUALLY !! --
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE infrastructure_network.infrastructure_links
(
    infrastructure_link_id   uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    infrastructure_link_geog geography(LinestringZ, 4326) NOT NULL
);

-- NETWORK TYPES

CREATE TABLE infrastructure_network.infrastructure_network_types
(
    infrastructure_network_type text NOT NULL PRIMARY KEY
);

INSERT INTO infrastructure_network.infrastructure_network_types
    (infrastructure_network_type)
VALUES ('road'),
       ('railway'),
       ('tram_track'),
       ('metro_track'),
       ('waterway');

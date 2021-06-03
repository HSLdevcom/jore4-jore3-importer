-- DIRECTION TYPES

CREATE TABLE network.network_direction_types
(
    network_direction_type text NOT NULL PRIMARY KEY
);

INSERT INTO network.network_direction_types
    (network_direction_type)
VALUES ('inbound'),
       ('outbound'),
       ('clockwise'),
       ('anticlockwise'),
       ('unknown');

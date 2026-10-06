BEGIN;

-- A one-way (forward) tram link digitized from west to east along latitude 60.17.
-- Points north of the link are on its left side (backward), points south of it on its right side (forward).
INSERT INTO infrastructure_network.infrastructure_link (
    infrastructure_link_id,
    direction,
    shape,
    estimated_length_in_metres,
    external_link_id,
    external_link_source
)
VALUES (
    'a1b2c3d4-0000-4000-8000-000000000001',
    'forward',
    ST_MakeLine(ST_SetSRID(ST_MakePoint(24.94, 60.17, 0), 4326), ST_SetSRID(ST_MakePoint(24.95, 60.17, 0), 4326)),
    555,
    'tram-1',
    'hsl_tram'
);

INSERT INTO infrastructure_network.vehicle_submode_on_infrastructure_link (
    infrastructure_link_id,
    vehicle_submode
)
VALUES (
    'a1b2c3d4-0000-4000-8000-000000000001',
    'generic_tram'
);

-- A bus link closer to the test points than the tram link. Used to verify that the vehicle submode filter works.
INSERT INTO infrastructure_network.infrastructure_link (
    infrastructure_link_id,
    direction,
    shape,
    estimated_length_in_metres,
    external_link_id,
    external_link_source
)
VALUES (
    'a1b2c3d4-0000-4000-8000-000000000002',
    'bidirectional',
    ST_MakeLine(ST_SetSRID(ST_MakePoint(24.94, 60.17005, 0), 4326), ST_SetSRID(ST_MakePoint(24.95, 60.17005, 0), 4326)),
    555,
    'bus-near-tram-1',
    'digiroad_r_mml'
);

INSERT INTO infrastructure_network.vehicle_submode_on_infrastructure_link (
    infrastructure_link_id,
    vehicle_submode
)
VALUES (
    'a1b2c3d4-0000-4000-8000-000000000002',
    'generic_bus'
);

COMMIT;


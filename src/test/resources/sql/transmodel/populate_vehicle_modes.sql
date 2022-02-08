INSERT INTO reusable_components.vehicle_mode
(vehicle_mode)
VALUES
    ('bus'),
    ('tram'),
    ('train'),
    ('metro'),
    ('ferry')
ON CONFLICT (vehicle_mode) DO NOTHING;

INSERT INTO reusable_components.vehicle_submode (
        vehicle_submode,
        belonging_to_vehicle_mode
)
VALUES (
        'generic_bus',
        'bus'
);
INSERT INTO reusable_components.vehicle_mode
(vehicle_mode)
VALUES
    ('bus'),
    ('tram'),
    ('train'),
    ('metro'),
    ('ferry')
ON CONFLICT (vehicle_mode) DO NOTHING;
CREATE SCHEMA infrastructure_network;

CREATE SCHEMA network;

-- Create extension conditionally. This is because in Azure PostgreSQL Flexible
-- Server v15, ordinary users (without the azure_pg_admin role) cannot create
-- extensions. These extensions need to be created before deploying this service
-- into the cloud.

-- Conditionally create the btree_gist extension.
DO $$
DECLARE
    ext_exists BOOLEAN;
BEGIN
    -- Check if btree_gist extension exists.
    SELECT EXISTS (SELECT 1 FROM pg_extension WHERE extname = 'btree_gist') INTO ext_exists;

    -- Create extension only if it doesn't exist.
    IF NOT ext_exists THEN
        CREATE EXTENSION IF NOT EXISTS btree_gist SCHEMA public;
    END IF;
END $$;

-- Conditionally create the pgcrypto extension.
DO $$
DECLARE
    ext_exists BOOLEAN;
BEGIN
    -- Check if pgcrypto extension already exists.
    SELECT EXISTS (SELECT 1 FROM pg_extension WHERE extname = 'pgcrypto') INTO ext_exists;

    -- Create extension only if it doesn't exist.
    IF NOT ext_exists THEN
        CREATE EXTENSION IF NOT EXISTS pgcrypto SCHEMA public;
    END IF;
END $$;

-- Conditionally create the postgis extension.
DO $$
DECLARE
    ext_exists BOOLEAN;
BEGIN
    -- Check if postgis extension already exists.
    SELECT EXISTS (SELECT 1 FROM pg_extension WHERE extname = 'postgis') INTO ext_exists;

    -- Create extension only if it doesn't exist.
    IF NOT ext_exists THEN
        CREATE EXTENSION IF NOT EXISTS postgis SCHEMA public;
    END IF;
END $$;

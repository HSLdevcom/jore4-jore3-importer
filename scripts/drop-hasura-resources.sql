-- Run this against each database used by a Hasura migration source.
-- It removes only schemas created by this repository's generic and HSL migrations.
-- CASCADE also removes contained tables, views, functions, triggers, types, sequences,
-- constraints, indexes, and schema-specific default privileges.

--DROP FUNCTION IF EXISTS public.drop_triggers(text[]);
--DROP FUNCTION IF EXISTS public.drop_constraints(text[]);
--DROP FUNCTION IF EXISTS public.drop_functions(text[]);

DROP SCHEMA IF EXISTS dssview CASCADE;
DROP SCHEMA IF EXISTS hsl_route CASCADE;

DROP SCHEMA IF EXISTS infrastructure_network CASCADE;
DROP SCHEMA IF EXISTS internal_service_calendar CASCADE;
DROP SCHEMA IF EXISTS internal_service_pattern CASCADE;
DROP SCHEMA IF EXISTS internal_utils CASCADE;
DROP SCHEMA IF EXISTS journey_pattern CASCADE;
DROP SCHEMA IF EXISTS network CASCADE;
DROP SCHEMA IF EXISTS passing_times CASCADE;
DROP SCHEMA IF EXISTS reusable_components CASCADE;
DROP SCHEMA IF EXISTS return_value CASCADE;
DROP SCHEMA IF EXISTS route CASCADE;
DROP SCHEMA IF EXISTS service_calendar CASCADE;
DROP SCHEMA IF EXISTS service_pattern CASCADE;
DROP SCHEMA IF EXISTS timetables CASCADE;
DROP SCHEMA IF EXISTS timing_pattern CASCADE;
DROP SCHEMA IF EXISTS vehicle_journey CASCADE;
DROP SCHEMA IF EXISTS vehicle_schedule CASCADE;
DROP SCHEMA IF EXISTS vehicle_service CASCADE;
DROP SCHEMA IF EXISTS vehicle_type CASCADE;

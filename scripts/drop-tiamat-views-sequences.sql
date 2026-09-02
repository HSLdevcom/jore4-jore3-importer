-- Idempotent script to drop all database objects created by tiamat Flyway
-- migrations.

-- ============================================================================
-- DROP VIEWS (in reverse dependency order to avoid dependency issues)
-- ============================================================================

DROP VIEW IF EXISTS quay_newest_version;
DROP VIEW IF EXISTS quay_alt_name_by_type;
DROP VIEW IF EXISTS quay_max_version;

DROP VIEW IF EXISTS stop_place_newest_version;
DROP VIEW IF EXISTS stop_place_max_version;

DROP VIEW IF EXISTS place_equipment;

DROP VIEW IF EXISTS group_of_stop_places_newest_version;


-- ============================================================================
-- DROP SEQUENCES (in any order since they're independent)
-- ============================================================================

-- Sequences from V1__Base_version.sql
DROP SEQUENCE IF EXISTS access_space_seq;
DROP SEQUENCE IF EXISTS accesses_rel_structure_seq;
DROP SEQUENCE IF EXISTS accessibility_assessment_seq;
DROP SEQUENCE IF EXISTS accessibility_limitation_seq;
DROP SEQUENCE IF EXISTS alternative_name_seq;
DROP SEQUENCE IF EXISTS boarding_position_seq;
DROP SEQUENCE IF EXISTS check_constraint_seq;
DROP SEQUENCE IF EXISTS equipment_place_seq;
DROP SEQUENCE IF EXISTS equipment_position_seq;
DROP SEQUENCE IF EXISTS explicit_equipments_rel_structure_seq;
DROP SEQUENCE IF EXISTS hibernate_sequence;
DROP SEQUENCE IF EXISTS installed_equipment_version_structure_seq;
DROP SEQUENCE IF EXISTS level_seq;
DROP SEQUENCE IF EXISTS navigation_paths_rel_structure_seq;
DROP SEQUENCE IF EXISTS parking_area_seq;
DROP SEQUENCE IF EXISTS parking_capacity_seq;
DROP SEQUENCE IF EXISTS parking_properties_seq;
DROP SEQUENCE IF EXISTS parking_seq;
DROP SEQUENCE IF EXISTS path_junction_seq;
DROP SEQUENCE IF EXISTS path_junctions_rel_structure_seq;
DROP SEQUENCE IF EXISTS path_link_end_seq;
DROP SEQUENCE IF EXISTS path_link_seq;
DROP SEQUENCE IF EXISTS persistable_polygon_seq;
DROP SEQUENCE IF EXISTS quay_seq;
DROP SEQUENCE IF EXISTS quays_rel_structure_seq;
DROP SEQUENCE IF EXISTS road_address_seq;
DROP SEQUENCE IF EXISTS seq_multilingual_string_entity;
DROP SEQUENCE IF EXISTS site_path_links_rel_structure_seq;
DROP SEQUENCE IF EXISTS stop_place_seq;
DROP SEQUENCE IF EXISTS tariff_zone_ref_seq;
DROP SEQUENCE IF EXISTS tariff_zone_seq;
DROP SEQUENCE IF EXISTS topographic_place_seq;
DROP SEQUENCE IF EXISTS valid_between_seq;
DROP SEQUENCE IF EXISTS vehicle_stopping_places_rel_structure_seq;

-- Sequences from V30__group_of_stop_places.sql
DROP SEQUENCE IF EXISTS group_of_stop_places_seq;

-- Sequences from V40__create_table_fare_zone.sql
DROP SEQUENCE IF EXISTS fare_zone_seq;

-- Sequences from V43__create_table_group_of_tariff_zones.sql
DROP SEQUENCE IF EXISTS group_of_tariff_zones_seq;

-- Sequences from V45__alter_table_group_of_stop_places.sql
DROP SEQUENCE IF EXISTS purpose_of_grouping_seq;

-- Sequences from V46__add_value_seq_sequence.sql
DROP SEQUENCE IF EXISTS value_seq;
DROP SEQUENCE IF EXISTS export_job_seq;

-- Sequences from V46.2__hsl_add_hsl_accessibility_properties.sql
DROP SEQUENCE IF EXISTS hsl_accessibility_properties_seq;

-- Sequences from V46.5__hsl_create_organisation_tables.sql
DROP SEQUENCE IF EXISTS contact_seq;
DROP SEQUENCE IF EXISTS organisation_seq;

-- Sequences from V46.6__add_info_spots.sql
DROP SEQUENCE IF EXISTS info_spot_seq;
DROP SEQUENCE IF EXISTS info_spot_poster_seq;

-- from https://github.com/nearform/temporal_tables/commit/6ba58c78d68d59b2d95cfc22223729309f0c4c72

-- Usage is e.g.
--
-- CREATE TRIGGER versioning_trigger
--     BEFORE INSERT OR UPDATE OR DELETE
--     ON schema_name.table_name
--     FOR EACH ROW
-- EXECUTE PROCEDURE temporal.versioning('entity_column_sys_period',
--                                       'schema_name.table_name_history', true, true);
-- where the two boolean parameters are:
-- 1: If true, try to mitigate against concurrent updates against the same rows
--    (https://github.com/arkhipov/temporal_tables#update-conflicts-and-time-adjustment)
-- 2: If true, ignore updates without actual changes
--    (https://github.com/nearform/temporal_tables#ignore-updates-without-actual-change)


-- The MIT License (MIT)
--
-- Copyright (c) 2016-2017 Nearform and contributors
--
-- Contributors listed at https://github.com/nearform/temporal_tables#the-team and in
-- the README file.
--
-- Permission is hereby granted, free of charge, to any person obtaining a copy
-- of this software and associated documentation files (the "Software"), to deal
-- in the Software without restriction, including without limitation the rights
-- to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
-- copies of the Software, and to permit persons to whom the Software is
-- furnished to do so, subject to the following conditions:
--
-- The above copyright notice and this permission notice shall be included in all
-- copies or substantial portions of the Software.
--
-- THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
-- IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
-- FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
-- AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
-- LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
-- OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
-- SOFTWARE.

CREATE SCHEMA temporal;

CREATE OR REPLACE FUNCTION temporal.versioning()
    RETURNS TRIGGER AS
$$
DECLARE
    sys_period              text;
    history_table           text;
    manipulate              jsonb;
    ignore_unchanged_values bool;
    commonColumns           text[];
    time_stamp_to_use       timestamptz := current_timestamp;
    range_lower             timestamptz;
    transaction_info        txid_snapshot;
    existing_range          tstzrange;
    holder                  record;
    holder2                 record;
    pg_version              integer;
BEGIN
    -- version 0.4.0

    IF TG_WHEN != 'BEFORE' OR TG_LEVEL != 'ROW' THEN
        RAISE TRIGGER_PROTOCOL_VIOLATED USING
            MESSAGE = 'function "versioning" must be fired BEFORE ROW';
    END IF;

    IF TG_OP != 'INSERT' AND TG_OP != 'UPDATE' AND TG_OP != 'DELETE' THEN
        RAISE TRIGGER_PROTOCOL_VIOLATED USING
            MESSAGE = 'function "versioning" must be fired for INSERT or UPDATE or DELETE';
    END IF;

    IF TG_NARGS not in (3, 4) THEN
        RAISE INVALID_PARAMETER_VALUE USING
            MESSAGE = 'wrong number of parameters for function "versioning"',
            HINT = 'expected 3 or 4 parameters but got ' || TG_NARGS;
    END IF;

    sys_period := TG_ARGV[0];
    history_table := TG_ARGV[1];
    ignore_unchanged_values := TG_ARGV[3];

    IF ignore_unchanged_values AND TG_OP = 'UPDATE' AND NEW IS NOT DISTINCT FROM OLD THEN
        RETURN OLD;
    END IF;

    -- check if sys_period exists on original table
    SELECT atttypid, attndims
    INTO holder
    FROM pg_attribute
    WHERE attrelid = TG_RELID
      AND attname = sys_period
      AND NOT attisdropped;
    IF NOT FOUND THEN
        RAISE 'column "%" of relation "%" does not exist', sys_period, TG_TABLE_NAME USING
            ERRCODE = 'undefined_column';
    END IF;
    IF holder.atttypid != to_regtype('tstzrange') THEN
        IF holder.attndims > 0 THEN
            RAISE 'system period column "%" of relation "%" is not a range but an array', sys_period, TG_TABLE_NAME USING
                ERRCODE = 'datatype_mismatch';
        END IF;

        SELECT rngsubtype INTO holder2 FROM pg_range WHERE rngtypid = holder.atttypid;
        IF FOUND THEN
            RAISE 'system period column "%" of relation "%" is not a range of timestamp with timezone but of type %', sys_period, TG_TABLE_NAME, format_type(holder2.rngsubtype, null) USING
                ERRCODE = 'datatype_mismatch';
        END IF;

        RAISE 'system period column "%" of relation "%" is not a range but type %', sys_period, TG_TABLE_NAME, format_type(holder.atttypid, null) USING
            ERRCODE = 'datatype_mismatch';
    END IF;

    IF TG_OP = 'UPDATE' OR TG_OP = 'DELETE' THEN
        -- Ignore rows already modified in this transaction
        transaction_info := txid_current_snapshot();
        IF OLD.xmin::text >= (txid_snapshot_xmin(transaction_info) % (2 ^ 32)::bigint)::text
            AND OLD.xmin::text <= (txid_snapshot_xmax(transaction_info) % (2 ^ 32)::bigint)::text THEN
            IF TG_OP = 'DELETE' THEN
                RETURN OLD;
            END IF;

            RETURN NEW;
        END IF;

        SELECT current_setting('server_version_num')::integer
        INTO pg_version;

        -- to support postgres < 9.6
        IF pg_version < 90600 THEN
            -- check if history table exits
            IF to_regclass(history_table::cstring) IS NULL THEN
                RAISE 'relation "%" does not exist', history_table;
            END IF;
        ELSE
            IF to_regclass(history_table) IS NULL THEN
                RAISE 'relation "%" does not exist', history_table;
            END IF;
        END IF;

        -- check if history table has sys_period
        IF NOT EXISTS(SELECT *
                      FROM pg_attribute
                      WHERE attrelid = history_table::regclass
                        AND attname = sys_period
                        AND NOT attisdropped) THEN
            RAISE 'history relation "%" does not contain system period column "%"', history_table, sys_period USING
                HINT =
                        'history relation must contain system period column with the same name and data type as the versioned one';
        END IF;

        EXECUTE format('SELECT $1.%I', sys_period) USING OLD INTO existing_range;

        IF existing_range IS NULL THEN
            RAISE 'system period column "%" of relation "%" must not be null', sys_period, TG_TABLE_NAME USING
                ERRCODE = 'null_value_not_allowed';
        END IF;

        IF isempty(existing_range) OR NOT upper_inf(existing_range) THEN
            RAISE 'system period column "%" of relation "%" contains invalid value', sys_period, TG_TABLE_NAME USING
                ERRCODE = 'data_exception',
                DETAIL = 'valid ranges must be non-empty and unbounded on the high side';
        END IF;

        IF TG_ARGV[2] = 'true' THEN
            -- mitigate update conflicts
            range_lower := lower(existing_range);
            IF range_lower >= time_stamp_to_use THEN
                time_stamp_to_use := range_lower + interval '1 microseconds';
            END IF;
        END IF;

        WITH history AS
                 (SELECT attname, atttypid
                  FROM pg_attribute
                  WHERE attrelid = history_table::regclass
                    AND attnum > 0
                    AND NOT attisdropped),
             main AS
                 (SELECT attname, atttypid
                  FROM pg_attribute
                  WHERE attrelid = TG_RELID
                    AND attnum > 0
                    AND NOT attisdropped)
        SELECT history.attname  AS history_name,
               main.attname     AS main_name,
               history.atttypid AS history_type,
               main.atttypid    AS main_type
        INTO holder
        FROM history
                 INNER JOIN main
                            ON history.attname = main.attname
        WHERE history.atttypid != main.atttypid;

        IF FOUND THEN
            RAISE 'column "%" of relation "%" is of type % but column "%" of history relation "%" is of type %',
                holder.main_name, TG_TABLE_NAME, format_type(holder.main_type, null), holder.history_name, history_table, format_type(holder.history_type, null)
                USING ERRCODE = 'datatype_mismatch';
        END IF;

        WITH history AS
                 (SELECT attname
                  FROM pg_attribute
                  WHERE attrelid = history_table::regclass
                    AND attnum > 0
                    AND NOT attisdropped),
             main AS
                 (SELECT attname
                  FROM pg_attribute
                  WHERE attrelid = TG_RELID
                    AND attnum > 0
                    AND NOT attisdropped)
        SELECT array_agg(quote_ident(history.attname))
        INTO commonColumns
        FROM history
                 INNER JOIN main
                            ON history.attname = main.attname
                                AND history.attname != sys_period;

        EXECUTE ('INSERT INTO ' ||
                 history_table ||
                 '(' ||
                 array_to_string(commonColumns, ',') ||
                 ',' ||
                 quote_ident(sys_period) ||
                 ') VALUES ($1.' ||
                 array_to_string(commonColumns, ',$1.') ||
                 ',tstzrange($2, $3, ''[)''))')
            USING OLD, range_lower, time_stamp_to_use;
    END IF;

    IF TG_OP = 'UPDATE' OR TG_OP = 'INSERT' THEN
        manipulate := jsonb_set('{}'::jsonb, ('{' || sys_period || '}')::text[],
                                to_jsonb(tstzrange(time_stamp_to_use, null, '[)')));

        RETURN jsonb_populate_record(NEW, manipulate);
    END IF;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

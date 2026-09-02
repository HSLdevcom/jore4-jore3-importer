DO $$
DECLARE
  tables_to_drop text;
BEGIN
  SELECT string_agg(format('%I.%I', namespace.nspname, class.relname), ', ')
  INTO tables_to_drop
  FROM pg_class class
  JOIN pg_namespace namespace ON namespace.oid = class.relnamespace
  WHERE class.relkind IN ('r', 'p')
    AND namespace.nspname NOT IN ('pg_catalog', 'information_schema')
    AND namespace.nspname !~ '^pg_toast'
    AND NOT EXISTS (
      SELECT 1
      FROM pg_depend dependency
      WHERE dependency.classid = 'pg_class'::regclass
        AND dependency.objid = class.oid
        AND dependency.deptype = 'e'
    );

  IF tables_to_drop IS NOT NULL THEN
    EXECUTE 'DROP TABLE ' || tables_to_drop || ' CASCADE';
  END IF;
END
$$;

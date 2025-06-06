/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.route.tables;


import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * The routes from Transmodel:
 * https://www.transmodel-cen.eu/model/index.htm?goto=2:1:3:483
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Route extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>route.route</code>
     */
    public static final Route ROUTE_ = new Route();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>route.route.route_id</code>. The ID of the route.
     */
    public final TableField<Record, UUID> ROUTE_ID = createField(DSL.name("route_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field(DSL.raw("public.gen_random_uuid()"), SQLDataType.UUID)), this, "The ID of the route.");

    /**
     * The column <code>route.route.description_i18n</code>. The description of
     * the route in the form of starting location - destination. Placeholder for
     * multilingual strings.
     */
    public final TableField<Record, JSONB> DESCRIPTION_I18N = createField(DSL.name("description_i18n"), SQLDataType.JSONB, this, "The description of the route in the form of starting location - destination. Placeholder for multilingual strings.");

    /**
     * The column <code>route.route.on_line_id</code>. The line to which this
     * route belongs.
     */
    public final TableField<Record, UUID> ON_LINE_ID = createField(DSL.name("on_line_id"), SQLDataType.UUID.nullable(false), this, "The line to which this route belongs.");

    /**
     * The column <code>route.route.validity_start</code>. The point in time
     * (inclusive) when the route becomes valid. If NULL, the route has been
     * always valid before end time of validity period.
     */
    public final TableField<Record, LocalDate> VALIDITY_START = createField(DSL.name("validity_start"), SQLDataType.LOCALDATE, this, "The point in time (inclusive) when the route becomes valid. If NULL, the route has been always valid before end time of validity period.");

    /**
     * The column <code>route.route.validity_end</code>. The point in time
     * (inclusive) from which onwards the route is no longer valid. If NULL, the
     * route is valid indefinitely after the start time of the validity period.
     */
    public final TableField<Record, LocalDate> VALIDITY_END = createField(DSL.name("validity_end"), SQLDataType.LOCALDATE, this, "The point in time (inclusive) from which onwards the route is no longer valid. If NULL, the route is valid indefinitely after the start time of the validity period.");

    /**
     * The column <code>route.route.priority</code>. The priority of the route
     * definition. The definition may be overridden by higher priority
     * definitions.
     */
    public final TableField<Record, Integer> PRIORITY = createField(DSL.name("priority"), SQLDataType.INTEGER.nullable(false), this, "The priority of the route definition. The definition may be overridden by higher priority definitions.");

    /**
     * The column <code>route.route.label</code>. The label of the route
     * definition.
     */
    public final TableField<Record, String> LABEL = createField(DSL.name("label"), SQLDataType.CLOB.nullable(false), this, "The label of the route definition.");

    /**
     * The column <code>route.route.direction</code>. The direction of the route
     * definition.
     */
    public final TableField<Record, String> DIRECTION = createField(DSL.name("direction"), SQLDataType.CLOB.nullable(false), this, "The direction of the route definition.");

    /**
     * The column <code>route.route.name_i18n</code>.
     */
    public final TableField<Record, JSONB> NAME_I18N = createField(DSL.name("name_i18n"), SQLDataType.JSONB.nullable(false), this, "");

    /**
     * The column <code>route.route.origin_name_i18n</code>.
     */
    public final TableField<Record, JSONB> ORIGIN_NAME_I18N = createField(DSL.name("origin_name_i18n"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>route.route.origin_short_name_i18n</code>.
     */
    public final TableField<Record, JSONB> ORIGIN_SHORT_NAME_I18N = createField(DSL.name("origin_short_name_i18n"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>route.route.destination_name_i18n</code>.
     */
    public final TableField<Record, JSONB> DESTINATION_NAME_I18N = createField(DSL.name("destination_name_i18n"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>route.route.destination_short_name_i18n</code>.
     */
    public final TableField<Record, JSONB> DESTINATION_SHORT_NAME_I18N = createField(DSL.name("destination_short_name_i18n"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>route.route.variant</code>. The variant for route
     * definition.
     */
    public final TableField<Record, Short> VARIANT = createField(DSL.name("variant"), SQLDataType.SMALLINT, this, "The variant for route definition.");

    /**
     * The column <code>route.route.unique_label</code>. Derived from label and
     * variant. Routes are unique for each unique label for a certain direction,
     * priority and validity period
     */
    public final TableField<Record, String> UNIQUE_LABEL = createField(DSL.name("unique_label"), SQLDataType.CLOB, this, "Derived from label and variant. Routes are unique for each unique label for a certain direction, priority and validity period");

    /**
     * The column <code>route.route.legacy_hsl_municipality_code</code>. Defines
     * the legacy municipality that is mainly used for data exports.
     */
    public final TableField<Record, String> LEGACY_HSL_MUNICIPALITY_CODE = createField(DSL.name("legacy_hsl_municipality_code"), SQLDataType.CLOB, this, "Defines the legacy municipality that is mainly used for data exports.");

    private Route(Name alias, Table<Record> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Route(Name alias, Table<Record> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment("The routes from Transmodel: https://www.transmodel-cen.eu/model/index.htm?goto=2:1:3:483"), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>route.route</code> table reference
     */
    public Route(String alias) {
        this(DSL.name(alias), ROUTE_);
    }

    /**
     * Create an aliased <code>route.route</code> table reference
     */
    public Route(Name alias) {
        this(alias, ROUTE_);
    }

    /**
     * Create a <code>route.route</code> table reference
     */
    public Route() {
        this(DSL.name("route"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : fi.hsl.jore.jore4.jooq.route.Route.ROUTE;
    }

    @Override
    public Route as(String alias) {
        return new Route(DSL.name(alias), this);
    }

    @Override
    public Route as(Name alias) {
        return new Route(alias, this);
    }

    @Override
    public Route as(Table<?> alias) {
        return new Route(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Route rename(String name) {
        return new Route(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Route rename(Name name) {
        return new Route(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Route rename(Table<?> name) {
        return new Route(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Route where(Condition condition) {
        return new Route(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Route where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Route where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Route where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Route where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Route where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Route where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Route where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Route whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Route whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}

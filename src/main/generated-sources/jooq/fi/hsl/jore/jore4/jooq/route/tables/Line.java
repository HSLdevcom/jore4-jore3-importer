/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.route.tables;


import fi.hsl.jore.jore4.jooq.route.Route;

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
 * The line from Transmodel:
 * http://www.transmodel-cen.eu/model/index.htm?goto=2:1:3:487
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Line extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>route.line</code>
     */
    public static final Line LINE = new Line();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>route.line.line_id</code>. The ID of the line.
     */
    public final TableField<Record, UUID> LINE_ID = createField(DSL.name("line_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field(DSL.raw("public.gen_random_uuid()"), SQLDataType.UUID)), this, "The ID of the line.");

    /**
     * The column <code>route.line.name_i18n</code>. The name of the line.
     * Placeholder for multilingual strings.
     */
    public final TableField<Record, JSONB> NAME_I18N = createField(DSL.name("name_i18n"), SQLDataType.JSONB.nullable(false), this, "The name of the line. Placeholder for multilingual strings.");

    /**
     * The column <code>route.line.short_name_i18n</code>. The shorted name of
     * the line. Placeholder for multilingual strings.
     */
    public final TableField<Record, JSONB> SHORT_NAME_I18N = createField(DSL.name("short_name_i18n"), SQLDataType.JSONB.nullable(false), this, "The shorted name of the line. Placeholder for multilingual strings.");

    /**
     * The column <code>route.line.primary_vehicle_mode</code>. The mode of the
     * vehicles used as primary on the line.
     */
    public final TableField<Record, String> PRIMARY_VEHICLE_MODE = createField(DSL.name("primary_vehicle_mode"), SQLDataType.CLOB.nullable(false), this, "The mode of the vehicles used as primary on the line.");

    /**
     * The column <code>route.line.validity_start</code>. The point in time when
     * the line becomes valid (inclusive). If NULL, the line has been always
     * valid.
     */
    public final TableField<Record, LocalDate> VALIDITY_START = createField(DSL.name("validity_start"), SQLDataType.LOCALDATE, this, "The point in time when the line becomes valid (inclusive). If NULL, the line has been always valid.");

    /**
     * The column <code>route.line.validity_end</code>. The point in time from
     * which onwards the line is no longer valid (inclusive). If NULL, the line
     * will be always valid.
     */
    public final TableField<Record, LocalDate> VALIDITY_END = createField(DSL.name("validity_end"), SQLDataType.LOCALDATE, this, "The point in time from which onwards the line is no longer valid (inclusive). If NULL, the line will be always valid.");

    /**
     * The column <code>route.line.priority</code>. The priority of the line
     * definition. The definition may be overridden by higher priority
     * definitions.
     */
    public final TableField<Record, Integer> PRIORITY = createField(DSL.name("priority"), SQLDataType.INTEGER.nullable(false), this, "The priority of the line definition. The definition may be overridden by higher priority definitions.");

    /**
     * The column <code>route.line.label</code>. The label of the line
     * definition. The label is unique for a certain priority and validity
     * period.
     */
    public final TableField<Record, String> LABEL = createField(DSL.name("label"), SQLDataType.CLOB.nullable(false), this, "The label of the line definition. The label is unique for a certain priority and validity period.");

    /**
     * The column <code>route.line.type_of_line</code>. The type of the line.
     */
    public final TableField<Record, String> TYPE_OF_LINE = createField(DSL.name("type_of_line"), SQLDataType.CLOB.nullable(false), this, "The type of the line.");

    /**
     * The column <code>route.line.transport_target</code>.
     */
    public final TableField<Record, String> TRANSPORT_TARGET = createField(DSL.name("transport_target"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>route.line.legacy_hsl_municipality_code</code>. Defines
     * the legacy municipality that is mainly used for data exports.
     */
    public final TableField<Record, String> LEGACY_HSL_MUNICIPALITY_CODE = createField(DSL.name("legacy_hsl_municipality_code"), SQLDataType.CLOB, this, "Defines the legacy municipality that is mainly used for data exports.");

    private Line(Name alias, Table<Record> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Line(Name alias, Table<Record> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment("The line from Transmodel: http://www.transmodel-cen.eu/model/index.htm?goto=2:1:3:487"), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>route.line</code> table reference
     */
    public Line(String alias) {
        this(DSL.name(alias), LINE);
    }

    /**
     * Create an aliased <code>route.line</code> table reference
     */
    public Line(Name alias) {
        this(alias, LINE);
    }

    /**
     * Create a <code>route.line</code> table reference
     */
    public Line() {
        this(DSL.name("line"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Route.ROUTE;
    }

    @Override
    public Line as(String alias) {
        return new Line(DSL.name(alias), this);
    }

    @Override
    public Line as(Name alias) {
        return new Line(alias, this);
    }

    @Override
    public Line as(Table<?> alias) {
        return new Line(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Line rename(String name) {
        return new Line(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Line rename(Name name) {
        return new Line(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Line rename(Table<?> name) {
        return new Line(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Line where(Condition condition) {
        return new Line(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Line where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Line where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Line where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Line where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Line where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Line where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Line where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Line whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Line whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}

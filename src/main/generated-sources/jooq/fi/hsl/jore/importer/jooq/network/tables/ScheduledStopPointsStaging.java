/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.jooq.network.Keys;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.ScheduledStopPointsStagingRecord;

import java.util.Collection;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ScheduledStopPointsStaging extends TableImpl<ScheduledStopPointsStagingRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>network.scheduled_stop_points_staging</code>
     */
    public static final ScheduledStopPointsStaging SCHEDULED_STOP_POINTS_STAGING = new ScheduledStopPointsStaging();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ScheduledStopPointsStagingRecord> getRecordType() {
        return ScheduledStopPointsStagingRecord.class;
    }

    /**
     * The column
     * <code>network.scheduled_stop_points_staging.scheduled_stop_point_ext_id</code>.
     */
    public final TableField<ScheduledStopPointsStagingRecord, String> SCHEDULED_STOP_POINT_EXT_ID = createField(DSL.name("scheduled_stop_point_ext_id"), SQLDataType.VARCHAR(7).nullable(false), this, "");

    /**
     * The column
     * <code>network.scheduled_stop_points_staging.scheduled_stop_point_ely_number</code>.
     */
    public final TableField<ScheduledStopPointsStagingRecord, Long> SCHEDULED_STOP_POINT_ELY_NUMBER = createField(DSL.name("scheduled_stop_point_ely_number"), SQLDataType.BIGINT, this, "");

    /**
     * The column
     * <code>network.scheduled_stop_points_staging.scheduled_stop_point_name</code>.
     */
    public final TableField<ScheduledStopPointsStagingRecord, JSONB> SCHEDULED_STOP_POINT_NAME = createField(DSL.name("scheduled_stop_point_name"), SQLDataType.JSONB.nullable(false), this, "");

    /**
     * The column
     * <code>network.scheduled_stop_points_staging.scheduled_stop_point_short_id</code>.
     */
    public final TableField<ScheduledStopPointsStagingRecord, String> SCHEDULED_STOP_POINT_SHORT_ID = createField(DSL.name("scheduled_stop_point_short_id"), SQLDataType.VARCHAR(6), this, "");

    /**
     * The column
     * <code>network.scheduled_stop_points_staging.usage_in_routes</code>.
     */
    public final TableField<ScheduledStopPointsStagingRecord, Integer> USAGE_IN_ROUTES = createField(DSL.name("usage_in_routes"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field(DSL.raw("0"), SQLDataType.INTEGER)), this, "");

    /**
     * The column
     * <code>network.scheduled_stop_points_staging.network_place_ext_id</code>.
     */
    public final TableField<ScheduledStopPointsStagingRecord, String> NETWORK_PLACE_EXT_ID = createField(DSL.name("network_place_ext_id"), SQLDataType.CLOB, this, "");

    private ScheduledStopPointsStaging(Name alias, Table<ScheduledStopPointsStagingRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private ScheduledStopPointsStaging(Name alias, Table<ScheduledStopPointsStagingRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>network.scheduled_stop_points_staging</code>
     * table reference
     */
    public ScheduledStopPointsStaging(String alias) {
        this(DSL.name(alias), SCHEDULED_STOP_POINTS_STAGING);
    }

    /**
     * Create an aliased <code>network.scheduled_stop_points_staging</code>
     * table reference
     */
    public ScheduledStopPointsStaging(Name alias) {
        this(alias, SCHEDULED_STOP_POINTS_STAGING);
    }

    /**
     * Create a <code>network.scheduled_stop_points_staging</code> table
     * reference
     */
    public ScheduledStopPointsStaging() {
        this(DSL.name("scheduled_stop_points_staging"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Network.NETWORK;
    }

    @Override
    public UniqueKey<ScheduledStopPointsStagingRecord> getPrimaryKey() {
        return Keys.SCHEDULED_STOP_POINTS_STAGING_PKEY;
    }

    @Override
    public ScheduledStopPointsStaging as(String alias) {
        return new ScheduledStopPointsStaging(DSL.name(alias), this);
    }

    @Override
    public ScheduledStopPointsStaging as(Name alias) {
        return new ScheduledStopPointsStaging(alias, this);
    }

    @Override
    public ScheduledStopPointsStaging as(Table<?> alias) {
        return new ScheduledStopPointsStaging(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPointsStaging rename(String name) {
        return new ScheduledStopPointsStaging(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPointsStaging rename(Name name) {
        return new ScheduledStopPointsStaging(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPointsStaging rename(Table<?> name) {
        return new ScheduledStopPointsStaging(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointsStaging where(Condition condition) {
        return new ScheduledStopPointsStaging(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointsStaging where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointsStaging where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointsStaging where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScheduledStopPointsStaging where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScheduledStopPointsStaging where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScheduledStopPointsStaging where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScheduledStopPointsStaging where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointsStaging whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointsStaging whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}

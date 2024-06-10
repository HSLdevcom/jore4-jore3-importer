/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.ScheduledStopPointsHistoryRecord;

import java.util.Collection;
import java.util.UUID;

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
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ScheduledStopPointsHistory extends TableImpl<ScheduledStopPointsHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>network.scheduled_stop_points_history</code>
     */
    public static final ScheduledStopPointsHistory SCHEDULED_STOP_POINTS_HISTORY = new ScheduledStopPointsHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ScheduledStopPointsHistoryRecord> getRecordType() {
        return ScheduledStopPointsHistoryRecord.class;
    }

    /**
     * The column
     * <code>network.scheduled_stop_points_history.scheduled_stop_point_id</code>.
     */
    public final TableField<ScheduledStopPointsHistoryRecord, UUID> SCHEDULED_STOP_POINT_ID = createField(DSL.name("scheduled_stop_point_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column
     * <code>network.scheduled_stop_points_history.scheduled_stop_point_ext_id</code>.
     */
    public final TableField<ScheduledStopPointsHistoryRecord, String> SCHEDULED_STOP_POINT_EXT_ID = createField(DSL.name("scheduled_stop_point_ext_id"), SQLDataType.VARCHAR(7).nullable(false), this, "");

    /**
     * The column
     * <code>network.scheduled_stop_points_history.infrastructure_node_id</code>.
     */
    public final TableField<ScheduledStopPointsHistoryRecord, UUID> INFRASTRUCTURE_NODE_ID = createField(DSL.name("infrastructure_node_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column
     * <code>network.scheduled_stop_points_history.scheduled_stop_point_ely_number</code>.
     */
    public final TableField<ScheduledStopPointsHistoryRecord, Long> SCHEDULED_STOP_POINT_ELY_NUMBER = createField(DSL.name("scheduled_stop_point_ely_number"), SQLDataType.BIGINT, this, "");

    /**
     * The column
     * <code>network.scheduled_stop_points_history.scheduled_stop_point_name</code>.
     */
    public final TableField<ScheduledStopPointsHistoryRecord, JSONB> SCHEDULED_STOP_POINT_NAME = createField(DSL.name("scheduled_stop_point_name"), SQLDataType.JSONB.nullable(false), this, "");

    /**
     * The column
     * <code>network.scheduled_stop_points_history.scheduled_stop_point_sys_period</code>.
     */
    public final TableField<ScheduledStopPointsHistoryRecord, TimeRange> SCHEDULED_STOP_POINT_SYS_PERIOD = createField(DSL.name("scheduled_stop_point_sys_period"), DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\"").nullable(false), this, "", new TimeRangeBinding());

    /**
     * The column
     * <code>network.scheduled_stop_points_history.scheduled_stop_point_short_id</code>.
     */
    public final TableField<ScheduledStopPointsHistoryRecord, String> SCHEDULED_STOP_POINT_SHORT_ID = createField(DSL.name("scheduled_stop_point_short_id"), SQLDataType.VARCHAR(6), this, "");

    /**
     * The column
     * <code>network.scheduled_stop_points_history.scheduled_stop_point_jore4_id</code>.
     */
    public final TableField<ScheduledStopPointsHistoryRecord, UUID> SCHEDULED_STOP_POINT_JORE4_ID = createField(DSL.name("scheduled_stop_point_jore4_id"), SQLDataType.UUID, this, "");

    /**
     * The column
     * <code>network.scheduled_stop_points_history.usage_in_routes</code>.
     */
    public final TableField<ScheduledStopPointsHistoryRecord, Integer> USAGE_IN_ROUTES = createField(DSL.name("usage_in_routes"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field(DSL.raw("0"), SQLDataType.INTEGER)), this, "");

    /**
     * The column
     * <code>network.scheduled_stop_points_history.network_place_id</code>.
     */
    public final TableField<ScheduledStopPointsHistoryRecord, UUID> NETWORK_PLACE_ID = createField(DSL.name("network_place_id"), SQLDataType.UUID, this, "");

    private ScheduledStopPointsHistory(Name alias, Table<ScheduledStopPointsHistoryRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private ScheduledStopPointsHistory(Name alias, Table<ScheduledStopPointsHistoryRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>network.scheduled_stop_points_history</code>
     * table reference
     */
    public ScheduledStopPointsHistory(String alias) {
        this(DSL.name(alias), SCHEDULED_STOP_POINTS_HISTORY);
    }

    /**
     * Create an aliased <code>network.scheduled_stop_points_history</code>
     * table reference
     */
    public ScheduledStopPointsHistory(Name alias) {
        this(alias, SCHEDULED_STOP_POINTS_HISTORY);
    }

    /**
     * Create a <code>network.scheduled_stop_points_history</code> table
     * reference
     */
    public ScheduledStopPointsHistory() {
        this(DSL.name("scheduled_stop_points_history"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Network.NETWORK;
    }

    @Override
    public ScheduledStopPointsHistory as(String alias) {
        return new ScheduledStopPointsHistory(DSL.name(alias), this);
    }

    @Override
    public ScheduledStopPointsHistory as(Name alias) {
        return new ScheduledStopPointsHistory(alias, this);
    }

    @Override
    public ScheduledStopPointsHistory as(Table<?> alias) {
        return new ScheduledStopPointsHistory(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPointsHistory rename(String name) {
        return new ScheduledStopPointsHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPointsHistory rename(Name name) {
        return new ScheduledStopPointsHistory(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPointsHistory rename(Table<?> name) {
        return new ScheduledStopPointsHistory(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointsHistory where(Condition condition) {
        return new ScheduledStopPointsHistory(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointsHistory where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointsHistory where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointsHistory where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScheduledStopPointsHistory where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScheduledStopPointsHistory where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScheduledStopPointsHistory where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScheduledStopPointsHistory where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointsHistory whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointsHistory whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}

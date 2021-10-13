/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.ScheduledStopPointsHistoryRecord;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ScheduledStopPointsHistory extends TableImpl<ScheduledStopPointsHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.scheduled_stop_points_history</code>
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
     * The column <code>network.scheduled_stop_points_history.scheduled_stop_point_id</code>.
     */
    public final TableField<ScheduledStopPointsHistoryRecord, UUID> SCHEDULED_STOP_POINT_ID = createField(DSL.name("scheduled_stop_point_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>network.scheduled_stop_points_history.scheduled_stop_point_ext_id</code>.
     */
    public final TableField<ScheduledStopPointsHistoryRecord, String> SCHEDULED_STOP_POINT_EXT_ID = createField(DSL.name("scheduled_stop_point_ext_id"), SQLDataType.VARCHAR(7).nullable(false), this, "");

    /**
     * The column <code>network.scheduled_stop_points_history.infrastructure_node_id</code>.
     */
    public final TableField<ScheduledStopPointsHistoryRecord, UUID> INFRASTRUCTURE_NODE_ID = createField(DSL.name("infrastructure_node_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>network.scheduled_stop_points_history.scheduled_stop_point_name</code>.
     */
    public final TableField<ScheduledStopPointsHistoryRecord, JSONB> SCHEDULED_STOP_POINT_NAME = createField(DSL.name("scheduled_stop_point_name"), SQLDataType.JSONB.nullable(false), this, "");

    /**
     * The column <code>network.scheduled_stop_points_history.scheduled_stop_point_sys_period</code>.
     */
    public final TableField<ScheduledStopPointsHistoryRecord, TimeRange> SCHEDULED_STOP_POINT_SYS_PERIOD = createField(DSL.name("scheduled_stop_point_sys_period"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\"").nullable(false), this, "", new TimeRangeBinding());

    private ScheduledStopPointsHistory(Name alias, Table<ScheduledStopPointsHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private ScheduledStopPointsHistory(Name alias, Table<ScheduledStopPointsHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>network.scheduled_stop_points_history</code> table reference
     */
    public ScheduledStopPointsHistory(String alias) {
        this(DSL.name(alias), SCHEDULED_STOP_POINTS_HISTORY);
    }

    /**
     * Create an aliased <code>network.scheduled_stop_points_history</code> table reference
     */
    public ScheduledStopPointsHistory(Name alias) {
        this(alias, SCHEDULED_STOP_POINTS_HISTORY);
    }

    /**
     * Create a <code>network.scheduled_stop_points_history</code> table reference
     */
    public ScheduledStopPointsHistory() {
        this(DSL.name("scheduled_stop_points_history"), null);
    }

    public <O extends Record> ScheduledStopPointsHistory(Table<O> child, ForeignKey<O, ScheduledStopPointsHistoryRecord> key) {
        super(child, key, SCHEDULED_STOP_POINTS_HISTORY);
    }

    @Override
    public Schema getSchema() {
        return Network.NETWORK;
    }

    @Override
    public ScheduledStopPointsHistory as(String alias) {
        return new ScheduledStopPointsHistory(DSL.name(alias), this);
    }

    @Override
    public ScheduledStopPointsHistory as(Name alias) {
        return new ScheduledStopPointsHistory(alias, this);
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

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<UUID, String, UUID, JSONB, TimeRange> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}

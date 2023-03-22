/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.ScheduledStopPointsWithHistoryRecord;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row10;
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
public class ScheduledStopPointsWithHistory extends TableImpl<ScheduledStopPointsWithHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.scheduled_stop_points_with_history</code>
     */
    public static final ScheduledStopPointsWithHistory SCHEDULED_STOP_POINTS_WITH_HISTORY = new ScheduledStopPointsWithHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ScheduledStopPointsWithHistoryRecord> getRecordType() {
        return ScheduledStopPointsWithHistoryRecord.class;
    }

    /**
     * The column <code>network.scheduled_stop_points_with_history.scheduled_stop_point_id</code>.
     */
    public final TableField<ScheduledStopPointsWithHistoryRecord, UUID> SCHEDULED_STOP_POINT_ID = createField(DSL.name("scheduled_stop_point_id"), SQLDataType.UUID, this, "");

    /**
     * The column <code>network.scheduled_stop_points_with_history.scheduled_stop_point_ext_id</code>.
     */
    public final TableField<ScheduledStopPointsWithHistoryRecord, String> SCHEDULED_STOP_POINT_EXT_ID = createField(DSL.name("scheduled_stop_point_ext_id"), SQLDataType.VARCHAR(7), this, "");

    /**
     * The column <code>network.scheduled_stop_points_with_history.infrastructure_node_id</code>.
     */
    public final TableField<ScheduledStopPointsWithHistoryRecord, UUID> INFRASTRUCTURE_NODE_ID = createField(DSL.name("infrastructure_node_id"), SQLDataType.UUID, this, "");

    /**
     * The column <code>network.scheduled_stop_points_with_history.scheduled_stop_point_ely_number</code>.
     */
    public final TableField<ScheduledStopPointsWithHistoryRecord, Long> SCHEDULED_STOP_POINT_ELY_NUMBER = createField(DSL.name("scheduled_stop_point_ely_number"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>network.scheduled_stop_points_with_history.scheduled_stop_point_name</code>.
     */
    public final TableField<ScheduledStopPointsWithHistoryRecord, JSONB> SCHEDULED_STOP_POINT_NAME = createField(DSL.name("scheduled_stop_point_name"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>network.scheduled_stop_points_with_history.scheduled_stop_point_sys_period</code>.
     */
    public final TableField<ScheduledStopPointsWithHistoryRecord, TimeRange> SCHEDULED_STOP_POINT_SYS_PERIOD = createField(DSL.name("scheduled_stop_point_sys_period"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\""), this, "", new TimeRangeBinding());

    /**
     * The column <code>network.scheduled_stop_points_with_history.scheduled_stop_point_short_id</code>.
     */
    public final TableField<ScheduledStopPointsWithHistoryRecord, String> SCHEDULED_STOP_POINT_SHORT_ID = createField(DSL.name("scheduled_stop_point_short_id"), SQLDataType.VARCHAR(6), this, "");

    /**
     * The column <code>network.scheduled_stop_points_with_history.scheduled_stop_point_jore4_id</code>.
     */
    public final TableField<ScheduledStopPointsWithHistoryRecord, UUID> SCHEDULED_STOP_POINT_JORE4_ID = createField(DSL.name("scheduled_stop_point_jore4_id"), SQLDataType.UUID, this, "");

    /**
     * The column <code>network.scheduled_stop_points_with_history.usage_in_routes</code>.
     */
    public final TableField<ScheduledStopPointsWithHistoryRecord, Integer> USAGE_IN_ROUTES = createField(DSL.name("usage_in_routes"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>network.scheduled_stop_points_with_history.hastus_place_id</code>.
     */
    public final TableField<ScheduledStopPointsWithHistoryRecord, String> HASTUS_PLACE_ID = createField(DSL.name("hastus_place_id"), SQLDataType.CLOB, this, "");

    private ScheduledStopPointsWithHistory(Name alias, Table<ScheduledStopPointsWithHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private ScheduledStopPointsWithHistory(Name alias, Table<ScheduledStopPointsWithHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view("create view \"scheduled_stop_points_with_history\" as  SELECT scheduled_stop_points.scheduled_stop_point_id,\n    scheduled_stop_points.scheduled_stop_point_ext_id,\n    scheduled_stop_points.infrastructure_node_id,\n    scheduled_stop_points.scheduled_stop_point_ely_number,\n    scheduled_stop_points.scheduled_stop_point_name,\n    scheduled_stop_points.scheduled_stop_point_sys_period,\n    scheduled_stop_points.scheduled_stop_point_short_id,\n    scheduled_stop_points.scheduled_stop_point_jore4_id,\n    scheduled_stop_points.usage_in_routes,\n    scheduled_stop_points.hastus_place_id\n   FROM network.scheduled_stop_points\nUNION ALL\n SELECT scheduled_stop_points_history.scheduled_stop_point_id,\n    scheduled_stop_points_history.scheduled_stop_point_ext_id,\n    scheduled_stop_points_history.infrastructure_node_id,\n    scheduled_stop_points_history.scheduled_stop_point_ely_number,\n    scheduled_stop_points_history.scheduled_stop_point_name,\n    scheduled_stop_points_history.scheduled_stop_point_sys_period,\n    scheduled_stop_points_history.scheduled_stop_point_short_id,\n    scheduled_stop_points_history.scheduled_stop_point_jore4_id,\n    scheduled_stop_points_history.usage_in_routes,\n    scheduled_stop_points_history.hastus_place_id\n   FROM network.scheduled_stop_points_history;"));
    }

    /**
     * Create an aliased <code>network.scheduled_stop_points_with_history</code> table reference
     */
    public ScheduledStopPointsWithHistory(String alias) {
        this(DSL.name(alias), SCHEDULED_STOP_POINTS_WITH_HISTORY);
    }

    /**
     * Create an aliased <code>network.scheduled_stop_points_with_history</code> table reference
     */
    public ScheduledStopPointsWithHistory(Name alias) {
        this(alias, SCHEDULED_STOP_POINTS_WITH_HISTORY);
    }

    /**
     * Create a <code>network.scheduled_stop_points_with_history</code> table reference
     */
    public ScheduledStopPointsWithHistory() {
        this(DSL.name("scheduled_stop_points_with_history"), null);
    }

    public <O extends Record> ScheduledStopPointsWithHistory(Table<O> child, ForeignKey<O, ScheduledStopPointsWithHistoryRecord> key) {
        super(child, key, SCHEDULED_STOP_POINTS_WITH_HISTORY);
    }

    @Override
    public Schema getSchema() {
        return Network.NETWORK;
    }

    @Override
    public ScheduledStopPointsWithHistory as(String alias) {
        return new ScheduledStopPointsWithHistory(DSL.name(alias), this);
    }

    @Override
    public ScheduledStopPointsWithHistory as(Name alias) {
        return new ScheduledStopPointsWithHistory(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPointsWithHistory rename(String name) {
        return new ScheduledStopPointsWithHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPointsWithHistory rename(Name name) {
        return new ScheduledStopPointsWithHistory(name, null);
    }

    // -------------------------------------------------------------------------
    // Row10 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row10<UUID, String, UUID, Long, JSONB, TimeRange, String, UUID, Integer, String> fieldsRow() {
        return (Row10) super.fieldsRow();
    }
}

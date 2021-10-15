/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRoutePointsHistoryRecord;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row6;
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
public class NetworkRoutePointsHistory extends TableImpl<NetworkRoutePointsHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.network_route_points_history</code>
     */
    public static final NetworkRoutePointsHistory NETWORK_ROUTE_POINTS_HISTORY = new NetworkRoutePointsHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkRoutePointsHistoryRecord> getRecordType() {
        return NetworkRoutePointsHistoryRecord.class;
    }

    /**
     * The column <code>network.network_route_points_history.network_route_point_id</code>.
     */
    public final TableField<NetworkRoutePointsHistoryRecord, UUID> NETWORK_ROUTE_POINT_ID = createField(DSL.name("network_route_point_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>network.network_route_points_history.network_route_direction_id</code>.
     */
    public final TableField<NetworkRoutePointsHistoryRecord, UUID> NETWORK_ROUTE_DIRECTION_ID = createField(DSL.name("network_route_direction_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>network.network_route_points_history.infrastructure_node</code>.
     */
    public final TableField<NetworkRoutePointsHistoryRecord, UUID> INFRASTRUCTURE_NODE = createField(DSL.name("infrastructure_node"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>network.network_route_points_history.network_route_point_ext_id</code>.
     */
    public final TableField<NetworkRoutePointsHistoryRecord, String> NETWORK_ROUTE_POINT_EXT_ID = createField(DSL.name("network_route_point_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_route_points_history.network_route_point_order</code>.
     */
    public final TableField<NetworkRoutePointsHistoryRecord, Integer> NETWORK_ROUTE_POINT_ORDER = createField(DSL.name("network_route_point_order"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>network.network_route_points_history.network_route_point_sys_period</code>.
     */
    public final TableField<NetworkRoutePointsHistoryRecord, TimeRange> NETWORK_ROUTE_POINT_SYS_PERIOD = createField(DSL.name("network_route_point_sys_period"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\"").nullable(false), this, "", new TimeRangeBinding());

    private NetworkRoutePointsHistory(Name alias, Table<NetworkRoutePointsHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private NetworkRoutePointsHistory(Name alias, Table<NetworkRoutePointsHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>network.network_route_points_history</code> table reference
     */
    public NetworkRoutePointsHistory(String alias) {
        this(DSL.name(alias), NETWORK_ROUTE_POINTS_HISTORY);
    }

    /**
     * Create an aliased <code>network.network_route_points_history</code> table reference
     */
    public NetworkRoutePointsHistory(Name alias) {
        this(alias, NETWORK_ROUTE_POINTS_HISTORY);
    }

    /**
     * Create a <code>network.network_route_points_history</code> table reference
     */
    public NetworkRoutePointsHistory() {
        this(DSL.name("network_route_points_history"), null);
    }

    public <O extends Record> NetworkRoutePointsHistory(Table<O> child, ForeignKey<O, NetworkRoutePointsHistoryRecord> key) {
        super(child, key, NETWORK_ROUTE_POINTS_HISTORY);
    }

    @Override
    public Schema getSchema() {
        return Network.NETWORK;
    }

    @Override
    public NetworkRoutePointsHistory as(String alias) {
        return new NetworkRoutePointsHistory(DSL.name(alias), this);
    }

    @Override
    public NetworkRoutePointsHistory as(Name alias) {
        return new NetworkRoutePointsHistory(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutePointsHistory rename(String name) {
        return new NetworkRoutePointsHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutePointsHistory rename(Name name) {
        return new NetworkRoutePointsHistory(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<UUID, UUID, UUID, String, Integer, TimeRange> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}

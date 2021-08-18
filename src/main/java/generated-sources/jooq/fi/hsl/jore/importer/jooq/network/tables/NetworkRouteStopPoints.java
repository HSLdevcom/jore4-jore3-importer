/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.network.Keys;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRouteStopPointsRecord;

import java.util.Arrays;
import java.util.List;
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
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkRouteStopPoints extends TableImpl<NetworkRouteStopPointsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.network_route_stop_points</code>
     */
    public static final NetworkRouteStopPoints NETWORK_ROUTE_STOP_POINTS = new NetworkRouteStopPoints();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkRouteStopPointsRecord> getRecordType() {
        return NetworkRouteStopPointsRecord.class;
    }

    /**
     * The column <code>network.network_route_stop_points.network_route_point_id</code>.
     */
    public final TableField<NetworkRouteStopPointsRecord, UUID> NETWORK_ROUTE_POINT_ID = createField(DSL.name("network_route_point_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>network.network_route_stop_points.network_route_stop_point_ext_id</code>.
     */
    public final TableField<NetworkRouteStopPointsRecord, String> NETWORK_ROUTE_STOP_POINT_EXT_ID = createField(DSL.name("network_route_stop_point_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_route_stop_points.network_route_stop_point_order</code>.
     */
    public final TableField<NetworkRouteStopPointsRecord, Integer> NETWORK_ROUTE_STOP_POINT_ORDER = createField(DSL.name("network_route_stop_point_order"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>network.network_route_stop_points.network_route_stop_point_hastus_point</code>.
     */
    public final TableField<NetworkRouteStopPointsRecord, Boolean> NETWORK_ROUTE_STOP_POINT_HASTUS_POINT = createField(DSL.name("network_route_stop_point_hastus_point"), SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>network.network_route_stop_points.network_route_stop_point_timetable_column</code>.
     */
    public final TableField<NetworkRouteStopPointsRecord, Integer> NETWORK_ROUTE_STOP_POINT_TIMETABLE_COLUMN = createField(DSL.name("network_route_stop_point_timetable_column"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>network.network_route_stop_points.network_route_stop_point_sys_period</code>.
     */
    public final TableField<NetworkRouteStopPointsRecord, TimeRange> NETWORK_ROUTE_STOP_POINT_SYS_PERIOD = createField(DSL.name("network_route_stop_point_sys_period"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\"").nullable(false).defaultValue(DSL.field("tstzrange(CURRENT_TIMESTAMP, NULL::timestamp with time zone)", org.jooq.impl.SQLDataType.OTHER)), this, "", new TimeRangeBinding());

    private NetworkRouteStopPoints(Name alias, Table<NetworkRouteStopPointsRecord> aliased) {
        this(alias, aliased, null);
    }

    private NetworkRouteStopPoints(Name alias, Table<NetworkRouteStopPointsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>network.network_route_stop_points</code> table reference
     */
    public NetworkRouteStopPoints(String alias) {
        this(DSL.name(alias), NETWORK_ROUTE_STOP_POINTS);
    }

    /**
     * Create an aliased <code>network.network_route_stop_points</code> table reference
     */
    public NetworkRouteStopPoints(Name alias) {
        this(alias, NETWORK_ROUTE_STOP_POINTS);
    }

    /**
     * Create a <code>network.network_route_stop_points</code> table reference
     */
    public NetworkRouteStopPoints() {
        this(DSL.name("network_route_stop_points"), null);
    }

    public <O extends Record> NetworkRouteStopPoints(Table<O> child, ForeignKey<O, NetworkRouteStopPointsRecord> key) {
        super(child, key, NETWORK_ROUTE_STOP_POINTS);
    }

    @Override
    public Schema getSchema() {
        return Network.NETWORK;
    }

    @Override
    public UniqueKey<NetworkRouteStopPointsRecord> getPrimaryKey() {
        return Keys.NETWORK_ROUTE_STOP_POINTS_PKEY;
    }

    @Override
    public List<UniqueKey<NetworkRouteStopPointsRecord>> getKeys() {
        return Arrays.<UniqueKey<NetworkRouteStopPointsRecord>>asList(Keys.NETWORK_ROUTE_STOP_POINTS_PKEY);
    }

    @Override
    public List<ForeignKey<NetworkRouteStopPointsRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<NetworkRouteStopPointsRecord, ?>>asList(Keys.NETWORK_ROUTE_STOP_POINTS__NETWORK_ROUTE_STOP_POINTS_NETWORK_ROUTE_POINT_ID_FKEY);
    }

    private transient NetworkRoutePoints _networkRoutePoints;

    public NetworkRoutePoints networkRoutePoints() {
        if (_networkRoutePoints == null)
            _networkRoutePoints = new NetworkRoutePoints(this, Keys.NETWORK_ROUTE_STOP_POINTS__NETWORK_ROUTE_STOP_POINTS_NETWORK_ROUTE_POINT_ID_FKEY);

        return _networkRoutePoints;
    }

    @Override
    public NetworkRouteStopPoints as(String alias) {
        return new NetworkRouteStopPoints(DSL.name(alias), this);
    }

    @Override
    public NetworkRouteStopPoints as(Name alias) {
        return new NetworkRouteStopPoints(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRouteStopPoints rename(String name) {
        return new NetworkRouteStopPoints(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRouteStopPoints rename(Name name) {
        return new NetworkRouteStopPoints(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<UUID, String, Integer, Boolean, Integer, TimeRange> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}
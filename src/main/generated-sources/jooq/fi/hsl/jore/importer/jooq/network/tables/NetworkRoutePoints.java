/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodes;
import fi.hsl.jore.importer.jooq.network.Keys;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRoutePointsRecord;

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
public class NetworkRoutePoints extends TableImpl<NetworkRoutePointsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.network_route_points</code>
     */
    public static final NetworkRoutePoints NETWORK_ROUTE_POINTS = new NetworkRoutePoints();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkRoutePointsRecord> getRecordType() {
        return NetworkRoutePointsRecord.class;
    }

    /**
     * The column <code>network.network_route_points.network_route_point_id</code>.
     */
    public final TableField<NetworkRoutePointsRecord, UUID> NETWORK_ROUTE_POINT_ID = createField(DSL.name("network_route_point_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("gen_random_uuid()", SQLDataType.UUID)), this, "");

    /**
     * The column <code>network.network_route_points.network_route_direction_id</code>.
     */
    public final TableField<NetworkRoutePointsRecord, UUID> NETWORK_ROUTE_DIRECTION_ID = createField(DSL.name("network_route_direction_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>network.network_route_points.infrastructure_node</code>.
     */
    public final TableField<NetworkRoutePointsRecord, UUID> INFRASTRUCTURE_NODE = createField(DSL.name("infrastructure_node"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>network.network_route_points.network_route_point_ext_id</code>.
     */
    public final TableField<NetworkRoutePointsRecord, String> NETWORK_ROUTE_POINT_EXT_ID = createField(DSL.name("network_route_point_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_route_points.network_route_point_order</code>.
     */
    public final TableField<NetworkRoutePointsRecord, Integer> NETWORK_ROUTE_POINT_ORDER = createField(DSL.name("network_route_point_order"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>network.network_route_points.network_route_point_sys_period</code>.
     */
    public final TableField<NetworkRoutePointsRecord, TimeRange> NETWORK_ROUTE_POINT_SYS_PERIOD = createField(DSL.name("network_route_point_sys_period"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\"").nullable(false).defaultValue(DSL.field("tstzrange(CURRENT_TIMESTAMP, NULL::timestamp with time zone)", org.jooq.impl.SQLDataType.OTHER)), this, "", new TimeRangeBinding());

    private NetworkRoutePoints(Name alias, Table<NetworkRoutePointsRecord> aliased) {
        this(alias, aliased, null);
    }

    private NetworkRoutePoints(Name alias, Table<NetworkRoutePointsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>network.network_route_points</code> table reference
     */
    public NetworkRoutePoints(String alias) {
        this(DSL.name(alias), NETWORK_ROUTE_POINTS);
    }

    /**
     * Create an aliased <code>network.network_route_points</code> table reference
     */
    public NetworkRoutePoints(Name alias) {
        this(alias, NETWORK_ROUTE_POINTS);
    }

    /**
     * Create a <code>network.network_route_points</code> table reference
     */
    public NetworkRoutePoints() {
        this(DSL.name("network_route_points"), null);
    }

    public <O extends Record> NetworkRoutePoints(Table<O> child, ForeignKey<O, NetworkRoutePointsRecord> key) {
        super(child, key, NETWORK_ROUTE_POINTS);
    }

    @Override
    public Schema getSchema() {
        return Network.NETWORK;
    }

    @Override
    public UniqueKey<NetworkRoutePointsRecord> getPrimaryKey() {
        return Keys.NETWORK_ROUTE_POINTS_PKEY;
    }

    @Override
    public List<UniqueKey<NetworkRoutePointsRecord>> getKeys() {
        return Arrays.<UniqueKey<NetworkRoutePointsRecord>>asList(Keys.NETWORK_ROUTE_POINTS_PKEY);
    }

    @Override
    public List<ForeignKey<NetworkRoutePointsRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<NetworkRoutePointsRecord, ?>>asList(Keys.NETWORK_ROUTE_POINTS__NETWORK_ROUTE_POINTS_NETWORK_ROUTE_DIRECTION_ID_FKEY, Keys.NETWORK_ROUTE_POINTS__NETWORK_ROUTE_POINTS_INFRASTRUCTURE_NODE_FKEY);
    }

    private transient NetworkRouteDirections _networkRouteDirections;
    private transient InfrastructureNodes _infrastructureNodes;

    public NetworkRouteDirections networkRouteDirections() {
        if (_networkRouteDirections == null)
            _networkRouteDirections = new NetworkRouteDirections(this, Keys.NETWORK_ROUTE_POINTS__NETWORK_ROUTE_POINTS_NETWORK_ROUTE_DIRECTION_ID_FKEY);

        return _networkRouteDirections;
    }

    public InfrastructureNodes infrastructureNodes() {
        if (_infrastructureNodes == null)
            _infrastructureNodes = new InfrastructureNodes(this, Keys.NETWORK_ROUTE_POINTS__NETWORK_ROUTE_POINTS_INFRASTRUCTURE_NODE_FKEY);

        return _infrastructureNodes;
    }

    @Override
    public NetworkRoutePoints as(String alias) {
        return new NetworkRoutePoints(DSL.name(alias), this);
    }

    @Override
    public NetworkRoutePoints as(Name alias) {
        return new NetworkRoutePoints(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutePoints rename(String name) {
        return new NetworkRoutePoints(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutePoints rename(Name name) {
        return new NetworkRoutePoints(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<UUID, UUID, UUID, String, Integer, TimeRange> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}

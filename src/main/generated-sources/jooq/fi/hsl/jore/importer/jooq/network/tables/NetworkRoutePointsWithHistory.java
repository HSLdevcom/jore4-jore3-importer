/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRoutePointsWithHistoryRecord;

import java.util.Collection;
import java.util.UUID;

import org.jooq.Condition;
import org.jooq.Field;
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
public class NetworkRoutePointsWithHistory extends TableImpl<NetworkRoutePointsWithHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>network.network_route_points_with_history</code>
     */
    public static final NetworkRoutePointsWithHistory NETWORK_ROUTE_POINTS_WITH_HISTORY = new NetworkRoutePointsWithHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkRoutePointsWithHistoryRecord> getRecordType() {
        return NetworkRoutePointsWithHistoryRecord.class;
    }

    /**
     * The column
     * <code>network.network_route_points_with_history.network_route_point_id</code>.
     */
    public final TableField<NetworkRoutePointsWithHistoryRecord, UUID> NETWORK_ROUTE_POINT_ID = createField(DSL.name("network_route_point_id"), SQLDataType.UUID, this, "");

    /**
     * The column
     * <code>network.network_route_points_with_history.network_route_direction_id</code>.
     */
    public final TableField<NetworkRoutePointsWithHistoryRecord, UUID> NETWORK_ROUTE_DIRECTION_ID = createField(DSL.name("network_route_direction_id"), SQLDataType.UUID, this, "");

    /**
     * The column
     * <code>network.network_route_points_with_history.infrastructure_node</code>.
     */
    public final TableField<NetworkRoutePointsWithHistoryRecord, UUID> INFRASTRUCTURE_NODE = createField(DSL.name("infrastructure_node"), SQLDataType.UUID, this, "");

    /**
     * The column
     * <code>network.network_route_points_with_history.network_route_point_ext_id</code>.
     */
    public final TableField<NetworkRoutePointsWithHistoryRecord, String> NETWORK_ROUTE_POINT_EXT_ID = createField(DSL.name("network_route_point_ext_id"), SQLDataType.CLOB, this, "");

    /**
     * The column
     * <code>network.network_route_points_with_history.network_route_point_order</code>.
     */
    public final TableField<NetworkRoutePointsWithHistoryRecord, Integer> NETWORK_ROUTE_POINT_ORDER = createField(DSL.name("network_route_point_order"), SQLDataType.INTEGER, this, "");

    /**
     * The column
     * <code>network.network_route_points_with_history.network_route_point_sys_period</code>.
     */
    public final TableField<NetworkRoutePointsWithHistoryRecord, TimeRange> NETWORK_ROUTE_POINT_SYS_PERIOD = createField(DSL.name("network_route_point_sys_period"), DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\""), this, "", new TimeRangeBinding());

    private NetworkRoutePointsWithHistory(Name alias, Table<NetworkRoutePointsWithHistoryRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private NetworkRoutePointsWithHistory(Name alias, Table<NetworkRoutePointsWithHistoryRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view("""
         create view "network_route_points_with_history" as  SELECT network_route_points.network_route_point_id,
            network_route_points.network_route_direction_id,
            network_route_points.infrastructure_node,
            network_route_points.network_route_point_ext_id,
            network_route_points.network_route_point_order,
            network_route_points.network_route_point_sys_period
           FROM network.network_route_points
        UNION ALL
         SELECT network_route_points_history.network_route_point_id,
            network_route_points_history.network_route_direction_id,
            network_route_points_history.infrastructure_node,
            network_route_points_history.network_route_point_ext_id,
            network_route_points_history.network_route_point_order,
            network_route_points_history.network_route_point_sys_period
           FROM network.network_route_points_history;
        """), where);
    }

    /**
     * Create an aliased <code>network.network_route_points_with_history</code>
     * table reference
     */
    public NetworkRoutePointsWithHistory(String alias) {
        this(DSL.name(alias), NETWORK_ROUTE_POINTS_WITH_HISTORY);
    }

    /**
     * Create an aliased <code>network.network_route_points_with_history</code>
     * table reference
     */
    public NetworkRoutePointsWithHistory(Name alias) {
        this(alias, NETWORK_ROUTE_POINTS_WITH_HISTORY);
    }

    /**
     * Create a <code>network.network_route_points_with_history</code> table
     * reference
     */
    public NetworkRoutePointsWithHistory() {
        this(DSL.name("network_route_points_with_history"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Network.NETWORK;
    }

    @Override
    public NetworkRoutePointsWithHistory as(String alias) {
        return new NetworkRoutePointsWithHistory(DSL.name(alias), this);
    }

    @Override
    public NetworkRoutePointsWithHistory as(Name alias) {
        return new NetworkRoutePointsWithHistory(alias, this);
    }

    @Override
    public NetworkRoutePointsWithHistory as(Table<?> alias) {
        return new NetworkRoutePointsWithHistory(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutePointsWithHistory rename(String name) {
        return new NetworkRoutePointsWithHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutePointsWithHistory rename(Name name) {
        return new NetworkRoutePointsWithHistory(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutePointsWithHistory rename(Table<?> name) {
        return new NetworkRoutePointsWithHistory(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRoutePointsWithHistory where(Condition condition) {
        return new NetworkRoutePointsWithHistory(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRoutePointsWithHistory where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRoutePointsWithHistory where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRoutePointsWithHistory where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkRoutePointsWithHistory where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkRoutePointsWithHistory where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkRoutePointsWithHistory where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkRoutePointsWithHistory where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRoutePointsWithHistory whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRoutePointsWithHistory whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}

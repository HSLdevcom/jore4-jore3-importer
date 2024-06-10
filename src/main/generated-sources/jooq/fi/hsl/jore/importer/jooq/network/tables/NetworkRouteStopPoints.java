/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.network.Keys;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutePoints.NetworkRoutePointsPath;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRouteStopPointsRecord;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.InverseForeignKey;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Path;
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
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;
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
     * The column
     * <code>network.network_route_stop_points.network_route_point_id</code>.
     */
    public final TableField<NetworkRouteStopPointsRecord, UUID> NETWORK_ROUTE_POINT_ID = createField(DSL.name("network_route_point_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column
     * <code>network.network_route_stop_points.network_route_stop_point_ext_id</code>.
     */
    public final TableField<NetworkRouteStopPointsRecord, String> NETWORK_ROUTE_STOP_POINT_EXT_ID = createField(DSL.name("network_route_stop_point_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>network.network_route_stop_points.network_route_stop_point_order</code>.
     */
    public final TableField<NetworkRouteStopPointsRecord, Integer> NETWORK_ROUTE_STOP_POINT_ORDER = createField(DSL.name("network_route_stop_point_order"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column
     * <code>network.network_route_stop_points.network_route_stop_point_hastus_point</code>.
     */
    public final TableField<NetworkRouteStopPointsRecord, Boolean> NETWORK_ROUTE_STOP_POINT_HASTUS_POINT = createField(DSL.name("network_route_stop_point_hastus_point"), SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column
     * <code>network.network_route_stop_points.network_route_stop_point_timetable_column</code>.
     */
    public final TableField<NetworkRouteStopPointsRecord, Integer> NETWORK_ROUTE_STOP_POINT_TIMETABLE_COLUMN = createField(DSL.name("network_route_stop_point_timetable_column"), SQLDataType.INTEGER, this, "");

    /**
     * The column
     * <code>network.network_route_stop_points.network_route_stop_point_sys_period</code>.
     */
    public final TableField<NetworkRouteStopPointsRecord, TimeRange> NETWORK_ROUTE_STOP_POINT_SYS_PERIOD = createField(DSL.name("network_route_stop_point_sys_period"), DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\"").nullable(false).defaultValue(DSL.field(DSL.raw("tstzrange(CURRENT_TIMESTAMP, NULL::timestamp with time zone)"), org.jooq.impl.SQLDataType.OTHER)), this, "", new TimeRangeBinding());

    /**
     * The column
     * <code>network.network_route_stop_points.network_route_stop_point_via_point</code>.
     */
    public final TableField<NetworkRouteStopPointsRecord, Boolean> NETWORK_ROUTE_STOP_POINT_VIA_POINT = createField(DSL.name("network_route_stop_point_via_point"), SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column
     * <code>network.network_route_stop_points.network_route_stop_point_via_name</code>.
     */
    public final TableField<NetworkRouteStopPointsRecord, JSONB> NETWORK_ROUTE_STOP_POINT_VIA_NAME = createField(DSL.name("network_route_stop_point_via_name"), SQLDataType.JSONB, this, "");

    /**
     * The column
     * <code>network.network_route_stop_points.network_route_stop_point_regulated_timing_point_status</code>.
     */
    public final TableField<NetworkRouteStopPointsRecord, Integer> NETWORK_ROUTE_STOP_POINT_REGULATED_TIMING_POINT_STATUS = createField(DSL.name("network_route_stop_point_regulated_timing_point_status"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field(DSL.raw("'-9999'::integer"), SQLDataType.INTEGER)), this, "");

    private NetworkRouteStopPoints(Name alias, Table<NetworkRouteStopPointsRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private NetworkRouteStopPoints(Name alias, Table<NetworkRouteStopPointsRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>network.network_route_stop_points</code> table
     * reference
     */
    public NetworkRouteStopPoints(String alias) {
        this(DSL.name(alias), NETWORK_ROUTE_STOP_POINTS);
    }

    /**
     * Create an aliased <code>network.network_route_stop_points</code> table
     * reference
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

    public <O extends Record> NetworkRouteStopPoints(Table<O> path, ForeignKey<O, NetworkRouteStopPointsRecord> childPath, InverseForeignKey<O, NetworkRouteStopPointsRecord> parentPath) {
        super(path, childPath, parentPath, NETWORK_ROUTE_STOP_POINTS);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class NetworkRouteStopPointsPath extends NetworkRouteStopPoints implements Path<NetworkRouteStopPointsRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> NetworkRouteStopPointsPath(Table<O> path, ForeignKey<O, NetworkRouteStopPointsRecord> childPath, InverseForeignKey<O, NetworkRouteStopPointsRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private NetworkRouteStopPointsPath(Name alias, Table<NetworkRouteStopPointsRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public NetworkRouteStopPointsPath as(String alias) {
            return new NetworkRouteStopPointsPath(DSL.name(alias), this);
        }

        @Override
        public NetworkRouteStopPointsPath as(Name alias) {
            return new NetworkRouteStopPointsPath(alias, this);
        }

        @Override
        public NetworkRouteStopPointsPath as(Table<?> alias) {
            return new NetworkRouteStopPointsPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Network.NETWORK;
    }

    @Override
    public UniqueKey<NetworkRouteStopPointsRecord> getPrimaryKey() {
        return Keys.NETWORK_ROUTE_STOP_POINTS_PKEY;
    }

    @Override
    public List<ForeignKey<NetworkRouteStopPointsRecord, ?>> getReferences() {
        return Arrays.asList(Keys.NETWORK_ROUTE_STOP_POINTS__NETWORK_ROUTE_STOP_POINTS_NETWORK_ROUTE_POINT_ID_FKEY);
    }

    private transient NetworkRoutePointsPath _networkRoutePoints;

    /**
     * Get the implicit join path to the
     * <code>network.network_route_points</code> table.
     */
    public NetworkRoutePointsPath networkRoutePoints() {
        if (_networkRoutePoints == null)
            _networkRoutePoints = new NetworkRoutePointsPath(this, Keys.NETWORK_ROUTE_STOP_POINTS__NETWORK_ROUTE_STOP_POINTS_NETWORK_ROUTE_POINT_ID_FKEY, null);

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

    @Override
    public NetworkRouteStopPoints as(Table<?> alias) {
        return new NetworkRouteStopPoints(alias.getQualifiedName(), this);
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

    /**
     * Rename this table
     */
    @Override
    public NetworkRouteStopPoints rename(Table<?> name) {
        return new NetworkRouteStopPoints(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRouteStopPoints where(Condition condition) {
        return new NetworkRouteStopPoints(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRouteStopPoints where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRouteStopPoints where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRouteStopPoints where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkRouteStopPoints where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkRouteStopPoints where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkRouteStopPoints where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkRouteStopPoints where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRouteStopPoints whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRouteStopPoints whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}

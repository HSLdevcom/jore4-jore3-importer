/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.jooq.network.Keys;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRouteStopPointsStagingRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
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
public class NetworkRouteStopPointsStaging extends TableImpl<NetworkRouteStopPointsStagingRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>network.network_route_stop_points_staging</code>
     */
    public static final NetworkRouteStopPointsStaging NETWORK_ROUTE_STOP_POINTS_STAGING = new NetworkRouteStopPointsStaging();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkRouteStopPointsStagingRecord> getRecordType() {
        return NetworkRouteStopPointsStagingRecord.class;
    }

    /**
     * The column
     * <code>network.network_route_stop_points_staging.network_route_stop_point_ext_id</code>.
     */
    public final TableField<NetworkRouteStopPointsStagingRecord, String> NETWORK_ROUTE_STOP_POINT_EXT_ID = createField(DSL.name("network_route_stop_point_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>network.network_route_stop_points_staging.network_route_stop_point_order</code>.
     */
    public final TableField<NetworkRouteStopPointsStagingRecord, Integer> NETWORK_ROUTE_STOP_POINT_ORDER = createField(DSL.name("network_route_stop_point_order"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column
     * <code>network.network_route_stop_points_staging.network_route_stop_point_hastus_point</code>.
     */
    public final TableField<NetworkRouteStopPointsStagingRecord, Boolean> NETWORK_ROUTE_STOP_POINT_HASTUS_POINT = createField(DSL.name("network_route_stop_point_hastus_point"), SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column
     * <code>network.network_route_stop_points_staging.network_route_stop_point_timetable_column</code>.
     */
    public final TableField<NetworkRouteStopPointsStagingRecord, Integer> NETWORK_ROUTE_STOP_POINT_TIMETABLE_COLUMN = createField(DSL.name("network_route_stop_point_timetable_column"), SQLDataType.INTEGER, this, "");

    /**
     * The column
     * <code>network.network_route_stop_points_staging.network_route_stop_point_via_point</code>.
     */
    public final TableField<NetworkRouteStopPointsStagingRecord, Boolean> NETWORK_ROUTE_STOP_POINT_VIA_POINT = createField(DSL.name("network_route_stop_point_via_point"), SQLDataType.BOOLEAN.nullable(false), this, "");

    private NetworkRouteStopPointsStaging(Name alias, Table<NetworkRouteStopPointsStagingRecord> aliased) {
        this(alias, aliased, null);
    }

    private NetworkRouteStopPointsStaging(Name alias, Table<NetworkRouteStopPointsStagingRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>network.network_route_stop_points_staging</code>
     * table reference
     */
    public NetworkRouteStopPointsStaging(String alias) {
        this(DSL.name(alias), NETWORK_ROUTE_STOP_POINTS_STAGING);
    }

    /**
     * Create an aliased <code>network.network_route_stop_points_staging</code>
     * table reference
     */
    public NetworkRouteStopPointsStaging(Name alias) {
        this(alias, NETWORK_ROUTE_STOP_POINTS_STAGING);
    }

    /**
     * Create a <code>network.network_route_stop_points_staging</code> table
     * reference
     */
    public NetworkRouteStopPointsStaging() {
        this(DSL.name("network_route_stop_points_staging"), null);
    }

    public <O extends Record> NetworkRouteStopPointsStaging(Table<O> child, ForeignKey<O, NetworkRouteStopPointsStagingRecord> key) {
        super(child, key, NETWORK_ROUTE_STOP_POINTS_STAGING);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Network.NETWORK;
    }

    @Override
    public UniqueKey<NetworkRouteStopPointsStagingRecord> getPrimaryKey() {
        return Keys.NETWORK_ROUTE_STOP_POINTS_STAGING_PKEY;
    }

    @Override
    public NetworkRouteStopPointsStaging as(String alias) {
        return new NetworkRouteStopPointsStaging(DSL.name(alias), this);
    }

    @Override
    public NetworkRouteStopPointsStaging as(Name alias) {
        return new NetworkRouteStopPointsStaging(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRouteStopPointsStaging rename(String name) {
        return new NetworkRouteStopPointsStaging(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRouteStopPointsStaging rename(Name name) {
        return new NetworkRouteStopPointsStaging(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, Integer, Boolean, Integer, Boolean> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}

/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.jooq.network.Keys;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRoutesStagingRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
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
public class NetworkRoutesStaging extends TableImpl<NetworkRoutesStagingRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.network_routes_staging</code>
     */
    public static final NetworkRoutesStaging NETWORK_ROUTES_STAGING = new NetworkRoutesStaging();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkRoutesStagingRecord> getRecordType() {
        return NetworkRoutesStagingRecord.class;
    }

    /**
     * The column <code>network.network_routes_staging.network_route_ext_id</code>.
     */
    public final TableField<NetworkRoutesStagingRecord, String> NETWORK_ROUTE_EXT_ID = createField(DSL.name("network_route_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_routes_staging.network_line_ext_id</code>.
     */
    public final TableField<NetworkRoutesStagingRecord, String> NETWORK_LINE_EXT_ID = createField(DSL.name("network_line_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_routes_staging.network_route_number</code>.
     */
    public final TableField<NetworkRoutesStagingRecord, String> NETWORK_ROUTE_NUMBER = createField(DSL.name("network_route_number"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_routes_staging.network_route_name</code>.
     */
    public final TableField<NetworkRoutesStagingRecord, JSONB> NETWORK_ROUTE_NAME = createField(DSL.name("network_route_name"), SQLDataType.JSONB.nullable(false), this, "");

    private NetworkRoutesStaging(Name alias, Table<NetworkRoutesStagingRecord> aliased) {
        this(alias, aliased, null);
    }

    private NetworkRoutesStaging(Name alias, Table<NetworkRoutesStagingRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>network.network_routes_staging</code> table reference
     */
    public NetworkRoutesStaging(String alias) {
        this(DSL.name(alias), NETWORK_ROUTES_STAGING);
    }

    /**
     * Create an aliased <code>network.network_routes_staging</code> table reference
     */
    public NetworkRoutesStaging(Name alias) {
        this(alias, NETWORK_ROUTES_STAGING);
    }

    /**
     * Create a <code>network.network_routes_staging</code> table reference
     */
    public NetworkRoutesStaging() {
        this(DSL.name("network_routes_staging"), null);
    }

    public <O extends Record> NetworkRoutesStaging(Table<O> child, ForeignKey<O, NetworkRoutesStagingRecord> key) {
        super(child, key, NETWORK_ROUTES_STAGING);
    }

    @Override
    public Schema getSchema() {
        return Network.NETWORK;
    }

    @Override
    public UniqueKey<NetworkRoutesStagingRecord> getPrimaryKey() {
        return Keys.NETWORK_ROUTES_STAGING_PKEY;
    }

    @Override
    public List<UniqueKey<NetworkRoutesStagingRecord>> getKeys() {
        return Arrays.<UniqueKey<NetworkRoutesStagingRecord>>asList(Keys.NETWORK_ROUTES_STAGING_PKEY);
    }

    @Override
    public NetworkRoutesStaging as(String alias) {
        return new NetworkRoutesStaging(DSL.name(alias), this);
    }

    @Override
    public NetworkRoutesStaging as(Name alias) {
        return new NetworkRoutesStaging(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutesStaging rename(String name) {
        return new NetworkRoutesStaging(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutesStaging rename(Name name) {
        return new NetworkRoutesStaging(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, String, JSONB> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}

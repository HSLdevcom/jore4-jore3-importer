/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.network.Keys;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRoutesRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row8;
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
public class NetworkRoutes extends TableImpl<NetworkRoutesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.network_routes</code>
     */
    public static final NetworkRoutes NETWORK_ROUTES = new NetworkRoutes();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkRoutesRecord> getRecordType() {
        return NetworkRoutesRecord.class;
    }

    /**
     * The column <code>network.network_routes.network_route_id</code>.
     */
    public final TableField<NetworkRoutesRecord, UUID> NETWORK_ROUTE_ID = createField(DSL.name("network_route_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("gen_random_uuid()", SQLDataType.UUID)), this, "");

    /**
     * The column <code>network.network_routes.network_line_id</code>.
     */
    public final TableField<NetworkRoutesRecord, UUID> NETWORK_LINE_ID = createField(DSL.name("network_line_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>network.network_routes.network_route_ext_id</code>.
     */
    public final TableField<NetworkRoutesRecord, String> NETWORK_ROUTE_EXT_ID = createField(DSL.name("network_route_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_routes.network_route_number</code>.
     */
    public final TableField<NetworkRoutesRecord, String> NETWORK_ROUTE_NUMBER = createField(DSL.name("network_route_number"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_routes.network_route_name</code>.
     */
    public final TableField<NetworkRoutesRecord, JSONB> NETWORK_ROUTE_NAME = createField(DSL.name("network_route_name"), SQLDataType.JSONB.nullable(false), this, "");

    /**
     * The column <code>network.network_routes.network_route_sys_period</code>.
     */
    public final TableField<NetworkRoutesRecord, TimeRange> NETWORK_ROUTE_SYS_PERIOD = createField(DSL.name("network_route_sys_period"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\"").nullable(false).defaultValue(DSL.field("tstzrange(CURRENT_TIMESTAMP, NULL::timestamp with time zone)", org.jooq.impl.SQLDataType.OTHER)), this, "", new TimeRangeBinding());

    /**
     * The column <code>network.network_routes.network_route_hidden_variant</code>.
     */
    public final TableField<NetworkRoutesRecord, Short> NETWORK_ROUTE_HIDDEN_VARIANT = createField(DSL.name("network_route_hidden_variant"), SQLDataType.SMALLINT, this, "");

    /**
     * The column <code>network.network_routes.network_route_legacy_hsl_municipality_code</code>.
     */
    public final TableField<NetworkRoutesRecord, String> NETWORK_ROUTE_LEGACY_HSL_MUNICIPALITY_CODE = createField(DSL.name("network_route_legacy_hsl_municipality_code"), SQLDataType.CLOB, this, "");

    private NetworkRoutes(Name alias, Table<NetworkRoutesRecord> aliased) {
        this(alias, aliased, null);
    }

    private NetworkRoutes(Name alias, Table<NetworkRoutesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>network.network_routes</code> table reference
     */
    public NetworkRoutes(String alias) {
        this(DSL.name(alias), NETWORK_ROUTES);
    }

    /**
     * Create an aliased <code>network.network_routes</code> table reference
     */
    public NetworkRoutes(Name alias) {
        this(alias, NETWORK_ROUTES);
    }

    /**
     * Create a <code>network.network_routes</code> table reference
     */
    public NetworkRoutes() {
        this(DSL.name("network_routes"), null);
    }

    public <O extends Record> NetworkRoutes(Table<O> child, ForeignKey<O, NetworkRoutesRecord> key) {
        super(child, key, NETWORK_ROUTES);
    }

    @Override
    public Schema getSchema() {
        return Network.NETWORK;
    }

    @Override
    public UniqueKey<NetworkRoutesRecord> getPrimaryKey() {
        return Keys.NETWORK_ROUTES_PKEY;
    }

    @Override
    public List<UniqueKey<NetworkRoutesRecord>> getKeys() {
        return Arrays.<UniqueKey<NetworkRoutesRecord>>asList(Keys.NETWORK_ROUTES_PKEY);
    }

    @Override
    public List<ForeignKey<NetworkRoutesRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<NetworkRoutesRecord, ?>>asList(Keys.NETWORK_ROUTES__NETWORK_ROUTES_NETWORK_LINE_ID_FKEY);
    }

    private transient NetworkLines _networkLines;

    public NetworkLines networkLines() {
        if (_networkLines == null)
            _networkLines = new NetworkLines(this, Keys.NETWORK_ROUTES__NETWORK_ROUTES_NETWORK_LINE_ID_FKEY);

        return _networkLines;
    }

    @Override
    public NetworkRoutes as(String alias) {
        return new NetworkRoutes(DSL.name(alias), this);
    }

    @Override
    public NetworkRoutes as(Name alias) {
        return new NetworkRoutes(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutes rename(String name) {
        return new NetworkRoutes(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutes rename(Name name) {
        return new NetworkRoutes(name, null);
    }

    // -------------------------------------------------------------------------
    // Row8 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row8<UUID, UUID, String, String, JSONB, TimeRange, Short, String> fieldsRow() {
        return (Row8) super.fieldsRow();
    }
}

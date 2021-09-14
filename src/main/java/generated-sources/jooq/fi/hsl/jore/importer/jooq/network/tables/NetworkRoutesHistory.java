/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRoutesHistoryRecord;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.JSONB;
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
public class NetworkRoutesHistory extends TableImpl<NetworkRoutesHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.network_routes_history</code>
     */
    public static final NetworkRoutesHistory NETWORK_ROUTES_HISTORY = new NetworkRoutesHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkRoutesHistoryRecord> getRecordType() {
        return NetworkRoutesHistoryRecord.class;
    }

    /**
     * The column <code>network.network_routes_history.network_route_id</code>.
     */
    public final TableField<NetworkRoutesHistoryRecord, UUID> NETWORK_ROUTE_ID = createField(DSL.name("network_route_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>network.network_routes_history.network_line_id</code>.
     */
    public final TableField<NetworkRoutesHistoryRecord, UUID> NETWORK_LINE_ID = createField(DSL.name("network_line_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>network.network_routes_history.network_route_ext_id</code>.
     */
    public final TableField<NetworkRoutesHistoryRecord, String> NETWORK_ROUTE_EXT_ID = createField(DSL.name("network_route_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_routes_history.network_route_number</code>.
     */
    public final TableField<NetworkRoutesHistoryRecord, String> NETWORK_ROUTE_NUMBER = createField(DSL.name("network_route_number"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_routes_history.network_route_name</code>.
     */
    public final TableField<NetworkRoutesHistoryRecord, JSONB> NETWORK_ROUTE_NAME = createField(DSL.name("network_route_name"), SQLDataType.JSONB.nullable(false), this, "");

    /**
     * The column <code>network.network_routes_history.network_route_sys_period</code>.
     */
    public final TableField<NetworkRoutesHistoryRecord, TimeRange> NETWORK_ROUTE_SYS_PERIOD = createField(DSL.name("network_route_sys_period"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\"").nullable(false), this, "", new TimeRangeBinding());

    private NetworkRoutesHistory(Name alias, Table<NetworkRoutesHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private NetworkRoutesHistory(Name alias, Table<NetworkRoutesHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>network.network_routes_history</code> table reference
     */
    public NetworkRoutesHistory(String alias) {
        this(DSL.name(alias), NETWORK_ROUTES_HISTORY);
    }

    /**
     * Create an aliased <code>network.network_routes_history</code> table reference
     */
    public NetworkRoutesHistory(Name alias) {
        this(alias, NETWORK_ROUTES_HISTORY);
    }

    /**
     * Create a <code>network.network_routes_history</code> table reference
     */
    public NetworkRoutesHistory() {
        this(DSL.name("network_routes_history"), null);
    }

    public <O extends Record> NetworkRoutesHistory(Table<O> child, ForeignKey<O, NetworkRoutesHistoryRecord> key) {
        super(child, key, NETWORK_ROUTES_HISTORY);
    }

    @Override
    public Schema getSchema() {
        return Network.NETWORK;
    }

    @Override
    public NetworkRoutesHistory as(String alias) {
        return new NetworkRoutesHistory(DSL.name(alias), this);
    }

    @Override
    public NetworkRoutesHistory as(Name alias) {
        return new NetworkRoutesHistory(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutesHistory rename(String name) {
        return new NetworkRoutesHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutesHistory rename(Name name) {
        return new NetworkRoutesHistory(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<UUID, UUID, String, String, JSONB, TimeRange> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}

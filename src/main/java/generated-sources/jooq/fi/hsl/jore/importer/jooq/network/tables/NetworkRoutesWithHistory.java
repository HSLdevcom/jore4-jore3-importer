/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRoutesWithHistoryRecord;

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
public class NetworkRoutesWithHistory extends TableImpl<NetworkRoutesWithHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.network_routes_with_history</code>
     */
    public static final NetworkRoutesWithHistory NETWORK_ROUTES_WITH_HISTORY = new NetworkRoutesWithHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkRoutesWithHistoryRecord> getRecordType() {
        return NetworkRoutesWithHistoryRecord.class;
    }

    /**
     * The column <code>network.network_routes_with_history.network_route_id</code>.
     */
    public final TableField<NetworkRoutesWithHistoryRecord, UUID> NETWORK_ROUTE_ID = createField(DSL.name("network_route_id"), SQLDataType.UUID, this, "");

    /**
     * The column <code>network.network_routes_with_history.network_line_id</code>.
     */
    public final TableField<NetworkRoutesWithHistoryRecord, UUID> NETWORK_LINE_ID = createField(DSL.name("network_line_id"), SQLDataType.UUID, this, "");

    /**
     * The column <code>network.network_routes_with_history.network_route_ext_id</code>.
     */
    public final TableField<NetworkRoutesWithHistoryRecord, String> NETWORK_ROUTE_EXT_ID = createField(DSL.name("network_route_ext_id"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>network.network_routes_with_history.network_route_number</code>.
     */
    public final TableField<NetworkRoutesWithHistoryRecord, String> NETWORK_ROUTE_NUMBER = createField(DSL.name("network_route_number"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>network.network_routes_with_history.network_route_name</code>.
     */
    public final TableField<NetworkRoutesWithHistoryRecord, JSONB> NETWORK_ROUTE_NAME = createField(DSL.name("network_route_name"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>network.network_routes_with_history.network_route_sys_period</code>.
     */
    public final TableField<NetworkRoutesWithHistoryRecord, TimeRange> NETWORK_ROUTE_SYS_PERIOD = createField(DSL.name("network_route_sys_period"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\""), this, "", new TimeRangeBinding());

    private NetworkRoutesWithHistory(Name alias, Table<NetworkRoutesWithHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private NetworkRoutesWithHistory(Name alias, Table<NetworkRoutesWithHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view("create view \"network_routes_with_history\" as  SELECT network_routes.network_route_id,\n    network_routes.network_line_id,\n    network_routes.network_route_ext_id,\n    network_routes.network_route_number,\n    network_routes.network_route_name,\n    network_routes.network_route_sys_period\n   FROM network.network_routes\nUNION ALL\n SELECT network_routes_history.network_route_id,\n    network_routes_history.network_line_id,\n    network_routes_history.network_route_ext_id,\n    network_routes_history.network_route_number,\n    network_routes_history.network_route_name,\n    network_routes_history.network_route_sys_period\n   FROM network.network_routes_history;"));
    }

    /**
     * Create an aliased <code>network.network_routes_with_history</code> table reference
     */
    public NetworkRoutesWithHistory(String alias) {
        this(DSL.name(alias), NETWORK_ROUTES_WITH_HISTORY);
    }

    /**
     * Create an aliased <code>network.network_routes_with_history</code> table reference
     */
    public NetworkRoutesWithHistory(Name alias) {
        this(alias, NETWORK_ROUTES_WITH_HISTORY);
    }

    /**
     * Create a <code>network.network_routes_with_history</code> table reference
     */
    public NetworkRoutesWithHistory() {
        this(DSL.name("network_routes_with_history"), null);
    }

    public <O extends Record> NetworkRoutesWithHistory(Table<O> child, ForeignKey<O, NetworkRoutesWithHistoryRecord> key) {
        super(child, key, NETWORK_ROUTES_WITH_HISTORY);
    }

    @Override
    public Schema getSchema() {
        return Network.NETWORK;
    }

    @Override
    public NetworkRoutesWithHistory as(String alias) {
        return new NetworkRoutesWithHistory(DSL.name(alias), this);
    }

    @Override
    public NetworkRoutesWithHistory as(Name alias) {
        return new NetworkRoutesWithHistory(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutesWithHistory rename(String name) {
        return new NetworkRoutesWithHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutesWithHistory rename(Name name) {
        return new NetworkRoutesWithHistory(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<UUID, UUID, String, String, JSONB, TimeRange> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}
/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkPlacesWithHistoryRecord;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
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
public class NetworkPlacesWithHistory extends TableImpl<NetworkPlacesWithHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.network_places_with_history</code>
     */
    public static final NetworkPlacesWithHistory NETWORK_PLACES_WITH_HISTORY = new NetworkPlacesWithHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkPlacesWithHistoryRecord> getRecordType() {
        return NetworkPlacesWithHistoryRecord.class;
    }

    /**
     * The column <code>network.network_places_with_history.network_place_id</code>.
     */
    public final TableField<NetworkPlacesWithHistoryRecord, UUID> NETWORK_PLACE_ID = createField(DSL.name("network_place_id"), SQLDataType.UUID, this, "");

    /**
     * The column <code>network.network_places_with_history.network_place_ext_id</code>.
     */
    public final TableField<NetworkPlacesWithHistoryRecord, String> NETWORK_PLACE_EXT_ID = createField(DSL.name("network_place_ext_id"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>network.network_places_with_history.network_place_name</code>.
     */
    public final TableField<NetworkPlacesWithHistoryRecord, String> NETWORK_PLACE_NAME = createField(DSL.name("network_place_name"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>network.network_places_with_history.network_place_sys_period</code>.
     */
    public final TableField<NetworkPlacesWithHistoryRecord, TimeRange> NETWORK_PLACE_SYS_PERIOD = createField(DSL.name("network_place_sys_period"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\""), this, "", new TimeRangeBinding());

    private NetworkPlacesWithHistory(Name alias, Table<NetworkPlacesWithHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private NetworkPlacesWithHistory(Name alias, Table<NetworkPlacesWithHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view("create view \"network_places_with_history\" as  SELECT network_places.network_place_id,\n    network_places.network_place_ext_id,\n    network_places.network_place_name,\n    network_places.network_place_sys_period\n   FROM network.network_places\nUNION ALL\n SELECT network_places_history.network_place_id,\n    network_places_history.network_place_ext_id,\n    network_places_history.network_place_name,\n    network_places_history.network_place_sys_period\n   FROM network.network_places_history;"));
    }

    /**
     * Create an aliased <code>network.network_places_with_history</code> table reference
     */
    public NetworkPlacesWithHistory(String alias) {
        this(DSL.name(alias), NETWORK_PLACES_WITH_HISTORY);
    }

    /**
     * Create an aliased <code>network.network_places_with_history</code> table reference
     */
    public NetworkPlacesWithHistory(Name alias) {
        this(alias, NETWORK_PLACES_WITH_HISTORY);
    }

    /**
     * Create a <code>network.network_places_with_history</code> table reference
     */
    public NetworkPlacesWithHistory() {
        this(DSL.name("network_places_with_history"), null);
    }

    public <O extends Record> NetworkPlacesWithHistory(Table<O> child, ForeignKey<O, NetworkPlacesWithHistoryRecord> key) {
        super(child, key, NETWORK_PLACES_WITH_HISTORY);
    }

    @Override
    public Schema getSchema() {
        return Network.NETWORK;
    }

    @Override
    public NetworkPlacesWithHistory as(String alias) {
        return new NetworkPlacesWithHistory(DSL.name(alias), this);
    }

    @Override
    public NetworkPlacesWithHistory as(Name alias) {
        return new NetworkPlacesWithHistory(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkPlacesWithHistory rename(String name) {
        return new NetworkPlacesWithHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkPlacesWithHistory rename(Name name) {
        return new NetworkPlacesWithHistory(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<UUID, String, String, TimeRange> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}

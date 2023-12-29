/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRangeBinding;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkLineHeadersWithHistoryRecord;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row10;
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
public class NetworkLineHeadersWithHistory extends TableImpl<NetworkLineHeadersWithHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.network_line_headers_with_history</code>
     */
    public static final NetworkLineHeadersWithHistory NETWORK_LINE_HEADERS_WITH_HISTORY = new NetworkLineHeadersWithHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkLineHeadersWithHistoryRecord> getRecordType() {
        return NetworkLineHeadersWithHistoryRecord.class;
    }

    /**
     * The column <code>network.network_line_headers_with_history.network_line_header_id</code>.
     */
    public final TableField<NetworkLineHeadersWithHistoryRecord, UUID> NETWORK_LINE_HEADER_ID = createField(DSL.name("network_line_header_id"), SQLDataType.UUID, this, "");

    /**
     * The column <code>network.network_line_headers_with_history.network_line_id</code>.
     */
    public final TableField<NetworkLineHeadersWithHistoryRecord, UUID> NETWORK_LINE_ID = createField(DSL.name("network_line_id"), SQLDataType.UUID, this, "");

    /**
     * The column <code>network.network_line_headers_with_history.network_line_header_ext_id</code>.
     */
    public final TableField<NetworkLineHeadersWithHistoryRecord, String> NETWORK_LINE_HEADER_EXT_ID = createField(DSL.name("network_line_header_ext_id"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>network.network_line_headers_with_history.network_line_header_name</code>.
     */
    public final TableField<NetworkLineHeadersWithHistoryRecord, JSONB> NETWORK_LINE_HEADER_NAME = createField(DSL.name("network_line_header_name"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>network.network_line_headers_with_history.network_line_header_name_short</code>.
     */
    public final TableField<NetworkLineHeadersWithHistoryRecord, JSONB> NETWORK_LINE_HEADER_NAME_SHORT = createField(DSL.name("network_line_header_name_short"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>network.network_line_headers_with_history.network_line_header_origin_1</code>.
     */
    public final TableField<NetworkLineHeadersWithHistoryRecord, JSONB> NETWORK_LINE_HEADER_ORIGIN_1 = createField(DSL.name("network_line_header_origin_1"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>network.network_line_headers_with_history.network_line_header_origin_2</code>.
     */
    public final TableField<NetworkLineHeadersWithHistoryRecord, JSONB> NETWORK_LINE_HEADER_ORIGIN_2 = createField(DSL.name("network_line_header_origin_2"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>network.network_line_headers_with_history.network_line_header_valid_date_range</code>.
     */
    public final TableField<NetworkLineHeadersWithHistoryRecord, DateRange> NETWORK_LINE_HEADER_VALID_DATE_RANGE = createField(DSL.name("network_line_header_valid_date_range"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"daterange\""), this, "", new DateRangeBinding());

    /**
     * The column <code>network.network_line_headers_with_history.network_line_header_sys_period</code>.
     */
    public final TableField<NetworkLineHeadersWithHistoryRecord, TimeRange> NETWORK_LINE_HEADER_SYS_PERIOD = createField(DSL.name("network_line_header_sys_period"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\""), this, "", new TimeRangeBinding());

    /**
     * The column <code>network.network_line_headers_with_history.jore4_line_id</code>.
     */
    public final TableField<NetworkLineHeadersWithHistoryRecord, UUID> JORE4_LINE_ID = createField(DSL.name("jore4_line_id"), SQLDataType.UUID, this, "");

    private NetworkLineHeadersWithHistory(Name alias, Table<NetworkLineHeadersWithHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private NetworkLineHeadersWithHistory(Name alias, Table<NetworkLineHeadersWithHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view("create view \"network_line_headers_with_history\" as  SELECT network_line_headers.network_line_header_id,\n    network_line_headers.network_line_id,\n    network_line_headers.network_line_header_ext_id,\n    network_line_headers.network_line_header_name,\n    network_line_headers.network_line_header_name_short,\n    network_line_headers.network_line_header_origin_1,\n    network_line_headers.network_line_header_origin_2,\n    network_line_headers.network_line_header_valid_date_range,\n    network_line_headers.network_line_header_sys_period,\n    network_line_headers.jore4_line_id\n   FROM network.network_line_headers\nUNION ALL\n SELECT network_line_headers_history.network_line_header_id,\n    network_line_headers_history.network_line_id,\n    network_line_headers_history.network_line_header_ext_id,\n    network_line_headers_history.network_line_header_name,\n    network_line_headers_history.network_line_header_name_short,\n    network_line_headers_history.network_line_header_origin_1,\n    network_line_headers_history.network_line_header_origin_2,\n    network_line_headers_history.network_line_header_valid_date_range,\n    network_line_headers_history.network_line_header_sys_period,\n    network_line_headers_history.jore4_line_id\n   FROM network.network_line_headers_history;"));
    }

    /**
     * Create an aliased <code>network.network_line_headers_with_history</code> table reference
     */
    public NetworkLineHeadersWithHistory(String alias) {
        this(DSL.name(alias), NETWORK_LINE_HEADERS_WITH_HISTORY);
    }

    /**
     * Create an aliased <code>network.network_line_headers_with_history</code> table reference
     */
    public NetworkLineHeadersWithHistory(Name alias) {
        this(alias, NETWORK_LINE_HEADERS_WITH_HISTORY);
    }

    /**
     * Create a <code>network.network_line_headers_with_history</code> table reference
     */
    public NetworkLineHeadersWithHistory() {
        this(DSL.name("network_line_headers_with_history"), null);
    }

    public <O extends Record> NetworkLineHeadersWithHistory(Table<O> child, ForeignKey<O, NetworkLineHeadersWithHistoryRecord> key) {
        super(child, key, NETWORK_LINE_HEADERS_WITH_HISTORY);
    }

    @Override
    public Schema getSchema() {
        return Network.NETWORK;
    }

    @Override
    public NetworkLineHeadersWithHistory as(String alias) {
        return new NetworkLineHeadersWithHistory(DSL.name(alias), this);
    }

    @Override
    public NetworkLineHeadersWithHistory as(Name alias) {
        return new NetworkLineHeadersWithHistory(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkLineHeadersWithHistory rename(String name) {
        return new NetworkLineHeadersWithHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkLineHeadersWithHistory rename(Name name) {
        return new NetworkLineHeadersWithHistory(name, null);
    }

    // -------------------------------------------------------------------------
    // Row10 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row10<UUID, UUID, String, JSONB, JSONB, JSONB, JSONB, DateRange, TimeRange, UUID> fieldsRow() {
        return (Row10) super.fieldsRow();
    }
}

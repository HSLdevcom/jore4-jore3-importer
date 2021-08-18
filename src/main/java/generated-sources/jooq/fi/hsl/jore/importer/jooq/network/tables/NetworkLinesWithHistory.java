/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkLinesWithHistoryRecord;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
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
public class NetworkLinesWithHistory extends TableImpl<NetworkLinesWithHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.network_lines_with_history</code>
     */
    public static final NetworkLinesWithHistory NETWORK_LINES_WITH_HISTORY = new NetworkLinesWithHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkLinesWithHistoryRecord> getRecordType() {
        return NetworkLinesWithHistoryRecord.class;
    }

    /**
     * The column <code>network.network_lines_with_history.network_line_id</code>.
     */
    public final TableField<NetworkLinesWithHistoryRecord, UUID> NETWORK_LINE_ID = createField(DSL.name("network_line_id"), SQLDataType.UUID, this, "");

    /**
     * The column <code>network.network_lines_with_history.network_line_ext_id</code>.
     */
    public final TableField<NetworkLinesWithHistoryRecord, String> NETWORK_LINE_EXT_ID = createField(DSL.name("network_line_ext_id"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>network.network_lines_with_history.network_line_number</code>.
     */
    public final TableField<NetworkLinesWithHistoryRecord, String> NETWORK_LINE_NUMBER = createField(DSL.name("network_line_number"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>network.network_lines_with_history.infrastructure_network_type</code>.
     */
    public final TableField<NetworkLinesWithHistoryRecord, String> INFRASTRUCTURE_NETWORK_TYPE = createField(DSL.name("infrastructure_network_type"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>network.network_lines_with_history.network_line_sys_period</code>.
     */
    public final TableField<NetworkLinesWithHistoryRecord, TimeRange> NETWORK_LINE_SYS_PERIOD = createField(DSL.name("network_line_sys_period"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\""), this, "", new TimeRangeBinding());

    private NetworkLinesWithHistory(Name alias, Table<NetworkLinesWithHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private NetworkLinesWithHistory(Name alias, Table<NetworkLinesWithHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view("create view \"network_lines_with_history\" as  SELECT network_lines.network_line_id,\n    network_lines.network_line_ext_id,\n    network_lines.network_line_number,\n    network_lines.infrastructure_network_type,\n    network_lines.network_line_sys_period\n   FROM network.network_lines\nUNION ALL\n SELECT network_lines_history.network_line_id,\n    network_lines_history.network_line_ext_id,\n    network_lines_history.network_line_number,\n    network_lines_history.infrastructure_network_type,\n    network_lines_history.network_line_sys_period\n   FROM network.network_lines_history;"));
    }

    /**
     * Create an aliased <code>network.network_lines_with_history</code> table reference
     */
    public NetworkLinesWithHistory(String alias) {
        this(DSL.name(alias), NETWORK_LINES_WITH_HISTORY);
    }

    /**
     * Create an aliased <code>network.network_lines_with_history</code> table reference
     */
    public NetworkLinesWithHistory(Name alias) {
        this(alias, NETWORK_LINES_WITH_HISTORY);
    }

    /**
     * Create a <code>network.network_lines_with_history</code> table reference
     */
    public NetworkLinesWithHistory() {
        this(DSL.name("network_lines_with_history"), null);
    }

    public <O extends Record> NetworkLinesWithHistory(Table<O> child, ForeignKey<O, NetworkLinesWithHistoryRecord> key) {
        super(child, key, NETWORK_LINES_WITH_HISTORY);
    }

    @Override
    public Schema getSchema() {
        return Network.NETWORK;
    }

    @Override
    public NetworkLinesWithHistory as(String alias) {
        return new NetworkLinesWithHistory(DSL.name(alias), this);
    }

    @Override
    public NetworkLinesWithHistory as(Name alias) {
        return new NetworkLinesWithHistory(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkLinesWithHistory rename(String name) {
        return new NetworkLinesWithHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkLinesWithHistory rename(Name name) {
        return new NetworkLinesWithHistory(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<UUID, String, String, String, TimeRange> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
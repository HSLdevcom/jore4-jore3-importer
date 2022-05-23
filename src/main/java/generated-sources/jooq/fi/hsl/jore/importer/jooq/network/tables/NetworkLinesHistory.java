/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkLinesHistoryRecord;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row7;
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
public class NetworkLinesHistory extends TableImpl<NetworkLinesHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.network_lines_history</code>
     */
    public static final NetworkLinesHistory NETWORK_LINES_HISTORY = new NetworkLinesHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkLinesHistoryRecord> getRecordType() {
        return NetworkLinesHistoryRecord.class;
    }

    /**
     * The column <code>network.network_lines_history.network_line_id</code>.
     */
    public final TableField<NetworkLinesHistoryRecord, UUID> NETWORK_LINE_ID = createField(DSL.name("network_line_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column
     * <code>network.network_lines_history.network_line_ext_id</code>.
     */
    public final TableField<NetworkLinesHistoryRecord, String> NETWORK_LINE_EXT_ID = createField(DSL.name("network_line_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>network.network_lines_history.network_line_number</code>.
     */
    public final TableField<NetworkLinesHistoryRecord, String> NETWORK_LINE_NUMBER = createField(DSL.name("network_line_number"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>network.network_lines_history.infrastructure_network_type</code>.
     */
    public final TableField<NetworkLinesHistoryRecord, String> INFRASTRUCTURE_NETWORK_TYPE = createField(DSL.name("infrastructure_network_type"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>network.network_lines_history.network_line_sys_period</code>.
     */
    public final TableField<NetworkLinesHistoryRecord, TimeRange> NETWORK_LINE_SYS_PERIOD = createField(DSL.name("network_line_sys_period"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\"").nullable(false), this, "", new TimeRangeBinding());

    /**
     * The column
     * <code>network.network_lines_history.network_line_transmodel_id</code>.
     */
    public final TableField<NetworkLinesHistoryRecord, UUID> NETWORK_LINE_TRANSMODEL_ID = createField(DSL.name("network_line_transmodel_id"), SQLDataType.UUID, this, "");

    /**
     * The column
     * <code>network.network_lines_history.network_line_type_of_line</code>.
     */
    public final TableField<NetworkLinesHistoryRecord, String> NETWORK_LINE_TYPE_OF_LINE = createField(DSL.name("network_line_type_of_line"), SQLDataType.CLOB.nullable(false), this, "");

    private NetworkLinesHistory(Name alias, Table<NetworkLinesHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private NetworkLinesHistory(Name alias, Table<NetworkLinesHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>network.network_lines_history</code> table
     * reference
     */
    public NetworkLinesHistory(String alias) {
        this(DSL.name(alias), NETWORK_LINES_HISTORY);
    }

    /**
     * Create an aliased <code>network.network_lines_history</code> table
     * reference
     */
    public NetworkLinesHistory(Name alias) {
        this(alias, NETWORK_LINES_HISTORY);
    }

    /**
     * Create a <code>network.network_lines_history</code> table reference
     */
    public NetworkLinesHistory() {
        this(DSL.name("network_lines_history"), null);
    }

    public <O extends Record> NetworkLinesHistory(Table<O> child, ForeignKey<O, NetworkLinesHistoryRecord> key) {
        super(child, key, NETWORK_LINES_HISTORY);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Network.NETWORK;
    }

    @Override
    public NetworkLinesHistory as(String alias) {
        return new NetworkLinesHistory(DSL.name(alias), this);
    }

    @Override
    public NetworkLinesHistory as(Name alias) {
        return new NetworkLinesHistory(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkLinesHistory rename(String name) {
        return new NetworkLinesHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkLinesHistory rename(Name name) {
        return new NetworkLinesHistory(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<UUID, String, String, String, TimeRange, UUID, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}

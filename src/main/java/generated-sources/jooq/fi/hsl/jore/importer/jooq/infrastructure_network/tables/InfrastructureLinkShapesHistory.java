/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables;


import fi.hsl.jore.importer.config.jooq.converter.geometry.LineStringBinding;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.infrastructure_network.InfrastructureNetwork;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureLinkShapesHistoryRecord;

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
import org.locationtech.jts.geom.LineString;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureLinkShapesHistory extends TableImpl<InfrastructureLinkShapesHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>infrastructure_network.infrastructure_link_shapes_history</code>
     */
    public static final InfrastructureLinkShapesHistory INFRASTRUCTURE_LINK_SHAPES_HISTORY = new InfrastructureLinkShapesHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InfrastructureLinkShapesHistoryRecord> getRecordType() {
        return InfrastructureLinkShapesHistoryRecord.class;
    }

    /**
     * The column <code>infrastructure_network.infrastructure_link_shapes_history.infrastructure_link_shape_id</code>.
     */
    public final TableField<InfrastructureLinkShapesHistoryRecord, UUID> INFRASTRUCTURE_LINK_SHAPE_ID = createField(DSL.name("infrastructure_link_shape_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>infrastructure_network.infrastructure_link_shapes_history.infrastructure_link_ext_id</code>.
     */
    public final TableField<InfrastructureLinkShapesHistoryRecord, String> INFRASTRUCTURE_LINK_EXT_ID = createField(DSL.name("infrastructure_link_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>infrastructure_network.infrastructure_link_shapes_history.infrastructure_link_id</code>.
     */
    public final TableField<InfrastructureLinkShapesHistoryRecord, UUID> INFRASTRUCTURE_LINK_ID = createField(DSL.name("infrastructure_link_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>infrastructure_network.infrastructure_link_shapes_history.infrastructure_link_shape</code>.
     */
    public final TableField<InfrastructureLinkShapesHistoryRecord, LineString> INFRASTRUCTURE_LINK_SHAPE = createField(DSL.name("infrastructure_link_shape"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\"").nullable(false), this, "", new LineStringBinding());

    /**
     * The column <code>infrastructure_network.infrastructure_link_shapes_history.infrastructure_link_shape_sys_period</code>.
     */
    public final TableField<InfrastructureLinkShapesHistoryRecord, TimeRange> INFRASTRUCTURE_LINK_SHAPE_SYS_PERIOD = createField(DSL.name("infrastructure_link_shape_sys_period"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\"").nullable(false), this, "", new TimeRangeBinding());

    private InfrastructureLinkShapesHistory(Name alias, Table<InfrastructureLinkShapesHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private InfrastructureLinkShapesHistory(Name alias, Table<InfrastructureLinkShapesHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>infrastructure_network.infrastructure_link_shapes_history</code> table reference
     */
    public InfrastructureLinkShapesHistory(String alias) {
        this(DSL.name(alias), INFRASTRUCTURE_LINK_SHAPES_HISTORY);
    }

    /**
     * Create an aliased <code>infrastructure_network.infrastructure_link_shapes_history</code> table reference
     */
    public InfrastructureLinkShapesHistory(Name alias) {
        this(alias, INFRASTRUCTURE_LINK_SHAPES_HISTORY);
    }

    /**
     * Create a <code>infrastructure_network.infrastructure_link_shapes_history</code> table reference
     */
    public InfrastructureLinkShapesHistory() {
        this(DSL.name("infrastructure_link_shapes_history"), null);
    }

    public <O extends Record> InfrastructureLinkShapesHistory(Table<O> child, ForeignKey<O, InfrastructureLinkShapesHistoryRecord> key) {
        super(child, key, INFRASTRUCTURE_LINK_SHAPES_HISTORY);
    }

    @Override
    public Schema getSchema() {
        return InfrastructureNetwork.INFRASTRUCTURE_NETWORK;
    }

    @Override
    public InfrastructureLinkShapesHistory as(String alias) {
        return new InfrastructureLinkShapesHistory(DSL.name(alias), this);
    }

    @Override
    public InfrastructureLinkShapesHistory as(Name alias) {
        return new InfrastructureLinkShapesHistory(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinkShapesHistory rename(String name) {
        return new InfrastructureLinkShapesHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinkShapesHistory rename(Name name) {
        return new InfrastructureLinkShapesHistory(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<UUID, String, UUID, LineString, TimeRange> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}

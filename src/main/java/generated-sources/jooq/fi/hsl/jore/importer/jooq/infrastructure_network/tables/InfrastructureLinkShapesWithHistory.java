/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables;


import fi.hsl.jore.importer.config.jooq.converter.geometry.LineStringBinding;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.infrastructure_network.InfrastructureNetwork;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureLinkShapesWithHistoryRecord;

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
public class InfrastructureLinkShapesWithHistory extends TableImpl<InfrastructureLinkShapesWithHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>infrastructure_network.infrastructure_link_shapes_with_history</code>
     */
    public static final InfrastructureLinkShapesWithHistory INFRASTRUCTURE_LINK_SHAPES_WITH_HISTORY = new InfrastructureLinkShapesWithHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InfrastructureLinkShapesWithHistoryRecord> getRecordType() {
        return InfrastructureLinkShapesWithHistoryRecord.class;
    }

    /**
     * The column <code>infrastructure_network.infrastructure_link_shapes_with_history.infrastructure_link_shape_id</code>.
     */
    public final TableField<InfrastructureLinkShapesWithHistoryRecord, UUID> INFRASTRUCTURE_LINK_SHAPE_ID = createField(DSL.name("infrastructure_link_shape_id"), SQLDataType.UUID, this, "");

    /**
     * The column <code>infrastructure_network.infrastructure_link_shapes_with_history.infrastructure_link_ext_id</code>.
     */
    public final TableField<InfrastructureLinkShapesWithHistoryRecord, String> INFRASTRUCTURE_LINK_EXT_ID = createField(DSL.name("infrastructure_link_ext_id"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>infrastructure_network.infrastructure_link_shapes_with_history.infrastructure_link_id</code>.
     */
    public final TableField<InfrastructureLinkShapesWithHistoryRecord, UUID> INFRASTRUCTURE_LINK_ID = createField(DSL.name("infrastructure_link_id"), SQLDataType.UUID, this, "");

    /**
     * The column <code>infrastructure_network.infrastructure_link_shapes_with_history.infrastructure_link_shape</code>.
     */
    public final TableField<InfrastructureLinkShapesWithHistoryRecord, LineString> INFRASTRUCTURE_LINK_SHAPE = createField(DSL.name("infrastructure_link_shape"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\""), this, "", new LineStringBinding());

    /**
     * The column <code>infrastructure_network.infrastructure_link_shapes_with_history.infrastructure_link_shape_sys_period</code>.
     */
    public final TableField<InfrastructureLinkShapesWithHistoryRecord, TimeRange> INFRASTRUCTURE_LINK_SHAPE_SYS_PERIOD = createField(DSL.name("infrastructure_link_shape_sys_period"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\""), this, "", new TimeRangeBinding());

    private InfrastructureLinkShapesWithHistory(Name alias, Table<InfrastructureLinkShapesWithHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private InfrastructureLinkShapesWithHistory(Name alias, Table<InfrastructureLinkShapesWithHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view("create view \"infrastructure_link_shapes_with_history\" as  SELECT infrastructure_link_shapes.infrastructure_link_shape_id,\n    infrastructure_link_shapes.infrastructure_link_ext_id,\n    infrastructure_link_shapes.infrastructure_link_id,\n    infrastructure_link_shapes.infrastructure_link_shape,\n    infrastructure_link_shapes.infrastructure_link_shape_sys_period\n   FROM infrastructure_network.infrastructure_link_shapes\nUNION ALL\n SELECT infrastructure_link_shapes_history.infrastructure_link_shape_id,\n    infrastructure_link_shapes_history.infrastructure_link_ext_id,\n    infrastructure_link_shapes_history.infrastructure_link_id,\n    infrastructure_link_shapes_history.infrastructure_link_shape,\n    infrastructure_link_shapes_history.infrastructure_link_shape_sys_period\n   FROM infrastructure_network.infrastructure_link_shapes_history;"));
    }

    /**
     * Create an aliased <code>infrastructure_network.infrastructure_link_shapes_with_history</code> table reference
     */
    public InfrastructureLinkShapesWithHistory(String alias) {
        this(DSL.name(alias), INFRASTRUCTURE_LINK_SHAPES_WITH_HISTORY);
    }

    /**
     * Create an aliased <code>infrastructure_network.infrastructure_link_shapes_with_history</code> table reference
     */
    public InfrastructureLinkShapesWithHistory(Name alias) {
        this(alias, INFRASTRUCTURE_LINK_SHAPES_WITH_HISTORY);
    }

    /**
     * Create a <code>infrastructure_network.infrastructure_link_shapes_with_history</code> table reference
     */
    public InfrastructureLinkShapesWithHistory() {
        this(DSL.name("infrastructure_link_shapes_with_history"), null);
    }

    public <O extends Record> InfrastructureLinkShapesWithHistory(Table<O> child, ForeignKey<O, InfrastructureLinkShapesWithHistoryRecord> key) {
        super(child, key, INFRASTRUCTURE_LINK_SHAPES_WITH_HISTORY);
    }

    @Override
    public Schema getSchema() {
        return InfrastructureNetwork.INFRASTRUCTURE_NETWORK;
    }

    @Override
    public InfrastructureLinkShapesWithHistory as(String alias) {
        return new InfrastructureLinkShapesWithHistory(DSL.name(alias), this);
    }

    @Override
    public InfrastructureLinkShapesWithHistory as(Name alias) {
        return new InfrastructureLinkShapesWithHistory(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinkShapesWithHistory rename(String name) {
        return new InfrastructureLinkShapesWithHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinkShapesWithHistory rename(Name name) {
        return new InfrastructureLinkShapesWithHistory(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<UUID, String, UUID, LineString, TimeRange> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}

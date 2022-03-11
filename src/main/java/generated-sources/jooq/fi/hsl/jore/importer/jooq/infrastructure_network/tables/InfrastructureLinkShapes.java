/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables;


import fi.hsl.jore.importer.config.jooq.converter.geometry.LineStringBinding;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.infrastructure_network.InfrastructureNetwork;
import fi.hsl.jore.importer.jooq.infrastructure_network.Keys;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureLinkShapesRecord;

import java.util.Arrays;
import java.util.List;
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
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.locationtech.jts.geom.LineString;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureLinkShapes extends TableImpl<InfrastructureLinkShapesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>infrastructure_network.infrastructure_link_shapes</code>
     */
    public static final InfrastructureLinkShapes INFRASTRUCTURE_LINK_SHAPES = new InfrastructureLinkShapes();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InfrastructureLinkShapesRecord> getRecordType() {
        return InfrastructureLinkShapesRecord.class;
    }

    /**
     * The column
     * <code>infrastructure_network.infrastructure_link_shapes.infrastructure_link_shape_id</code>.
     */
    public final TableField<InfrastructureLinkShapesRecord, UUID> INFRASTRUCTURE_LINK_SHAPE_ID = createField(DSL.name("infrastructure_link_shape_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("gen_random_uuid()", SQLDataType.UUID)), this, "");

    /**
     * The column
     * <code>infrastructure_network.infrastructure_link_shapes.infrastructure_link_ext_id</code>.
     */
    public final TableField<InfrastructureLinkShapesRecord, String> INFRASTRUCTURE_LINK_EXT_ID = createField(DSL.name("infrastructure_link_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>infrastructure_network.infrastructure_link_shapes.infrastructure_link_id</code>.
     */
    public final TableField<InfrastructureLinkShapesRecord, UUID> INFRASTRUCTURE_LINK_ID = createField(DSL.name("infrastructure_link_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column
     * <code>infrastructure_network.infrastructure_link_shapes.infrastructure_link_shape</code>.
     */
    public final TableField<InfrastructureLinkShapesRecord, LineString> INFRASTRUCTURE_LINK_SHAPE = createField(DSL.name("infrastructure_link_shape"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\"").nullable(false), this, "", new LineStringBinding());

    /**
     * The column
     * <code>infrastructure_network.infrastructure_link_shapes.infrastructure_link_shape_sys_period</code>.
     */
    public final TableField<InfrastructureLinkShapesRecord, TimeRange> INFRASTRUCTURE_LINK_SHAPE_SYS_PERIOD = createField(DSL.name("infrastructure_link_shape_sys_period"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\"").nullable(false).defaultValue(DSL.field("tstzrange(CURRENT_TIMESTAMP, NULL::timestamp with time zone)", org.jooq.impl.SQLDataType.OTHER)), this, "", new TimeRangeBinding());

    private InfrastructureLinkShapes(Name alias, Table<InfrastructureLinkShapesRecord> aliased) {
        this(alias, aliased, null);
    }

    private InfrastructureLinkShapes(Name alias, Table<InfrastructureLinkShapesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased
     * <code>infrastructure_network.infrastructure_link_shapes</code> table
     * reference
     */
    public InfrastructureLinkShapes(String alias) {
        this(DSL.name(alias), INFRASTRUCTURE_LINK_SHAPES);
    }

    /**
     * Create an aliased
     * <code>infrastructure_network.infrastructure_link_shapes</code> table
     * reference
     */
    public InfrastructureLinkShapes(Name alias) {
        this(alias, INFRASTRUCTURE_LINK_SHAPES);
    }

    /**
     * Create a <code>infrastructure_network.infrastructure_link_shapes</code>
     * table reference
     */
    public InfrastructureLinkShapes() {
        this(DSL.name("infrastructure_link_shapes"), null);
    }

    public <O extends Record> InfrastructureLinkShapes(Table<O> child, ForeignKey<O, InfrastructureLinkShapesRecord> key) {
        super(child, key, INFRASTRUCTURE_LINK_SHAPES);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : InfrastructureNetwork.INFRASTRUCTURE_NETWORK;
    }

    @Override
    public UniqueKey<InfrastructureLinkShapesRecord> getPrimaryKey() {
        return Keys.INFRASTRUCTURE_LINK_SHAPES_PKEY;
    }

    @Override
    public List<ForeignKey<InfrastructureLinkShapesRecord, ?>> getReferences() {
        return Arrays.asList(Keys.INFRASTRUCTURE_LINK_SHAPES__INFRASTRUCTURE_LINK_SHAPES_INFRASTRUCTURE_LINK_ID_FKEY);
    }

    private transient InfrastructureLinks _infrastructureLinks;

    public InfrastructureLinks infrastructureLinks() {
        if (_infrastructureLinks == null)
            _infrastructureLinks = new InfrastructureLinks(this, Keys.INFRASTRUCTURE_LINK_SHAPES__INFRASTRUCTURE_LINK_SHAPES_INFRASTRUCTURE_LINK_ID_FKEY);

        return _infrastructureLinks;
    }

    @Override
    public InfrastructureLinkShapes as(String alias) {
        return new InfrastructureLinkShapes(DSL.name(alias), this);
    }

    @Override
    public InfrastructureLinkShapes as(Name alias) {
        return new InfrastructureLinkShapes(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinkShapes rename(String name) {
        return new InfrastructureLinkShapes(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinkShapes rename(Name name) {
        return new InfrastructureLinkShapes(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<UUID, String, UUID, LineString, TimeRange> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}

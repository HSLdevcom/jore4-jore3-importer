/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables;


import fi.hsl.jore.importer.config.jooq.converter.geometry.LineStringBinding;
import fi.hsl.jore.importer.jooq.infrastructure_network.InfrastructureNetwork;
import fi.hsl.jore.importer.jooq.infrastructure_network.Keys;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureLinkShapesStagingRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
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
public class InfrastructureLinkShapesStaging extends TableImpl<InfrastructureLinkShapesStagingRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>infrastructure_network.infrastructure_link_shapes_staging</code>
     */
    public static final InfrastructureLinkShapesStaging INFRASTRUCTURE_LINK_SHAPES_STAGING = new InfrastructureLinkShapesStaging();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InfrastructureLinkShapesStagingRecord> getRecordType() {
        return InfrastructureLinkShapesStagingRecord.class;
    }

    /**
     * The column <code>infrastructure_network.infrastructure_link_shapes_staging.infrastructure_link_ext_id</code>.
     */
    public final TableField<InfrastructureLinkShapesStagingRecord, String> INFRASTRUCTURE_LINK_EXT_ID = createField(DSL.name("infrastructure_link_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>infrastructure_network.infrastructure_link_shapes_staging.infrastructure_link_shape</code>.
     */
    public final TableField<InfrastructureLinkShapesStagingRecord, LineString> INFRASTRUCTURE_LINK_SHAPE = createField(DSL.name("infrastructure_link_shape"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\"").nullable(false), this, "", new LineStringBinding());

    private InfrastructureLinkShapesStaging(Name alias, Table<InfrastructureLinkShapesStagingRecord> aliased) {
        this(alias, aliased, null);
    }

    private InfrastructureLinkShapesStaging(Name alias, Table<InfrastructureLinkShapesStagingRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>infrastructure_network.infrastructure_link_shapes_staging</code> table reference
     */
    public InfrastructureLinkShapesStaging(String alias) {
        this(DSL.name(alias), INFRASTRUCTURE_LINK_SHAPES_STAGING);
    }

    /**
     * Create an aliased <code>infrastructure_network.infrastructure_link_shapes_staging</code> table reference
     */
    public InfrastructureLinkShapesStaging(Name alias) {
        this(alias, INFRASTRUCTURE_LINK_SHAPES_STAGING);
    }

    /**
     * Create a <code>infrastructure_network.infrastructure_link_shapes_staging</code> table reference
     */
    public InfrastructureLinkShapesStaging() {
        this(DSL.name("infrastructure_link_shapes_staging"), null);
    }

    public <O extends Record> InfrastructureLinkShapesStaging(Table<O> child, ForeignKey<O, InfrastructureLinkShapesStagingRecord> key) {
        super(child, key, INFRASTRUCTURE_LINK_SHAPES_STAGING);
    }

    @Override
    public Schema getSchema() {
        return InfrastructureNetwork.INFRASTRUCTURE_NETWORK;
    }

    @Override
    public UniqueKey<InfrastructureLinkShapesStagingRecord> getPrimaryKey() {
        return Keys.INFRASTRUCTURE_LINK_SHAPES_STAGING_PKEY;
    }

    @Override
    public List<UniqueKey<InfrastructureLinkShapesStagingRecord>> getKeys() {
        return Arrays.<UniqueKey<InfrastructureLinkShapesStagingRecord>>asList(Keys.INFRASTRUCTURE_LINK_SHAPES_STAGING_PKEY);
    }

    @Override
    public InfrastructureLinkShapesStaging as(String alias) {
        return new InfrastructureLinkShapesStaging(DSL.name(alias), this);
    }

    @Override
    public InfrastructureLinkShapesStaging as(Name alias) {
        return new InfrastructureLinkShapesStaging(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinkShapesStaging rename(String name) {
        return new InfrastructureLinkShapesStaging(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinkShapesStaging rename(Name name) {
        return new InfrastructureLinkShapesStaging(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<String, LineString> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}

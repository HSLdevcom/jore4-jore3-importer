/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables;


import fi.hsl.jore.importer.config.jooq.converter.geometry.PointBinding;
import fi.hsl.jore.importer.jooq.infrastructure_network.InfrastructureNetwork;
import fi.hsl.jore.importer.jooq.infrastructure_network.Keys;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureNodesStagingRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.locationtech.jts.geom.Point;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureNodesStaging extends TableImpl<InfrastructureNodesStagingRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>infrastructure_network.infrastructure_nodes_staging</code>
     */
    public static final InfrastructureNodesStaging INFRASTRUCTURE_NODES_STAGING = new InfrastructureNodesStaging();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InfrastructureNodesStagingRecord> getRecordType() {
        return InfrastructureNodesStagingRecord.class;
    }

    /**
     * The column <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_ext_id</code>.
     */
    public final TableField<InfrastructureNodesStagingRecord, String> INFRASTRUCTURE_NODE_EXT_ID = createField(DSL.name("infrastructure_node_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_type</code>.
     */
    public final TableField<InfrastructureNodesStagingRecord, String> INFRASTRUCTURE_NODE_TYPE = createField(DSL.name("infrastructure_node_type"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_location</code>.
     */
    public final TableField<InfrastructureNodesStagingRecord, Point> INFRASTRUCTURE_NODE_LOCATION = createField(DSL.name("infrastructure_node_location"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geometry\"").nullable(false), this, "", new PointBinding());

    /**
     * The column <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_projected_location</code>.
     */
    public final TableField<InfrastructureNodesStagingRecord, Point> INFRASTRUCTURE_NODE_PROJECTED_LOCATION = createField(DSL.name("infrastructure_node_projected_location"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geometry\""), this, "", new PointBinding());

    private InfrastructureNodesStaging(Name alias, Table<InfrastructureNodesStagingRecord> aliased) {
        this(alias, aliased, null);
    }

    private InfrastructureNodesStaging(Name alias, Table<InfrastructureNodesStagingRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>infrastructure_network.infrastructure_nodes_staging</code> table reference
     */
    public InfrastructureNodesStaging(String alias) {
        this(DSL.name(alias), INFRASTRUCTURE_NODES_STAGING);
    }

    /**
     * Create an aliased <code>infrastructure_network.infrastructure_nodes_staging</code> table reference
     */
    public InfrastructureNodesStaging(Name alias) {
        this(alias, INFRASTRUCTURE_NODES_STAGING);
    }

    /**
     * Create a <code>infrastructure_network.infrastructure_nodes_staging</code> table reference
     */
    public InfrastructureNodesStaging() {
        this(DSL.name("infrastructure_nodes_staging"), null);
    }

    public <O extends Record> InfrastructureNodesStaging(Table<O> child, ForeignKey<O, InfrastructureNodesStagingRecord> key) {
        super(child, key, INFRASTRUCTURE_NODES_STAGING);
    }

    @Override
    public Schema getSchema() {
        return InfrastructureNetwork.INFRASTRUCTURE_NETWORK;
    }

    @Override
    public UniqueKey<InfrastructureNodesStagingRecord> getPrimaryKey() {
        return Keys.INFRASTRUCTURE_NODES_STAGING_PKEY;
    }

    @Override
    public List<UniqueKey<InfrastructureNodesStagingRecord>> getKeys() {
        return Arrays.<UniqueKey<InfrastructureNodesStagingRecord>>asList(Keys.INFRASTRUCTURE_NODES_STAGING_PKEY);
    }

    @Override
    public List<ForeignKey<InfrastructureNodesStagingRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<InfrastructureNodesStagingRecord, ?>>asList(Keys.INFRASTRUCTURE_NODES_STAGING__INFRASTRUCTURE_NODES_STAGING_INFRASTRUCTURE_NODE_TYPE_FKEY);
    }

    private transient InfrastructureNodeTypes _infrastructureNodeTypes;

    public InfrastructureNodeTypes infrastructureNodeTypes() {
        if (_infrastructureNodeTypes == null)
            _infrastructureNodeTypes = new InfrastructureNodeTypes(this, Keys.INFRASTRUCTURE_NODES_STAGING__INFRASTRUCTURE_NODES_STAGING_INFRASTRUCTURE_NODE_TYPE_FKEY);

        return _infrastructureNodeTypes;
    }

    @Override
    public InfrastructureNodesStaging as(String alias) {
        return new InfrastructureNodesStaging(DSL.name(alias), this);
    }

    @Override
    public InfrastructureNodesStaging as(Name alias) {
        return new InfrastructureNodesStaging(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureNodesStaging rename(String name) {
        return new InfrastructureNodesStaging(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureNodesStaging rename(Name name) {
        return new InfrastructureNodesStaging(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, Point, Point> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}

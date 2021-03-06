/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables.records;


import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodeTypes;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureNodeTypesRecord extends UpdatableRecordImpl<InfrastructureNodeTypesRecord> implements Record2<String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>infrastructure_network.infrastructure_node_types.infrastructure_node_type_value</code>.
     */
    public void setInfrastructureNodeTypeValue(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>infrastructure_network.infrastructure_node_types.infrastructure_node_type_value</code>.
     */
    public String getInfrastructureNodeTypeValue() {
        return (String) get(0);
    }

    /**
     * Setter for <code>infrastructure_network.infrastructure_node_types.infrastructure_node_type_comment</code>.
     */
    public void setInfrastructureNodeTypeComment(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>infrastructure_network.infrastructure_node_types.infrastructure_node_type_comment</code>.
     */
    public String getInfrastructureNodeTypeComment() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<String, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<String, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return InfrastructureNodeTypes.INFRASTRUCTURE_NODE_TYPES.INFRASTRUCTURE_NODE_TYPE_VALUE;
    }

    @Override
    public Field<String> field2() {
        return InfrastructureNodeTypes.INFRASTRUCTURE_NODE_TYPES.INFRASTRUCTURE_NODE_TYPE_COMMENT;
    }

    @Override
    public String component1() {
        return getInfrastructureNodeTypeValue();
    }

    @Override
    public String component2() {
        return getInfrastructureNodeTypeComment();
    }

    @Override
    public String value1() {
        return getInfrastructureNodeTypeValue();
    }

    @Override
    public String value2() {
        return getInfrastructureNodeTypeComment();
    }

    @Override
    public InfrastructureNodeTypesRecord value1(String value) {
        setInfrastructureNodeTypeValue(value);
        return this;
    }

    @Override
    public InfrastructureNodeTypesRecord value2(String value) {
        setInfrastructureNodeTypeComment(value);
        return this;
    }

    @Override
    public InfrastructureNodeTypesRecord values(String value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached InfrastructureNodeTypesRecord
     */
    public InfrastructureNodeTypesRecord() {
        super(InfrastructureNodeTypes.INFRASTRUCTURE_NODE_TYPES);
    }

    /**
     * Create a detached, initialised InfrastructureNodeTypesRecord
     */
    public InfrastructureNodeTypesRecord(String infrastructureNodeTypeValue, String infrastructureNodeTypeComment) {
        super(InfrastructureNodeTypes.INFRASTRUCTURE_NODE_TYPES);

        setInfrastructureNodeTypeValue(infrastructureNodeTypeValue);
        setInfrastructureNodeTypeComment(infrastructureNodeTypeComment);
    }
}

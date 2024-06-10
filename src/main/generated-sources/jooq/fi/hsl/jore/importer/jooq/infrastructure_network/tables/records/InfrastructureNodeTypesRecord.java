/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables.records;


import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodeTypes;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureNodeTypesRecord extends UpdatableRecordImpl<InfrastructureNodeTypesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_node_types.infrastructure_node_type_value</code>.
     */
    public void setInfrastructureNodeTypeValue(String value) {
        set(0, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_node_types.infrastructure_node_type_value</code>.
     */
    public String getInfrastructureNodeTypeValue() {
        return (String) get(0);
    }

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_node_types.infrastructure_node_type_comment</code>.
     */
    public void setInfrastructureNodeTypeComment(String value) {
        set(1, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_node_types.infrastructure_node_type_comment</code>.
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
        resetChangedOnNotNull();
    }
}

/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables.records;


import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodesStaging;

import org.jooq.Record2;
import org.jooq.impl.UpdatableRecordImpl;
import org.locationtech.jts.geom.Point;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureNodesStagingRecord extends UpdatableRecordImpl<InfrastructureNodesStagingRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_ext_id</code>.
     */
    public void setInfrastructureNodeExtId(String value) {
        set(0, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_ext_id</code>.
     */
    public String getInfrastructureNodeExtId() {
        return (String) get(0);
    }

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_type</code>.
     */
    public void setInfrastructureNodeType(String value) {
        set(1, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_type</code>.
     */
    public String getInfrastructureNodeType() {
        return (String) get(1);
    }

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_location</code>.
     */
    public void setInfrastructureNodeLocation(Point value) {
        set(2, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_location</code>.
     */
    public Point getInfrastructureNodeLocation() {
        return (Point) get(2);
    }

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_projected_location</code>.
     */
    public void setInfrastructureNodeProjectedLocation(Point value) {
        set(3, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_projected_location</code>.
     */
    public Point getInfrastructureNodeProjectedLocation() {
        return (Point) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<String, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached InfrastructureNodesStagingRecord
     */
    public InfrastructureNodesStagingRecord() {
        super(InfrastructureNodesStaging.INFRASTRUCTURE_NODES_STAGING);
    }

    /**
     * Create a detached, initialised InfrastructureNodesStagingRecord
     */
    public InfrastructureNodesStagingRecord(String infrastructureNodeExtId, String infrastructureNodeType, Point infrastructureNodeLocation, Point infrastructureNodeProjectedLocation) {
        super(InfrastructureNodesStaging.INFRASTRUCTURE_NODES_STAGING);

        setInfrastructureNodeExtId(infrastructureNodeExtId);
        setInfrastructureNodeType(infrastructureNodeType);
        setInfrastructureNodeLocation(infrastructureNodeLocation);
        setInfrastructureNodeProjectedLocation(infrastructureNodeProjectedLocation);
        resetChangedOnNotNull();
    }
}

/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodes;

import java.util.UUID;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;
import org.locationtech.jts.geom.Point;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureNodesRecord extends UpdatableRecordImpl<InfrastructureNodesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_nodes.infrastructure_node_id</code>.
     */
    public void setInfrastructureNodeId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_nodes.infrastructure_node_id</code>.
     */
    public UUID getInfrastructureNodeId() {
        return (UUID) get(0);
    }

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_nodes.infrastructure_node_ext_id</code>.
     */
    public void setInfrastructureNodeExtId(String value) {
        set(1, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_nodes.infrastructure_node_ext_id</code>.
     */
    public String getInfrastructureNodeExtId() {
        return (String) get(1);
    }

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_nodes.infrastructure_node_type</code>.
     */
    public void setInfrastructureNodeType(String value) {
        set(2, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_nodes.infrastructure_node_type</code>.
     */
    public String getInfrastructureNodeType() {
        return (String) get(2);
    }

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_nodes.infrastructure_node_location</code>.
     */
    public void setInfrastructureNodeLocation(Point value) {
        set(3, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_nodes.infrastructure_node_location</code>.
     */
    public Point getInfrastructureNodeLocation() {
        return (Point) get(3);
    }

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_nodes.infrastructure_node_projected_location</code>.
     */
    public void setInfrastructureNodeProjectedLocation(Point value) {
        set(4, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_nodes.infrastructure_node_projected_location</code>.
     */
    public Point getInfrastructureNodeProjectedLocation() {
        return (Point) get(4);
    }

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_nodes.infrastructure_node_sys_period</code>.
     */
    public void setInfrastructureNodeSysPeriod(TimeRange value) {
        set(5, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_nodes.infrastructure_node_sys_period</code>.
     */
    public TimeRange getInfrastructureNodeSysPeriod() {
        return (TimeRange) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached InfrastructureNodesRecord
     */
    public InfrastructureNodesRecord() {
        super(InfrastructureNodes.INFRASTRUCTURE_NODES);
    }

    /**
     * Create a detached, initialised InfrastructureNodesRecord
     */
    public InfrastructureNodesRecord(UUID infrastructureNodeId, String infrastructureNodeExtId, String infrastructureNodeType, Point infrastructureNodeLocation, Point infrastructureNodeProjectedLocation, TimeRange infrastructureNodeSysPeriod) {
        super(InfrastructureNodes.INFRASTRUCTURE_NODES);

        setInfrastructureNodeId(infrastructureNodeId);
        setInfrastructureNodeExtId(infrastructureNodeExtId);
        setInfrastructureNodeType(infrastructureNodeType);
        setInfrastructureNodeLocation(infrastructureNodeLocation);
        setInfrastructureNodeProjectedLocation(infrastructureNodeProjectedLocation);
        setInfrastructureNodeSysPeriod(infrastructureNodeSysPeriod);
        resetChangedOnNotNull();
    }
}

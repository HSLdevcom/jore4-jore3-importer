/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables.records;


import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodesStaging;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;
import org.locationtech.jts.geom.Point;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureNodesStagingRecord extends UpdatableRecordImpl<InfrastructureNodesStagingRecord> implements Record4<String, String, Point, Point> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_ext_id</code>.
     */
    public void setInfrastructureNodeExtId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_ext_id</code>.
     */
    public String getInfrastructureNodeExtId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_type</code>.
     */
    public void setInfrastructureNodeType(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_type</code>.
     */
    public String getInfrastructureNodeType() {
        return (String) get(1);
    }

    /**
     * Setter for <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_location</code>.
     */
    public void setInfrastructureNodeLocation(Point value) {
        set(2, value);
    }

    /**
     * Getter for <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_location</code>.
     */
    public Point getInfrastructureNodeLocation() {
        return (Point) get(2);
    }

    /**
     * Setter for <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_projected_location</code>.
     */
    public void setInfrastructureNodeProjectedLocation(Point value) {
        set(3, value);
    }

    /**
     * Getter for <code>infrastructure_network.infrastructure_nodes_staging.infrastructure_node_projected_location</code>.
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
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, Point, Point> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<String, String, Point, Point> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return InfrastructureNodesStaging.INFRASTRUCTURE_NODES_STAGING.INFRASTRUCTURE_NODE_EXT_ID;
    }

    @Override
    public Field<String> field2() {
        return InfrastructureNodesStaging.INFRASTRUCTURE_NODES_STAGING.INFRASTRUCTURE_NODE_TYPE;
    }

    @Override
    public Field<Point> field3() {
        return InfrastructureNodesStaging.INFRASTRUCTURE_NODES_STAGING.INFRASTRUCTURE_NODE_LOCATION;
    }

    @Override
    public Field<Point> field4() {
        return InfrastructureNodesStaging.INFRASTRUCTURE_NODES_STAGING.INFRASTRUCTURE_NODE_PROJECTED_LOCATION;
    }

    @Override
    public String component1() {
        return getInfrastructureNodeExtId();
    }

    @Override
    public String component2() {
        return getInfrastructureNodeType();
    }

    @Override
    public Point component3() {
        return getInfrastructureNodeLocation();
    }

    @Override
    public Point component4() {
        return getInfrastructureNodeProjectedLocation();
    }

    @Override
    public String value1() {
        return getInfrastructureNodeExtId();
    }

    @Override
    public String value2() {
        return getInfrastructureNodeType();
    }

    @Override
    public Point value3() {
        return getInfrastructureNodeLocation();
    }

    @Override
    public Point value4() {
        return getInfrastructureNodeProjectedLocation();
    }

    @Override
    public InfrastructureNodesStagingRecord value1(String value) {
        setInfrastructureNodeExtId(value);
        return this;
    }

    @Override
    public InfrastructureNodesStagingRecord value2(String value) {
        setInfrastructureNodeType(value);
        return this;
    }

    @Override
    public InfrastructureNodesStagingRecord value3(Point value) {
        setInfrastructureNodeLocation(value);
        return this;
    }

    @Override
    public InfrastructureNodesStagingRecord value4(Point value) {
        setInfrastructureNodeProjectedLocation(value);
        return this;
    }

    @Override
    public InfrastructureNodesStagingRecord values(String value1, String value2, Point value3, Point value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
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
    }
}

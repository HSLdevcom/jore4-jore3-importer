/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.route.tables.records;


import fi.hsl.jore.jore4.jooq.route.tables.InfrastructureLinkAlongRoute;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * The infrastructure links along which the routes are defined.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureLinkAlongRouteRecord extends UpdatableRecordImpl<InfrastructureLinkAlongRouteRecord> implements Record4<UUID, UUID, Integer, Boolean> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>route.infrastructure_link_along_route.route_id</code>. The ID of the route.
     */
    public void setRouteId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>route.infrastructure_link_along_route.route_id</code>. The ID of the route.
     */
    public UUID getRouteId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>route.infrastructure_link_along_route.infrastructure_link_id</code>. The ID of the infrastructure link.
     */
    public void setInfrastructureLinkId(UUID value) {
        set(1, value);
    }

    /**
     * Getter for <code>route.infrastructure_link_along_route.infrastructure_link_id</code>. The ID of the infrastructure link.
     */
    public UUID getInfrastructureLinkId() {
        return (UUID) get(1);
    }

    /**
     * Setter for <code>route.infrastructure_link_along_route.infrastructure_link_sequence</code>. The order of the infrastructure link within the journey pattern.
     */
    public void setInfrastructureLinkSequence(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>route.infrastructure_link_along_route.infrastructure_link_sequence</code>. The order of the infrastructure link within the journey pattern.
     */
    public Integer getInfrastructureLinkSequence() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>route.infrastructure_link_along_route.is_traversal_forwards</code>. Is the infrastructure link traversed in the direction of its linestring?
     */
    public void setIsTraversalForwards(Boolean value) {
        set(3, value);
    }

    /**
     * Getter for <code>route.infrastructure_link_along_route.is_traversal_forwards</code>. Is the infrastructure link traversed in the direction of its linestring?
     */
    public Boolean getIsTraversalForwards() {
        return (Boolean) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<UUID, Integer> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<UUID, UUID, Integer, Boolean> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<UUID, UUID, Integer, Boolean> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return InfrastructureLinkAlongRoute.INFRASTRUCTURE_LINK_ALONG_ROUTE.ROUTE_ID;
    }

    @Override
    public Field<UUID> field2() {
        return InfrastructureLinkAlongRoute.INFRASTRUCTURE_LINK_ALONG_ROUTE.INFRASTRUCTURE_LINK_ID;
    }

    @Override
    public Field<Integer> field3() {
        return InfrastructureLinkAlongRoute.INFRASTRUCTURE_LINK_ALONG_ROUTE.INFRASTRUCTURE_LINK_SEQUENCE;
    }

    @Override
    public Field<Boolean> field4() {
        return InfrastructureLinkAlongRoute.INFRASTRUCTURE_LINK_ALONG_ROUTE.IS_TRAVERSAL_FORWARDS;
    }

    @Override
    public UUID component1() {
        return getRouteId();
    }

    @Override
    public UUID component2() {
        return getInfrastructureLinkId();
    }

    @Override
    public Integer component3() {
        return getInfrastructureLinkSequence();
    }

    @Override
    public Boolean component4() {
        return getIsTraversalForwards();
    }

    @Override
    public UUID value1() {
        return getRouteId();
    }

    @Override
    public UUID value2() {
        return getInfrastructureLinkId();
    }

    @Override
    public Integer value3() {
        return getInfrastructureLinkSequence();
    }

    @Override
    public Boolean value4() {
        return getIsTraversalForwards();
    }

    @Override
    public InfrastructureLinkAlongRouteRecord value1(UUID value) {
        setRouteId(value);
        return this;
    }

    @Override
    public InfrastructureLinkAlongRouteRecord value2(UUID value) {
        setInfrastructureLinkId(value);
        return this;
    }

    @Override
    public InfrastructureLinkAlongRouteRecord value3(Integer value) {
        setInfrastructureLinkSequence(value);
        return this;
    }

    @Override
    public InfrastructureLinkAlongRouteRecord value4(Boolean value) {
        setIsTraversalForwards(value);
        return this;
    }

    @Override
    public InfrastructureLinkAlongRouteRecord values(UUID value1, UUID value2, Integer value3, Boolean value4) {
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
     * Create a detached InfrastructureLinkAlongRouteRecord
     */
    public InfrastructureLinkAlongRouteRecord() {
        super(InfrastructureLinkAlongRoute.INFRASTRUCTURE_LINK_ALONG_ROUTE);
    }

    /**
     * Create a detached, initialised InfrastructureLinkAlongRouteRecord
     */
    public InfrastructureLinkAlongRouteRecord(UUID routeId, UUID infrastructureLinkId, Integer infrastructureLinkSequence, Boolean isTraversalForwards) {
        super(InfrastructureLinkAlongRoute.INFRASTRUCTURE_LINK_ALONG_ROUTE);

        setRouteId(routeId);
        setInfrastructureLinkId(infrastructureLinkId);
        setInfrastructureLinkSequence(infrastructureLinkSequence);
        setIsTraversalForwards(isTraversalForwards);
    }
}

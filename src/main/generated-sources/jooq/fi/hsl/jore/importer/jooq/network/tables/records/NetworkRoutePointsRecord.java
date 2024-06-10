/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutePoints;

import java.util.UUID;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkRoutePointsRecord extends UpdatableRecordImpl<NetworkRoutePointsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>network.network_route_points.network_route_point_id</code>.
     */
    public void setNetworkRoutePointId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for
     * <code>network.network_route_points.network_route_point_id</code>.
     */
    public UUID getNetworkRoutePointId() {
        return (UUID) get(0);
    }

    /**
     * Setter for
     * <code>network.network_route_points.network_route_direction_id</code>.
     */
    public void setNetworkRouteDirectionId(UUID value) {
        set(1, value);
    }

    /**
     * Getter for
     * <code>network.network_route_points.network_route_direction_id</code>.
     */
    public UUID getNetworkRouteDirectionId() {
        return (UUID) get(1);
    }

    /**
     * Setter for <code>network.network_route_points.infrastructure_node</code>.
     */
    public void setInfrastructureNode(UUID value) {
        set(2, value);
    }

    /**
     * Getter for <code>network.network_route_points.infrastructure_node</code>.
     */
    public UUID getInfrastructureNode() {
        return (UUID) get(2);
    }

    /**
     * Setter for
     * <code>network.network_route_points.network_route_point_ext_id</code>.
     */
    public void setNetworkRoutePointExtId(String value) {
        set(3, value);
    }

    /**
     * Getter for
     * <code>network.network_route_points.network_route_point_ext_id</code>.
     */
    public String getNetworkRoutePointExtId() {
        return (String) get(3);
    }

    /**
     * Setter for
     * <code>network.network_route_points.network_route_point_order</code>.
     */
    public void setNetworkRoutePointOrder(Integer value) {
        set(4, value);
    }

    /**
     * Getter for
     * <code>network.network_route_points.network_route_point_order</code>.
     */
    public Integer getNetworkRoutePointOrder() {
        return (Integer) get(4);
    }

    /**
     * Setter for
     * <code>network.network_route_points.network_route_point_sys_period</code>.
     */
    public void setNetworkRoutePointSysPeriod(TimeRange value) {
        set(5, value);
    }

    /**
     * Getter for
     * <code>network.network_route_points.network_route_point_sys_period</code>.
     */
    public TimeRange getNetworkRoutePointSysPeriod() {
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
     * Create a detached NetworkRoutePointsRecord
     */
    public NetworkRoutePointsRecord() {
        super(NetworkRoutePoints.NETWORK_ROUTE_POINTS);
    }

    /**
     * Create a detached, initialised NetworkRoutePointsRecord
     */
    public NetworkRoutePointsRecord(UUID networkRoutePointId, UUID networkRouteDirectionId, UUID infrastructureNode, String networkRoutePointExtId, Integer networkRoutePointOrder, TimeRange networkRoutePointSysPeriod) {
        super(NetworkRoutePoints.NETWORK_ROUTE_POINTS);

        setNetworkRoutePointId(networkRoutePointId);
        setNetworkRouteDirectionId(networkRouteDirectionId);
        setInfrastructureNode(infrastructureNode);
        setNetworkRoutePointExtId(networkRoutePointExtId);
        setNetworkRoutePointOrder(networkRoutePointOrder);
        setNetworkRoutePointSysPeriod(networkRoutePointSysPeriod);
        resetChangedOnNotNull();
    }
}

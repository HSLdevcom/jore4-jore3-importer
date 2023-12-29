/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutePointsWithHistory;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkRoutePointsWithHistoryRecord extends TableRecordImpl<NetworkRoutePointsWithHistoryRecord> implements Record6<UUID, UUID, UUID, String, Integer, TimeRange> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>network.network_route_points_with_history.network_route_point_id</code>.
     */
    public void setNetworkRoutePointId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>network.network_route_points_with_history.network_route_point_id</code>.
     */
    public UUID getNetworkRoutePointId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>network.network_route_points_with_history.network_route_direction_id</code>.
     */
    public void setNetworkRouteDirectionId(UUID value) {
        set(1, value);
    }

    /**
     * Getter for <code>network.network_route_points_with_history.network_route_direction_id</code>.
     */
    public UUID getNetworkRouteDirectionId() {
        return (UUID) get(1);
    }

    /**
     * Setter for <code>network.network_route_points_with_history.infrastructure_node</code>.
     */
    public void setInfrastructureNode(UUID value) {
        set(2, value);
    }

    /**
     * Getter for <code>network.network_route_points_with_history.infrastructure_node</code>.
     */
    public UUID getInfrastructureNode() {
        return (UUID) get(2);
    }

    /**
     * Setter for <code>network.network_route_points_with_history.network_route_point_ext_id</code>.
     */
    public void setNetworkRoutePointExtId(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>network.network_route_points_with_history.network_route_point_ext_id</code>.
     */
    public String getNetworkRoutePointExtId() {
        return (String) get(3);
    }

    /**
     * Setter for <code>network.network_route_points_with_history.network_route_point_order</code>.
     */
    public void setNetworkRoutePointOrder(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>network.network_route_points_with_history.network_route_point_order</code>.
     */
    public Integer getNetworkRoutePointOrder() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>network.network_route_points_with_history.network_route_point_sys_period</code>.
     */
    public void setNetworkRoutePointSysPeriod(TimeRange value) {
        set(5, value);
    }

    /**
     * Getter for <code>network.network_route_points_with_history.network_route_point_sys_period</code>.
     */
    public TimeRange getNetworkRoutePointSysPeriod() {
        return (TimeRange) get(5);
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<UUID, UUID, UUID, String, Integer, TimeRange> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<UUID, UUID, UUID, String, Integer, TimeRange> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return NetworkRoutePointsWithHistory.NETWORK_ROUTE_POINTS_WITH_HISTORY.NETWORK_ROUTE_POINT_ID;
    }

    @Override
    public Field<UUID> field2() {
        return NetworkRoutePointsWithHistory.NETWORK_ROUTE_POINTS_WITH_HISTORY.NETWORK_ROUTE_DIRECTION_ID;
    }

    @Override
    public Field<UUID> field3() {
        return NetworkRoutePointsWithHistory.NETWORK_ROUTE_POINTS_WITH_HISTORY.INFRASTRUCTURE_NODE;
    }

    @Override
    public Field<String> field4() {
        return NetworkRoutePointsWithHistory.NETWORK_ROUTE_POINTS_WITH_HISTORY.NETWORK_ROUTE_POINT_EXT_ID;
    }

    @Override
    public Field<Integer> field5() {
        return NetworkRoutePointsWithHistory.NETWORK_ROUTE_POINTS_WITH_HISTORY.NETWORK_ROUTE_POINT_ORDER;
    }

    @Override
    public Field<TimeRange> field6() {
        return NetworkRoutePointsWithHistory.NETWORK_ROUTE_POINTS_WITH_HISTORY.NETWORK_ROUTE_POINT_SYS_PERIOD;
    }

    @Override
    public UUID component1() {
        return getNetworkRoutePointId();
    }

    @Override
    public UUID component2() {
        return getNetworkRouteDirectionId();
    }

    @Override
    public UUID component3() {
        return getInfrastructureNode();
    }

    @Override
    public String component4() {
        return getNetworkRoutePointExtId();
    }

    @Override
    public Integer component5() {
        return getNetworkRoutePointOrder();
    }

    @Override
    public TimeRange component6() {
        return getNetworkRoutePointSysPeriod();
    }

    @Override
    public UUID value1() {
        return getNetworkRoutePointId();
    }

    @Override
    public UUID value2() {
        return getNetworkRouteDirectionId();
    }

    @Override
    public UUID value3() {
        return getInfrastructureNode();
    }

    @Override
    public String value4() {
        return getNetworkRoutePointExtId();
    }

    @Override
    public Integer value5() {
        return getNetworkRoutePointOrder();
    }

    @Override
    public TimeRange value6() {
        return getNetworkRoutePointSysPeriod();
    }

    @Override
    public NetworkRoutePointsWithHistoryRecord value1(UUID value) {
        setNetworkRoutePointId(value);
        return this;
    }

    @Override
    public NetworkRoutePointsWithHistoryRecord value2(UUID value) {
        setNetworkRouteDirectionId(value);
        return this;
    }

    @Override
    public NetworkRoutePointsWithHistoryRecord value3(UUID value) {
        setInfrastructureNode(value);
        return this;
    }

    @Override
    public NetworkRoutePointsWithHistoryRecord value4(String value) {
        setNetworkRoutePointExtId(value);
        return this;
    }

    @Override
    public NetworkRoutePointsWithHistoryRecord value5(Integer value) {
        setNetworkRoutePointOrder(value);
        return this;
    }

    @Override
    public NetworkRoutePointsWithHistoryRecord value6(TimeRange value) {
        setNetworkRoutePointSysPeriod(value);
        return this;
    }

    @Override
    public NetworkRoutePointsWithHistoryRecord values(UUID value1, UUID value2, UUID value3, String value4, Integer value5, TimeRange value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached NetworkRoutePointsWithHistoryRecord
     */
    public NetworkRoutePointsWithHistoryRecord() {
        super(NetworkRoutePointsWithHistory.NETWORK_ROUTE_POINTS_WITH_HISTORY);
    }

    /**
     * Create a detached, initialised NetworkRoutePointsWithHistoryRecord
     */
    public NetworkRoutePointsWithHistoryRecord(UUID networkRoutePointId, UUID networkRouteDirectionId, UUID infrastructureNode, String networkRoutePointExtId, Integer networkRoutePointOrder, TimeRange networkRoutePointSysPeriod) {
        super(NetworkRoutePointsWithHistory.NETWORK_ROUTE_POINTS_WITH_HISTORY);

        setNetworkRoutePointId(networkRoutePointId);
        setNetworkRouteDirectionId(networkRouteDirectionId);
        setInfrastructureNode(infrastructureNode);
        setNetworkRoutePointExtId(networkRoutePointExtId);
        setNetworkRoutePointOrder(networkRoutePointOrder);
        setNetworkRoutePointSysPeriod(networkRoutePointSysPeriod);
    }
}
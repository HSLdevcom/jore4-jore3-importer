/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutePointsStaging;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkRoutePointsStagingRecord extends UpdatableRecordImpl<NetworkRoutePointsStagingRecord> implements Record4<String, String, String, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>network.network_route_points_staging.network_route_point_ext_id</code>.
     */
    public void setNetworkRoutePointExtId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>network.network_route_points_staging.network_route_point_ext_id</code>.
     */
    public String getNetworkRoutePointExtId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>network.network_route_points_staging.network_route_direction_ext_id</code>.
     */
    public void setNetworkRouteDirectionExtId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>network.network_route_points_staging.network_route_direction_ext_id</code>.
     */
    public String getNetworkRouteDirectionExtId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>network.network_route_points_staging.infrastructure_node_ext_id</code>.
     */
    public void setInfrastructureNodeExtId(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>network.network_route_points_staging.infrastructure_node_ext_id</code>.
     */
    public String getInfrastructureNodeExtId() {
        return (String) get(2);
    }

    /**
     * Setter for <code>network.network_route_points_staging.network_route_point_order</code>.
     */
    public void setNetworkRoutePointOrder(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>network.network_route_points_staging.network_route_point_order</code>.
     */
    public Integer getNetworkRoutePointOrder() {
        return (Integer) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, String, Integer> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<String, String, String, Integer> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return NetworkRoutePointsStaging.NETWORK_ROUTE_POINTS_STAGING.NETWORK_ROUTE_POINT_EXT_ID;
    }

    @Override
    public Field<String> field2() {
        return NetworkRoutePointsStaging.NETWORK_ROUTE_POINTS_STAGING.NETWORK_ROUTE_DIRECTION_EXT_ID;
    }

    @Override
    public Field<String> field3() {
        return NetworkRoutePointsStaging.NETWORK_ROUTE_POINTS_STAGING.INFRASTRUCTURE_NODE_EXT_ID;
    }

    @Override
    public Field<Integer> field4() {
        return NetworkRoutePointsStaging.NETWORK_ROUTE_POINTS_STAGING.NETWORK_ROUTE_POINT_ORDER;
    }

    @Override
    public String component1() {
        return getNetworkRoutePointExtId();
    }

    @Override
    public String component2() {
        return getNetworkRouteDirectionExtId();
    }

    @Override
    public String component3() {
        return getInfrastructureNodeExtId();
    }

    @Override
    public Integer component4() {
        return getNetworkRoutePointOrder();
    }

    @Override
    public String value1() {
        return getNetworkRoutePointExtId();
    }

    @Override
    public String value2() {
        return getNetworkRouteDirectionExtId();
    }

    @Override
    public String value3() {
        return getInfrastructureNodeExtId();
    }

    @Override
    public Integer value4() {
        return getNetworkRoutePointOrder();
    }

    @Override
    public NetworkRoutePointsStagingRecord value1(String value) {
        setNetworkRoutePointExtId(value);
        return this;
    }

    @Override
    public NetworkRoutePointsStagingRecord value2(String value) {
        setNetworkRouteDirectionExtId(value);
        return this;
    }

    @Override
    public NetworkRoutePointsStagingRecord value3(String value) {
        setInfrastructureNodeExtId(value);
        return this;
    }

    @Override
    public NetworkRoutePointsStagingRecord value4(Integer value) {
        setNetworkRoutePointOrder(value);
        return this;
    }

    @Override
    public NetworkRoutePointsStagingRecord values(String value1, String value2, String value3, Integer value4) {
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
     * Create a detached NetworkRoutePointsStagingRecord
     */
    public NetworkRoutePointsStagingRecord() {
        super(NetworkRoutePointsStaging.NETWORK_ROUTE_POINTS_STAGING);
    }

    /**
     * Create a detached, initialised NetworkRoutePointsStagingRecord
     */
    public NetworkRoutePointsStagingRecord(String networkRoutePointExtId, String networkRouteDirectionExtId, String infrastructureNodeExtId, Integer networkRoutePointOrder) {
        super(NetworkRoutePointsStaging.NETWORK_ROUTE_POINTS_STAGING);

        setNetworkRoutePointExtId(networkRoutePointExtId);
        setNetworkRouteDirectionExtId(networkRouteDirectionExtId);
        setInfrastructureNodeExtId(infrastructureNodeExtId);
        setNetworkRoutePointOrder(networkRoutePointOrder);
    }
}

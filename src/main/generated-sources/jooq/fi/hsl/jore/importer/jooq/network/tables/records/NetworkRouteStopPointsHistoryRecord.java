/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteStopPointsHistory;

import java.util.UUID;

import org.jooq.JSONB;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkRouteStopPointsHistoryRecord extends TableRecordImpl<NetworkRouteStopPointsHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>network.network_route_stop_points_history.network_route_point_id</code>.
     */
    public void setNetworkRoutePointId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for
     * <code>network.network_route_stop_points_history.network_route_point_id</code>.
     */
    public UUID getNetworkRoutePointId() {
        return (UUID) get(0);
    }

    /**
     * Setter for
     * <code>network.network_route_stop_points_history.network_route_stop_point_ext_id</code>.
     */
    public void setNetworkRouteStopPointExtId(String value) {
        set(1, value);
    }

    /**
     * Getter for
     * <code>network.network_route_stop_points_history.network_route_stop_point_ext_id</code>.
     */
    public String getNetworkRouteStopPointExtId() {
        return (String) get(1);
    }

    /**
     * Setter for
     * <code>network.network_route_stop_points_history.network_route_stop_point_order</code>.
     */
    public void setNetworkRouteStopPointOrder(Integer value) {
        set(2, value);
    }

    /**
     * Getter for
     * <code>network.network_route_stop_points_history.network_route_stop_point_order</code>.
     */
    public Integer getNetworkRouteStopPointOrder() {
        return (Integer) get(2);
    }

    /**
     * Setter for
     * <code>network.network_route_stop_points_history.network_route_stop_point_hastus_point</code>.
     */
    public void setNetworkRouteStopPointHastusPoint(Boolean value) {
        set(3, value);
    }

    /**
     * Getter for
     * <code>network.network_route_stop_points_history.network_route_stop_point_hastus_point</code>.
     */
    public Boolean getNetworkRouteStopPointHastusPoint() {
        return (Boolean) get(3);
    }

    /**
     * Setter for
     * <code>network.network_route_stop_points_history.network_route_stop_point_timetable_column</code>.
     */
    public void setNetworkRouteStopPointTimetableColumn(Integer value) {
        set(4, value);
    }

    /**
     * Getter for
     * <code>network.network_route_stop_points_history.network_route_stop_point_timetable_column</code>.
     */
    public Integer getNetworkRouteStopPointTimetableColumn() {
        return (Integer) get(4);
    }

    /**
     * Setter for
     * <code>network.network_route_stop_points_history.network_route_stop_point_sys_period</code>.
     */
    public void setNetworkRouteStopPointSysPeriod(TimeRange value) {
        set(5, value);
    }

    /**
     * Getter for
     * <code>network.network_route_stop_points_history.network_route_stop_point_sys_period</code>.
     */
    public TimeRange getNetworkRouteStopPointSysPeriod() {
        return (TimeRange) get(5);
    }

    /**
     * Setter for
     * <code>network.network_route_stop_points_history.network_route_stop_point_via_point</code>.
     */
    public void setNetworkRouteStopPointViaPoint(Boolean value) {
        set(6, value);
    }

    /**
     * Getter for
     * <code>network.network_route_stop_points_history.network_route_stop_point_via_point</code>.
     */
    public Boolean getNetworkRouteStopPointViaPoint() {
        return (Boolean) get(6);
    }

    /**
     * Setter for
     * <code>network.network_route_stop_points_history.network_route_stop_point_via_name</code>.
     */
    public void setNetworkRouteStopPointViaName(JSONB value) {
        set(7, value);
    }

    /**
     * Getter for
     * <code>network.network_route_stop_points_history.network_route_stop_point_via_name</code>.
     */
    public JSONB getNetworkRouteStopPointViaName() {
        return (JSONB) get(7);
    }

    /**
     * Setter for
     * <code>network.network_route_stop_points_history.network_route_stop_point_regulated_timing_point_status</code>.
     */
    public void setNetworkRouteStopPointRegulatedTimingPointStatus(Integer value) {
        set(8, value);
    }

    /**
     * Getter for
     * <code>network.network_route_stop_points_history.network_route_stop_point_regulated_timing_point_status</code>.
     */
    public Integer getNetworkRouteStopPointRegulatedTimingPointStatus() {
        return (Integer) get(8);
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached NetworkRouteStopPointsHistoryRecord
     */
    public NetworkRouteStopPointsHistoryRecord() {
        super(NetworkRouteStopPointsHistory.NETWORK_ROUTE_STOP_POINTS_HISTORY);
    }

    /**
     * Create a detached, initialised NetworkRouteStopPointsHistoryRecord
     */
    public NetworkRouteStopPointsHistoryRecord(UUID networkRoutePointId, String networkRouteStopPointExtId, Integer networkRouteStopPointOrder, Boolean networkRouteStopPointHastusPoint, Integer networkRouteStopPointTimetableColumn, TimeRange networkRouteStopPointSysPeriod, Boolean networkRouteStopPointViaPoint, JSONB networkRouteStopPointViaName, Integer networkRouteStopPointRegulatedTimingPointStatus) {
        super(NetworkRouteStopPointsHistory.NETWORK_ROUTE_STOP_POINTS_HISTORY);

        setNetworkRoutePointId(networkRoutePointId);
        setNetworkRouteStopPointExtId(networkRouteStopPointExtId);
        setNetworkRouteStopPointOrder(networkRouteStopPointOrder);
        setNetworkRouteStopPointHastusPoint(networkRouteStopPointHastusPoint);
        setNetworkRouteStopPointTimetableColumn(networkRouteStopPointTimetableColumn);
        setNetworkRouteStopPointSysPeriod(networkRouteStopPointSysPeriod);
        setNetworkRouteStopPointViaPoint(networkRouteStopPointViaPoint);
        setNetworkRouteStopPointViaName(networkRouteStopPointViaName);
        setNetworkRouteStopPointRegulatedTimingPointStatus(networkRouteStopPointRegulatedTimingPointStatus);
        resetChangedOnNotNull();
    }
}

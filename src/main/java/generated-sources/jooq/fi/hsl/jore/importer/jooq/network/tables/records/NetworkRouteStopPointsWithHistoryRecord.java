/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteStopPointsWithHistory;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkRouteStopPointsWithHistoryRecord extends TableRecordImpl<NetworkRouteStopPointsWithHistoryRecord> implements Record7<UUID, String, Integer, Boolean, Integer, TimeRange, Boolean> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>network.network_route_stop_points_with_history.network_route_point_id</code>.
     */
    public void setNetworkRoutePointId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>network.network_route_stop_points_with_history.network_route_point_id</code>.
     */
    public UUID getNetworkRoutePointId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>network.network_route_stop_points_with_history.network_route_stop_point_ext_id</code>.
     */
    public void setNetworkRouteStopPointExtId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>network.network_route_stop_points_with_history.network_route_stop_point_ext_id</code>.
     */
    public String getNetworkRouteStopPointExtId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>network.network_route_stop_points_with_history.network_route_stop_point_order</code>.
     */
    public void setNetworkRouteStopPointOrder(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>network.network_route_stop_points_with_history.network_route_stop_point_order</code>.
     */
    public Integer getNetworkRouteStopPointOrder() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>network.network_route_stop_points_with_history.network_route_stop_point_hastus_point</code>.
     */
    public void setNetworkRouteStopPointHastusPoint(Boolean value) {
        set(3, value);
    }

    /**
     * Getter for <code>network.network_route_stop_points_with_history.network_route_stop_point_hastus_point</code>.
     */
    public Boolean getNetworkRouteStopPointHastusPoint() {
        return (Boolean) get(3);
    }

    /**
     * Setter for <code>network.network_route_stop_points_with_history.network_route_stop_point_timetable_column</code>.
     */
    public void setNetworkRouteStopPointTimetableColumn(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>network.network_route_stop_points_with_history.network_route_stop_point_timetable_column</code>.
     */
    public Integer getNetworkRouteStopPointTimetableColumn() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>network.network_route_stop_points_with_history.network_route_stop_point_sys_period</code>.
     */
    public void setNetworkRouteStopPointSysPeriod(TimeRange value) {
        set(5, value);
    }

    /**
     * Getter for <code>network.network_route_stop_points_with_history.network_route_stop_point_sys_period</code>.
     */
    public TimeRange getNetworkRouteStopPointSysPeriod() {
        return (TimeRange) get(5);
    }

    /**
     * Setter for <code>network.network_route_stop_points_with_history.network_route_stop_point_via_point</code>.
     */
    public void setNetworkRouteStopPointViaPoint(Boolean value) {
        set(6, value);
    }

    /**
     * Getter for <code>network.network_route_stop_points_with_history.network_route_stop_point_via_point</code>.
     */
    public Boolean getNetworkRouteStopPointViaPoint() {
        return (Boolean) get(6);
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<UUID, String, Integer, Boolean, Integer, TimeRange, Boolean> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<UUID, String, Integer, Boolean, Integer, TimeRange, Boolean> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return NetworkRouteStopPointsWithHistory.NETWORK_ROUTE_STOP_POINTS_WITH_HISTORY.NETWORK_ROUTE_POINT_ID;
    }

    @Override
    public Field<String> field2() {
        return NetworkRouteStopPointsWithHistory.NETWORK_ROUTE_STOP_POINTS_WITH_HISTORY.NETWORK_ROUTE_STOP_POINT_EXT_ID;
    }

    @Override
    public Field<Integer> field3() {
        return NetworkRouteStopPointsWithHistory.NETWORK_ROUTE_STOP_POINTS_WITH_HISTORY.NETWORK_ROUTE_STOP_POINT_ORDER;
    }

    @Override
    public Field<Boolean> field4() {
        return NetworkRouteStopPointsWithHistory.NETWORK_ROUTE_STOP_POINTS_WITH_HISTORY.NETWORK_ROUTE_STOP_POINT_HASTUS_POINT;
    }

    @Override
    public Field<Integer> field5() {
        return NetworkRouteStopPointsWithHistory.NETWORK_ROUTE_STOP_POINTS_WITH_HISTORY.NETWORK_ROUTE_STOP_POINT_TIMETABLE_COLUMN;
    }

    @Override
    public Field<TimeRange> field6() {
        return NetworkRouteStopPointsWithHistory.NETWORK_ROUTE_STOP_POINTS_WITH_HISTORY.NETWORK_ROUTE_STOP_POINT_SYS_PERIOD;
    }

    @Override
    public Field<Boolean> field7() {
        return NetworkRouteStopPointsWithHistory.NETWORK_ROUTE_STOP_POINTS_WITH_HISTORY.NETWORK_ROUTE_STOP_POINT_VIA_POINT;
    }

    @Override
    public UUID component1() {
        return getNetworkRoutePointId();
    }

    @Override
    public String component2() {
        return getNetworkRouteStopPointExtId();
    }

    @Override
    public Integer component3() {
        return getNetworkRouteStopPointOrder();
    }

    @Override
    public Boolean component4() {
        return getNetworkRouteStopPointHastusPoint();
    }

    @Override
    public Integer component5() {
        return getNetworkRouteStopPointTimetableColumn();
    }

    @Override
    public TimeRange component6() {
        return getNetworkRouteStopPointSysPeriod();
    }

    @Override
    public Boolean component7() {
        return getNetworkRouteStopPointViaPoint();
    }

    @Override
    public UUID value1() {
        return getNetworkRoutePointId();
    }

    @Override
    public String value2() {
        return getNetworkRouteStopPointExtId();
    }

    @Override
    public Integer value3() {
        return getNetworkRouteStopPointOrder();
    }

    @Override
    public Boolean value4() {
        return getNetworkRouteStopPointHastusPoint();
    }

    @Override
    public Integer value5() {
        return getNetworkRouteStopPointTimetableColumn();
    }

    @Override
    public TimeRange value6() {
        return getNetworkRouteStopPointSysPeriod();
    }

    @Override
    public Boolean value7() {
        return getNetworkRouteStopPointViaPoint();
    }

    @Override
    public NetworkRouteStopPointsWithHistoryRecord value1(UUID value) {
        setNetworkRoutePointId(value);
        return this;
    }

    @Override
    public NetworkRouteStopPointsWithHistoryRecord value2(String value) {
        setNetworkRouteStopPointExtId(value);
        return this;
    }

    @Override
    public NetworkRouteStopPointsWithHistoryRecord value3(Integer value) {
        setNetworkRouteStopPointOrder(value);
        return this;
    }

    @Override
    public NetworkRouteStopPointsWithHistoryRecord value4(Boolean value) {
        setNetworkRouteStopPointHastusPoint(value);
        return this;
    }

    @Override
    public NetworkRouteStopPointsWithHistoryRecord value5(Integer value) {
        setNetworkRouteStopPointTimetableColumn(value);
        return this;
    }

    @Override
    public NetworkRouteStopPointsWithHistoryRecord value6(TimeRange value) {
        setNetworkRouteStopPointSysPeriod(value);
        return this;
    }

    @Override
    public NetworkRouteStopPointsWithHistoryRecord value7(Boolean value) {
        setNetworkRouteStopPointViaPoint(value);
        return this;
    }

    @Override
    public NetworkRouteStopPointsWithHistoryRecord values(UUID value1, String value2, Integer value3, Boolean value4, Integer value5, TimeRange value6, Boolean value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached NetworkRouteStopPointsWithHistoryRecord
     */
    public NetworkRouteStopPointsWithHistoryRecord() {
        super(NetworkRouteStopPointsWithHistory.NETWORK_ROUTE_STOP_POINTS_WITH_HISTORY);
    }

    /**
     * Create a detached, initialised NetworkRouteStopPointsWithHistoryRecord
     */
    public NetworkRouteStopPointsWithHistoryRecord(UUID networkRoutePointId, String networkRouteStopPointExtId, Integer networkRouteStopPointOrder, Boolean networkRouteStopPointHastusPoint, Integer networkRouteStopPointTimetableColumn, TimeRange networkRouteStopPointSysPeriod, Boolean networkRouteStopPointViaPoint) {
        super(NetworkRouteStopPointsWithHistory.NETWORK_ROUTE_STOP_POINTS_WITH_HISTORY);

        setNetworkRoutePointId(networkRoutePointId);
        setNetworkRouteStopPointExtId(networkRouteStopPointExtId);
        setNetworkRouteStopPointOrder(networkRouteStopPointOrder);
        setNetworkRouteStopPointHastusPoint(networkRouteStopPointHastusPoint);
        setNetworkRouteStopPointTimetableColumn(networkRouteStopPointTimetableColumn);
        setNetworkRouteStopPointSysPeriod(networkRouteStopPointSysPeriod);
        setNetworkRouteStopPointViaPoint(networkRouteStopPointViaPoint);
    }
}

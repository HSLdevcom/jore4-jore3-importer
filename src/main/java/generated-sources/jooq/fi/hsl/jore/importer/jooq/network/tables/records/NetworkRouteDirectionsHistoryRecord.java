/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirectionsHistory;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record11;
import org.jooq.Row11;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkRouteDirectionsHistoryRecord extends TableRecordImpl<NetworkRouteDirectionsHistoryRecord> implements Record11<UUID, UUID, String, String, Integer, JSONB, JSONB, JSONB, JSONB, DateRange, TimeRange> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>network.network_route_directions_history.network_route_direction_id</code>.
     */
    public void setNetworkRouteDirectionId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>network.network_route_directions_history.network_route_direction_id</code>.
     */
    public UUID getNetworkRouteDirectionId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>network.network_route_directions_history.network_route_id</code>.
     */
    public void setNetworkRouteId(UUID value) {
        set(1, value);
    }

    /**
     * Getter for <code>network.network_route_directions_history.network_route_id</code>.
     */
    public UUID getNetworkRouteId() {
        return (UUID) get(1);
    }

    /**
     * Setter for <code>network.network_route_directions_history.network_route_direction_type</code>.
     */
    public void setNetworkRouteDirectionType(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>network.network_route_directions_history.network_route_direction_type</code>.
     */
    public String getNetworkRouteDirectionType() {
        return (String) get(2);
    }

    /**
     * Setter for <code>network.network_route_directions_history.network_route_direction_ext_id</code>.
     */
    public void setNetworkRouteDirectionExtId(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>network.network_route_directions_history.network_route_direction_ext_id</code>.
     */
    public String getNetworkRouteDirectionExtId() {
        return (String) get(3);
    }

    /**
     * Setter for <code>network.network_route_directions_history.network_route_direction_length</code>.
     */
    public void setNetworkRouteDirectionLength(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>network.network_route_directions_history.network_route_direction_length</code>.
     */
    public Integer getNetworkRouteDirectionLength() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>network.network_route_directions_history.network_route_direction_name</code>.
     */
    public void setNetworkRouteDirectionName(JSONB value) {
        set(5, value);
    }

    /**
     * Getter for <code>network.network_route_directions_history.network_route_direction_name</code>.
     */
    public JSONB getNetworkRouteDirectionName() {
        return (JSONB) get(5);
    }

    /**
     * Setter for <code>network.network_route_directions_history.network_route_direction_name_short</code>.
     */
    public void setNetworkRouteDirectionNameShort(JSONB value) {
        set(6, value);
    }

    /**
     * Getter for <code>network.network_route_directions_history.network_route_direction_name_short</code>.
     */
    public JSONB getNetworkRouteDirectionNameShort() {
        return (JSONB) get(6);
    }

    /**
     * Setter for <code>network.network_route_directions_history.network_route_direction_origin</code>.
     */
    public void setNetworkRouteDirectionOrigin(JSONB value) {
        set(7, value);
    }

    /**
     * Getter for <code>network.network_route_directions_history.network_route_direction_origin</code>.
     */
    public JSONB getNetworkRouteDirectionOrigin() {
        return (JSONB) get(7);
    }

    /**
     * Setter for <code>network.network_route_directions_history.network_route_direction_destination</code>.
     */
    public void setNetworkRouteDirectionDestination(JSONB value) {
        set(8, value);
    }

    /**
     * Getter for <code>network.network_route_directions_history.network_route_direction_destination</code>.
     */
    public JSONB getNetworkRouteDirectionDestination() {
        return (JSONB) get(8);
    }

    /**
     * Setter for <code>network.network_route_directions_history.network_route_direction_valid_date_range</code>.
     */
    public void setNetworkRouteDirectionValidDateRange(DateRange value) {
        set(9, value);
    }

    /**
     * Getter for <code>network.network_route_directions_history.network_route_direction_valid_date_range</code>.
     */
    public DateRange getNetworkRouteDirectionValidDateRange() {
        return (DateRange) get(9);
    }

    /**
     * Setter for <code>network.network_route_directions_history.network_route_direction_sys_period</code>.
     */
    public void setNetworkRouteDirectionSysPeriod(TimeRange value) {
        set(10, value);
    }

    /**
     * Getter for <code>network.network_route_directions_history.network_route_direction_sys_period</code>.
     */
    public TimeRange getNetworkRouteDirectionSysPeriod() {
        return (TimeRange) get(10);
    }

    // -------------------------------------------------------------------------
    // Record11 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row11<UUID, UUID, String, String, Integer, JSONB, JSONB, JSONB, JSONB, DateRange, TimeRange> fieldsRow() {
        return (Row11) super.fieldsRow();
    }

    @Override
    public Row11<UUID, UUID, String, String, Integer, JSONB, JSONB, JSONB, JSONB, DateRange, TimeRange> valuesRow() {
        return (Row11) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return NetworkRouteDirectionsHistory.NETWORK_ROUTE_DIRECTIONS_HISTORY.NETWORK_ROUTE_DIRECTION_ID;
    }

    @Override
    public Field<UUID> field2() {
        return NetworkRouteDirectionsHistory.NETWORK_ROUTE_DIRECTIONS_HISTORY.NETWORK_ROUTE_ID;
    }

    @Override
    public Field<String> field3() {
        return NetworkRouteDirectionsHistory.NETWORK_ROUTE_DIRECTIONS_HISTORY.NETWORK_ROUTE_DIRECTION_TYPE;
    }

    @Override
    public Field<String> field4() {
        return NetworkRouteDirectionsHistory.NETWORK_ROUTE_DIRECTIONS_HISTORY.NETWORK_ROUTE_DIRECTION_EXT_ID;
    }

    @Override
    public Field<Integer> field5() {
        return NetworkRouteDirectionsHistory.NETWORK_ROUTE_DIRECTIONS_HISTORY.NETWORK_ROUTE_DIRECTION_LENGTH;
    }

    @Override
    public Field<JSONB> field6() {
        return NetworkRouteDirectionsHistory.NETWORK_ROUTE_DIRECTIONS_HISTORY.NETWORK_ROUTE_DIRECTION_NAME;
    }

    @Override
    public Field<JSONB> field7() {
        return NetworkRouteDirectionsHistory.NETWORK_ROUTE_DIRECTIONS_HISTORY.NETWORK_ROUTE_DIRECTION_NAME_SHORT;
    }

    @Override
    public Field<JSONB> field8() {
        return NetworkRouteDirectionsHistory.NETWORK_ROUTE_DIRECTIONS_HISTORY.NETWORK_ROUTE_DIRECTION_ORIGIN;
    }

    @Override
    public Field<JSONB> field9() {
        return NetworkRouteDirectionsHistory.NETWORK_ROUTE_DIRECTIONS_HISTORY.NETWORK_ROUTE_DIRECTION_DESTINATION;
    }

    @Override
    public Field<DateRange> field10() {
        return NetworkRouteDirectionsHistory.NETWORK_ROUTE_DIRECTIONS_HISTORY.NETWORK_ROUTE_DIRECTION_VALID_DATE_RANGE;
    }

    @Override
    public Field<TimeRange> field11() {
        return NetworkRouteDirectionsHistory.NETWORK_ROUTE_DIRECTIONS_HISTORY.NETWORK_ROUTE_DIRECTION_SYS_PERIOD;
    }

    @Override
    public UUID component1() {
        return getNetworkRouteDirectionId();
    }

    @Override
    public UUID component2() {
        return getNetworkRouteId();
    }

    @Override
    public String component3() {
        return getNetworkRouteDirectionType();
    }

    @Override
    public String component4() {
        return getNetworkRouteDirectionExtId();
    }

    @Override
    public Integer component5() {
        return getNetworkRouteDirectionLength();
    }

    @Override
    public JSONB component6() {
        return getNetworkRouteDirectionName();
    }

    @Override
    public JSONB component7() {
        return getNetworkRouteDirectionNameShort();
    }

    @Override
    public JSONB component8() {
        return getNetworkRouteDirectionOrigin();
    }

    @Override
    public JSONB component9() {
        return getNetworkRouteDirectionDestination();
    }

    @Override
    public DateRange component10() {
        return getNetworkRouteDirectionValidDateRange();
    }

    @Override
    public TimeRange component11() {
        return getNetworkRouteDirectionSysPeriod();
    }

    @Override
    public UUID value1() {
        return getNetworkRouteDirectionId();
    }

    @Override
    public UUID value2() {
        return getNetworkRouteId();
    }

    @Override
    public String value3() {
        return getNetworkRouteDirectionType();
    }

    @Override
    public String value4() {
        return getNetworkRouteDirectionExtId();
    }

    @Override
    public Integer value5() {
        return getNetworkRouteDirectionLength();
    }

    @Override
    public JSONB value6() {
        return getNetworkRouteDirectionName();
    }

    @Override
    public JSONB value7() {
        return getNetworkRouteDirectionNameShort();
    }

    @Override
    public JSONB value8() {
        return getNetworkRouteDirectionOrigin();
    }

    @Override
    public JSONB value9() {
        return getNetworkRouteDirectionDestination();
    }

    @Override
    public DateRange value10() {
        return getNetworkRouteDirectionValidDateRange();
    }

    @Override
    public TimeRange value11() {
        return getNetworkRouteDirectionSysPeriod();
    }

    @Override
    public NetworkRouteDirectionsHistoryRecord value1(UUID value) {
        setNetworkRouteDirectionId(value);
        return this;
    }

    @Override
    public NetworkRouteDirectionsHistoryRecord value2(UUID value) {
        setNetworkRouteId(value);
        return this;
    }

    @Override
    public NetworkRouteDirectionsHistoryRecord value3(String value) {
        setNetworkRouteDirectionType(value);
        return this;
    }

    @Override
    public NetworkRouteDirectionsHistoryRecord value4(String value) {
        setNetworkRouteDirectionExtId(value);
        return this;
    }

    @Override
    public NetworkRouteDirectionsHistoryRecord value5(Integer value) {
        setNetworkRouteDirectionLength(value);
        return this;
    }

    @Override
    public NetworkRouteDirectionsHistoryRecord value6(JSONB value) {
        setNetworkRouteDirectionName(value);
        return this;
    }

    @Override
    public NetworkRouteDirectionsHistoryRecord value7(JSONB value) {
        setNetworkRouteDirectionNameShort(value);
        return this;
    }

    @Override
    public NetworkRouteDirectionsHistoryRecord value8(JSONB value) {
        setNetworkRouteDirectionOrigin(value);
        return this;
    }

    @Override
    public NetworkRouteDirectionsHistoryRecord value9(JSONB value) {
        setNetworkRouteDirectionDestination(value);
        return this;
    }

    @Override
    public NetworkRouteDirectionsHistoryRecord value10(DateRange value) {
        setNetworkRouteDirectionValidDateRange(value);
        return this;
    }

    @Override
    public NetworkRouteDirectionsHistoryRecord value11(TimeRange value) {
        setNetworkRouteDirectionSysPeriod(value);
        return this;
    }

    @Override
    public NetworkRouteDirectionsHistoryRecord values(UUID value1, UUID value2, String value3, String value4, Integer value5, JSONB value6, JSONB value7, JSONB value8, JSONB value9, DateRange value10, TimeRange value11) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached NetworkRouteDirectionsHistoryRecord
     */
    public NetworkRouteDirectionsHistoryRecord() {
        super(NetworkRouteDirectionsHistory.NETWORK_ROUTE_DIRECTIONS_HISTORY);
    }

    /**
     * Create a detached, initialised NetworkRouteDirectionsHistoryRecord
     */
    public NetworkRouteDirectionsHistoryRecord(UUID networkRouteDirectionId, UUID networkRouteId, String networkRouteDirectionType, String networkRouteDirectionExtId, Integer networkRouteDirectionLength, JSONB networkRouteDirectionName, JSONB networkRouteDirectionNameShort, JSONB networkRouteDirectionOrigin, JSONB networkRouteDirectionDestination, DateRange networkRouteDirectionValidDateRange, TimeRange networkRouteDirectionSysPeriod) {
        super(NetworkRouteDirectionsHistory.NETWORK_ROUTE_DIRECTIONS_HISTORY);

        setNetworkRouteDirectionId(networkRouteDirectionId);
        setNetworkRouteId(networkRouteId);
        setNetworkRouteDirectionType(networkRouteDirectionType);
        setNetworkRouteDirectionExtId(networkRouteDirectionExtId);
        setNetworkRouteDirectionLength(networkRouteDirectionLength);
        setNetworkRouteDirectionName(networkRouteDirectionName);
        setNetworkRouteDirectionNameShort(networkRouteDirectionNameShort);
        setNetworkRouteDirectionOrigin(networkRouteDirectionOrigin);
        setNetworkRouteDirectionDestination(networkRouteDirectionDestination);
        setNetworkRouteDirectionValidDateRange(networkRouteDirectionValidDateRange);
        setNetworkRouteDirectionSysPeriod(networkRouteDirectionSysPeriod);
    }
}

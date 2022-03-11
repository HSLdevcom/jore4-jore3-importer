/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutesWithHistory;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkRoutesWithHistoryRecord extends TableRecordImpl<NetworkRoutesWithHistoryRecord> implements Record6<UUID, UUID, String, String, JSONB, TimeRange> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>network.network_routes_with_history.network_route_id</code>.
     */
    public void setNetworkRouteId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for
     * <code>network.network_routes_with_history.network_route_id</code>.
     */
    public UUID getNetworkRouteId() {
        return (UUID) get(0);
    }

    /**
     * Setter for
     * <code>network.network_routes_with_history.network_line_id</code>.
     */
    public void setNetworkLineId(UUID value) {
        set(1, value);
    }

    /**
     * Getter for
     * <code>network.network_routes_with_history.network_line_id</code>.
     */
    public UUID getNetworkLineId() {
        return (UUID) get(1);
    }

    /**
     * Setter for
     * <code>network.network_routes_with_history.network_route_ext_id</code>.
     */
    public void setNetworkRouteExtId(String value) {
        set(2, value);
    }

    /**
     * Getter for
     * <code>network.network_routes_with_history.network_route_ext_id</code>.
     */
    public String getNetworkRouteExtId() {
        return (String) get(2);
    }

    /**
     * Setter for
     * <code>network.network_routes_with_history.network_route_number</code>.
     */
    public void setNetworkRouteNumber(String value) {
        set(3, value);
    }

    /**
     * Getter for
     * <code>network.network_routes_with_history.network_route_number</code>.
     */
    public String getNetworkRouteNumber() {
        return (String) get(3);
    }

    /**
     * Setter for
     * <code>network.network_routes_with_history.network_route_name</code>.
     */
    public void setNetworkRouteName(JSONB value) {
        set(4, value);
    }

    /**
     * Getter for
     * <code>network.network_routes_with_history.network_route_name</code>.
     */
    public JSONB getNetworkRouteName() {
        return (JSONB) get(4);
    }

    /**
     * Setter for
     * <code>network.network_routes_with_history.network_route_sys_period</code>.
     */
    public void setNetworkRouteSysPeriod(TimeRange value) {
        set(5, value);
    }

    /**
     * Getter for
     * <code>network.network_routes_with_history.network_route_sys_period</code>.
     */
    public TimeRange getNetworkRouteSysPeriod() {
        return (TimeRange) get(5);
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<UUID, UUID, String, String, JSONB, TimeRange> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<UUID, UUID, String, String, JSONB, TimeRange> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return NetworkRoutesWithHistory.NETWORK_ROUTES_WITH_HISTORY.NETWORK_ROUTE_ID;
    }

    @Override
    public Field<UUID> field2() {
        return NetworkRoutesWithHistory.NETWORK_ROUTES_WITH_HISTORY.NETWORK_LINE_ID;
    }

    @Override
    public Field<String> field3() {
        return NetworkRoutesWithHistory.NETWORK_ROUTES_WITH_HISTORY.NETWORK_ROUTE_EXT_ID;
    }

    @Override
    public Field<String> field4() {
        return NetworkRoutesWithHistory.NETWORK_ROUTES_WITH_HISTORY.NETWORK_ROUTE_NUMBER;
    }

    @Override
    public Field<JSONB> field5() {
        return NetworkRoutesWithHistory.NETWORK_ROUTES_WITH_HISTORY.NETWORK_ROUTE_NAME;
    }

    @Override
    public Field<TimeRange> field6() {
        return NetworkRoutesWithHistory.NETWORK_ROUTES_WITH_HISTORY.NETWORK_ROUTE_SYS_PERIOD;
    }

    @Override
    public UUID component1() {
        return getNetworkRouteId();
    }

    @Override
    public UUID component2() {
        return getNetworkLineId();
    }

    @Override
    public String component3() {
        return getNetworkRouteExtId();
    }

    @Override
    public String component4() {
        return getNetworkRouteNumber();
    }

    @Override
    public JSONB component5() {
        return getNetworkRouteName();
    }

    @Override
    public TimeRange component6() {
        return getNetworkRouteSysPeriod();
    }

    @Override
    public UUID value1() {
        return getNetworkRouteId();
    }

    @Override
    public UUID value2() {
        return getNetworkLineId();
    }

    @Override
    public String value3() {
        return getNetworkRouteExtId();
    }

    @Override
    public String value4() {
        return getNetworkRouteNumber();
    }

    @Override
    public JSONB value5() {
        return getNetworkRouteName();
    }

    @Override
    public TimeRange value6() {
        return getNetworkRouteSysPeriod();
    }

    @Override
    public NetworkRoutesWithHistoryRecord value1(UUID value) {
        setNetworkRouteId(value);
        return this;
    }

    @Override
    public NetworkRoutesWithHistoryRecord value2(UUID value) {
        setNetworkLineId(value);
        return this;
    }

    @Override
    public NetworkRoutesWithHistoryRecord value3(String value) {
        setNetworkRouteExtId(value);
        return this;
    }

    @Override
    public NetworkRoutesWithHistoryRecord value4(String value) {
        setNetworkRouteNumber(value);
        return this;
    }

    @Override
    public NetworkRoutesWithHistoryRecord value5(JSONB value) {
        setNetworkRouteName(value);
        return this;
    }

    @Override
    public NetworkRoutesWithHistoryRecord value6(TimeRange value) {
        setNetworkRouteSysPeriod(value);
        return this;
    }

    @Override
    public NetworkRoutesWithHistoryRecord values(UUID value1, UUID value2, String value3, String value4, JSONB value5, TimeRange value6) {
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
     * Create a detached NetworkRoutesWithHistoryRecord
     */
    public NetworkRoutesWithHistoryRecord() {
        super(NetworkRoutesWithHistory.NETWORK_ROUTES_WITH_HISTORY);
    }

    /**
     * Create a detached, initialised NetworkRoutesWithHistoryRecord
     */
    public NetworkRoutesWithHistoryRecord(UUID networkRouteId, UUID networkLineId, String networkRouteExtId, String networkRouteNumber, JSONB networkRouteName, TimeRange networkRouteSysPeriod) {
        super(NetworkRoutesWithHistory.NETWORK_ROUTES_WITH_HISTORY);

        setNetworkRouteId(networkRouteId);
        setNetworkLineId(networkLineId);
        setNetworkRouteExtId(networkRouteExtId);
        setNetworkRouteNumber(networkRouteNumber);
        setNetworkRouteName(networkRouteName);
        setNetworkRouteSysPeriod(networkRouteSysPeriod);
    }
}

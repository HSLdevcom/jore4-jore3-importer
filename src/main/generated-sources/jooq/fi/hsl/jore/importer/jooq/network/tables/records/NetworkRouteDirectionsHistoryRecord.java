/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirectionsHistory;

import java.util.UUID;

import org.jooq.JSONB;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkRouteDirectionsHistoryRecord extends TableRecordImpl<NetworkRouteDirectionsHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>network.network_route_directions_history.network_route_direction_id</code>.
     */
    public void setNetworkRouteDirectionId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for
     * <code>network.network_route_directions_history.network_route_direction_id</code>.
     */
    public UUID getNetworkRouteDirectionId() {
        return (UUID) get(0);
    }

    /**
     * Setter for
     * <code>network.network_route_directions_history.network_route_id</code>.
     */
    public void setNetworkRouteId(UUID value) {
        set(1, value);
    }

    /**
     * Getter for
     * <code>network.network_route_directions_history.network_route_id</code>.
     */
    public UUID getNetworkRouteId() {
        return (UUID) get(1);
    }

    /**
     * Setter for
     * <code>network.network_route_directions_history.network_route_direction_type</code>.
     */
    public void setNetworkRouteDirectionType(String value) {
        set(2, value);
    }

    /**
     * Getter for
     * <code>network.network_route_directions_history.network_route_direction_type</code>.
     */
    public String getNetworkRouteDirectionType() {
        return (String) get(2);
    }

    /**
     * Setter for
     * <code>network.network_route_directions_history.network_route_direction_ext_id</code>.
     */
    public void setNetworkRouteDirectionExtId(String value) {
        set(3, value);
    }

    /**
     * Getter for
     * <code>network.network_route_directions_history.network_route_direction_ext_id</code>.
     */
    public String getNetworkRouteDirectionExtId() {
        return (String) get(3);
    }

    /**
     * Setter for
     * <code>network.network_route_directions_history.network_route_direction_length</code>.
     */
    public void setNetworkRouteDirectionLength(Integer value) {
        set(4, value);
    }

    /**
     * Getter for
     * <code>network.network_route_directions_history.network_route_direction_length</code>.
     */
    public Integer getNetworkRouteDirectionLength() {
        return (Integer) get(4);
    }

    /**
     * Setter for
     * <code>network.network_route_directions_history.network_route_direction_name</code>.
     */
    public void setNetworkRouteDirectionName(JSONB value) {
        set(5, value);
    }

    /**
     * Getter for
     * <code>network.network_route_directions_history.network_route_direction_name</code>.
     */
    public JSONB getNetworkRouteDirectionName() {
        return (JSONB) get(5);
    }

    /**
     * Setter for
     * <code>network.network_route_directions_history.network_route_direction_name_short</code>.
     */
    public void setNetworkRouteDirectionNameShort(JSONB value) {
        set(6, value);
    }

    /**
     * Getter for
     * <code>network.network_route_directions_history.network_route_direction_name_short</code>.
     */
    public JSONB getNetworkRouteDirectionNameShort() {
        return (JSONB) get(6);
    }

    /**
     * Setter for
     * <code>network.network_route_directions_history.network_route_direction_origin</code>.
     */
    public void setNetworkRouteDirectionOrigin(JSONB value) {
        set(7, value);
    }

    /**
     * Getter for
     * <code>network.network_route_directions_history.network_route_direction_origin</code>.
     */
    public JSONB getNetworkRouteDirectionOrigin() {
        return (JSONB) get(7);
    }

    /**
     * Setter for
     * <code>network.network_route_directions_history.network_route_direction_destination</code>.
     */
    public void setNetworkRouteDirectionDestination(JSONB value) {
        set(8, value);
    }

    /**
     * Getter for
     * <code>network.network_route_directions_history.network_route_direction_destination</code>.
     */
    public JSONB getNetworkRouteDirectionDestination() {
        return (JSONB) get(8);
    }

    /**
     * Setter for
     * <code>network.network_route_directions_history.network_route_direction_valid_date_range</code>.
     */
    public void setNetworkRouteDirectionValidDateRange(DateRange value) {
        set(9, value);
    }

    /**
     * Getter for
     * <code>network.network_route_directions_history.network_route_direction_valid_date_range</code>.
     */
    public DateRange getNetworkRouteDirectionValidDateRange() {
        return (DateRange) get(9);
    }

    /**
     * Setter for
     * <code>network.network_route_directions_history.network_route_direction_sys_period</code>.
     */
    public void setNetworkRouteDirectionSysPeriod(TimeRange value) {
        set(10, value);
    }

    /**
     * Getter for
     * <code>network.network_route_directions_history.network_route_direction_sys_period</code>.
     */
    public TimeRange getNetworkRouteDirectionSysPeriod() {
        return (TimeRange) get(10);
    }

    /**
     * Setter for
     * <code>network.network_route_directions_history.network_route_jore4_id</code>.
     */
    public void setNetworkRouteJore4Id(UUID value) {
        set(11, value);
    }

    /**
     * Getter for
     * <code>network.network_route_directions_history.network_route_jore4_id</code>.
     */
    public UUID getNetworkRouteJore4Id() {
        return (UUID) get(11);
    }

    /**
     * Setter for
     * <code>network.network_route_directions_history.journey_pattern_jore4_id</code>.
     */
    public void setJourneyPatternJore4Id(UUID value) {
        set(12, value);
    }

    /**
     * Getter for
     * <code>network.network_route_directions_history.journey_pattern_jore4_id</code>.
     */
    public UUID getJourneyPatternJore4Id() {
        return (UUID) get(12);
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
    public NetworkRouteDirectionsHistoryRecord(UUID networkRouteDirectionId, UUID networkRouteId, String networkRouteDirectionType, String networkRouteDirectionExtId, Integer networkRouteDirectionLength, JSONB networkRouteDirectionName, JSONB networkRouteDirectionNameShort, JSONB networkRouteDirectionOrigin, JSONB networkRouteDirectionDestination, DateRange networkRouteDirectionValidDateRange, TimeRange networkRouteDirectionSysPeriod, UUID networkRouteJore4Id, UUID journeyPatternJore4Id) {
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
        setNetworkRouteJore4Id(networkRouteJore4Id);
        setJourneyPatternJore4Id(journeyPatternJore4Id);
        resetChangedOnNotNull();
    }
}

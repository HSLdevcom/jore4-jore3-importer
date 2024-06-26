/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.network.tables.NetworkPlacesWithHistory;

import java.util.UUID;

import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkPlacesWithHistoryRecord extends TableRecordImpl<NetworkPlacesWithHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>network.network_places_with_history.network_place_id</code>.
     */
    public void setNetworkPlaceId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for
     * <code>network.network_places_with_history.network_place_id</code>.
     */
    public UUID getNetworkPlaceId() {
        return (UUID) get(0);
    }

    /**
     * Setter for
     * <code>network.network_places_with_history.network_place_ext_id</code>.
     */
    public void setNetworkPlaceExtId(String value) {
        set(1, value);
    }

    /**
     * Getter for
     * <code>network.network_places_with_history.network_place_ext_id</code>.
     */
    public String getNetworkPlaceExtId() {
        return (String) get(1);
    }

    /**
     * Setter for
     * <code>network.network_places_with_history.network_place_name</code>.
     */
    public void setNetworkPlaceName(String value) {
        set(2, value);
    }

    /**
     * Getter for
     * <code>network.network_places_with_history.network_place_name</code>.
     */
    public String getNetworkPlaceName() {
        return (String) get(2);
    }

    /**
     * Setter for
     * <code>network.network_places_with_history.network_place_sys_period</code>.
     */
    public void setNetworkPlaceSysPeriod(TimeRange value) {
        set(3, value);
    }

    /**
     * Getter for
     * <code>network.network_places_with_history.network_place_sys_period</code>.
     */
    public TimeRange getNetworkPlaceSysPeriod() {
        return (TimeRange) get(3);
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached NetworkPlacesWithHistoryRecord
     */
    public NetworkPlacesWithHistoryRecord() {
        super(NetworkPlacesWithHistory.NETWORK_PLACES_WITH_HISTORY);
    }

    /**
     * Create a detached, initialised NetworkPlacesWithHistoryRecord
     */
    public NetworkPlacesWithHistoryRecord(UUID networkPlaceId, String networkPlaceExtId, String networkPlaceName, TimeRange networkPlaceSysPeriod) {
        super(NetworkPlacesWithHistory.NETWORK_PLACES_WITH_HISTORY);

        setNetworkPlaceId(networkPlaceId);
        setNetworkPlaceExtId(networkPlaceExtId);
        setNetworkPlaceName(networkPlaceName);
        setNetworkPlaceSysPeriod(networkPlaceSysPeriod);
        resetChangedOnNotNull();
    }
}

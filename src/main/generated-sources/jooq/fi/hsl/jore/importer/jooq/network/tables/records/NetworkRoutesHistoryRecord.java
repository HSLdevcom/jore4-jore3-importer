/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutesHistory;

import java.util.UUID;

import org.jooq.JSONB;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkRoutesHistoryRecord extends TableRecordImpl<NetworkRoutesHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>network.network_routes_history.network_route_id</code>.
     */
    public void setNetworkRouteId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>network.network_routes_history.network_route_id</code>.
     */
    public UUID getNetworkRouteId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>network.network_routes_history.network_line_id</code>.
     */
    public void setNetworkLineId(UUID value) {
        set(1, value);
    }

    /**
     * Getter for <code>network.network_routes_history.network_line_id</code>.
     */
    public UUID getNetworkLineId() {
        return (UUID) get(1);
    }

    /**
     * Setter for
     * <code>network.network_routes_history.network_route_ext_id</code>.
     */
    public void setNetworkRouteExtId(String value) {
        set(2, value);
    }

    /**
     * Getter for
     * <code>network.network_routes_history.network_route_ext_id</code>.
     */
    public String getNetworkRouteExtId() {
        return (String) get(2);
    }

    /**
     * Setter for
     * <code>network.network_routes_history.network_route_number</code>.
     */
    public void setNetworkRouteNumber(String value) {
        set(3, value);
    }

    /**
     * Getter for
     * <code>network.network_routes_history.network_route_number</code>.
     */
    public String getNetworkRouteNumber() {
        return (String) get(3);
    }

    /**
     * Setter for
     * <code>network.network_routes_history.network_route_name</code>.
     */
    public void setNetworkRouteName(JSONB value) {
        set(4, value);
    }

    /**
     * Getter for
     * <code>network.network_routes_history.network_route_name</code>.
     */
    public JSONB getNetworkRouteName() {
        return (JSONB) get(4);
    }

    /**
     * Setter for
     * <code>network.network_routes_history.network_route_sys_period</code>.
     */
    public void setNetworkRouteSysPeriod(TimeRange value) {
        set(5, value);
    }

    /**
     * Getter for
     * <code>network.network_routes_history.network_route_sys_period</code>.
     */
    public TimeRange getNetworkRouteSysPeriod() {
        return (TimeRange) get(5);
    }

    /**
     * Setter for
     * <code>network.network_routes_history.network_route_hidden_variant</code>.
     */
    public void setNetworkRouteHiddenVariant(Short value) {
        set(6, value);
    }

    /**
     * Getter for
     * <code>network.network_routes_history.network_route_hidden_variant</code>.
     */
    public Short getNetworkRouteHiddenVariant() {
        return (Short) get(6);
    }

    /**
     * Setter for
     * <code>network.network_routes_history.network_route_legacy_hsl_municipality_code</code>.
     */
    public void setNetworkRouteLegacyHslMunicipalityCode(String value) {
        set(7, value);
    }

    /**
     * Getter for
     * <code>network.network_routes_history.network_route_legacy_hsl_municipality_code</code>.
     */
    public String getNetworkRouteLegacyHslMunicipalityCode() {
        return (String) get(7);
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached NetworkRoutesHistoryRecord
     */
    public NetworkRoutesHistoryRecord() {
        super(NetworkRoutesHistory.NETWORK_ROUTES_HISTORY);
    }

    /**
     * Create a detached, initialised NetworkRoutesHistoryRecord
     */
    public NetworkRoutesHistoryRecord(UUID networkRouteId, UUID networkLineId, String networkRouteExtId, String networkRouteNumber, JSONB networkRouteName, TimeRange networkRouteSysPeriod, Short networkRouteHiddenVariant, String networkRouteLegacyHslMunicipalityCode) {
        super(NetworkRoutesHistory.NETWORK_ROUTES_HISTORY);

        setNetworkRouteId(networkRouteId);
        setNetworkLineId(networkLineId);
        setNetworkRouteExtId(networkRouteExtId);
        setNetworkRouteNumber(networkRouteNumber);
        setNetworkRouteName(networkRouteName);
        setNetworkRouteSysPeriod(networkRouteSysPeriod);
        setNetworkRouteHiddenVariant(networkRouteHiddenVariant);
        setNetworkRouteLegacyHslMunicipalityCode(networkRouteLegacyHslMunicipalityCode);
        resetChangedOnNotNull();
    }
}

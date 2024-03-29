/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutesStaging;

import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkRoutesStagingRecord extends UpdatableRecordImpl<NetworkRoutesStagingRecord> implements Record6<String, String, String, JSONB, Short, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>network.network_routes_staging.network_route_ext_id</code>.
     */
    public void setNetworkRouteExtId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>network.network_routes_staging.network_route_ext_id</code>.
     */
    public String getNetworkRouteExtId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>network.network_routes_staging.network_line_ext_id</code>.
     */
    public void setNetworkLineExtId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>network.network_routes_staging.network_line_ext_id</code>.
     */
    public String getNetworkLineExtId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>network.network_routes_staging.network_route_number</code>.
     */
    public void setNetworkRouteNumber(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>network.network_routes_staging.network_route_number</code>.
     */
    public String getNetworkRouteNumber() {
        return (String) get(2);
    }

    /**
     * Setter for <code>network.network_routes_staging.network_route_name</code>.
     */
    public void setNetworkRouteName(JSONB value) {
        set(3, value);
    }

    /**
     * Getter for <code>network.network_routes_staging.network_route_name</code>.
     */
    public JSONB getNetworkRouteName() {
        return (JSONB) get(3);
    }

    /**
     * Setter for <code>network.network_routes_staging.network_route_hidden_variant</code>.
     */
    public void setNetworkRouteHiddenVariant(Short value) {
        set(4, value);
    }

    /**
     * Getter for <code>network.network_routes_staging.network_route_hidden_variant</code>.
     */
    public Short getNetworkRouteHiddenVariant() {
        return (Short) get(4);
    }

    /**
     * Setter for <code>network.network_routes_staging.network_route_legacy_hsl_municipality_code</code>.
     */
    public void setNetworkRouteLegacyHslMunicipalityCode(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>network.network_routes_staging.network_route_legacy_hsl_municipality_code</code>.
     */
    public String getNetworkRouteLegacyHslMunicipalityCode() {
        return (String) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<String, String, String, JSONB, Short, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<String, String, String, JSONB, Short, String> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return NetworkRoutesStaging.NETWORK_ROUTES_STAGING.NETWORK_ROUTE_EXT_ID;
    }

    @Override
    public Field<String> field2() {
        return NetworkRoutesStaging.NETWORK_ROUTES_STAGING.NETWORK_LINE_EXT_ID;
    }

    @Override
    public Field<String> field3() {
        return NetworkRoutesStaging.NETWORK_ROUTES_STAGING.NETWORK_ROUTE_NUMBER;
    }

    @Override
    public Field<JSONB> field4() {
        return NetworkRoutesStaging.NETWORK_ROUTES_STAGING.NETWORK_ROUTE_NAME;
    }

    @Override
    public Field<Short> field5() {
        return NetworkRoutesStaging.NETWORK_ROUTES_STAGING.NETWORK_ROUTE_HIDDEN_VARIANT;
    }

    @Override
    public Field<String> field6() {
        return NetworkRoutesStaging.NETWORK_ROUTES_STAGING.NETWORK_ROUTE_LEGACY_HSL_MUNICIPALITY_CODE;
    }

    @Override
    public String component1() {
        return getNetworkRouteExtId();
    }

    @Override
    public String component2() {
        return getNetworkLineExtId();
    }

    @Override
    public String component3() {
        return getNetworkRouteNumber();
    }

    @Override
    public JSONB component4() {
        return getNetworkRouteName();
    }

    @Override
    public Short component5() {
        return getNetworkRouteHiddenVariant();
    }

    @Override
    public String component6() {
        return getNetworkRouteLegacyHslMunicipalityCode();
    }

    @Override
    public String value1() {
        return getNetworkRouteExtId();
    }

    @Override
    public String value2() {
        return getNetworkLineExtId();
    }

    @Override
    public String value3() {
        return getNetworkRouteNumber();
    }

    @Override
    public JSONB value4() {
        return getNetworkRouteName();
    }

    @Override
    public Short value5() {
        return getNetworkRouteHiddenVariant();
    }

    @Override
    public String value6() {
        return getNetworkRouteLegacyHslMunicipalityCode();
    }

    @Override
    public NetworkRoutesStagingRecord value1(String value) {
        setNetworkRouteExtId(value);
        return this;
    }

    @Override
    public NetworkRoutesStagingRecord value2(String value) {
        setNetworkLineExtId(value);
        return this;
    }

    @Override
    public NetworkRoutesStagingRecord value3(String value) {
        setNetworkRouteNumber(value);
        return this;
    }

    @Override
    public NetworkRoutesStagingRecord value4(JSONB value) {
        setNetworkRouteName(value);
        return this;
    }

    @Override
    public NetworkRoutesStagingRecord value5(Short value) {
        setNetworkRouteHiddenVariant(value);
        return this;
    }

    @Override
    public NetworkRoutesStagingRecord value6(String value) {
        setNetworkRouteLegacyHslMunicipalityCode(value);
        return this;
    }

    @Override
    public NetworkRoutesStagingRecord values(String value1, String value2, String value3, JSONB value4, Short value5, String value6) {
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
     * Create a detached NetworkRoutesStagingRecord
     */
    public NetworkRoutesStagingRecord() {
        super(NetworkRoutesStaging.NETWORK_ROUTES_STAGING);
    }

    /**
     * Create a detached, initialised NetworkRoutesStagingRecord
     */
    public NetworkRoutesStagingRecord(String networkRouteExtId, String networkLineExtId, String networkRouteNumber, JSONB networkRouteName, Short networkRouteHiddenVariant, String networkRouteLegacyHslMunicipalityCode) {
        super(NetworkRoutesStaging.NETWORK_ROUTES_STAGING);

        setNetworkRouteExtId(networkRouteExtId);
        setNetworkLineExtId(networkLineExtId);
        setNetworkRouteNumber(networkRouteNumber);
        setNetworkRouteName(networkRouteName);
        setNetworkRouteHiddenVariant(networkRouteHiddenVariant);
        setNetworkRouteLegacyHslMunicipalityCode(networkRouteLegacyHslMunicipalityCode);
    }
}

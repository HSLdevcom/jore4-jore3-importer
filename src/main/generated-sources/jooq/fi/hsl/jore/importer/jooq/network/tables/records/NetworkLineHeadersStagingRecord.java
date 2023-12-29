/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLineHeadersStaging;

import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkLineHeadersStagingRecord extends UpdatableRecordImpl<NetworkLineHeadersStagingRecord> implements Record7<String, String, JSONB, JSONB, JSONB, JSONB, DateRange> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>network.network_line_headers_staging.network_line_header_ext_id</code>.
     */
    public void setNetworkLineHeaderExtId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>network.network_line_headers_staging.network_line_header_ext_id</code>.
     */
    public String getNetworkLineHeaderExtId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>network.network_line_headers_staging.network_line_ext_id</code>.
     */
    public void setNetworkLineExtId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>network.network_line_headers_staging.network_line_ext_id</code>.
     */
    public String getNetworkLineExtId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>network.network_line_headers_staging.network_line_header_name</code>.
     */
    public void setNetworkLineHeaderName(JSONB value) {
        set(2, value);
    }

    /**
     * Getter for <code>network.network_line_headers_staging.network_line_header_name</code>.
     */
    public JSONB getNetworkLineHeaderName() {
        return (JSONB) get(2);
    }

    /**
     * Setter for <code>network.network_line_headers_staging.network_line_header_name_short</code>.
     */
    public void setNetworkLineHeaderNameShort(JSONB value) {
        set(3, value);
    }

    /**
     * Getter for <code>network.network_line_headers_staging.network_line_header_name_short</code>.
     */
    public JSONB getNetworkLineHeaderNameShort() {
        return (JSONB) get(3);
    }

    /**
     * Setter for <code>network.network_line_headers_staging.network_line_header_origin_1</code>.
     */
    public void setNetworkLineHeaderOrigin_1(JSONB value) {
        set(4, value);
    }

    /**
     * Getter for <code>network.network_line_headers_staging.network_line_header_origin_1</code>.
     */
    public JSONB getNetworkLineHeaderOrigin_1() {
        return (JSONB) get(4);
    }

    /**
     * Setter for <code>network.network_line_headers_staging.network_line_header_origin_2</code>.
     */
    public void setNetworkLineHeaderOrigin_2(JSONB value) {
        set(5, value);
    }

    /**
     * Getter for <code>network.network_line_headers_staging.network_line_header_origin_2</code>.
     */
    public JSONB getNetworkLineHeaderOrigin_2() {
        return (JSONB) get(5);
    }

    /**
     * Setter for <code>network.network_line_headers_staging.network_line_header_valid_date_range</code>.
     */
    public void setNetworkLineHeaderValidDateRange(DateRange value) {
        set(6, value);
    }

    /**
     * Getter for <code>network.network_line_headers_staging.network_line_header_valid_date_range</code>.
     */
    public DateRange getNetworkLineHeaderValidDateRange() {
        return (DateRange) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<String, String, JSONB, JSONB, JSONB, JSONB, DateRange> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<String, String, JSONB, JSONB, JSONB, JSONB, DateRange> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return NetworkLineHeadersStaging.NETWORK_LINE_HEADERS_STAGING.NETWORK_LINE_HEADER_EXT_ID;
    }

    @Override
    public Field<String> field2() {
        return NetworkLineHeadersStaging.NETWORK_LINE_HEADERS_STAGING.NETWORK_LINE_EXT_ID;
    }

    @Override
    public Field<JSONB> field3() {
        return NetworkLineHeadersStaging.NETWORK_LINE_HEADERS_STAGING.NETWORK_LINE_HEADER_NAME;
    }

    @Override
    public Field<JSONB> field4() {
        return NetworkLineHeadersStaging.NETWORK_LINE_HEADERS_STAGING.NETWORK_LINE_HEADER_NAME_SHORT;
    }

    @Override
    public Field<JSONB> field5() {
        return NetworkLineHeadersStaging.NETWORK_LINE_HEADERS_STAGING.NETWORK_LINE_HEADER_ORIGIN_1;
    }

    @Override
    public Field<JSONB> field6() {
        return NetworkLineHeadersStaging.NETWORK_LINE_HEADERS_STAGING.NETWORK_LINE_HEADER_ORIGIN_2;
    }

    @Override
    public Field<DateRange> field7() {
        return NetworkLineHeadersStaging.NETWORK_LINE_HEADERS_STAGING.NETWORK_LINE_HEADER_VALID_DATE_RANGE;
    }

    @Override
    public String component1() {
        return getNetworkLineHeaderExtId();
    }

    @Override
    public String component2() {
        return getNetworkLineExtId();
    }

    @Override
    public JSONB component3() {
        return getNetworkLineHeaderName();
    }

    @Override
    public JSONB component4() {
        return getNetworkLineHeaderNameShort();
    }

    @Override
    public JSONB component5() {
        return getNetworkLineHeaderOrigin_1();
    }

    @Override
    public JSONB component6() {
        return getNetworkLineHeaderOrigin_2();
    }

    @Override
    public DateRange component7() {
        return getNetworkLineHeaderValidDateRange();
    }

    @Override
    public String value1() {
        return getNetworkLineHeaderExtId();
    }

    @Override
    public String value2() {
        return getNetworkLineExtId();
    }

    @Override
    public JSONB value3() {
        return getNetworkLineHeaderName();
    }

    @Override
    public JSONB value4() {
        return getNetworkLineHeaderNameShort();
    }

    @Override
    public JSONB value5() {
        return getNetworkLineHeaderOrigin_1();
    }

    @Override
    public JSONB value6() {
        return getNetworkLineHeaderOrigin_2();
    }

    @Override
    public DateRange value7() {
        return getNetworkLineHeaderValidDateRange();
    }

    @Override
    public NetworkLineHeadersStagingRecord value1(String value) {
        setNetworkLineHeaderExtId(value);
        return this;
    }

    @Override
    public NetworkLineHeadersStagingRecord value2(String value) {
        setNetworkLineExtId(value);
        return this;
    }

    @Override
    public NetworkLineHeadersStagingRecord value3(JSONB value) {
        setNetworkLineHeaderName(value);
        return this;
    }

    @Override
    public NetworkLineHeadersStagingRecord value4(JSONB value) {
        setNetworkLineHeaderNameShort(value);
        return this;
    }

    @Override
    public NetworkLineHeadersStagingRecord value5(JSONB value) {
        setNetworkLineHeaderOrigin_1(value);
        return this;
    }

    @Override
    public NetworkLineHeadersStagingRecord value6(JSONB value) {
        setNetworkLineHeaderOrigin_2(value);
        return this;
    }

    @Override
    public NetworkLineHeadersStagingRecord value7(DateRange value) {
        setNetworkLineHeaderValidDateRange(value);
        return this;
    }

    @Override
    public NetworkLineHeadersStagingRecord values(String value1, String value2, JSONB value3, JSONB value4, JSONB value5, JSONB value6, DateRange value7) {
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
     * Create a detached NetworkLineHeadersStagingRecord
     */
    public NetworkLineHeadersStagingRecord() {
        super(NetworkLineHeadersStaging.NETWORK_LINE_HEADERS_STAGING);
    }

    /**
     * Create a detached, initialised NetworkLineHeadersStagingRecord
     */
    public NetworkLineHeadersStagingRecord(String networkLineHeaderExtId, String networkLineExtId, JSONB networkLineHeaderName, JSONB networkLineHeaderNameShort, JSONB networkLineHeaderOrigin_1, JSONB networkLineHeaderOrigin_2, DateRange networkLineHeaderValidDateRange) {
        super(NetworkLineHeadersStaging.NETWORK_LINE_HEADERS_STAGING);

        setNetworkLineHeaderExtId(networkLineHeaderExtId);
        setNetworkLineExtId(networkLineExtId);
        setNetworkLineHeaderName(networkLineHeaderName);
        setNetworkLineHeaderNameShort(networkLineHeaderNameShort);
        setNetworkLineHeaderOrigin_1(networkLineHeaderOrigin_1);
        setNetworkLineHeaderOrigin_2(networkLineHeaderOrigin_2);
        setNetworkLineHeaderValidDateRange(networkLineHeaderValidDateRange);
    }
}
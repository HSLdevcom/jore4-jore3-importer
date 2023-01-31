/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLines;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkLinesRecord extends UpdatableRecordImpl<NetworkLinesRecord> implements Record8<UUID, String, String, String, TimeRange, UUID, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>network.network_lines.network_line_id</code>.
     */
    public void setNetworkLineId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>network.network_lines.network_line_id</code>.
     */
    public UUID getNetworkLineId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>network.network_lines.network_line_ext_id</code>.
     */
    public void setNetworkLineExtId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>network.network_lines.network_line_ext_id</code>.
     */
    public String getNetworkLineExtId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>network.network_lines.network_line_number</code>.
     */
    public void setNetworkLineNumber(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>network.network_lines.network_line_number</code>.
     */
    public String getNetworkLineNumber() {
        return (String) get(2);
    }

    /**
     * Setter for
     * <code>network.network_lines.infrastructure_network_type</code>.
     */
    public void setInfrastructureNetworkType(String value) {
        set(3, value);
    }

    /**
     * Getter for
     * <code>network.network_lines.infrastructure_network_type</code>.
     */
    public String getInfrastructureNetworkType() {
        return (String) get(3);
    }

    /**
     * Setter for <code>network.network_lines.network_line_sys_period</code>.
     */
    public void setNetworkLineSysPeriod(TimeRange value) {
        set(4, value);
    }

    /**
     * Getter for <code>network.network_lines.network_line_sys_period</code>.
     */
    public TimeRange getNetworkLineSysPeriod() {
        return (TimeRange) get(4);
    }

    /**
     * Setter for <code>network.network_lines.network_line_jore4_id</code>.
     */
    public void setNetworkLineJore4Id(UUID value) {
        set(5, value);
    }

    /**
     * Getter for <code>network.network_lines.network_line_jore4_id</code>.
     */
    public UUID getNetworkLineJore4Id() {
        return (UUID) get(5);
    }

    /**
     * Setter for <code>network.network_lines.network_line_type_of_line</code>.
     */
    public void setNetworkLineTypeOfLine(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>network.network_lines.network_line_type_of_line</code>.
     */
    public String getNetworkLineTypeOfLine() {
        return (String) get(6);
    }

    /**
     * Setter for
     * <code>network.network_lines.network_line_legacy_hsl_municipality_code</code>.
     */
    public void setNetworkLineLegacyHslMunicipalityCode(String value) {
        set(7, value);
    }

    /**
     * Getter for
     * <code>network.network_lines.network_line_legacy_hsl_municipality_code</code>.
     */
    public String getNetworkLineLegacyHslMunicipalityCode() {
        return (String) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row8<UUID, String, String, String, TimeRange, UUID, String, String> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<UUID, String, String, String, TimeRange, UUID, String, String> valuesRow() {
        return (Row8) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return NetworkLines.NETWORK_LINES.NETWORK_LINE_ID;
    }

    @Override
    public Field<String> field2() {
        return NetworkLines.NETWORK_LINES.NETWORK_LINE_EXT_ID;
    }

    @Override
    public Field<String> field3() {
        return NetworkLines.NETWORK_LINES.NETWORK_LINE_NUMBER;
    }

    @Override
    public Field<String> field4() {
        return NetworkLines.NETWORK_LINES.INFRASTRUCTURE_NETWORK_TYPE;
    }

    @Override
    public Field<TimeRange> field5() {
        return NetworkLines.NETWORK_LINES.NETWORK_LINE_SYS_PERIOD;
    }

    @Override
    public Field<UUID> field6() {
        return NetworkLines.NETWORK_LINES.NETWORK_LINE_JORE4_ID;
    }

    @Override
    public Field<String> field7() {
        return NetworkLines.NETWORK_LINES.NETWORK_LINE_TYPE_OF_LINE;
    }

    @Override
    public Field<String> field8() {
        return NetworkLines.NETWORK_LINES.NETWORK_LINE_LEGACY_HSL_MUNICIPALITY_CODE;
    }

    @Override
    public UUID component1() {
        return getNetworkLineId();
    }

    @Override
    public String component2() {
        return getNetworkLineExtId();
    }

    @Override
    public String component3() {
        return getNetworkLineNumber();
    }

    @Override
    public String component4() {
        return getInfrastructureNetworkType();
    }

    @Override
    public TimeRange component5() {
        return getNetworkLineSysPeriod();
    }

    @Override
    public UUID component6() {
        return getNetworkLineJore4Id();
    }

    @Override
    public String component7() {
        return getNetworkLineTypeOfLine();
    }

    @Override
    public String component8() {
        return getNetworkLineLegacyHslMunicipalityCode();
    }

    @Override
    public UUID value1() {
        return getNetworkLineId();
    }

    @Override
    public String value2() {
        return getNetworkLineExtId();
    }

    @Override
    public String value3() {
        return getNetworkLineNumber();
    }

    @Override
    public String value4() {
        return getInfrastructureNetworkType();
    }

    @Override
    public TimeRange value5() {
        return getNetworkLineSysPeriod();
    }

    @Override
    public UUID value6() {
        return getNetworkLineJore4Id();
    }

    @Override
    public String value7() {
        return getNetworkLineTypeOfLine();
    }

    @Override
    public String value8() {
        return getNetworkLineLegacyHslMunicipalityCode();
    }

    @Override
    public NetworkLinesRecord value1(UUID value) {
        setNetworkLineId(value);
        return this;
    }

    @Override
    public NetworkLinesRecord value2(String value) {
        setNetworkLineExtId(value);
        return this;
    }

    @Override
    public NetworkLinesRecord value3(String value) {
        setNetworkLineNumber(value);
        return this;
    }

    @Override
    public NetworkLinesRecord value4(String value) {
        setInfrastructureNetworkType(value);
        return this;
    }

    @Override
    public NetworkLinesRecord value5(TimeRange value) {
        setNetworkLineSysPeriod(value);
        return this;
    }

    @Override
    public NetworkLinesRecord value6(UUID value) {
        setNetworkLineJore4Id(value);
        return this;
    }

    @Override
    public NetworkLinesRecord value7(String value) {
        setNetworkLineTypeOfLine(value);
        return this;
    }

    @Override
    public NetworkLinesRecord value8(String value) {
        setNetworkLineLegacyHslMunicipalityCode(value);
        return this;
    }

    @Override
    public NetworkLinesRecord values(UUID value1, String value2, String value3, String value4, TimeRange value5, UUID value6, String value7, String value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached NetworkLinesRecord
     */
    public NetworkLinesRecord() {
        super(NetworkLines.NETWORK_LINES);
    }

    /**
     * Create a detached, initialised NetworkLinesRecord
     */
    public NetworkLinesRecord(UUID networkLineId, String networkLineExtId, String networkLineNumber, String infrastructureNetworkType, TimeRange networkLineSysPeriod, UUID networkLineJore4Id, String networkLineTypeOfLine, String networkLineLegacyHslMunicipalityCode) {
        super(NetworkLines.NETWORK_LINES);

        setNetworkLineId(networkLineId);
        setNetworkLineExtId(networkLineExtId);
        setNetworkLineNumber(networkLineNumber);
        setInfrastructureNetworkType(infrastructureNetworkType);
        setNetworkLineSysPeriod(networkLineSysPeriod);
        setNetworkLineJore4Id(networkLineJore4Id);
        setNetworkLineTypeOfLine(networkLineTypeOfLine);
        setNetworkLineLegacyHslMunicipalityCode(networkLineLegacyHslMunicipalityCode);
    }
}

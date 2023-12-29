/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLinesHistory;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkLinesHistoryRecord extends TableRecordImpl<NetworkLinesHistoryRecord> implements Record7<UUID, String, String, String, TimeRange, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>network.network_lines_history.network_line_id</code>.
     */
    public void setNetworkLineId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>network.network_lines_history.network_line_id</code>.
     */
    public UUID getNetworkLineId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>network.network_lines_history.network_line_ext_id</code>.
     */
    public void setNetworkLineExtId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>network.network_lines_history.network_line_ext_id</code>.
     */
    public String getNetworkLineExtId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>network.network_lines_history.network_line_number</code>.
     */
    public void setNetworkLineNumber(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>network.network_lines_history.network_line_number</code>.
     */
    public String getNetworkLineNumber() {
        return (String) get(2);
    }

    /**
     * Setter for <code>network.network_lines_history.infrastructure_network_type</code>.
     */
    public void setInfrastructureNetworkType(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>network.network_lines_history.infrastructure_network_type</code>.
     */
    public String getInfrastructureNetworkType() {
        return (String) get(3);
    }

    /**
     * Setter for <code>network.network_lines_history.network_line_sys_period</code>.
     */
    public void setNetworkLineSysPeriod(TimeRange value) {
        set(4, value);
    }

    /**
     * Getter for <code>network.network_lines_history.network_line_sys_period</code>.
     */
    public TimeRange getNetworkLineSysPeriod() {
        return (TimeRange) get(4);
    }

    /**
     * Setter for <code>network.network_lines_history.network_line_type_of_line</code>.
     */
    public void setNetworkLineTypeOfLine(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>network.network_lines_history.network_line_type_of_line</code>.
     */
    public String getNetworkLineTypeOfLine() {
        return (String) get(5);
    }

    /**
     * Setter for <code>network.network_lines_history.network_line_legacy_hsl_municipality_code</code>.
     */
    public void setNetworkLineLegacyHslMunicipalityCode(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>network.network_lines_history.network_line_legacy_hsl_municipality_code</code>.
     */
    public String getNetworkLineLegacyHslMunicipalityCode() {
        return (String) get(6);
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<UUID, String, String, String, TimeRange, String, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<UUID, String, String, String, TimeRange, String, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return NetworkLinesHistory.NETWORK_LINES_HISTORY.NETWORK_LINE_ID;
    }

    @Override
    public Field<String> field2() {
        return NetworkLinesHistory.NETWORK_LINES_HISTORY.NETWORK_LINE_EXT_ID;
    }

    @Override
    public Field<String> field3() {
        return NetworkLinesHistory.NETWORK_LINES_HISTORY.NETWORK_LINE_NUMBER;
    }

    @Override
    public Field<String> field4() {
        return NetworkLinesHistory.NETWORK_LINES_HISTORY.INFRASTRUCTURE_NETWORK_TYPE;
    }

    @Override
    public Field<TimeRange> field5() {
        return NetworkLinesHistory.NETWORK_LINES_HISTORY.NETWORK_LINE_SYS_PERIOD;
    }

    @Override
    public Field<String> field6() {
        return NetworkLinesHistory.NETWORK_LINES_HISTORY.NETWORK_LINE_TYPE_OF_LINE;
    }

    @Override
    public Field<String> field7() {
        return NetworkLinesHistory.NETWORK_LINES_HISTORY.NETWORK_LINE_LEGACY_HSL_MUNICIPALITY_CODE;
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
    public String component6() {
        return getNetworkLineTypeOfLine();
    }

    @Override
    public String component7() {
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
    public String value6() {
        return getNetworkLineTypeOfLine();
    }

    @Override
    public String value7() {
        return getNetworkLineLegacyHslMunicipalityCode();
    }

    @Override
    public NetworkLinesHistoryRecord value1(UUID value) {
        setNetworkLineId(value);
        return this;
    }

    @Override
    public NetworkLinesHistoryRecord value2(String value) {
        setNetworkLineExtId(value);
        return this;
    }

    @Override
    public NetworkLinesHistoryRecord value3(String value) {
        setNetworkLineNumber(value);
        return this;
    }

    @Override
    public NetworkLinesHistoryRecord value4(String value) {
        setInfrastructureNetworkType(value);
        return this;
    }

    @Override
    public NetworkLinesHistoryRecord value5(TimeRange value) {
        setNetworkLineSysPeriod(value);
        return this;
    }

    @Override
    public NetworkLinesHistoryRecord value6(String value) {
        setNetworkLineTypeOfLine(value);
        return this;
    }

    @Override
    public NetworkLinesHistoryRecord value7(String value) {
        setNetworkLineLegacyHslMunicipalityCode(value);
        return this;
    }

    @Override
    public NetworkLinesHistoryRecord values(UUID value1, String value2, String value3, String value4, TimeRange value5, String value6, String value7) {
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
     * Create a detached NetworkLinesHistoryRecord
     */
    public NetworkLinesHistoryRecord() {
        super(NetworkLinesHistory.NETWORK_LINES_HISTORY);
    }

    /**
     * Create a detached, initialised NetworkLinesHistoryRecord
     */
    public NetworkLinesHistoryRecord(UUID networkLineId, String networkLineExtId, String networkLineNumber, String infrastructureNetworkType, TimeRange networkLineSysPeriod, String networkLineTypeOfLine, String networkLineLegacyHslMunicipalityCode) {
        super(NetworkLinesHistory.NETWORK_LINES_HISTORY);

        setNetworkLineId(networkLineId);
        setNetworkLineExtId(networkLineExtId);
        setNetworkLineNumber(networkLineNumber);
        setInfrastructureNetworkType(infrastructureNetworkType);
        setNetworkLineSysPeriod(networkLineSysPeriod);
        setNetworkLineTypeOfLine(networkLineTypeOfLine);
        setNetworkLineLegacyHslMunicipalityCode(networkLineLegacyHslMunicipalityCode);
    }
}

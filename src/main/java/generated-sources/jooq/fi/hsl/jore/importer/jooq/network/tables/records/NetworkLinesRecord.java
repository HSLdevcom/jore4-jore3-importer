/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLines;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkLinesRecord extends UpdatableRecordImpl<NetworkLinesRecord> implements Record6<UUID, String, String, String, TimeRange, UUID> {

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
     * Setter for <code>network.network_lines.network_line_transmodel_id</code>.
     */
    public void setNetworkLineTransmodelId(UUID value) {
        set(5, value);
    }

    /**
     * Getter for <code>network.network_lines.network_line_transmodel_id</code>.
     */
    public UUID getNetworkLineTransmodelId() {
        return (UUID) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<UUID, String, String, String, TimeRange, UUID> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<UUID, String, String, String, TimeRange, UUID> valuesRow() {
        return (Row6) super.valuesRow();
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
        return NetworkLines.NETWORK_LINES.NETWORK_LINE_TRANSMODEL_ID;
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
        return getNetworkLineTransmodelId();
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
        return getNetworkLineTransmodelId();
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
        setNetworkLineTransmodelId(value);
        return this;
    }

    @Override
    public NetworkLinesRecord values(UUID value1, String value2, String value3, String value4, TimeRange value5, UUID value6) {
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
     * Create a detached NetworkLinesRecord
     */
    public NetworkLinesRecord() {
        super(NetworkLines.NETWORK_LINES);
    }

    /**
     * Create a detached, initialised NetworkLinesRecord
     */
    public NetworkLinesRecord(UUID networkLineId, String networkLineExtId, String networkLineNumber, String infrastructureNetworkType, TimeRange networkLineSysPeriod, UUID networkLineTransmodelId) {
        super(NetworkLines.NETWORK_LINES);

        setNetworkLineId(networkLineId);
        setNetworkLineExtId(networkLineExtId);
        setNetworkLineNumber(networkLineNumber);
        setInfrastructureNetworkType(infrastructureNetworkType);
        setNetworkLineSysPeriod(networkLineSysPeriod);
        setNetworkLineTransmodelId(networkLineTransmodelId);
    }
}

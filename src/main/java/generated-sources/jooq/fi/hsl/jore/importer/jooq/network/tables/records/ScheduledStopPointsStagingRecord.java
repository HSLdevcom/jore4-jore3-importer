/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPointsStaging;

import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ScheduledStopPointsStagingRecord extends UpdatableRecordImpl<ScheduledStopPointsStagingRecord> implements Record4<String, String, JSONB, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>network.scheduled_stop_points_staging.scheduled_stop_point_ext_id</code>.
     */
    public void setScheduledStopPointExtId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>network.scheduled_stop_points_staging.scheduled_stop_point_ext_id</code>.
     */
    public String getScheduledStopPointExtId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>network.scheduled_stop_points_staging.scheduled_stop_point_ely_number</code>.
     */
    public void setScheduledStopPointElyNumber(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>network.scheduled_stop_points_staging.scheduled_stop_point_ely_number</code>.
     */
    public String getScheduledStopPointElyNumber() {
        return (String) get(1);
    }

    /**
     * Setter for <code>network.scheduled_stop_points_staging.scheduled_stop_point_name</code>.
     */
    public void setScheduledStopPointName(JSONB value) {
        set(2, value);
    }

    /**
     * Getter for <code>network.scheduled_stop_points_staging.scheduled_stop_point_name</code>.
     */
    public JSONB getScheduledStopPointName() {
        return (JSONB) get(2);
    }

    /**
     * Setter for <code>network.scheduled_stop_points_staging.scheduled_stop_point_short_id</code>.
     */
    public void setScheduledStopPointShortId(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>network.scheduled_stop_points_staging.scheduled_stop_point_short_id</code>.
     */
    public String getScheduledStopPointShortId() {
        return (String) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, JSONB, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<String, String, JSONB, String> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return ScheduledStopPointsStaging.SCHEDULED_STOP_POINTS_STAGING.SCHEDULED_STOP_POINT_EXT_ID;
    }

    @Override
    public Field<String> field2() {
        return ScheduledStopPointsStaging.SCHEDULED_STOP_POINTS_STAGING.SCHEDULED_STOP_POINT_ELY_NUMBER;
    }

    @Override
    public Field<JSONB> field3() {
        return ScheduledStopPointsStaging.SCHEDULED_STOP_POINTS_STAGING.SCHEDULED_STOP_POINT_NAME;
    }

    @Override
    public Field<String> field4() {
        return ScheduledStopPointsStaging.SCHEDULED_STOP_POINTS_STAGING.SCHEDULED_STOP_POINT_SHORT_ID;
    }

    @Override
    public String component1() {
        return getScheduledStopPointExtId();
    }

    @Override
    public String component2() {
        return getScheduledStopPointElyNumber();
    }

    @Override
    public JSONB component3() {
        return getScheduledStopPointName();
    }

    @Override
    public String component4() {
        return getScheduledStopPointShortId();
    }

    @Override
    public String value1() {
        return getScheduledStopPointExtId();
    }

    @Override
    public String value2() {
        return getScheduledStopPointElyNumber();
    }

    @Override
    public JSONB value3() {
        return getScheduledStopPointName();
    }

    @Override
    public String value4() {
        return getScheduledStopPointShortId();
    }

    @Override
    public ScheduledStopPointsStagingRecord value1(String value) {
        setScheduledStopPointExtId(value);
        return this;
    }

    @Override
    public ScheduledStopPointsStagingRecord value2(String value) {
        setScheduledStopPointElyNumber(value);
        return this;
    }

    @Override
    public ScheduledStopPointsStagingRecord value3(JSONB value) {
        setScheduledStopPointName(value);
        return this;
    }

    @Override
    public ScheduledStopPointsStagingRecord value4(String value) {
        setScheduledStopPointShortId(value);
        return this;
    }

    @Override
    public ScheduledStopPointsStagingRecord values(String value1, String value2, JSONB value3, String value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ScheduledStopPointsStagingRecord
     */
    public ScheduledStopPointsStagingRecord() {
        super(ScheduledStopPointsStaging.SCHEDULED_STOP_POINTS_STAGING);
    }

    /**
     * Create a detached, initialised ScheduledStopPointsStagingRecord
     */
    public ScheduledStopPointsStagingRecord(String scheduledStopPointExtId, String scheduledStopPointElyNumber, JSONB scheduledStopPointName, String scheduledStopPointShortId) {
        super(ScheduledStopPointsStaging.SCHEDULED_STOP_POINTS_STAGING);

        setScheduledStopPointExtId(scheduledStopPointExtId);
        setScheduledStopPointElyNumber(scheduledStopPointElyNumber);
        setScheduledStopPointName(scheduledStopPointName);
        setScheduledStopPointShortId(scheduledStopPointShortId);
    }
}

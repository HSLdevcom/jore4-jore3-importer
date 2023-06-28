/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPointsHistory;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record10;
import org.jooq.Row10;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ScheduledStopPointsHistoryRecord extends TableRecordImpl<ScheduledStopPointsHistoryRecord> implements Record10<UUID, String, UUID, Long, JSONB, TimeRange, String, UUID, Integer, UUID> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>network.scheduled_stop_points_history.scheduled_stop_point_id</code>.
     */
    public void setScheduledStopPointId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>network.scheduled_stop_points_history.scheduled_stop_point_id</code>.
     */
    public UUID getScheduledStopPointId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>network.scheduled_stop_points_history.scheduled_stop_point_ext_id</code>.
     */
    public void setScheduledStopPointExtId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>network.scheduled_stop_points_history.scheduled_stop_point_ext_id</code>.
     */
    public String getScheduledStopPointExtId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>network.scheduled_stop_points_history.infrastructure_node_id</code>.
     */
    public void setInfrastructureNodeId(UUID value) {
        set(2, value);
    }

    /**
     * Getter for <code>network.scheduled_stop_points_history.infrastructure_node_id</code>.
     */
    public UUID getInfrastructureNodeId() {
        return (UUID) get(2);
    }

    /**
     * Setter for <code>network.scheduled_stop_points_history.scheduled_stop_point_ely_number</code>.
     */
    public void setScheduledStopPointElyNumber(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>network.scheduled_stop_points_history.scheduled_stop_point_ely_number</code>.
     */
    public Long getScheduledStopPointElyNumber() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>network.scheduled_stop_points_history.scheduled_stop_point_name</code>.
     */
    public void setScheduledStopPointName(JSONB value) {
        set(4, value);
    }

    /**
     * Getter for <code>network.scheduled_stop_points_history.scheduled_stop_point_name</code>.
     */
    public JSONB getScheduledStopPointName() {
        return (JSONB) get(4);
    }

    /**
     * Setter for <code>network.scheduled_stop_points_history.scheduled_stop_point_sys_period</code>.
     */
    public void setScheduledStopPointSysPeriod(TimeRange value) {
        set(5, value);
    }

    /**
     * Getter for <code>network.scheduled_stop_points_history.scheduled_stop_point_sys_period</code>.
     */
    public TimeRange getScheduledStopPointSysPeriod() {
        return (TimeRange) get(5);
    }

    /**
     * Setter for <code>network.scheduled_stop_points_history.scheduled_stop_point_short_id</code>.
     */
    public void setScheduledStopPointShortId(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>network.scheduled_stop_points_history.scheduled_stop_point_short_id</code>.
     */
    public String getScheduledStopPointShortId() {
        return (String) get(6);
    }

    /**
     * Setter for <code>network.scheduled_stop_points_history.scheduled_stop_point_jore4_id</code>.
     */
    public void setScheduledStopPointJore4Id(UUID value) {
        set(7, value);
    }

    /**
     * Getter for <code>network.scheduled_stop_points_history.scheduled_stop_point_jore4_id</code>.
     */
    public UUID getScheduledStopPointJore4Id() {
        return (UUID) get(7);
    }

    /**
     * Setter for <code>network.scheduled_stop_points_history.usage_in_routes</code>.
     */
    public void setUsageInRoutes(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>network.scheduled_stop_points_history.usage_in_routes</code>.
     */
    public Integer getUsageInRoutes() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>network.scheduled_stop_points_history.network_place_id</code>.
     */
    public void setNetworkPlaceId(UUID value) {
        set(9, value);
    }

    /**
     * Getter for <code>network.scheduled_stop_points_history.network_place_id</code>.
     */
    public UUID getNetworkPlaceId() {
        return (UUID) get(9);
    }

    // -------------------------------------------------------------------------
    // Record10 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row10<UUID, String, UUID, Long, JSONB, TimeRange, String, UUID, Integer, UUID> fieldsRow() {
        return (Row10) super.fieldsRow();
    }

    @Override
    public Row10<UUID, String, UUID, Long, JSONB, TimeRange, String, UUID, Integer, UUID> valuesRow() {
        return (Row10) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return ScheduledStopPointsHistory.SCHEDULED_STOP_POINTS_HISTORY.SCHEDULED_STOP_POINT_ID;
    }

    @Override
    public Field<String> field2() {
        return ScheduledStopPointsHistory.SCHEDULED_STOP_POINTS_HISTORY.SCHEDULED_STOP_POINT_EXT_ID;
    }

    @Override
    public Field<UUID> field3() {
        return ScheduledStopPointsHistory.SCHEDULED_STOP_POINTS_HISTORY.INFRASTRUCTURE_NODE_ID;
    }

    @Override
    public Field<Long> field4() {
        return ScheduledStopPointsHistory.SCHEDULED_STOP_POINTS_HISTORY.SCHEDULED_STOP_POINT_ELY_NUMBER;
    }

    @Override
    public Field<JSONB> field5() {
        return ScheduledStopPointsHistory.SCHEDULED_STOP_POINTS_HISTORY.SCHEDULED_STOP_POINT_NAME;
    }

    @Override
    public Field<TimeRange> field6() {
        return ScheduledStopPointsHistory.SCHEDULED_STOP_POINTS_HISTORY.SCHEDULED_STOP_POINT_SYS_PERIOD;
    }

    @Override
    public Field<String> field7() {
        return ScheduledStopPointsHistory.SCHEDULED_STOP_POINTS_HISTORY.SCHEDULED_STOP_POINT_SHORT_ID;
    }

    @Override
    public Field<UUID> field8() {
        return ScheduledStopPointsHistory.SCHEDULED_STOP_POINTS_HISTORY.SCHEDULED_STOP_POINT_JORE4_ID;
    }

    @Override
    public Field<Integer> field9() {
        return ScheduledStopPointsHistory.SCHEDULED_STOP_POINTS_HISTORY.USAGE_IN_ROUTES;
    }

    @Override
    public Field<UUID> field10() {
        return ScheduledStopPointsHistory.SCHEDULED_STOP_POINTS_HISTORY.NETWORK_PLACE_ID;
    }

    @Override
    public UUID component1() {
        return getScheduledStopPointId();
    }

    @Override
    public String component2() {
        return getScheduledStopPointExtId();
    }

    @Override
    public UUID component3() {
        return getInfrastructureNodeId();
    }

    @Override
    public Long component4() {
        return getScheduledStopPointElyNumber();
    }

    @Override
    public JSONB component5() {
        return getScheduledStopPointName();
    }

    @Override
    public TimeRange component6() {
        return getScheduledStopPointSysPeriod();
    }

    @Override
    public String component7() {
        return getScheduledStopPointShortId();
    }

    @Override
    public UUID component8() {
        return getScheduledStopPointJore4Id();
    }

    @Override
    public Integer component9() {
        return getUsageInRoutes();
    }

    @Override
    public UUID component10() {
        return getNetworkPlaceId();
    }

    @Override
    public UUID value1() {
        return getScheduledStopPointId();
    }

    @Override
    public String value2() {
        return getScheduledStopPointExtId();
    }

    @Override
    public UUID value3() {
        return getInfrastructureNodeId();
    }

    @Override
    public Long value4() {
        return getScheduledStopPointElyNumber();
    }

    @Override
    public JSONB value5() {
        return getScheduledStopPointName();
    }

    @Override
    public TimeRange value6() {
        return getScheduledStopPointSysPeriod();
    }

    @Override
    public String value7() {
        return getScheduledStopPointShortId();
    }

    @Override
    public UUID value8() {
        return getScheduledStopPointJore4Id();
    }

    @Override
    public Integer value9() {
        return getUsageInRoutes();
    }

    @Override
    public UUID value10() {
        return getNetworkPlaceId();
    }

    @Override
    public ScheduledStopPointsHistoryRecord value1(UUID value) {
        setScheduledStopPointId(value);
        return this;
    }

    @Override
    public ScheduledStopPointsHistoryRecord value2(String value) {
        setScheduledStopPointExtId(value);
        return this;
    }

    @Override
    public ScheduledStopPointsHistoryRecord value3(UUID value) {
        setInfrastructureNodeId(value);
        return this;
    }

    @Override
    public ScheduledStopPointsHistoryRecord value4(Long value) {
        setScheduledStopPointElyNumber(value);
        return this;
    }

    @Override
    public ScheduledStopPointsHistoryRecord value5(JSONB value) {
        setScheduledStopPointName(value);
        return this;
    }

    @Override
    public ScheduledStopPointsHistoryRecord value6(TimeRange value) {
        setScheduledStopPointSysPeriod(value);
        return this;
    }

    @Override
    public ScheduledStopPointsHistoryRecord value7(String value) {
        setScheduledStopPointShortId(value);
        return this;
    }

    @Override
    public ScheduledStopPointsHistoryRecord value8(UUID value) {
        setScheduledStopPointJore4Id(value);
        return this;
    }

    @Override
    public ScheduledStopPointsHistoryRecord value9(Integer value) {
        setUsageInRoutes(value);
        return this;
    }

    @Override
    public ScheduledStopPointsHistoryRecord value10(UUID value) {
        setNetworkPlaceId(value);
        return this;
    }

    @Override
    public ScheduledStopPointsHistoryRecord values(UUID value1, String value2, UUID value3, Long value4, JSONB value5, TimeRange value6, String value7, UUID value8, Integer value9, UUID value10) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ScheduledStopPointsHistoryRecord
     */
    public ScheduledStopPointsHistoryRecord() {
        super(ScheduledStopPointsHistory.SCHEDULED_STOP_POINTS_HISTORY);
    }

    /**
     * Create a detached, initialised ScheduledStopPointsHistoryRecord
     */
    public ScheduledStopPointsHistoryRecord(UUID scheduledStopPointId, String scheduledStopPointExtId, UUID infrastructureNodeId, Long scheduledStopPointElyNumber, JSONB scheduledStopPointName, TimeRange scheduledStopPointSysPeriod, String scheduledStopPointShortId, UUID scheduledStopPointJore4Id, Integer usageInRoutes, UUID networkPlaceId) {
        super(ScheduledStopPointsHistory.SCHEDULED_STOP_POINTS_HISTORY);

        setScheduledStopPointId(scheduledStopPointId);
        setScheduledStopPointExtId(scheduledStopPointExtId);
        setInfrastructureNodeId(infrastructureNodeId);
        setScheduledStopPointElyNumber(scheduledStopPointElyNumber);
        setScheduledStopPointName(scheduledStopPointName);
        setScheduledStopPointSysPeriod(scheduledStopPointSysPeriod);
        setScheduledStopPointShortId(scheduledStopPointShortId);
        setScheduledStopPointJore4Id(scheduledStopPointJore4Id);
        setUsageInRoutes(usageInRoutes);
        setNetworkPlaceId(networkPlaceId);
    }
}

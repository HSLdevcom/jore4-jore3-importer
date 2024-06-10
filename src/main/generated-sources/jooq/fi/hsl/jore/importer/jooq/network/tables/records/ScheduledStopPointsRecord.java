/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPoints;

import java.util.UUID;

import org.jooq.JSONB;
import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ScheduledStopPointsRecord extends UpdatableRecordImpl<ScheduledStopPointsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>network.scheduled_stop_points.scheduled_stop_point_id</code>.
     */
    public void setScheduledStopPointId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for
     * <code>network.scheduled_stop_points.scheduled_stop_point_id</code>.
     */
    public UUID getScheduledStopPointId() {
        return (UUID) get(0);
    }

    /**
     * Setter for
     * <code>network.scheduled_stop_points.scheduled_stop_point_ext_id</code>.
     */
    public void setScheduledStopPointExtId(String value) {
        set(1, value);
    }

    /**
     * Getter for
     * <code>network.scheduled_stop_points.scheduled_stop_point_ext_id</code>.
     */
    public String getScheduledStopPointExtId() {
        return (String) get(1);
    }

    /**
     * Setter for
     * <code>network.scheduled_stop_points.infrastructure_node_id</code>.
     */
    public void setInfrastructureNodeId(UUID value) {
        set(2, value);
    }

    /**
     * Getter for
     * <code>network.scheduled_stop_points.infrastructure_node_id</code>.
     */
    public UUID getInfrastructureNodeId() {
        return (UUID) get(2);
    }

    /**
     * Setter for
     * <code>network.scheduled_stop_points.scheduled_stop_point_ely_number</code>.
     */
    public void setScheduledStopPointElyNumber(Long value) {
        set(3, value);
    }

    /**
     * Getter for
     * <code>network.scheduled_stop_points.scheduled_stop_point_ely_number</code>.
     */
    public Long getScheduledStopPointElyNumber() {
        return (Long) get(3);
    }

    /**
     * Setter for
     * <code>network.scheduled_stop_points.scheduled_stop_point_name</code>.
     */
    public void setScheduledStopPointName(JSONB value) {
        set(4, value);
    }

    /**
     * Getter for
     * <code>network.scheduled_stop_points.scheduled_stop_point_name</code>.
     */
    public JSONB getScheduledStopPointName() {
        return (JSONB) get(4);
    }

    /**
     * Setter for
     * <code>network.scheduled_stop_points.scheduled_stop_point_sys_period</code>.
     */
    public void setScheduledStopPointSysPeriod(TimeRange value) {
        set(5, value);
    }

    /**
     * Getter for
     * <code>network.scheduled_stop_points.scheduled_stop_point_sys_period</code>.
     */
    public TimeRange getScheduledStopPointSysPeriod() {
        return (TimeRange) get(5);
    }

    /**
     * Setter for
     * <code>network.scheduled_stop_points.scheduled_stop_point_short_id</code>.
     */
    public void setScheduledStopPointShortId(String value) {
        set(6, value);
    }

    /**
     * Getter for
     * <code>network.scheduled_stop_points.scheduled_stop_point_short_id</code>.
     */
    public String getScheduledStopPointShortId() {
        return (String) get(6);
    }

    /**
     * Setter for
     * <code>network.scheduled_stop_points.scheduled_stop_point_jore4_id</code>.
     */
    public void setScheduledStopPointJore4Id(UUID value) {
        set(7, value);
    }

    /**
     * Getter for
     * <code>network.scheduled_stop_points.scheduled_stop_point_jore4_id</code>.
     */
    public UUID getScheduledStopPointJore4Id() {
        return (UUID) get(7);
    }

    /**
     * Setter for <code>network.scheduled_stop_points.usage_in_routes</code>.
     */
    public void setUsageInRoutes(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>network.scheduled_stop_points.usage_in_routes</code>.
     */
    public Integer getUsageInRoutes() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>network.scheduled_stop_points.network_place_id</code>.
     */
    public void setNetworkPlaceId(UUID value) {
        set(9, value);
    }

    /**
     * Getter for <code>network.scheduled_stop_points.network_place_id</code>.
     */
    public UUID getNetworkPlaceId() {
        return (UUID) get(9);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ScheduledStopPointsRecord
     */
    public ScheduledStopPointsRecord() {
        super(ScheduledStopPoints.SCHEDULED_STOP_POINTS);
    }

    /**
     * Create a detached, initialised ScheduledStopPointsRecord
     */
    public ScheduledStopPointsRecord(UUID scheduledStopPointId, String scheduledStopPointExtId, UUID infrastructureNodeId, Long scheduledStopPointElyNumber, JSONB scheduledStopPointName, TimeRange scheduledStopPointSysPeriod, String scheduledStopPointShortId, UUID scheduledStopPointJore4Id, Integer usageInRoutes, UUID networkPlaceId) {
        super(ScheduledStopPoints.SCHEDULED_STOP_POINTS);

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
        resetChangedOnNotNull();
    }
}

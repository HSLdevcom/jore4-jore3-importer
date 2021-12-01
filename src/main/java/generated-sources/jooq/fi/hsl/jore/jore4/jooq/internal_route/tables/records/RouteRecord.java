/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.internal_route.tables.records;


import fi.hsl.jore.jore4.jooq.internal_route.tables.Route;

import java.time.LocalDateTime;
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
public class RouteRecord extends UpdatableRecordImpl<RouteRecord> implements Record8<UUID, String, UUID, UUID, UUID, LocalDateTime, LocalDateTime, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>internal_route.route.route_id</code>.
     */
    public void setRouteId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>internal_route.route.route_id</code>.
     */
    public UUID getRouteId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>internal_route.route.description_i18n</code>.
     */
    public void setDescriptionI18n(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>internal_route.route.description_i18n</code>.
     */
    public String getDescriptionI18n() {
        return (String) get(1);
    }

    /**
     * Setter for <code>internal_route.route.starts_from_scheduled_stop_point_id</code>.
     */
    public void setStartsFromScheduledStopPointId(UUID value) {
        set(2, value);
    }

    /**
     * Getter for <code>internal_route.route.starts_from_scheduled_stop_point_id</code>.
     */
    public UUID getStartsFromScheduledStopPointId() {
        return (UUID) get(2);
    }

    /**
     * Setter for <code>internal_route.route.ends_at_scheduled_stop_point_id</code>.
     */
    public void setEndsAtScheduledStopPointId(UUID value) {
        set(3, value);
    }

    /**
     * Getter for <code>internal_route.route.ends_at_scheduled_stop_point_id</code>.
     */
    public UUID getEndsAtScheduledStopPointId() {
        return (UUID) get(3);
    }

    /**
     * Setter for <code>internal_route.route.on_line_id</code>.
     */
    public void setOnLineId(UUID value) {
        set(4, value);
    }

    /**
     * Getter for <code>internal_route.route.on_line_id</code>.
     */
    public UUID getOnLineId() {
        return (UUID) get(4);
    }

    /**
     * Setter for <code>internal_route.route.validity_start</code>.
     */
    public void setValidityStart(LocalDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>internal_route.route.validity_start</code>.
     */
    public LocalDateTime getValidityStart() {
        return (LocalDateTime) get(5);
    }

    /**
     * Setter for <code>internal_route.route.validity_end</code>.
     */
    public void setValidityEnd(LocalDateTime value) {
        set(6, value);
    }

    /**
     * Getter for <code>internal_route.route.validity_end</code>.
     */
    public LocalDateTime getValidityEnd() {
        return (LocalDateTime) get(6);
    }

    /**
     * Setter for <code>internal_route.route.priority</code>.
     */
    public void setPriority(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>internal_route.route.priority</code>.
     */
    public Integer getPriority() {
        return (Integer) get(7);
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
    public Row8<UUID, String, UUID, UUID, UUID, LocalDateTime, LocalDateTime, Integer> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<UUID, String, UUID, UUID, UUID, LocalDateTime, LocalDateTime, Integer> valuesRow() {
        return (Row8) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return Route.ROUTE.ROUTE_ID;
    }

    @Override
    public Field<String> field2() {
        return Route.ROUTE.DESCRIPTION_I18N;
    }

    @Override
    public Field<UUID> field3() {
        return Route.ROUTE.STARTS_FROM_SCHEDULED_STOP_POINT_ID;
    }

    @Override
    public Field<UUID> field4() {
        return Route.ROUTE.ENDS_AT_SCHEDULED_STOP_POINT_ID;
    }

    @Override
    public Field<UUID> field5() {
        return Route.ROUTE.ON_LINE_ID;
    }

    @Override
    public Field<LocalDateTime> field6() {
        return Route.ROUTE.VALIDITY_START;
    }

    @Override
    public Field<LocalDateTime> field7() {
        return Route.ROUTE.VALIDITY_END;
    }

    @Override
    public Field<Integer> field8() {
        return Route.ROUTE.PRIORITY;
    }

    @Override
    public UUID component1() {
        return getRouteId();
    }

    @Override
    public String component2() {
        return getDescriptionI18n();
    }

    @Override
    public UUID component3() {
        return getStartsFromScheduledStopPointId();
    }

    @Override
    public UUID component4() {
        return getEndsAtScheduledStopPointId();
    }

    @Override
    public UUID component5() {
        return getOnLineId();
    }

    @Override
    public LocalDateTime component6() {
        return getValidityStart();
    }

    @Override
    public LocalDateTime component7() {
        return getValidityEnd();
    }

    @Override
    public Integer component8() {
        return getPriority();
    }

    @Override
    public UUID value1() {
        return getRouteId();
    }

    @Override
    public String value2() {
        return getDescriptionI18n();
    }

    @Override
    public UUID value3() {
        return getStartsFromScheduledStopPointId();
    }

    @Override
    public UUID value4() {
        return getEndsAtScheduledStopPointId();
    }

    @Override
    public UUID value5() {
        return getOnLineId();
    }

    @Override
    public LocalDateTime value6() {
        return getValidityStart();
    }

    @Override
    public LocalDateTime value7() {
        return getValidityEnd();
    }

    @Override
    public Integer value8() {
        return getPriority();
    }

    @Override
    public RouteRecord value1(UUID value) {
        setRouteId(value);
        return this;
    }

    @Override
    public RouteRecord value2(String value) {
        setDescriptionI18n(value);
        return this;
    }

    @Override
    public RouteRecord value3(UUID value) {
        setStartsFromScheduledStopPointId(value);
        return this;
    }

    @Override
    public RouteRecord value4(UUID value) {
        setEndsAtScheduledStopPointId(value);
        return this;
    }

    @Override
    public RouteRecord value5(UUID value) {
        setOnLineId(value);
        return this;
    }

    @Override
    public RouteRecord value6(LocalDateTime value) {
        setValidityStart(value);
        return this;
    }

    @Override
    public RouteRecord value7(LocalDateTime value) {
        setValidityEnd(value);
        return this;
    }

    @Override
    public RouteRecord value8(Integer value) {
        setPriority(value);
        return this;
    }

    @Override
    public RouteRecord values(UUID value1, String value2, UUID value3, UUID value4, UUID value5, LocalDateTime value6, LocalDateTime value7, Integer value8) {
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
     * Create a detached RouteRecord
     */
    public RouteRecord() {
        super(Route.ROUTE);
    }

    /**
     * Create a detached, initialised RouteRecord
     */
    public RouteRecord(UUID routeId, String descriptionI18n, UUID startsFromScheduledStopPointId, UUID endsAtScheduledStopPointId, UUID onLineId, LocalDateTime validityStart, LocalDateTime validityEnd, Integer priority) {
        super(Route.ROUTE);

        setRouteId(routeId);
        setDescriptionI18n(descriptionI18n);
        setStartsFromScheduledStopPointId(startsFromScheduledStopPointId);
        setEndsAtScheduledStopPointId(endsAtScheduledStopPointId);
        setOnLineId(onLineId);
        setValidityStart(validityStart);
        setValidityEnd(validityEnd);
        setPriority(priority);
    }
}

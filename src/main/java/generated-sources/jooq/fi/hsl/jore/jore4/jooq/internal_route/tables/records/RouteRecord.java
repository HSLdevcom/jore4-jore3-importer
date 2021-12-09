/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.internal_route.tables.records;


import fi.hsl.jore.jore4.jooq.internal_route.tables.Route;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Row10;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RouteRecord extends UpdatableRecordImpl<RouteRecord> implements Record10<UUID, String, UUID, UUID, UUID, OffsetDateTime, OffsetDateTime, Integer, String, String> {

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
    public void setValidityStart(OffsetDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>internal_route.route.validity_start</code>.
     */
    public OffsetDateTime getValidityStart() {
        return (OffsetDateTime) get(5);
    }

    /**
     * Setter for <code>internal_route.route.validity_end</code>.
     */
    public void setValidityEnd(OffsetDateTime value) {
        set(6, value);
    }

    /**
     * Getter for <code>internal_route.route.validity_end</code>.
     */
    public OffsetDateTime getValidityEnd() {
        return (OffsetDateTime) get(6);
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

    /**
     * Setter for <code>internal_route.route.label</code>.
     */
    public void setLabel(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>internal_route.route.label</code>.
     */
    public String getLabel() {
        return (String) get(8);
    }

    /**
     * Setter for <code>internal_route.route.direction</code>.
     */
    public void setDirection(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>internal_route.route.direction</code>.
     */
    public String getDirection() {
        return (String) get(9);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record10 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row10<UUID, String, UUID, UUID, UUID, OffsetDateTime, OffsetDateTime, Integer, String, String> fieldsRow() {
        return (Row10) super.fieldsRow();
    }

    @Override
    public Row10<UUID, String, UUID, UUID, UUID, OffsetDateTime, OffsetDateTime, Integer, String, String> valuesRow() {
        return (Row10) super.valuesRow();
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
    public Field<OffsetDateTime> field6() {
        return Route.ROUTE.VALIDITY_START;
    }

    @Override
    public Field<OffsetDateTime> field7() {
        return Route.ROUTE.VALIDITY_END;
    }

    @Override
    public Field<Integer> field8() {
        return Route.ROUTE.PRIORITY;
    }

    @Override
    public Field<String> field9() {
        return Route.ROUTE.LABEL;
    }

    @Override
    public Field<String> field10() {
        return Route.ROUTE.DIRECTION;
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
    public OffsetDateTime component6() {
        return getValidityStart();
    }

    @Override
    public OffsetDateTime component7() {
        return getValidityEnd();
    }

    @Override
    public Integer component8() {
        return getPriority();
    }

    @Override
    public String component9() {
        return getLabel();
    }

    @Override
    public String component10() {
        return getDirection();
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
    public OffsetDateTime value6() {
        return getValidityStart();
    }

    @Override
    public OffsetDateTime value7() {
        return getValidityEnd();
    }

    @Override
    public Integer value8() {
        return getPriority();
    }

    @Override
    public String value9() {
        return getLabel();
    }

    @Override
    public String value10() {
        return getDirection();
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
    public RouteRecord value6(OffsetDateTime value) {
        setValidityStart(value);
        return this;
    }

    @Override
    public RouteRecord value7(OffsetDateTime value) {
        setValidityEnd(value);
        return this;
    }

    @Override
    public RouteRecord value8(Integer value) {
        setPriority(value);
        return this;
    }

    @Override
    public RouteRecord value9(String value) {
        setLabel(value);
        return this;
    }

    @Override
    public RouteRecord value10(String value) {
        setDirection(value);
        return this;
    }

    @Override
    public RouteRecord values(UUID value1, String value2, UUID value3, UUID value4, UUID value5, OffsetDateTime value6, OffsetDateTime value7, Integer value8, String value9, String value10) {
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
     * Create a detached RouteRecord
     */
    public RouteRecord() {
        super(Route.ROUTE);
    }

    /**
     * Create a detached, initialised RouteRecord
     */
    public RouteRecord(UUID routeId, String descriptionI18n, UUID startsFromScheduledStopPointId, UUID endsAtScheduledStopPointId, UUID onLineId, OffsetDateTime validityStart, OffsetDateTime validityEnd, Integer priority, String label, String direction) {
        super(Route.ROUTE);

        setRouteId(routeId);
        setDescriptionI18n(descriptionI18n);
        setStartsFromScheduledStopPointId(startsFromScheduledStopPointId);
        setEndsAtScheduledStopPointId(endsAtScheduledStopPointId);
        setOnLineId(onLineId);
        setValidityStart(validityStart);
        setValidityEnd(validityEnd);
        setPriority(priority);
        setLabel(label);
        setDirection(direction);
    }
}

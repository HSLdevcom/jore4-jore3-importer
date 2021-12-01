/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.service_pattern.tables.records;


import fi.hsl.jore.jore4.jooq.service_pattern.tables.ScheduledStopPoint;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record10;
import org.jooq.Row10;
import org.jooq.impl.TableRecordImpl;
import org.locationtech.jts.geom.Point;


/**
 * The scheduled stop points: https://www.transmodel-cen.eu/model/index.htm?goto=2:3:4:845 
 * . Colloquially known as stops from the perspective of timetable planning.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ScheduledStopPointRecord extends TableRecordImpl<ScheduledStopPointRecord> implements Record10<UUID, String, Point, UUID, String, Double, Object, LocalDateTime, LocalDateTime, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>service_pattern.scheduled_stop_point.scheduled_stop_point_id</code>. The ID of the scheduled stop point.
     */
    public void setScheduledStopPointId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>service_pattern.scheduled_stop_point.scheduled_stop_point_id</code>. The ID of the scheduled stop point.
     */
    public UUID getScheduledStopPointId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>service_pattern.scheduled_stop_point.label</code>. The label is the short code that identifies the stop to the passengers. There can be at most one stop with the same label at a time. The label matches the GTFS stop_code.
     */
    public void setLabel(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>service_pattern.scheduled_stop_point.label</code>. The label is the short code that identifies the stop to the passengers. There can be at most one stop with the same label at a time. The label matches the GTFS stop_code.
     */
    public String getLabel() {
        return (String) get(1);
    }

    /**
     * Setter for <code>service_pattern.scheduled_stop_point.measured_location</code>. The measured location describes the physical location of the stop. For some stops this describes the location of the pole-mounted flag. A PostGIS PointZ geography in EPSG:4326.
     */
    public void setMeasuredLocation(Point value) {
        set(2, value);
    }

    /**
     * Getter for <code>service_pattern.scheduled_stop_point.measured_location</code>. The measured location describes the physical location of the stop. For some stops this describes the location of the pole-mounted flag. A PostGIS PointZ geography in EPSG:4326.
     */
    public Point getMeasuredLocation() {
        return (Point) get(2);
    }

    /**
     * Setter for <code>service_pattern.scheduled_stop_point.located_on_infrastructure_link_id</code>. The infrastructure link on which the stop is located.
     */
    public void setLocatedOnInfrastructureLinkId(UUID value) {
        set(3, value);
    }

    /**
     * Getter for <code>service_pattern.scheduled_stop_point.located_on_infrastructure_link_id</code>. The infrastructure link on which the stop is located.
     */
    public UUID getLocatedOnInfrastructureLinkId() {
        return (UUID) get(3);
    }

    /**
     * Setter for <code>service_pattern.scheduled_stop_point.direction</code>. The direction(s) of traffic with respect to the digitization, i.e. the direction of the specified line string.
     */
    public void setDirection(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>service_pattern.scheduled_stop_point.direction</code>. The direction(s) of traffic with respect to the digitization, i.e. the direction of the specified line string.
     */
    public String getDirection() {
        return (String) get(4);
    }

    /**
     * Setter for <code>service_pattern.scheduled_stop_point.relative_distance_from_infrastructure_link_start</code>. The relative distance of the stop from the start of the linestring along the infrastructure link. Regardless of the specified direction, this value is the distance from the beginning of the linestring. The distance is normalized to the closed interval [0, 1].
     */
    public void setRelativeDistanceFromInfrastructureLinkStart(Double value) {
        set(5, value);
    }

    /**
     * Getter for <code>service_pattern.scheduled_stop_point.relative_distance_from_infrastructure_link_start</code>. The relative distance of the stop from the start of the linestring along the infrastructure link. Regardless of the specified direction, this value is the distance from the beginning of the linestring. The distance is normalized to the closed interval [0, 1].
     */
    public Double getRelativeDistanceFromInfrastructureLinkStart() {
        return (Double) get(5);
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public void setClosestPointOnInfrastructureLink(Object value) {
        set(6, value);
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public Object getClosestPointOnInfrastructureLink() {
        return get(6);
    }

    /**
     * Setter for <code>service_pattern.scheduled_stop_point.validity_start</code>. The point in time when the stop becomes valid. If NULL, the stop has been always valid.
     */
    public void setValidityStart(LocalDateTime value) {
        set(7, value);
    }

    /**
     * Getter for <code>service_pattern.scheduled_stop_point.validity_start</code>. The point in time when the stop becomes valid. If NULL, the stop has been always valid.
     */
    public LocalDateTime getValidityStart() {
        return (LocalDateTime) get(7);
    }

    /**
     * Setter for <code>service_pattern.scheduled_stop_point.validity_end</code>. The point in time from which onwards the stop is no longer valid. If NULL, the stop will be always valid.
     */
    public void setValidityEnd(LocalDateTime value) {
        set(8, value);
    }

    /**
     * Getter for <code>service_pattern.scheduled_stop_point.validity_end</code>. The point in time from which onwards the stop is no longer valid. If NULL, the stop will be always valid.
     */
    public LocalDateTime getValidityEnd() {
        return (LocalDateTime) get(8);
    }

    /**
     * Setter for <code>service_pattern.scheduled_stop_point.priority</code>. The priority of the stop definition. The definition may be overridden by higher priority definitions.
     */
    public void setPriority(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>service_pattern.scheduled_stop_point.priority</code>. The priority of the stop definition. The definition may be overridden by higher priority definitions.
     */
    public Integer getPriority() {
        return (Integer) get(9);
    }

    // -------------------------------------------------------------------------
    // Record10 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row10<UUID, String, Point, UUID, String, Double, Object, LocalDateTime, LocalDateTime, Integer> fieldsRow() {
        return (Row10) super.fieldsRow();
    }

    @Override
    public Row10<UUID, String, Point, UUID, String, Double, Object, LocalDateTime, LocalDateTime, Integer> valuesRow() {
        return (Row10) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return ScheduledStopPoint.SCHEDULED_STOP_POINT.SCHEDULED_STOP_POINT_ID;
    }

    @Override
    public Field<String> field2() {
        return ScheduledStopPoint.SCHEDULED_STOP_POINT.LABEL;
    }

    @Override
    public Field<Point> field3() {
        return ScheduledStopPoint.SCHEDULED_STOP_POINT.MEASURED_LOCATION;
    }

    @Override
    public Field<UUID> field4() {
        return ScheduledStopPoint.SCHEDULED_STOP_POINT.LOCATED_ON_INFRASTRUCTURE_LINK_ID;
    }

    @Override
    public Field<String> field5() {
        return ScheduledStopPoint.SCHEDULED_STOP_POINT.DIRECTION;
    }

    @Override
    public Field<Double> field6() {
        return ScheduledStopPoint.SCHEDULED_STOP_POINT.RELATIVE_DISTANCE_FROM_INFRASTRUCTURE_LINK_START;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    @Override
    public Field<Object> field7() {
        return ScheduledStopPoint.SCHEDULED_STOP_POINT.CLOSEST_POINT_ON_INFRASTRUCTURE_LINK;
    }

    @Override
    public Field<LocalDateTime> field8() {
        return ScheduledStopPoint.SCHEDULED_STOP_POINT.VALIDITY_START;
    }

    @Override
    public Field<LocalDateTime> field9() {
        return ScheduledStopPoint.SCHEDULED_STOP_POINT.VALIDITY_END;
    }

    @Override
    public Field<Integer> field10() {
        return ScheduledStopPoint.SCHEDULED_STOP_POINT.PRIORITY;
    }

    @Override
    public UUID component1() {
        return getScheduledStopPointId();
    }

    @Override
    public String component2() {
        return getLabel();
    }

    @Override
    public Point component3() {
        return getMeasuredLocation();
    }

    @Override
    public UUID component4() {
        return getLocatedOnInfrastructureLinkId();
    }

    @Override
    public String component5() {
        return getDirection();
    }

    @Override
    public Double component6() {
        return getRelativeDistanceFromInfrastructureLinkStart();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    @Override
    public Object component7() {
        return getClosestPointOnInfrastructureLink();
    }

    @Override
    public LocalDateTime component8() {
        return getValidityStart();
    }

    @Override
    public LocalDateTime component9() {
        return getValidityEnd();
    }

    @Override
    public Integer component10() {
        return getPriority();
    }

    @Override
    public UUID value1() {
        return getScheduledStopPointId();
    }

    @Override
    public String value2() {
        return getLabel();
    }

    @Override
    public Point value3() {
        return getMeasuredLocation();
    }

    @Override
    public UUID value4() {
        return getLocatedOnInfrastructureLinkId();
    }

    @Override
    public String value5() {
        return getDirection();
    }

    @Override
    public Double value6() {
        return getRelativeDistanceFromInfrastructureLinkStart();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    @Override
    public Object value7() {
        return getClosestPointOnInfrastructureLink();
    }

    @Override
    public LocalDateTime value8() {
        return getValidityStart();
    }

    @Override
    public LocalDateTime value9() {
        return getValidityEnd();
    }

    @Override
    public Integer value10() {
        return getPriority();
    }

    @Override
    public ScheduledStopPointRecord value1(UUID value) {
        setScheduledStopPointId(value);
        return this;
    }

    @Override
    public ScheduledStopPointRecord value2(String value) {
        setLabel(value);
        return this;
    }

    @Override
    public ScheduledStopPointRecord value3(Point value) {
        setMeasuredLocation(value);
        return this;
    }

    @Override
    public ScheduledStopPointRecord value4(UUID value) {
        setLocatedOnInfrastructureLinkId(value);
        return this;
    }

    @Override
    public ScheduledStopPointRecord value5(String value) {
        setDirection(value);
        return this;
    }

    @Override
    public ScheduledStopPointRecord value6(Double value) {
        setRelativeDistanceFromInfrastructureLinkStart(value);
        return this;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    @Override
    public ScheduledStopPointRecord value7(Object value) {
        setClosestPointOnInfrastructureLink(value);
        return this;
    }

    @Override
    public ScheduledStopPointRecord value8(LocalDateTime value) {
        setValidityStart(value);
        return this;
    }

    @Override
    public ScheduledStopPointRecord value9(LocalDateTime value) {
        setValidityEnd(value);
        return this;
    }

    @Override
    public ScheduledStopPointRecord value10(Integer value) {
        setPriority(value);
        return this;
    }

    @Override
    public ScheduledStopPointRecord values(UUID value1, String value2, Point value3, UUID value4, String value5, Double value6, Object value7, LocalDateTime value8, LocalDateTime value9, Integer value10) {
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
     * Create a detached ScheduledStopPointRecord
     */
    public ScheduledStopPointRecord() {
        super(ScheduledStopPoint.SCHEDULED_STOP_POINT);
    }

    /**
     * Create a detached, initialised ScheduledStopPointRecord
     */
    public ScheduledStopPointRecord(UUID scheduledStopPointId, String label, Point measuredLocation, UUID locatedOnInfrastructureLinkId, String direction, Double relativeDistanceFromInfrastructureLinkStart, Object closestPointOnInfrastructureLink, LocalDateTime validityStart, LocalDateTime validityEnd, Integer priority) {
        super(ScheduledStopPoint.SCHEDULED_STOP_POINT);

        setScheduledStopPointId(scheduledStopPointId);
        setLabel(label);
        setMeasuredLocation(measuredLocation);
        setLocatedOnInfrastructureLinkId(locatedOnInfrastructureLinkId);
        setDirection(direction);
        setRelativeDistanceFromInfrastructureLinkStart(relativeDistanceFromInfrastructureLinkStart);
        setClosestPointOnInfrastructureLink(closestPointOnInfrastructureLink);
        setValidityStart(validityStart);
        setValidityEnd(validityEnd);
        setPriority(priority);
    }
}

/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.journey_pattern.routines;


import fi.hsl.jore.jore4.jooq.journey_pattern.JourneyPattern;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CheckRouteJourneyPatternRefs extends AbstractRoutine<Boolean> {

    private static final long serialVersionUID = 1L;

    /**
     * The parameter
     * <code>journey_pattern.check_route_journey_pattern_refs.RETURN_VALUE</code>.
     */
    public static final Parameter<Boolean> RETURN_VALUE = Internal.createParameter("RETURN_VALUE", SQLDataType.BOOLEAN, false, false);

    /**
     * The parameter
     * <code>journey_pattern.check_route_journey_pattern_refs.filter_journey_pattern_id</code>.
     */
    public static final Parameter<UUID> FILTER_JOURNEY_PATTERN_ID = Internal.createParameter("filter_journey_pattern_id", SQLDataType.UUID, false, false);

    /**
     * The parameter
     * <code>journey_pattern.check_route_journey_pattern_refs.filter_route_id</code>.
     */
    public static final Parameter<UUID> FILTER_ROUTE_ID = Internal.createParameter("filter_route_id", SQLDataType.UUID, false, false);

    /**
     * The parameter
     * <code>journey_pattern.check_route_journey_pattern_refs.replace_scheduled_stop_point_id</code>.
     */
    public static final Parameter<UUID> REPLACE_SCHEDULED_STOP_POINT_ID = Internal.createParameter("replace_scheduled_stop_point_id", SQLDataType.UUID.defaultValue(DSL.field("NULL::uuid", SQLDataType.UUID)), true, false);

    /**
     * The parameter
     * <code>journey_pattern.check_route_journey_pattern_refs.new_located_on_infrastructure_link_id</code>.
     */
    public static final Parameter<UUID> NEW_LOCATED_ON_INFRASTRUCTURE_LINK_ID = Internal.createParameter("new_located_on_infrastructure_link_id", SQLDataType.UUID.defaultValue(DSL.field("NULL::uuid", SQLDataType.UUID)), true, false);

    /**
     * @deprecated Unknown data type. Please define an explicit {@link
     * org.jooq.Binding} to specify how this type should be handled. Deprecation
     * can be turned off using {@literal <deprecationOnUnknownTypes/>} in your
     * code generator configuration.
     */
    @Deprecated
    public static final Parameter<Object> NEW_MEASURED_LOCATION = Internal.createParameter("new_measured_location", org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\"").defaultValue(DSL.field("NULL::geography", org.jooq.impl.SQLDataType.OTHER)), true, false);

    /**
     * The parameter
     * <code>journey_pattern.check_route_journey_pattern_refs.new_direction</code>.
     */
    public static final Parameter<String> NEW_DIRECTION = Internal.createParameter("new_direction", SQLDataType.CLOB.defaultValue(DSL.field("NULL::text", SQLDataType.CLOB)), true, false);

    /**
     * The parameter
     * <code>journey_pattern.check_route_journey_pattern_refs.new_label</code>.
     */
    public static final Parameter<String> NEW_LABEL = Internal.createParameter("new_label", SQLDataType.CLOB.defaultValue(DSL.field("NULL::text", SQLDataType.CLOB)), true, false);

    /**
     * The parameter
     * <code>journey_pattern.check_route_journey_pattern_refs.new_validity_start</code>.
     */
    public static final Parameter<OffsetDateTime> NEW_VALIDITY_START = Internal.createParameter("new_validity_start", SQLDataType.TIMESTAMPWITHTIMEZONE(6).defaultValue(DSL.field("NULL::timestamp with time zone", SQLDataType.TIMESTAMPWITHTIMEZONE)), true, false);

    /**
     * The parameter
     * <code>journey_pattern.check_route_journey_pattern_refs.new_validity_end</code>.
     */
    public static final Parameter<OffsetDateTime> NEW_VALIDITY_END = Internal.createParameter("new_validity_end", SQLDataType.TIMESTAMPWITHTIMEZONE(6).defaultValue(DSL.field("NULL::timestamp with time zone", SQLDataType.TIMESTAMPWITHTIMEZONE)), true, false);

    /**
     * Create a new routine call instance
     */
    public CheckRouteJourneyPatternRefs() {
        super("check_route_journey_pattern_refs", JourneyPattern.JOURNEY_PATTERN, SQLDataType.BOOLEAN);

        setReturnParameter(RETURN_VALUE);
        addInParameter(FILTER_JOURNEY_PATTERN_ID);
        addInParameter(FILTER_ROUTE_ID);
        addInParameter(REPLACE_SCHEDULED_STOP_POINT_ID);
        addInParameter(NEW_LOCATED_ON_INFRASTRUCTURE_LINK_ID);
        addInParameter(NEW_MEASURED_LOCATION);
        addInParameter(NEW_DIRECTION);
        addInParameter(NEW_LABEL);
        addInParameter(NEW_VALIDITY_START);
        addInParameter(NEW_VALIDITY_END);
    }

    /**
     * Set the <code>filter_journey_pattern_id</code> parameter IN value to the
     * routine
     */
    public void setFilterJourneyPatternId(UUID value) {
        setValue(FILTER_JOURNEY_PATTERN_ID, value);
    }

    /**
     * Set the <code>filter_journey_pattern_id</code> parameter to the function
     * to be used with a {@link org.jooq.Select} statement
     */
    public void setFilterJourneyPatternId(Field<UUID> field) {
        setField(FILTER_JOURNEY_PATTERN_ID, field);
    }

    /**
     * Set the <code>filter_route_id</code> parameter IN value to the routine
     */
    public void setFilterRouteId(UUID value) {
        setValue(FILTER_ROUTE_ID, value);
    }

    /**
     * Set the <code>filter_route_id</code> parameter to the function to be used
     * with a {@link org.jooq.Select} statement
     */
    public void setFilterRouteId(Field<UUID> field) {
        setField(FILTER_ROUTE_ID, field);
    }

    /**
     * Set the <code>replace_scheduled_stop_point_id</code> parameter IN value
     * to the routine
     */
    public void setReplaceScheduledStopPointId(UUID value) {
        setValue(REPLACE_SCHEDULED_STOP_POINT_ID, value);
    }

    /**
     * Set the <code>replace_scheduled_stop_point_id</code> parameter to the
     * function to be used with a {@link org.jooq.Select} statement
     */
    public void setReplaceScheduledStopPointId(Field<UUID> field) {
        setField(REPLACE_SCHEDULED_STOP_POINT_ID, field);
    }

    /**
     * Set the <code>new_located_on_infrastructure_link_id</code> parameter IN
     * value to the routine
     */
    public void setNewLocatedOnInfrastructureLinkId(UUID value) {
        setValue(NEW_LOCATED_ON_INFRASTRUCTURE_LINK_ID, value);
    }

    /**
     * Set the <code>new_located_on_infrastructure_link_id</code> parameter to
     * the function to be used with a {@link org.jooq.Select} statement
     */
    public void setNewLocatedOnInfrastructureLinkId(Field<UUID> field) {
        setField(NEW_LOCATED_ON_INFRASTRUCTURE_LINK_ID, field);
    }

    /**
     * Set the <code>new_measured_location</code> parameter IN value to the
     * routine
     */
    public void setNewMeasuredLocation(Object value) {
        setValue(NEW_MEASURED_LOCATION, value);
    }

    /**
     * Set the <code>new_measured_location</code> parameter to the function to
     * be used with a {@link org.jooq.Select} statement
     */
    public void setNewMeasuredLocation(Field<Object> field) {
        setField(NEW_MEASURED_LOCATION, field);
    }

    /**
     * Set the <code>new_direction</code> parameter IN value to the routine
     */
    public void setNewDirection(String value) {
        setValue(NEW_DIRECTION, value);
    }

    /**
     * Set the <code>new_direction</code> parameter to the function to be used
     * with a {@link org.jooq.Select} statement
     */
    public void setNewDirection(Field<String> field) {
        setField(NEW_DIRECTION, field);
    }

    /**
     * Set the <code>new_label</code> parameter IN value to the routine
     */
    public void setNewLabel(String value) {
        setValue(NEW_LABEL, value);
    }

    /**
     * Set the <code>new_label</code> parameter to the function to be used with
     * a {@link org.jooq.Select} statement
     */
    public void setNewLabel(Field<String> field) {
        setField(NEW_LABEL, field);
    }

    /**
     * Set the <code>new_validity_start</code> parameter IN value to the routine
     */
    public void setNewValidityStart(OffsetDateTime value) {
        setValue(NEW_VALIDITY_START, value);
    }

    /**
     * Set the <code>new_validity_start</code> parameter to the function to be
     * used with a {@link org.jooq.Select} statement
     */
    public void setNewValidityStart(Field<OffsetDateTime> field) {
        setField(NEW_VALIDITY_START, field);
    }

    /**
     * Set the <code>new_validity_end</code> parameter IN value to the routine
     */
    public void setNewValidityEnd(OffsetDateTime value) {
        setValue(NEW_VALIDITY_END, value);
    }

    /**
     * Set the <code>new_validity_end</code> parameter to the function to be
     * used with a {@link org.jooq.Select} statement
     */
    public void setNewValidityEnd(Field<OffsetDateTime> field) {
        setField(NEW_VALIDITY_END, field);
    }
}
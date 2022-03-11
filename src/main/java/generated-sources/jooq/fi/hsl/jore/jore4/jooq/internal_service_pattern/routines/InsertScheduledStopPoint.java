/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.internal_service_pattern.routines;


import fi.hsl.jore.importer.config.jooq.converter.geometry.PointBinding;
import fi.hsl.jore.jore4.jooq.internal_service_pattern.InternalServicePattern;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.locationtech.jts.geom.Point;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InsertScheduledStopPoint extends AbstractRoutine<java.lang.Void> {

    private static final long serialVersionUID = 1L;

    /**
     * The parameter
     * <code>internal_service_pattern.insert_scheduled_stop_point.scheduled_stop_point_id</code>.
     */
    public static final Parameter<UUID> SCHEDULED_STOP_POINT_ID = Internal.createParameter("scheduled_stop_point_id", SQLDataType.UUID, false, false);

    /**
     * The parameter
     * <code>internal_service_pattern.insert_scheduled_stop_point.measured_location</code>.
     */
    public static final Parameter<Point> MEASURED_LOCATION = Internal.createParameter("measured_location", org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\""), false, false, new PointBinding());

    /**
     * The parameter
     * <code>internal_service_pattern.insert_scheduled_stop_point.located_on_infrastructure_link_id</code>.
     */
    public static final Parameter<UUID> LOCATED_ON_INFRASTRUCTURE_LINK_ID = Internal.createParameter("located_on_infrastructure_link_id", SQLDataType.UUID, false, false);

    /**
     * The parameter
     * <code>internal_service_pattern.insert_scheduled_stop_point.direction</code>.
     */
    public static final Parameter<String> DIRECTION = Internal.createParameter("direction", SQLDataType.CLOB, false, false);

    /**
     * The parameter
     * <code>internal_service_pattern.insert_scheduled_stop_point.label</code>.
     */
    public static final Parameter<String> LABEL = Internal.createParameter("label", SQLDataType.CLOB, false, false);

    /**
     * The parameter
     * <code>internal_service_pattern.insert_scheduled_stop_point.validity_start</code>.
     */
    public static final Parameter<OffsetDateTime> VALIDITY_START = Internal.createParameter("validity_start", SQLDataType.TIMESTAMPWITHTIMEZONE(6), false, false);

    /**
     * The parameter
     * <code>internal_service_pattern.insert_scheduled_stop_point.validity_end</code>.
     */
    public static final Parameter<OffsetDateTime> VALIDITY_END = Internal.createParameter("validity_end", SQLDataType.TIMESTAMPWITHTIMEZONE(6), false, false);

    /**
     * The parameter
     * <code>internal_service_pattern.insert_scheduled_stop_point.priority</code>.
     */
    public static final Parameter<Integer> PRIORITY = Internal.createParameter("priority", SQLDataType.INTEGER, false, false);

    /**
     * The parameter
     * <code>internal_service_pattern.insert_scheduled_stop_point.supported_vehicle_mode</code>.
     */
    public static final Parameter<String> SUPPORTED_VEHICLE_MODE = Internal.createParameter("supported_vehicle_mode", SQLDataType.CLOB, false, false);

    /**
     * Create a new routine call instance
     */
    public InsertScheduledStopPoint() {
        super("insert_scheduled_stop_point", InternalServicePattern.INTERNAL_SERVICE_PATTERN);

        addInParameter(SCHEDULED_STOP_POINT_ID);
        addInParameter(MEASURED_LOCATION);
        addInParameter(LOCATED_ON_INFRASTRUCTURE_LINK_ID);
        addInParameter(DIRECTION);
        addInParameter(LABEL);
        addInParameter(VALIDITY_START);
        addInParameter(VALIDITY_END);
        addInParameter(PRIORITY);
        addInParameter(SUPPORTED_VEHICLE_MODE);
    }

    /**
     * Set the <code>scheduled_stop_point_id</code> parameter IN value to the
     * routine
     */
    public void setScheduledStopPointId(UUID value) {
        setValue(SCHEDULED_STOP_POINT_ID, value);
    }

    /**
     * Set the <code>measured_location</code> parameter IN value to the routine
     */
    public void setMeasuredLocation(Point value) {
        setValue(MEASURED_LOCATION, value);
    }

    /**
     * Set the <code>located_on_infrastructure_link_id</code> parameter IN value
     * to the routine
     */
    public void setLocatedOnInfrastructureLinkId(UUID value) {
        setValue(LOCATED_ON_INFRASTRUCTURE_LINK_ID, value);
    }

    /**
     * Set the <code>direction</code> parameter IN value to the routine
     */
    public void setDirection(String value) {
        setValue(DIRECTION, value);
    }

    /**
     * Set the <code>label</code> parameter IN value to the routine
     */
    public void setLabel(String value) {
        setValue(LABEL, value);
    }

    /**
     * Set the <code>validity_start</code> parameter IN value to the routine
     */
    public void setValidityStart(OffsetDateTime value) {
        setValue(VALIDITY_START, value);
    }

    /**
     * Set the <code>validity_end</code> parameter IN value to the routine
     */
    public void setValidityEnd(OffsetDateTime value) {
        setValue(VALIDITY_END, value);
    }

    /**
     * Set the <code>priority</code> parameter IN value to the routine
     */
    public void setPriority(Integer value) {
        setValue(PRIORITY, value);
    }

    /**
     * Set the <code>supported_vehicle_mode</code> parameter IN value to the
     * routine
     */
    public void setSupportedVehicleMode(String value) {
        setValue(SUPPORTED_VEHICLE_MODE, value);
    }
}

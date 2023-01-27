/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.service_pattern.routines;


import fi.hsl.jore.jore4.jooq.service_pattern.ServicePattern;

import org.jooq.Field;
import org.jooq.Parameter;
import org.jooq.Record;
import org.jooq.impl.AbstractRoutine;
import org.jooq.impl.Internal;



/**
 * @deprecated Unknown data type. Please define an explicit {@link
 * org.jooq.Binding} to specify how this type should be handled. Deprecation can
 * be turned off using {@literal <deprecationOnUnknownTypes/>} in your code
 * generator configuration.
 */
@Deprecated
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ScheduledStopPointClosestPointOnInfrastructureLink extends AbstractRoutine<Object> {

    private static final long serialVersionUID = 1L;

    /**
     * @deprecated Unknown data type. Please define an explicit {@link
     * org.jooq.Binding} to specify how this type should be handled. Deprecation
     * can be turned off using {@literal <deprecationOnUnknownTypes/>} in your
     * code generator configuration.
     */
    @Deprecated
    public static final Parameter<Object> RETURN_VALUE = Internal.createParameter("RETURN_VALUE", org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\""), false, false);

    /**
     * The parameter
     * <code>service_pattern.scheduled_stop_point_closest_point_on_infrastructure_link.ssp</code>.
     */
    public static final Parameter<Record> SSP = Internal.createParameter("ssp", fi.hsl.jore.jore4.jooq.service_pattern.tables.ScheduledStopPoint.SCHEDULED_STOP_POINT.getDataType(), false, false);

    /**
     * Create a new routine call instance
     */
    public ScheduledStopPointClosestPointOnInfrastructureLink() {
        super("scheduled_stop_point_closest_point_on_infrastructure_link", ServicePattern.SERVICE_PATTERN, org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\""));

        setReturnParameter(RETURN_VALUE);
        addInParameter(SSP);
    }

    /**
     * Set the <code>ssp</code> parameter IN value to the routine
     */
    public void setSsp(Record value) {
        setValue(SSP, value);
    }

    /**
     * Set the <code>ssp</code> parameter to the function to be used with a
     * {@link org.jooq.Select} statement
     */
    public void setSsp(Field<Record> field) {
        setField(SSP, field);
    }
}
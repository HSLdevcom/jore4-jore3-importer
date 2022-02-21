/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.internal_service_pattern;


import fi.hsl.jore.jore4.jooq.internal_service_pattern.routines.InsertScheduledStopPoint;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.jooq.Configuration;
import org.locationtech.jts.geom.Point;


/**
 * Convenience access to all stored procedures and functions in internal_service_pattern.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Routines {

    /**
     * Call <code>internal_service_pattern.insert_scheduled_stop_point</code>
     */
    public static void insertScheduledStopPoint(
          Configuration configuration
        , UUID scheduledStopPointId
        , Point measuredLocation
        , UUID locatedOnInfrastructureLinkId
        , String direction
        , String label
        , OffsetDateTime validityStart
        , OffsetDateTime validityEnd
        , Integer priority
        , String supportedVehicleMode
    ) {
        InsertScheduledStopPoint p = new InsertScheduledStopPoint();
        p.setScheduledStopPointId(scheduledStopPointId);
        p.setMeasuredLocation(measuredLocation);
        p.setLocatedOnInfrastructureLinkId(locatedOnInfrastructureLinkId);
        p.setDirection(direction);
        p.setLabel(label);
        p.setValidityStart(validityStart);
        p.setValidityEnd(validityEnd);
        p.setPriority(priority);
        p.setSupportedVehicleMode(supportedVehicleMode);

        p.execute(configuration);
    }
}

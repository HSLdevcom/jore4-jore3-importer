/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.service_pattern;


import fi.hsl.jore.jore4.jooq.DefaultCatalog;
import fi.hsl.jore.jore4.jooq.service_pattern.tables.ScheduledStopPoint;
import fi.hsl.jore.jore4.jooq.service_pattern.tables.ScheduledStopPointServicedByVehicleMode;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ServicePattern extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>service_pattern</code>
     */
    public static final ServicePattern SERVICE_PATTERN = new ServicePattern();

    /**
     * The scheduled stop points: https://www.transmodel-cen.eu/model/index.htm?goto=2:3:4:845 . Colloquially known as stops from the perspective of timetable planning.
     */
    public final ScheduledStopPoint SCHEDULED_STOP_POINT = ScheduledStopPoint.SCHEDULED_STOP_POINT;

    /**
     * Which scheduled stop points are serviced by which vehicle modes?
     */
    public final ScheduledStopPointServicedByVehicleMode SCHEDULED_STOP_POINT_SERVICED_BY_VEHICLE_MODE = ScheduledStopPointServicedByVehicleMode.SCHEDULED_STOP_POINT_SERVICED_BY_VEHICLE_MODE;

    /**
     * No further instances allowed
     */
    private ServicePattern() {
        super("service_pattern", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            ScheduledStopPoint.SCHEDULED_STOP_POINT,
            ScheduledStopPointServicedByVehicleMode.SCHEDULED_STOP_POINT_SERVICED_BY_VEHICLE_MODE);
    }
}
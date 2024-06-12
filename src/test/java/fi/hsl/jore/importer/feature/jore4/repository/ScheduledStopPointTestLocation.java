package fi.hsl.jore.importer.feature.jore4.repository;

/**
 * A test utility class which is used to ensure that the location of a scheduled stop point is inserted correctly into
 * the Jore 4 database.
 */
public class ScheduledStopPointTestLocation {

    public static final String SQL_ALIAS_X_COORDINATE = "x";
    public static final String SQL_ALIAS_Y_COORDINATE = "y";
    public static final String SQL_QUERY_GET_MEASURED_LOCATION =
            """
            SELECT ST_X(measured_location::geometry) AS x,
                   ST_Y(measured_location::geometry) AS y
            FROM service_pattern.scheduled_stop_point
            """;

    final double x;
    final double y;

    public ScheduledStopPointTestLocation(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

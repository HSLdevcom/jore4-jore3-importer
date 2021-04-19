package fi.hsl.jore.importer.config.jooq.converter.geometry;

import fi.hsl.jore.importer.util.GeometryUtil;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PointConverterTest {

    private static final PointConverter CONVERTER = PointConverter.INSTANCE;

    @Test
    public void pointXYZToWktString() {
        final Point point =
                GeometryUtil.toPoint(
                        GeometryUtil.SRID_WGS84,
                        new Coordinate(60.168988620, 24.949328727, 0));

        final String output = (String) CONVERTER.to(point);
        final String expected = "SRID=4326;POINT Z(60.16898862 24.949328727 0)";

        assertThat(output,
                   is(expected));
    }

    @Test
    public void pointXYToWktString() {
        final Point point =
                GeometryUtil.toPoint(
                        GeometryUtil.SRID_WGS84,
                        new Coordinate(60.168988620, 24.949328727));

        final String output = (String) CONVERTER.to(point);
        final String expected = "SRID=4326;POINT (60.16898862 24.949328727)";

        assertThat(output,
                   is(expected));
    }

    @Test
    public void lineStringWkbThrows() {
        final String ewkb = "01020000A0E610000002000000E9254A6BA1154E404A9E1B3507F338400000000000000000BE9150659E154E40103A46840FF238400000000000000000";

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CONVERTER.from(ewkb);
        });

        assertThat(exception.getMessage(),
                   is("Unsupported geometry type : LineString"));
    }

    @Test
    public void wkbStringToPoint() {
        final String ewkb = "01010000A0E6100000E9254A6BA1154E404A9E1B3507F338400000000000000000";

        final Point point = CONVERTER.from(ewkb);

        final Point expected = GeometryUtil.toPoint(
                GeometryUtil.SRID_WGS84,
                new Coordinate(60.168988620, 24.949328727, 0)
        );

        assertThat(point,
                   is(notNullValue()));
        assertThat(point,
                   is(expected));
        assertThat(point.equalsExact(expected),
                   is(true));
    }
}

package fi.hsl.jore.importer.config.jooq.converter.geometry;

import fi.hsl.jore.importer.util.GeometryUtil;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LineStringConverterTest {

    private static final LineStringConverter CONVERTER = LineStringConverter.INSTANCE;

    @Test
    public void lineStringXyzToEwktString() {
        final LineString line =
                GeometryUtil.toLineString(
                        GeometryUtil.SRID_WGS84,
                        new Coordinate(60.168988620, 24.949328727, 0),
                        new Coordinate(60.168896355, 24.945549266, 0));

        final var output = CONVERTER.to(line);
        final var expected = "SRID=4326;LINESTRING Z(60.16898862 24.949328727 0, 60.168896355 24.945549266 0)";

        assertThat(output,
                   is(expected));
    }

    @Test
    public void lineStringXyToEwktString() {
        final LineString line =
                GeometryUtil.toLineString(
                        GeometryUtil.SRID_WGS84,
                        new Coordinate(60.168988620, 24.949328727),
                        new Coordinate(60.168896355, 24.945549266));

        final var output = CONVERTER.to(line);
        final var expected = "SRID=4326;LINESTRING (60.16898862 24.949328727, 60.168896355 24.945549266)";

        assertThat(output,
                   is(expected));
    }

    @Test
    public void pointWkbThrows() {
        final String ewkb = "01010000A0E6100000E9254A6BA1154E404A9E1B3507F338400000000000000000";

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CONVERTER.from(ewkb);
        });

        assertThat(exception.getMessage(),
                   is("Unsupported geometry type : Point"));
    }

    @Test
    public void ewkbStringToLineString() {
        final String ewkb = "01020000A0E610000002000000E9254A6BA1154E404A9E1B3507F338400000000000000000BE9150659E154E40103A46840FF238400000000000000000";

        final LineString line = CONVERTER.from(ewkb);

        final LineString expected = GeometryUtil.toLineString(
                GeometryUtil.SRID_WGS84,
                new Coordinate(60.168988620, 24.949328727, 0),
                new Coordinate(60.168896355, 24.945549266, 0)
        );

        assertThat(line,
                   is(notNullValue()));
        assertThat(line,
                   is(expected));
        assertThat(line.equalsExact(expected),
                   is(true));
    }
}

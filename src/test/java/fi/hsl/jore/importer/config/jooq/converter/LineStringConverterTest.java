package fi.hsl.jore.importer.config.jooq.converter;

import fi.hsl.jore.importer.feature.util.GeometryUtil;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class LineStringConverterTest {

    private static final LineStringConverter CONVERTER = new LineStringConverter();

    @Test
    public void lineStringXYZToWktString() {
        final LineString line =
                GeometryUtil.toLineString(
                        new Coordinate(60.168988620, 24.949328727, 0),
                        new Coordinate(60.168896355, 24.945549266, 0));

        final String output = (String) CONVERTER.to(line);
        final String expected = "LINESTRING Z(60.16898862 24.949328727 0, 60.168896355 24.945549266 0)";

        assertThat(output,
                   is(expected));
    }

    @Test
    public void lineStringXYToWktString() {
        final LineString line =
                GeometryUtil.toLineString(
                        new Coordinate(60.168988620, 24.949328727),
                        new Coordinate(60.168896355, 24.945549266));

        final String output = (String) CONVERTER.to(line);
        final String expected = "LINESTRING (60.16898862 24.949328727, 60.168896355 24.945549266)";

        assertThat(output,
                   is(expected));
    }

    @Test
    public void wkbStringToLineString() {
        final String wkb = "01020000A0E610000002000000E9254A6BA1154E404A9E1B3507F338400000000000000000BE9150659E154E40103A46840FF238400000000000000000";

        final LineString line = CONVERTER.from(wkb);

        final LineString expected = GeometryUtil.toLineString(
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

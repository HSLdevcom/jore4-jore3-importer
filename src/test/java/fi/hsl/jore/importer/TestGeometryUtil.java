package fi.hsl.jore.importer;

import com.google.common.base.Preconditions;
import fi.hsl.jore.importer.util.GeometryUtil;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public final class TestGeometryUtil {

    private static final Random R = new Random();

    private static final double TOLERANCE = 0.000001;

    private TestGeometryUtil() {}

    private static double randomLatitude() {
        return R.nextDouble() * 60.0;
    }

    private static double randomLongitude() {
        return R.nextDouble() * 25.0;
    }

    private static Coordinate randomCoordinate() {
        return new Coordinate(randomLatitude(), randomLongitude(), 0);
    }

    public static Point randomPoint() {
        return GeometryUtil.toPoint(GeometryUtil.SRID_WGS84, randomCoordinate());
    }

    public static LineString randomLine(final int length) {
        Preconditions.checkArgument(length >= 2, "LineString length must be at least 2");
        final List<Coordinate> coords =
                IntStream.range(0, length).boxed().map(i -> randomCoordinate()).toList();
        return GeometryUtil.toLineString(GeometryUtil.SRID_WGS84, coords);
    }

    public static LineString randomLine() {
        return randomLine(2);
    }

    /**
     * Plain assertThat(a, is(b)) sometimes rarely causes "expected 1.23, got 1.23" type issues
     *
     * @param a The first geometry
     * @param b The second geometry
     * @return True, if geometries match (wrt. a tolerance)
     */
    public static boolean geometriesMatch(final Geometry a, final Geometry b) {
        return a.getSRID() == b.getSRID() && a.equalsExact(b, TOLERANCE);
    }
}

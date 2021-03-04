package fi.hsl.jore.importer.feature.util;

import io.vavr.Tuple;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.util.Arrays;

public final class GeometryUtil {

    public static final int SRID_WGS84 = 4326;

    public static final Set<Integer> SRIDS = HashSet.of(SRID_WGS84);

    public static final Map<Integer, GeometryFactory> FACTORIES_BY_SRID =
            SRIDS.toMap(srid -> Tuple.of(srid, buildFor(srid)));

    private GeometryUtil() {
    }

    private static GeometryFactory buildFor(final int srid) {
        return new GeometryFactory(new PrecisionModel(),
                                   srid);
    }

    public static LineString toLineString(final GeometryFactory factory,
                                          final List<Coordinate> coordinates) {
        return factory.createLineString(coordinates.toJavaArray(Coordinate[]::new));
    }

    public static LineString toLineString(final GeometryFactory factory,
                                          final Coordinate... coordinates) {
        return factory.createLineString(coordinates);
    }

    public static LineString toLineString(final GeometryFactory factory,
                                          final Point... points) {
        return factory.createLineString(Arrays.stream(points)
                                              .map(Point::getCoordinate)
                                              .toArray(Coordinate[]::new));
    }
}

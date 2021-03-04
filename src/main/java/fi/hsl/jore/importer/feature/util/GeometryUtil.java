package fi.hsl.jore.importer.feature.util;

import io.vavr.collection.List;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

public final class GeometryUtil {

    private static final GeometryFactory FACTORY = JTSFactoryFinder.getGeometryFactory();

    private GeometryUtil() {
    }

    public static LineString toLineString(final List<Coordinate> coordinates) {
        return FACTORY.createLineString(coordinates.toJavaArray(Coordinate[]::new));
    }

    public static LineString toLineString(final Coordinate... coordinates) {
        return FACTORY.createLineString(coordinates);
    }
}

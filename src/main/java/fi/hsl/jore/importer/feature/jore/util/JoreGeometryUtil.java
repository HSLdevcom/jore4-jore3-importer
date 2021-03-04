package fi.hsl.jore.importer.feature.jore.util;

import fi.hsl.jore.importer.feature.util.GeometryUtil;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public final class JoreGeometryUtil {

    public static final int JORE_SRID = GeometryUtil.SRID_WGS84;

    public static final GeometryFactory FACTORY = GeometryUtil.factoryForSrid(JORE_SRID);

    private JoreGeometryUtil() {
    }

    public static Point fromDbCoordinates(final double latitude,
                                          final double longitude) {
        return FACTORY.createPoint(new Coordinate(longitude, latitude, 0));
    }
}

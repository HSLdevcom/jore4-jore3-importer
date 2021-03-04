package fi.hsl.jore.importer.feature.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.util.Arrays;

public final class GeometryUtil {

    public static final int SRID_WGS84 = 4326;

    private static final LoadingCache<Integer, GeometryFactory> FACTORY_CACHE =
            CacheBuilder.newBuilder()
                        .build(new GeometryFactoryLoader());

    private static class GeometryFactoryLoader extends CacheLoader<Integer, GeometryFactory> {
        @Override
        public GeometryFactory load(final Integer srid) {
            return new GeometryFactory(new PrecisionModel(),
                                       srid);
        }
    }

    private GeometryUtil() {
    }

    public static GeometryFactory factoryForSrid(final int srid) {
        return FACTORY_CACHE.getUnchecked(srid);
    }

    public static LineString toLineString(final int srid,
                                          final Coordinate... coordinates) {
        return factoryForSrid(srid)
                .createLineString(coordinates);
    }

    public static LineString toLineString(final int srid,
                                          final Point... points) {
        return factoryForSrid(srid)
                .createLineString(Arrays.stream(points)
                                        .map(Point::getCoordinate)
                                        .toArray(Coordinate[]::new));
    }

}

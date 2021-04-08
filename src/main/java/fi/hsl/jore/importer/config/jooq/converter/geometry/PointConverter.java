package fi.hsl.jore.importer.config.jooq.converter.geometry;


import org.jooq.Converter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import javax.annotation.Nullable;

/**
 * Converts between Point geometries and Postgis EWKT/EWKB formats.
 */
public class PointConverter
        implements Converter<Object, Point> {

    private static final GeometryConverter CONVERTER = new GeometryConverter(Geometry.TYPENAME_POINT);

    public static final PointConverter INSTANCE = new PointConverter();

    @Override
    @Nullable
    public Point from(@Nullable final Object databaseObject) {
        return (Point) CONVERTER.from(databaseObject);
    }

    @Override
    @Nullable
    public Object to(final Point userObject) {
        return GeometryConverter.to(userObject);
    }

    @Override
    public Class<Object> fromType() {
        return Object.class;
    }

    @Override
    public Class<Point> toType() {
        return Point.class;
    }
}

package fi.hsl.jore.importer.config.jooq.converter.geometry;


import org.jooq.Converter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

import javax.annotation.Nullable;

/**
 * Converts between LineString geometries and Postgis EWKT/EWKB formats.
 */
public class LineStringConverter
        implements Converter<Object, LineString> {

    private static final GeometryConverter CONVERTER = new GeometryConverter(Geometry.TYPENAME_LINESTRING);

    public static final LineStringConverter INSTANCE = new LineStringConverter();

    @Override
    @Nullable
    public LineString from(@Nullable final Object databaseObject) {
        return (LineString) CONVERTER.from(databaseObject);
    }

    @Override
    @Nullable
    public Object to(final LineString userObject) {
        return GeometryConverter.to(userObject);
    }

    @Override
    public Class<Object> fromType() {
        return Object.class;
    }

    @Override
    public Class<LineString> toType() {
        return LineString.class;
    }
}

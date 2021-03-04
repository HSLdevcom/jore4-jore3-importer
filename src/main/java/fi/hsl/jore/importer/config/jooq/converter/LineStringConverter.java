package fi.hsl.jore.importer.config.jooq.converter;


import org.geotools.geometry.jts.WKBReader;
import org.jooq.Converter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTWriter;

import javax.annotation.Nullable;

public class LineStringConverter implements Converter<Object, LineString> {

    @Override
    @Nullable
    public LineString from(@Nullable final Object databaseObject) {
        if (databaseObject == null) {
            return null;
        }

        final WKBReader reader = new WKBReader();
        try {
            final Geometry geom = reader.read(WKBReader.hexToBytes(databaseObject.toString()));

            if (!Geometry.TYPENAME_LINESTRING.equals(geom.getGeometryType())) {
                throw new IllegalArgumentException("Unsupported geometry type : " + geom.getGeometryType());
            }

            return (LineString) geom;
        } catch (final ParseException e) {
            throw new RuntimeException("Failed to parse WKB string", e);
        }
    }

    @Override
    @Nullable
    public Object to(final LineString userObject) {
        if (userObject == null) {
            return null;
        }
        final WKTWriter writer = new WKTWriter(3);
        return writer.write(userObject);
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

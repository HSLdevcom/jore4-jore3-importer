package fi.hsl.jore.importer.config.jooq.converter.geometry;

import javax.annotation.Nullable;
import org.geotools.geometry.jts.WKBReader;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTWriter;

public class GeometryConverter {

    private final String typeName;

    public GeometryConverter(final String typeName) {
        this.typeName = typeName;
    }

    @Nullable
    public Geometry from(@Nullable final Object databaseObject) {
        if (databaseObject == null) {
            return null;
        }

        // Note that the WKBReader also supports the PostGIS EWKB format
        final WKBReader reader = new WKBReader();
        try {
            final Geometry geom = reader.read(WKBReader.hexToBytes(databaseObject.toString()));

            if (!typeName.equals(geom.getGeometryType())) {
                throw new IllegalArgumentException("Unsupported geometry type : " + geom.getGeometryType());
            }

            return geom;
        } catch (final ParseException e) {
            throw new RuntimeException("Failed to parse EWKB string", e);
        }
    }

    @Nullable
    public static Object to(@Nullable final Geometry userObject) {
        if (userObject == null) {
            return null;
        }
        // Note that the WKTWriter does _not_ support the PostGIS EWKT format,
        // hence we must manually add the SRID prefix
        final WKTWriter writer = new WKTWriter(3);
        return String.format("SRID=%d;%s", userObject.getSRID(), writer.write(userObject));
    }
}

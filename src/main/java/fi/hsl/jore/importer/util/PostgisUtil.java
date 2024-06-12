package fi.hsl.jore.importer.util;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.locationtech.jts.geom.Geometry;

public final class PostgisUtil {

    private PostgisUtil() {}

    public static Condition geometryEquals(final Field<? extends Geometry> a, final Field<? extends Geometry> b) {
        return DSL.condition("ST_EQUALS({0}::geometry, {1}::geometry)", a, b);
    }
}

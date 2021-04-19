package fi.hsl.jore.importer.feature.jore3.mixin;

import com.google.common.base.Preconditions;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore3.util.JoreGeometryUtil;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

public interface IHasCoordinates {

    @JoreColumn(name = "solx")
    double latitude();

    @JoreColumn(name = "soly")
    double longitude();

    @Value.Check
    default void checkCoordinates() {
        Preconditions.checkState(latitude() <= 90.0D);
        Preconditions.checkState(longitude() <= 180.0D);
    }

    default Point location() {
        return JoreGeometryUtil.fromDbCoordinates(latitude(), longitude());
    }
}

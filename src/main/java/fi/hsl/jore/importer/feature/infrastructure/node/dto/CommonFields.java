package fi.hsl.jore.importer.feature.infrastructure.node.dto;

import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;
import java.util.Optional;
import org.locationtech.jts.geom.Point;

public interface CommonFields<T> extends IHasExternalId {
    NodeType nodeType();

    Point location();

    Optional<Point> projectedLocation();

    T withProjectedLocation(Point projectedLocation);
}

package fi.hsl.jore.importer.feature.infrastructure.node.dto;

import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;
import org.locationtech.jts.geom.Point;

import java.util.Optional;


public interface CommonFields<T> extends IHasExternalId {
    NodeType nodeType();

    Point location();

    Optional<Point> projectedLocation();

    T withProjectedLocation(Point projectedLocation);
}

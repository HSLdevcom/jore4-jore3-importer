package fi.hsl.jore.importer.feature.infrastructure.node.dto;

import org.locationtech.jts.geom.Point;

import java.util.Optional;


public interface ModifiableFields<T> {
    NodeType nodeType();

    Point location();

    Optional<Point> projectedLocation();

    T withNodeType(NodeType pointType);

    T withLocation(Point location);

    T withProjectedLocation(Point projectedLocation);
}

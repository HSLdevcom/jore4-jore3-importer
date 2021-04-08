package fi.hsl.jore.importer.feature.infrastructure.node.dto;

import org.locationtech.jts.geom.Point;


public interface ModifiableFields<T> {
    NodeType nodeType();

    Point geometry();

    T withNodeType(NodeType pointType);

    T withGeometry(Point geometry);
}

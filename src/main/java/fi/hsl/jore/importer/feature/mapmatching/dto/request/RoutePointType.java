package fi.hsl.jore.importer.feature.mapmatching.dto.request;

import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;

/**
 * Identifies the type of a route point.
 */
public enum RoutePointType {

    PUBLIC_TRANSPORT_STOP,
    ROAD_JUNCTION,
    OTHER;

    /**
     * Transforms the {@link NodeType} given as a method parameter
     * into a {@link RoutePointType}.
     * @param input The node type.
     * @return  The {@link RoutePointType} which corresponds with the
     *          {@link NodeType} given as a method parameter.
     */
    static RoutePointType fromNodeType(NodeType input) {
        switch (input) {
            case CROSSROADS:
                return RoutePointType.ROAD_JUNCTION;
            case STOP:
                return RoutePointType.PUBLIC_TRANSPORT_STOP;
            default:
                return RoutePointType.OTHER;
        }
    }
}

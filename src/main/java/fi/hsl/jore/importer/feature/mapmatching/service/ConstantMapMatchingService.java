package fi.hsl.jore.importer.feature.mapmatching.service;

import fi.hsl.jore.importer.feature.mapmatching.dto.response.ExternalLinkRefResponseDTO;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.InfrastructureLinkResponseDTO;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.MapMatchingResponseDTO;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.RouteResponseDTO;
import fi.hsl.jore.importer.feature.network.route_point.dto.ExportableRouteGeometry;
import fi.hsl.jore.importer.feature.network.route_point.dto.ExportableRoutePoint;
import io.vavr.collection.List;

/**
 * This implementation returns the same map matching response
 * every time when the {@link IMapMatchingService#sendMapMatchingRequest(ExportableRouteGeometry, List)}
 * method is invoked. 
 */
public class ConstantMapMatchingService implements IMapMatchingService {

    public static final String INFRASTRUCTURE_LINK_EXT_ID = "133202";
    public static final boolean INFRASTRUCTURE_LINK_IS_TRAVERSAL_FORWARDS = true;
    public static final String INFRASTRUCTURE_LINK_SOURCE = "digiroad_r";

    private static final String MAP_MATCHING_OK_RESPONSE_CODE = "Ok";

    @Override
    public MapMatchingResponseDTO sendMapMatchingRequest(final ExportableRouteGeometry routeGeometry,
                                                         final List<ExportableRoutePoint> routePoints) {
        final MapMatchingResponseDTO mapMatchingResponse = new MapMatchingResponseDTO();
        mapMatchingResponse.setCode(MAP_MATCHING_OK_RESPONSE_CODE);

        final RouteResponseDTO route = new RouteResponseDTO();

        final ExternalLinkRefResponseDTO infrastructureLinkSource = new ExternalLinkRefResponseDTO();
        infrastructureLinkSource.setInfrastructureSource(INFRASTRUCTURE_LINK_SOURCE);
        infrastructureLinkSource.setExternalLinkId(INFRASTRUCTURE_LINK_EXT_ID);

        final InfrastructureLinkResponseDTO infrastructureLink = new InfrastructureLinkResponseDTO();
        infrastructureLink.setExternalLinkRef(infrastructureLinkSource);
        infrastructureLink.setIsTraversalForwards(INFRASTRUCTURE_LINK_IS_TRAVERSAL_FORWARDS);

        route.setPaths(List.of(infrastructureLink).asJava());
        mapMatchingResponse.setRoutes(List.of(route).asJava());

        return mapMatchingResponse;
    }
}

package fi.hsl.jore.importer.feature.mapmatching.service;

import fi.hsl.jore.importer.feature.mapmatching.dto.response.ExternalLinkRefDTO;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.InfrastructureLinkDTO;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.MapMatchingSuccessResponseDTO;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.RouteDTO;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRouteGeometry;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRoutePoint;
import io.vavr.collection.List;

/**
 * This implementation returns the same map matching response every time when the {@link
 * IMapMatchingService#sendMapMatchingRequest(ImporterRouteGeometry, List)} method is invoked.
 */
public class MockMapMatchingService implements IMapMatchingService {

    public static final String INFRASTRUCTURE_LINK_EXT_ID = "133202";
    public static final boolean INFRASTRUCTURE_LINK_IS_TRAVERSAL_FORWARDS = true;
    public static final String INFRASTRUCTURE_LINK_SOURCE = "digiroad_r";

    private static final String MAP_MATCHING_OK_RESPONSE_CODE = "Ok";

    @Override
    public MapMatchingSuccessResponseDTO sendMapMatchingRequest(
            final ImporterRouteGeometry routeGeometry, final List<ImporterRoutePoint> routePoints) {
        final MapMatchingSuccessResponseDTO mapMatchingResponse =
                new MapMatchingSuccessResponseDTO();
        mapMatchingResponse.setCode(MAP_MATCHING_OK_RESPONSE_CODE);

        final RouteDTO route = new RouteDTO();

        final ExternalLinkRefDTO infrastructureLinkSource = new ExternalLinkRefDTO();
        infrastructureLinkSource.setInfrastructureSource(INFRASTRUCTURE_LINK_SOURCE);
        infrastructureLinkSource.setExternalLinkId(INFRASTRUCTURE_LINK_EXT_ID);

        final InfrastructureLinkDTO infrastructureLink = new InfrastructureLinkDTO();
        infrastructureLink.setExternalLinkRef(infrastructureLinkSource);
        infrastructureLink.setIsTraversalForwards(INFRASTRUCTURE_LINK_IS_TRAVERSAL_FORWARDS);

        route.setPaths(List.of(infrastructureLink).asJava());
        mapMatchingResponse.setRoutes(List.of(route).asJava());

        return mapMatchingResponse;
    }
}

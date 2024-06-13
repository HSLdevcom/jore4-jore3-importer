package fi.hsl.jore.importer.feature.batch.route;

import static fi.hsl.jore.importer.util.JoreCollectionUtils.mapWithIndex;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4RouteGeometry;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4RouteInfrastructureLink;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.ExternalLinkRefDTO;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.InfrastructureLinkDTO;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.MapMatchingSuccessResponseDTO;
import fi.hsl.jore.importer.feature.mapmatching.service.IMapMatchingService;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRouteGeometry;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRoutePoint;
import fi.hsl.jore.importer.feature.network.route_point.repository.IRoutePointExportRepository;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Maps route geometry from Jore 4 topology network to Jore 4 topology network. */
@Component
public class MapMatchingProcessor implements ItemProcessor<ImporterRouteGeometry, Jore4RouteGeometry> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapMatchingProcessor.class);

    private final IMapMatchingService mapMatchingService;
    private final IRoutePointExportRepository routePointRepository;

    @Autowired
    public MapMatchingProcessor(
            final IMapMatchingService mapMatchingService, final IRoutePointExportRepository routePointRepository) {
        this.mapMatchingService = mapMatchingService;
        this.routePointRepository = routePointRepository;
    }

    @Override
    public Jore4RouteGeometry process(final ImporterRouteGeometry routeGeometryInput) throws Exception {
        LOGGER.debug(
                "Processing route geometry with routeDirectionId: {} and routeDirectionExtId: {}",
                routeGeometryInput.routeDirectionId(),
                routeGeometryInput.routeDirectionExtId());

        final List<ImporterRoutePoint> routePointsInput =
                routePointRepository.findImporterRoutePointsByRouteDirectionId(routeGeometryInput.routeDirectionId());
        if (routePointsInput.isEmpty()) {
            LOGGER.error(
                    "Cannot process route geometry because no route points were found for route with route direction id: {}",
                    routeGeometryInput.routeDirectionId());
            return null;
        }

        final MapMatchingSuccessResponseDTO mapMatchingResponse =
                mapMatchingService.sendMapMatchingRequest(routeGeometryInput, routePointsInput);

        return createRouteGeometry(routeGeometryInput.routeJore4Id(), mapMatchingResponse);
    }

    private Jore4RouteGeometry createRouteGeometry(
            final UUID routeId, final MapMatchingSuccessResponseDTO mapMatchingResponse) {
        final List<InfrastructureLinkDTO> matchedInfraLinks =
                mapMatchingResponse.getRoutes().get(0).getPaths();

        List<Jore4RouteInfrastructureLink> jore4InfraLinks = mapWithIndex(
                        matchedInfraLinks, (index, matchedInfraLink) -> {
                            final ExternalLinkRefDTO externalLink = matchedInfraLink.getExternalLinkRef();
                            return Jore4RouteInfrastructureLink.of(
                                    externalLink.getInfrastructureSource(),
                                    externalLink.getExternalLinkId(),
                                    index,
                                    matchedInfraLink.isTraversalForwards());
                        })
                .toList();

        return Jore4RouteGeometry.of(routeId, jore4InfraLinks);
    }
}

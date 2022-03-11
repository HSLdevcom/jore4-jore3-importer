package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.mapmatching.dto.response.ExternalLinkRefDTO;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.InfrastructureLinkDTO;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.MapMatchingResponseDTO;
import fi.hsl.jore.importer.feature.mapmatching.service.IMapMatchingService;
import fi.hsl.jore.importer.feature.network.route_point.dto.ExportableRouteGeometry;
import fi.hsl.jore.importer.feature.network.route_point.dto.ExportableRoutePoint;
import fi.hsl.jore.importer.feature.network.route_point.repository.IRoutePointExportRepository;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRouteGeometry;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRouteInfrastructureLink;
import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Transforms the information of an exported route geometry into a format
 * that can be inserted into the Jore 4 database.
 */
@Component
public class RouteGeometryExportProcessor implements ItemProcessor<ExportableRouteGeometry, TransmodelRouteGeometry> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteGeometryExportProcessor.class);

    private final IMapMatchingService mapMatchingService;
    private final IRoutePointExportRepository routePointRepository;

    @Autowired
    public RouteGeometryExportProcessor(final IMapMatchingService mapMatchingService,
                                        final IRoutePointExportRepository routePointRepository) {
        this.mapMatchingService = mapMatchingService;
        this.routePointRepository = routePointRepository;
    }

    @Override
    public TransmodelRouteGeometry process(final ExportableRouteGeometry routeGeometryInput) throws Exception {
        final List<ExportableRoutePoint> routePointsInput = routePointRepository.findExportableRoutePointsByRouteDirectionId(routeGeometryInput.routeDirectionId());
        if (routePointsInput.isEmpty()) {
            LOGGER.error(
                    "Cannot process route geometry because no route points were found for route with route direction id: {}",
                    routeGeometryInput.routeDirectionId()
            );
            return null;
        }

        final MapMatchingResponseDTO mapMatchingResponse = mapMatchingService.sendMapMatchingRequest(routeGeometryInput,
                routePointsInput
        );

        return createRouteGeometry(routeGeometryInput.routeTransmodelId(), mapMatchingResponse);
    }

    private TransmodelRouteGeometry createRouteGeometry(final UUID routeId,
                                                        final MapMatchingResponseDTO mapMatchingResponse) {
        final java.util.List<InfrastructureLinkDTO> matchedInfraLinks = mapMatchingResponse
                .getRoutes().get(0)
                .getPaths();

        List<TransmodelRouteInfrastructureLink> transmodelInfraLinks = List.empty();
        int infrastructureLinkSequence = 0;

        for (final InfrastructureLinkDTO matchedInfraLink: matchedInfraLinks) {
            final ExternalLinkRefDTO externalLink = matchedInfraLink.getExternalLinkRef();
            transmodelInfraLinks = transmodelInfraLinks.append(TransmodelRouteInfrastructureLink.of(
                    externalLink.getInfrastructureSource(),
                    externalLink.getExternalLinkId(),
                    infrastructureLinkSequence,
                    matchedInfraLink.isTraversalForwards()
            ));
            infrastructureLinkSequence++;
        }


        return TransmodelRouteGeometry.of(routeId, transmodelInfraLinks);
    }
 }

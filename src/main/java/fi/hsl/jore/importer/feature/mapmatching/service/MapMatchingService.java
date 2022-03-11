package fi.hsl.jore.importer.feature.mapmatching.service;

import fi.hsl.jore.importer.feature.mapmatching.dto.request.MapMatchingRequestBuilder;
import fi.hsl.jore.importer.feature.mapmatching.dto.request.MapMatchingRequestDTO;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.MapMatchingResponseDTO;
import fi.hsl.jore.importer.feature.network.route_point.dto.ExportableRouteGeometry;
import fi.hsl.jore.importer.feature.network.route_point.dto.ExportableRoutePoint;
import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * This implementation queries the map matching response
 * from the map matching API.
 */
public class MapMatchingService implements IMapMatchingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapMatchingService.class);

    private final String mapMatchingApiUrl;
    private final RestTemplate restTemplate;

    public MapMatchingService(final String mapMatchingApiBaseUrl, final RestTemplate restTemplate) {
        this.mapMatchingApiUrl = MapMatchingApiUrlFactory.buildMapMatchingApiUrl(mapMatchingApiBaseUrl);
        this.restTemplate = restTemplate;
    }

    @Override
    public MapMatchingResponseDTO sendMapMatchingRequest(final ExportableRouteGeometry routeGeometry,
                                                         final List<ExportableRoutePoint> routePoints) {
        LOGGER.debug("Sending map matching request for the route direction: {}", routeGeometry.routeDirectionId());

        try {
            final MapMatchingRequestDTO request = MapMatchingRequestBuilder.createMapMatchingRequest(routeGeometry, routePoints);
            final ResponseEntity<MapMatchingResponseDTO> httpResponse = restTemplate.postForEntity(mapMatchingApiUrl,
                    request,
                    MapMatchingResponseDTO.class
            );

            final MapMatchingResponseDTO mapMatchingResponse = httpResponse.getBody();
            final String mapMatchingResponseCode = mapMatchingResponse.getCode();
            if (!mapMatchingResponseCode.equals("Ok")) {
                LOGGER.debug("The map matching response status: {} was returned for the route direction: {}",
                        mapMatchingResponseCode,
                        routeGeometry.routeDirectionId()
                );
                throw new MapMatchingException(String.format(
                        "The map matching response status: %s was returned for the route direction: %s",
                        mapMatchingResponseCode,
                        routeGeometry.routeDirectionId()
                ));
            }

            LOGGER.debug("Received map matching response for the route direction: {}", routeGeometry.routeDirectionId());
            return mapMatchingResponse;
        }
        catch (final Exception ex) {
            LOGGER.debug("Map matching failed for the route direction: {} because of an error: {}",
                    routeGeometry.routeDirectionId(),
                    ex
            );
            throw new MapMatchingException(String.format(
                    "Map matching failed for the route direction: %s because of an error: %s",
                    routeGeometry.routeDirectionId(),
                    ex.getMessage()
            ));
        }
    }
}

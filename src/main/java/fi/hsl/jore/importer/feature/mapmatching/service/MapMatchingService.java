package fi.hsl.jore.importer.feature.mapmatching.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.hsl.jore.importer.feature.mapmatching.dto.request.MapMatchingRequestBuilder;
import fi.hsl.jore.importer.feature.mapmatching.dto.request.MapMatchingRequestDTO;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.MapMatchingErrorResponseDTO;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.MapMatchingSuccessResponseDTO;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRouteGeometry;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRoutePoint;
import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/** This implementation queries the map matching response from the map matching API. */
public class MapMatchingService implements IMapMatchingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapMatchingService.class);

    private static final String ERROR_RESPONSE_PROPERTY = "\"message\":";
    private static final String RESPONSE_CODE_OK = "Ok";

    private final String mapMatchingApiUrl;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public MapMatchingService(
            final String mapMatchingApiBaseUrl, final ObjectMapper objectMapper, final RestTemplate restTemplate) {
        this.mapMatchingApiUrl = MapMatchingApiUrlFactory.buildMapMatchingApiUrl(mapMatchingApiBaseUrl);
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public MapMatchingSuccessResponseDTO sendMapMatchingRequest(
            final ImporterRouteGeometry routeGeometry, final List<ImporterRoutePoint> routePoints) {
        LOGGER.debug("Sending map matching request for the route direction: {}", routeGeometry.routeDirectionId());

        try {
            final MapMatchingRequestDTO request =
                    MapMatchingRequestBuilder.createMapMatchingRequest(routeGeometry, routePoints);
            final ResponseEntity<String> httpResponse =
                    restTemplate.postForEntity(mapMatchingApiUrl, request, String.class);

            final MapMatchingSuccessResponseDTO mapMatchingResponse = getSuccessResponse(httpResponse);
            final String mapMatchingResponseCode = mapMatchingResponse.getCode();
            if (!RESPONSE_CODE_OK.equals(mapMatchingResponseCode)) {
                final String errorMessage = String.format(
                        "The map matching response status: %s was returned for the route direction: %s",
                        mapMatchingResponseCode, routeGeometry.routeDirectionId());
                LOGGER.debug(errorMessage);
                throw new MapMatchingException(errorMessage);
            }

            LOGGER.debug(
                    "Received map matching response for the route direction: {}", routeGeometry.routeDirectionId());
            return mapMatchingResponse;
        } catch (final Exception ex) {
            final String errorMessage = String.format(
                    "Map matching failed for the route direction: %s because of an error: %s",
                    routeGeometry.routeDirectionId(), ex.getMessage());
            LOGGER.debug(errorMessage);
            throw new MapMatchingException(errorMessage);
        }
    }

    private MapMatchingSuccessResponseDTO getSuccessResponse(final ResponseEntity<String> httpResponse)
            throws JsonProcessingException {
        final String responseBody = httpResponse.getBody();
        if (responseBody == null || responseBody.contains(ERROR_RESPONSE_PROPERTY)) {
            final MapMatchingErrorResponseDTO errorResponse =
                    objectMapper.readValue(responseBody, MapMatchingErrorResponseDTO.class);
            throw new MapMatchingException(errorResponse.getMessage());
        }

        return objectMapper.readValue(responseBody, MapMatchingSuccessResponseDTO.class);
    }
}

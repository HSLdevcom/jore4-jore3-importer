package fi.hsl.jore.importer.feature.mapmatching.dto.response;

import java.util.List;

/**
 * Contains the information that's returned by the map matching
 * API.
 */
public class MapMatchingResponseDTO {

    private String code;
    private List<RouteResponseDTO> routes;

    public String getCode() {
        return code;
    }

    public List<RouteResponseDTO> getRoutes() {
        return routes;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setRoutes(final List<RouteResponseDTO> routes) {
        this.routes = routes;
    }
}

package fi.hsl.jore.importer.feature.mapmatching.dto.response;

import java.util.List;

/** Contains the information that's returned by the map matching API when a match was found. */
public class MapMatchingSuccessResponseDTO {

    private String code;
    private List<RouteDTO> routes;

    public String getCode() {
        return code;
    }

    public List<RouteDTO> getRoutes() {
        return routes;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setRoutes(final List<RouteDTO> routes) {
        this.routes = routes;
    }
}

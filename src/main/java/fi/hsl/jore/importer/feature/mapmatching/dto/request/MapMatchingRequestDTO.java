package fi.hsl.jore.importer.feature.mapmatching.dto.request;

import java.util.List;

/**
 * Contains the information that's is send to the
 * Map Matching API.
 */
public class MapMatchingRequestDTO {

    private String routeId;
    private RouteGeometryRequestDTO routeGeometry;
    private List<RoutePointRequestDTO> routePoints;

    public MapMatchingRequestDTO() {}

    public String getRouteId() {
        return routeId;
    }

    public RouteGeometryRequestDTO getRouteGeometry() {
        return routeGeometry;
    }

    public List<RoutePointRequestDTO> getRoutePoints() {
        return routePoints;
    }

    public void setRouteId(final String routeId) {
        this.routeId = routeId;
    }

    public void setRouteGeometry(final RouteGeometryRequestDTO routeGeometry) {
        this.routeGeometry = routeGeometry;
    }

    public void setRoutePoints(final List<RoutePointRequestDTO> routePoints) {
        this.routePoints = routePoints;
    }
}

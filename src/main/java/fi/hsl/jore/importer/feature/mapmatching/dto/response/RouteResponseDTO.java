package fi.hsl.jore.importer.feature.mapmatching.dto.response;

import java.util.List;

/**
 * Contains the information of a map matching response
 * which is returned by the map matching API.
 */
public class RouteResponseDTO {

    private double distance;
    private GeometryResponseDTO geometry;
    private List<InfrastructureLinkResponseDTO> paths;
    private double weight;

    public double getDistance() {
        return distance;
    }

    public GeometryResponseDTO getGeometry() {
        return geometry;
    }

    public List<InfrastructureLinkResponseDTO> getPaths() {
        return paths;
    }

    public double getWeight() {
        return weight;
    }

    public void setDistance(final double distance) {
        this.distance = distance;
    }

    public void setGeometry(final GeometryResponseDTO geometry) {
        this.geometry = geometry;
    }

    public void setPaths(final List<InfrastructureLinkResponseDTO> paths) {
        this.paths = paths;
    }

    public void setWeight(final double weight) {
        this.weight = weight;
    }
}

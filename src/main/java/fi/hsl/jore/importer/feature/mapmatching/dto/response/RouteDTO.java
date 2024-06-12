package fi.hsl.jore.importer.feature.mapmatching.dto.response;

import java.util.List;
import org.geojson.LineString;

/**
 * Contains the information of a map matching response which is returned by the map matching API.
 */
public class RouteDTO {

    private LineString geometry;
    private List<InfrastructureLinkDTO> paths;
    private double weight;
    private double distance;

    public double getDistance() {
        return distance;
    }

    public LineString getGeometry() {
        return geometry;
    }

    public List<InfrastructureLinkDTO> getPaths() {
        return paths;
    }

    public double getWeight() {
        return weight;
    }

    public void setDistance(final double distance) {
        this.distance = distance;
    }

    public void setGeometry(final LineString geometry) {
        this.geometry = geometry;
    }

    public void setPaths(final List<InfrastructureLinkDTO> paths) {
        this.paths = paths;
    }

    public void setWeight(final double weight) {
        this.weight = weight;
    }
}

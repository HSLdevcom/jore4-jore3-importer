package fi.hsl.jore.importer.feature.mapmatching.dto.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the route geometry which is send to the
 * map matching API.
 */
public class RouteGeometryRequestDTO {

    private final String type = "LineString";
    private final List<double[]> coordinates = new ArrayList<>();

    public void addCoordinate(final double x, final double y) {
        coordinates.add(new double[]{x, y});
    }

    public String getType() {
        return type;
    }

    public List<double[]> getCoordinates() {
        return coordinates;
    }
}

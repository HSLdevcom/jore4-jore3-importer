package fi.hsl.jore.importer.feature.mapmatching.dto.response;

import java.util.List;

/**
 * Contains a geometry which is returned by the
 * map matching API.
 */
public class GeometryResponseDTO {

    private String type;
    private List<double[]> coordinates;

    public String getType() {
        return type;
    }

    public List<double[]> getCoordinates() {
        return coordinates;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setCoordinates(final List<double[]> coordinates) {
        this.coordinates = coordinates;
    }
}

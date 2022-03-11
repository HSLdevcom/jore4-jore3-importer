package fi.hsl.jore.importer.feature.mapmatching.dto.request;

/**
 * Contains the location of a route point which is send to
 * the map matching API.
 */
public class RoutePointLocationRequestDTO {

    private final String type = "Point";
    private final double[] coordinates = new double[2];

    public String getType() {
        return type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    /**
     * Sets the coordinates of a route point.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void setCoordinates(final double x, final double y) {
        coordinates[0] = x;
        coordinates[1] = y;
    }
}

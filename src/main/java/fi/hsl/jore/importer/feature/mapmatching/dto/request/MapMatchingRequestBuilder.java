package fi.hsl.jore.importer.feature.mapmatching.dto.request;

import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRouteGeometry;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRoutePoint;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class MapMatchingRequestBuilder {

    /**
     * Creates a new map matching request which can be send
     * to the map matching API.
     * @param routeGeometry The exported route geometry.
     * @param routePoints   The route points of the route whose route
     *                      geometry is exported to Jore 4.
     * @return  The created map matching request.
     */
    public static MapMatchingRequestDTO createMapMatchingRequest(final ImporterRouteGeometry routeGeometry,
                                                          final io.vavr.collection.List<ImporterRoutePoint> routePoints) {
        final LineString requestGeometry = createRequestGeometry(routeGeometry);
        final List<RoutePointRequestDTO> requestRoutePoints = createRequestRoutePoints(routePoints);

        final MapMatchingRequestDTO request = new MapMatchingRequestDTO();
        request.setRouteId(routeGeometry.routeDirectionExtId());
        request.setRouteGeometry(requestGeometry);
        request.setRoutePoints(requestRoutePoints);

        return request;
    }

    private static LineString createRequestGeometry(final ImporterRouteGeometry inputGeometry) {
        final List<LngLatAlt> coordinates = new ArrayList<>();

        final Coordinate[] inputCoordinates = inputGeometry.geometry().getCoordinates();
        for (final Coordinate inputCoordinate: inputCoordinates) {
            coordinates.add(new LngLatAlt(inputCoordinate.getX(), inputCoordinate.getY()));
        }

        final LngLatAlt[] resultArray = new LngLatAlt[coordinates.size()];
        return new LineString(coordinates.toArray(resultArray));
    }

    private static List<RoutePointRequestDTO> createRequestRoutePoints(final io.vavr.collection.List<ImporterRoutePoint> inputRoutePoints) {
        final List<RoutePointRequestDTO> routePoints = new ArrayList<>();

        inputRoutePoints.forEach(input -> {
            final RoutePointRequestDTO routePoint = new RoutePointRequestDTO();

            routePoint.setType(RoutePointType.fromNodeType(input.type()));
            final Point location = toGeoJson(input.location());
            routePoint.setLocation(location);

            if (input.type() == NodeType.STOP) {
                routePoint.setNationalId(input.stopPointElyNumber().orElse(null));
                routePoint.setPassengerId(input.stopPointShortId().orElseThrow(
                        () -> new NullPointerException(String.format(
                                "Cannot create map matching request. passengerId is null and the route point is stop point with national id: %d",
                                routePoint.getNationalId()
                        ))
                ));

                final Point projectedLocation = toGeoJson(input.projectedLocation().orElseThrow(
                        () -> new NullPointerException(String.format(
                                "Cannot create map matching request. projectedLocation is null and the route point is stop point with national id: %d",
                                routePoint.getNationalId()
                        ))
                ));
                routePoint.setProjectedLocation(projectedLocation);
            }

            routePoints.add(routePoint);
        });

        return routePoints;
    }

    private static Point toGeoJson(final org.locationtech.jts.geom.Point input) {
        if (input == null) {
            return null;
        }
        return new Point(input.getX(), input.getY());
    }
}

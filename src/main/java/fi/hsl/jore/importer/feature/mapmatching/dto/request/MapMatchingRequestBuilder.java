package fi.hsl.jore.importer.feature.mapmatching.dto.request;

import fi.hsl.jore.importer.feature.network.route_point.dto.ExportableRouteGeometry;
import fi.hsl.jore.importer.feature.network.route_point.dto.ExportableRoutePoint;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

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
    public static MapMatchingRequestDTO createMapMatchingRequest(final ExportableRouteGeometry routeGeometry,
                                                          final io.vavr.collection.List<ExportableRoutePoint> routePoints) {
        final RouteGeometryRequestDTO requestGeometry = createRequestGeometry(routeGeometry);
        final List<RoutePointRequestDTO> requestRoutePoints = createRequestRoutePoints(routePoints);

        final MapMatchingRequestDTO request = new MapMatchingRequestDTO();
        request.setRouteId(routeGeometry.routeDirectionId().toString());
        request.setRouteGeometry(requestGeometry);
        request.setRoutePoints(requestRoutePoints);

        return request;
    }

    private static RouteGeometryRequestDTO createRequestGeometry(final ExportableRouteGeometry inputGeometry) {
        final RouteGeometryRequestDTO routeGeometry = new RouteGeometryRequestDTO();

        final Coordinate[] inputCoordinates = inputGeometry.geometry().getCoordinates();
        for (final Coordinate inputCoordinate: inputCoordinates) {
            routeGeometry.addCoordinate(inputCoordinate.getX(), inputCoordinate.getY());
        }

        return routeGeometry;
    }

    private static List<RoutePointRequestDTO> createRequestRoutePoints(final io.vavr.collection.List<ExportableRoutePoint> inputRoutePoints) {
        final List<RoutePointRequestDTO> routePoints = new ArrayList<>();

        inputRoutePoints.forEach(input -> {
            final RoutePointRequestDTO routePoint = new RoutePointRequestDTO();

            routePoint.setType(RoutePointType.fromNodeType(input.type()));
            routePoint.setNationalId(input.stopPointElyNumber().orElse(null));
            routePoint.setPassengerId(input.stopPointShortId().orElse(null));

            final RoutePointLocationRequestDTO location = pointToLocation(input.location());
            routePoint.setLocation(location);

            final RoutePointLocationRequestDTO projectedLocation = pointToLocation(input.projectedLocation().orElse(null));
            routePoint.setProjectedLocation(projectedLocation);

            routePoints.add(routePoint);
        });

        return routePoints;
    }

    private static RoutePointLocationRequestDTO pointToLocation(final Point input) {
        if (input == null) {
            return null;
        }

        final RoutePointLocationRequestDTO location = new RoutePointLocationRequestDTO();
        location.setCoordinates(input.getX(), input.getY());

        return location;
    }
}

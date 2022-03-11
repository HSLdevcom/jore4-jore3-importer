package fi.hsl.jore.importer.feature.mapmatching.dto.request;

import fi.hsl.jore.importer.feature.network.route_point.dto.ExportableRouteGeometry;
import fi.hsl.jore.importer.feature.network.route_point.dto.ExportableRoutePoint;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        final LineString requestGeometry = createRequestGeometry(routeGeometry);
        final List<RoutePointRequestDTO> requestRoutePoints = createRequestRoutePoints(routePoints);

        final MapMatchingRequestDTO request = new MapMatchingRequestDTO();
        request.setRouteId(routeGeometry.routeDirectionExtId());
        request.setRouteGeometry(requestGeometry);
        request.setRoutePoints(requestRoutePoints);

        return request;
    }

    private static LineString createRequestGeometry(final ExportableRouteGeometry inputGeometry) {
        final List<LngLatAlt> coordinates = new ArrayList<>();

        final Coordinate[] inputCoordinates = inputGeometry.geometry().getCoordinates();
        for (final Coordinate inputCoordinate: inputCoordinates) {
            coordinates.add(new LngLatAlt(inputCoordinate.getX(), inputCoordinate.getY()));
        }

        final LngLatAlt[] resultArray = new LngLatAlt[coordinates.size()];
        return new LineString(coordinates.toArray(resultArray));
    }

    private static List<RoutePointRequestDTO> createRequestRoutePoints(final io.vavr.collection.List<ExportableRoutePoint> inputRoutePoints) {
        final List<RoutePointRequestDTO> routePoints = new ArrayList<>();

        inputRoutePoints.forEach(input -> {
            final RoutePointRequestDTO routePoint = new RoutePointRequestDTO();

            routePoint.setType(RoutePointType.fromNodeType(input.type()));
            routePoint.setNationalId(parseToNationalId(input.stopPointElyNumber()));
            routePoint.setPassengerId(input.stopPointShortId().orElse(null));

            final Point location = pointToLocation(input.location());
            routePoint.setLocation(location);

            final Point projectedLocation = pointToLocation(input.projectedLocation().orElse(null));
            routePoint.setProjectedLocation(projectedLocation);

            routePoints.add(routePoint);
        });

        return routePoints;
    }

    private static Integer parseToNationalId(final Optional<String> elyNumberContainer) {
        return elyNumberContainer
                .filter(elyNumber -> !elyNumber.isBlank())
                .map(elyNumber -> {
                    try {
                        return Integer.parseInt(elyNumber);
                    }
                    catch (final Exception ex) {
                        return null;
                    }
                })
                .orElse(null);
    }

    private static Point pointToLocation(final org.locationtech.jts.geom.Point input) {
        if (input == null) {
            return null;
        }
        return new Point(input.getX(), input.getY());
    }
}

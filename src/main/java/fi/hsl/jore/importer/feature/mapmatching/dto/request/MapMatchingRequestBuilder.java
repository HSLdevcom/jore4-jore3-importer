package fi.hsl.jore.importer.feature.mapmatching.dto.request;

import static fi.hsl.jore.importer.util.Utils.streamIterable;

import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRouteGeometry;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRoutePoint;
import java.util.ArrayList;
import java.util.List;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.locationtech.jts.geom.Coordinate;

public class MapMatchingRequestBuilder {

    /**
     * Creates a new map matching request which can be sent to the map matching API.
     *
     * @param routeGeometry The exported route geometry.
     * @param routePoints The route points of the route whose route geometry is exported to Jore 4.
     * @return The created map matching request.
     */
    public static MapMatchingRequestDTO createMapMatchingRequest(
            final ImporterRouteGeometry routeGeometry, final Iterable<ImporterRoutePoint> routePoints) {
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
        for (final Coordinate inputCoordinate : inputCoordinates) {
            coordinates.add(new LngLatAlt(inputCoordinate.getX(), inputCoordinate.getY()));
        }

        final LngLatAlt[] resultArray = new LngLatAlt[coordinates.size()];
        return new LineString(coordinates.toArray(resultArray));
    }

    private static List<RoutePointRequestDTO> createRequestRoutePoints(
            final Iterable<ImporterRoutePoint> inputRoutePoints) {
        return streamIterable(inputRoutePoints)
                .map(MapMatchingRequestBuilder::mapImporterPointToRoutePointRequest)
                .toList();
    }

    private static RoutePointRequestDTO mapImporterPointToRoutePointRequest(final ImporterRoutePoint input) {
        final RoutePointRequestDTO routePoint = new RoutePointRequestDTO();

        routePoint.setType(RoutePointType.fromNodeType(input.type()));
        final Point location = toGeoJson(input.location());
        routePoint.setLocation(location);

        if (input.type() == NodeType.STOP) {
            routePoint.setNationalId(input.stopPointElyNumber().orElse(null));
            routePoint.setPassengerId(input.stopPointShortId()
                    .orElseThrow(() -> getMappingNpe("passengerId", routePoint.getNationalId())));

            final Point projectedLocation = toGeoJson(input.projectedLocation()
                    .orElseThrow(() -> getMappingNpe("projectedLocation", routePoint.getNationalId())));
            routePoint.setProjectedLocation(projectedLocation);
        }

        return routePoint;
    }

    private static Point toGeoJson(final org.locationtech.jts.geom.Point input) {
        if (input == null) {
            return null;
        }
        return new Point(input.getX(), input.getY());
    }

    private static NullPointerException getMappingNpe(final String fieldName, final Long nationalId) {
        final String message = String.format(
                "Cannot create map matching request. %s is null and the route point is stop point with national id: %d",
                fieldName, nationalId);
        return new NullPointerException(message);
    }
}

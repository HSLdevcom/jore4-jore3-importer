package fi.hsl.jore.importer.feature.mapmatching.service;

import fi.hsl.jore.importer.feature.mapmatching.dto.response.MapMatchingSuccessResponseDTO;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRouteGeometry;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRoutePoint;
import io.vavr.collection.List;

public interface IMapMatchingService {

    /**
     * Sends a map matching request to the map matching API.
     *
     * @param routeGeometry The route geometry queried from the importer's database.
     * @param routePoints The route points of the route. These route points are queried from the importer's database.
     * @return A response that describes the result of the map matching request. If the request was successful, the
     *     response contains the information of a public transport route which can be inserted into the Jore 4 database.
     */
    MapMatchingSuccessResponseDTO sendMapMatchingRequest(
            ImporterRouteGeometry routeGeometry, List<ImporterRoutePoint> routePoints);
}

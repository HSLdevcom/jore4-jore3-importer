package fi.hsl.jore.importer.feature.network.route_point.repository;

import fi.hsl.jore.importer.feature.network.route_point.dto.ExportableRoutePoint;
import io.vavr.collection.List;
import java.util.UUID;

public interface IRoutePointExportRepository {

    /**
     * Finds the information of a
     * @param routeDirectionId
     * @return
     */
    List<ExportableRoutePoint> findExportableRoutePointsByRouteDirectionId(UUID routeDirectionId);
}

package fi.hsl.jore.importer.feature.network.route_point.repository;

import com.google.common.annotations.VisibleForTesting;
import fi.hsl.jore.importer.feature.common.repository.IBasicCrudRepository;
import fi.hsl.jore.importer.feature.common.repository.IHistoryRepository;
import fi.hsl.jore.importer.feature.network.route_point.dto.PersistableRoutePoint;
import fi.hsl.jore.importer.feature.network.route_point.dto.RoutePoint;
import fi.hsl.jore.importer.feature.network.route_point.dto.generated.RoutePointPK;

/**
 * Only for testing, use {@link fi.hsl.jore.importer.feature.batch.route_link.support.IRoutePointImportRepository this} to
 * insert/update/delete route points.
 */
@VisibleForTesting
public interface IRoutePointTestRepository extends IBasicCrudRepository<RoutePointPK, RoutePoint, PersistableRoutePoint>,
                                                   IHistoryRepository<RoutePoint> {
}

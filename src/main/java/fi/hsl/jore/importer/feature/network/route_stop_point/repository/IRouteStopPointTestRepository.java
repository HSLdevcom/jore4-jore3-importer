package fi.hsl.jore.importer.feature.network.route_stop_point.repository;

import com.google.common.annotations.VisibleForTesting;
import fi.hsl.jore.importer.feature.common.repository.IBasicCrudRepository;
import fi.hsl.jore.importer.feature.common.repository.IHistoryRepository;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.PersistableRouteStopPoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.RouteStopPoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.generated.RouteStopPointPK;

/**
 * Only for testing, use {@link
 * fi.hsl.jore.importer.feature.batch.route_link.support.IRouteStopPointImportRepository this} to
 * insert/update/delete route stop points.
 */
@VisibleForTesting
public interface IRouteStopPointTestRepository
        extends IBasicCrudRepository<RouteStopPointPK, RouteStopPoint, PersistableRouteStopPoint>,
                IHistoryRepository<RouteStopPoint> {}

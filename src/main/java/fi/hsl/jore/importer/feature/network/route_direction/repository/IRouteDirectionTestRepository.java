package fi.hsl.jore.importer.feature.network.route_direction.repository;

import com.google.common.annotations.VisibleForTesting;
import fi.hsl.jore.importer.feature.common.repository.IBasicCrudRepository;
import fi.hsl.jore.importer.feature.common.repository.IHistoryRepository;
import fi.hsl.jore.importer.feature.network.route_direction.dto.PersistableRouteDirection;
import fi.hsl.jore.importer.feature.network.route_direction.dto.RouteDirection;
import fi.hsl.jore.importer.feature.network.route_direction.dto.generated.RouteDirectionPK;

/**
 * Only for testing, use {@link
 * fi.hsl.jore.importer.feature.batch.route_direction.support.IRouteDirectionImportRepository this}
 * to insert/update/delete routes.
 */
@VisibleForTesting
public interface IRouteDirectionTestRepository
        extends IBasicCrudRepository<RouteDirectionPK, RouteDirection, PersistableRouteDirection>,
                IHistoryRepository<RouteDirection> {}

package fi.hsl.jore.importer.feature.network.route_link.repository;

import com.google.common.annotations.VisibleForTesting;
import fi.hsl.jore.importer.feature.common.repository.IBasicCrudRepository;
import fi.hsl.jore.importer.feature.common.repository.IHistoryRepository;
import fi.hsl.jore.importer.feature.network.route_link.dto.PersistableRouteLink;
import fi.hsl.jore.importer.feature.network.route_link.dto.RouteLink;
import fi.hsl.jore.importer.feature.network.route_link.dto.generated.RouteLinkPK;

/**
 * Only for testing, use {@link fi.hsl.jore.importer.feature.batch.route_link.support.IRouteLinkImportRepository this}
 * to insert/update/delete route links.
 */
@VisibleForTesting
public interface IRouteLinkTestRepository
        extends IBasicCrudRepository<RouteLinkPK, RouteLink, PersistableRouteLink>, IHistoryRepository<RouteLink> {}

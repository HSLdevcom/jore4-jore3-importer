package fi.hsl.jore.importer.feature.batch.route_link.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.network.route_link.dto.Jore3RouteLink;
import fi.hsl.jore.importer.feature.network.route_link.dto.generated.RouteLinkPK;

public interface IRouteLinkImportRepository extends IImportRepository<Jore3RouteLink, RouteLinkPK> {
}

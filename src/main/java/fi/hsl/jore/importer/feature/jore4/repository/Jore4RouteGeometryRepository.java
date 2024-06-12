package fi.hsl.jore.importer.feature.jore4.repository;

import static fi.hsl.jore.jore4.jooq.infrastructure_network.Tables.INFRASTRUCTURE_LINK;
import static fi.hsl.jore.jore4.jooq.route.Tables.INFRASTRUCTURE_LINK_ALONG_ROUTE;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4RouteGeometry;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4RouteInfrastructureLink;
import java.util.UUID;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class Jore4RouteGeometryRepository implements IJore4RouteGeometryRepository {
    private final DSLContext db;

    @Autowired
    Jore4RouteGeometryRepository(@Qualifier("jore4Dsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    public void insert(final Iterable<? extends Jore4RouteGeometry> routeGeometries) {
        BatchBindStep batch = db.batch(db.insertInto(
                        INFRASTRUCTURE_LINK_ALONG_ROUTE,
                        INFRASTRUCTURE_LINK_ALONG_ROUTE.INFRASTRUCTURE_LINK_ID,
                        INFRASTRUCTURE_LINK_ALONG_ROUTE.ROUTE_ID,
                        INFRASTRUCTURE_LINK_ALONG_ROUTE.INFRASTRUCTURE_LINK_SEQUENCE,
                        INFRASTRUCTURE_LINK_ALONG_ROUTE.IS_TRAVERSAL_FORWARDS)
                .values((UUID) null, null, null, null));

        for (final Jore4RouteGeometry routeGeometry : routeGeometries) {
            for (final Jore4RouteInfrastructureLink infrastructureLink : routeGeometry.infrastructureLinks()) {
                batch = batch.bind(
                        db.select(INFRASTRUCTURE_LINK.INFRASTRUCTURE_LINK_ID)
                                .from(INFRASTRUCTURE_LINK)
                                .where(
                                        INFRASTRUCTURE_LINK.EXTERNAL_LINK_SOURCE.eq(
                                                infrastructureLink.infrastructureLinkSource()),
                                        INFRASTRUCTURE_LINK.EXTERNAL_LINK_ID.eq(
                                                infrastructureLink.infrastructureLinkExtId()))
                                .fetchOneInto(UUID.class),
                        routeGeometry.routeId(),
                        infrastructureLink.infrastructureLinkSequence(),
                        infrastructureLink.isTraversalForwards());
            }
        }

        if (batch.size() > 0) {
            batch.execute();
        }
    }
}

package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4RouteGeometry;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4RouteInfrastructureLink;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static fi.hsl.jore.jore4.jooq.infrastructure_network.Tables.INFRASTRUCTURE_LINK;
import static fi.hsl.jore.jore4.jooq.route.Tables.INFRASTRUCTURE_LINK_ALONG_ROUTE;

@Repository
public class Jore4RouteGeometryRepository implements IJore4RouteGeometryRepository {

    private final DSLContext db;

    @Autowired
    Jore4RouteGeometryRepository(@Qualifier("jore4Dsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    public void insert(final List<? extends Jore4RouteGeometry> routeGeometries) {
        if (!routeGeometries.isEmpty()) {
            final BatchBindStep batch = db.batch(db.insertInto(
                    INFRASTRUCTURE_LINK_ALONG_ROUTE,
                    INFRASTRUCTURE_LINK_ALONG_ROUTE.INFRASTRUCTURE_LINK_ID,
                    INFRASTRUCTURE_LINK_ALONG_ROUTE.ROUTE_ID,
                    INFRASTRUCTURE_LINK_ALONG_ROUTE.INFRASTRUCTURE_LINK_SEQUENCE,
                    INFRASTRUCTURE_LINK_ALONG_ROUTE.IS_TRAVERSAL_FORWARDS
                ).values((UUID) null, null, null, null)
            );

            routeGeometries.forEach(routeGeometry -> {
                final List<Jore4RouteInfrastructureLink> infrastructureLinks = routeGeometry.infrastructureLinks()
                        .asJava();

                infrastructureLinks.forEach(infrastructureLink -> batch.bind(
                        db.select(INFRASTRUCTURE_LINK.INFRASTRUCTURE_LINK_ID)
                                .from(INFRASTRUCTURE_LINK)
                                .where(
                                        INFRASTRUCTURE_LINK.EXTERNAL_LINK_SOURCE.eq(infrastructureLink.infrastructureLinkSource()),
                                        INFRASTRUCTURE_LINK.EXTERNAL_LINK_ID.eq(infrastructureLink.infrastructureLinkExtId())
                                )
                                .fetchOneInto(UUID.class),
                        routeGeometry.routeId(),
                        infrastructureLink.infrastructureLinkSequence(),
                        infrastructureLink.isTraversalForwards()
                ));
            });

            batch.execute();
        }
    }
}

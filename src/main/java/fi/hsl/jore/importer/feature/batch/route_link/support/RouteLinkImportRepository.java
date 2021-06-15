package fi.hsl.jore.importer.feature.batch.route_link.support;

import fi.hsl.jore.importer.feature.batch.common.AbstractImportRepository;
import fi.hsl.jore.importer.feature.network.route_link.dto.ImportableRouteLink;
import fi.hsl.jore.importer.feature.network.route_link.dto.generated.RouteLinkPK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinks;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirections;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteLinks;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteLinksStaging;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.jooq.impl.DSL.selectOne;

@Repository
public class RouteLinkImportRepository
        extends AbstractImportRepository<ImportableRouteLink, RouteLinkPK>
        implements IRouteLinkImportRepository {

    private static final NetworkRouteLinksStaging STAGING_TABLE = NetworkRouteLinksStaging.NETWORK_ROUTE_LINKS_STAGING;
    private static final NetworkRouteLinks TARGET_TABLE = NetworkRouteLinks.NETWORK_ROUTE_LINKS;
    private static final NetworkRouteDirections ROUTE_DIRECTIONS_TABLE = NetworkRouteDirections.NETWORK_ROUTE_DIRECTIONS;
    private static final InfrastructureLinks LINKS_TABLE = InfrastructureLinks.INFRASTRUCTURE_LINKS;

    private final DSLContext db;

    @Autowired
    public RouteLinkImportRepository(final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public void clearStagingTable() {
        db.truncate(STAGING_TABLE)
          .execute();
    }

    @Override
    @Transactional
    public void submitToStaging(final Iterable<? extends ImportableRouteLink> links) {
        final BatchBindStep batch = db.batch(db.insertInto(STAGING_TABLE,
                                                           STAGING_TABLE.NETWORK_ROUTE_LINK_EXT_ID,
                                                           STAGING_TABLE.NETWORK_ROUTE_DIRECTION_EXT_ID,
                                                           STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID,
                                                           STAGING_TABLE.NETWORK_ROUTE_LINK_ORDER)
                                               .values((String) null, null, null, null));

        links.forEach(link ->
                              batch.bind(
                                      link.externalId().value(),
                                      link.routeDirection().value(),
                                      link.link().value(),
                                      link.orderNumber()
                              ));

        batch.execute();
    }

    protected Set<RouteLinkPK> delete() {
        return db.deleteFrom(TARGET_TABLE)
                 // Find rows which are missing from the latest dataset
                 .whereNotExists(selectOne()
                                         .from(STAGING_TABLE)
                                         .where(STAGING_TABLE.NETWORK_ROUTE_LINK_EXT_ID.eq(TARGET_TABLE.NETWORK_ROUTE_LINK_EXT_ID)))
                 .returningResult(TARGET_TABLE.NETWORK_ROUTE_LINK_ID)
                 .fetch()
                 .stream()
                 .map(row -> RouteLinkPK.of(row.value1()))
                 .collect(HashSet.collector());
    }

    protected Set<RouteLinkPK> update() {
        return db.update(TARGET_TABLE)
                 // What fields to update
                 .set(TARGET_TABLE.NETWORK_ROUTE_LINK_ORDER,
                      STAGING_TABLE.NETWORK_ROUTE_LINK_ORDER)
                 .from(STAGING_TABLE)
                 // Find source rows..
                 .where(TARGET_TABLE.NETWORK_ROUTE_LINK_EXT_ID
                                .eq(STAGING_TABLE.NETWORK_ROUTE_LINK_EXT_ID))
                 // .. with updated fields
                 .and(
                         (TARGET_TABLE.NETWORK_ROUTE_LINK_ORDER.ne(STAGING_TABLE.NETWORK_ROUTE_LINK_ORDER))
                 )
                 .returningResult(TARGET_TABLE.NETWORK_ROUTE_LINK_ID)
                 .fetch()
                 .stream()
                 .map(row -> RouteLinkPK.of(row.value1()))
                 .collect(HashSet.collector());
    }

    protected Set<RouteLinkPK> insert() {
        return db.insertInto(TARGET_TABLE)
                 .columns(TARGET_TABLE.NETWORK_ROUTE_LINK_EXT_ID,
                          TARGET_TABLE.NETWORK_ROUTE_DIRECTION_ID,
                          TARGET_TABLE.INFRASTRUCTURE_LINK_ID,
                          TARGET_TABLE.NETWORK_ROUTE_LINK_ORDER)
                 .select(db.select(STAGING_TABLE.NETWORK_ROUTE_LINK_EXT_ID,
                                   ROUTE_DIRECTIONS_TABLE.NETWORK_ROUTE_DIRECTION_ID,
                                   LINKS_TABLE.INFRASTRUCTURE_LINK_ID,
                                   STAGING_TABLE.NETWORK_ROUTE_LINK_ORDER)
                           .from(STAGING_TABLE)
                           .innerJoin(ROUTE_DIRECTIONS_TABLE).on(ROUTE_DIRECTIONS_TABLE.NETWORK_ROUTE_DIRECTION_EXT_ID.eq(STAGING_TABLE.NETWORK_ROUTE_DIRECTION_EXT_ID))
                           .innerJoin(LINKS_TABLE).on(LINKS_TABLE.INFRASTRUCTURE_LINK_EXT_ID.eq(STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID))
                           .whereNotExists(selectOne()
                                                   .from(TARGET_TABLE)
                                                   .where(TARGET_TABLE.NETWORK_ROUTE_LINK_EXT_ID.eq(STAGING_TABLE.NETWORK_ROUTE_LINK_EXT_ID))))
                 .returningResult(TARGET_TABLE.NETWORK_ROUTE_LINK_ID)
                 .fetch()
                 .stream()
                 .map(row -> RouteLinkPK.of(row.value1()))
                 .collect(HashSet.collector());
    }
}

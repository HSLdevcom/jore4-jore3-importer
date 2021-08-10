package fi.hsl.jore.importer.feature.batch.route_link.support;

import fi.hsl.jore.importer.feature.batch.common.AbstractImportRepository;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImportableRoutePoint;
import fi.hsl.jore.importer.feature.network.route_point.dto.generated.RoutePointPK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodes;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirections;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutePoints;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutePointsStaging;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.jooq.impl.DSL.selectOne;

@Repository
public class RoutePointImportRepository
        extends AbstractImportRepository<ImportableRoutePoint, RoutePointPK>
        implements IRoutePointImportRepository {

    private static final NetworkRoutePointsStaging STAGING_TABLE = NetworkRoutePointsStaging.NETWORK_ROUTE_POINTS_STAGING;
    private static final NetworkRoutePoints TARGET_TABLE = NetworkRoutePoints.NETWORK_ROUTE_POINTS;
    private static final NetworkRouteDirections ROUTE_DIRECTIONS_TABLE = NetworkRouteDirections.NETWORK_ROUTE_DIRECTIONS;
    private static final InfrastructureNodes NODES_TABLE = InfrastructureNodes.INFRASTRUCTURE_NODES;

    private final DSLContext db;

    @Autowired
    public RoutePointImportRepository(final DSLContext db) {
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
    public void submitToStaging(final Iterable<? extends ImportableRoutePoint> points) {
        final BatchBindStep batch = db.batch(db.insertInto(STAGING_TABLE,
                                                           STAGING_TABLE.NETWORK_ROUTE_POINT_EXT_ID,
                                                           STAGING_TABLE.NETWORK_ROUTE_DIRECTION_EXT_ID,
                                                           STAGING_TABLE.INFRASTRUCTURE_NODE_EXT_ID,
                                                           STAGING_TABLE.NETWORK_ROUTE_POINT_ORDER)
                                               .values((String) null, null, null, null));

        points.forEach(point ->
                               batch.bind(
                                       point.externalId().value(),
                                       point.routeDirection().value(),
                                       point.node().value(),
                                       point.orderNumber()
                               ));

        batch.execute();
    }

    protected Set<RoutePointPK> delete() {
        return db.deleteFrom(TARGET_TABLE)
                 // Find rows which are missing from the latest dataset
                 .whereNotExists(selectOne()
                                         .from(STAGING_TABLE)
                                         .where(STAGING_TABLE.NETWORK_ROUTE_POINT_EXT_ID.eq(TARGET_TABLE.NETWORK_ROUTE_POINT_EXT_ID)))
                 .returningResult(TARGET_TABLE.NETWORK_ROUTE_POINT_ID)
                 .fetch()
                 .stream()
                 .map(row -> RoutePointPK.of(row.value1()))
                 .collect(HashSet.collector());
    }

    protected Set<RoutePointPK> update() {
        return db.update(TARGET_TABLE)
                 // What fields to update
                 .set(TARGET_TABLE.NETWORK_ROUTE_POINT_ORDER,
                      STAGING_TABLE.NETWORK_ROUTE_POINT_ORDER)
                 .from(STAGING_TABLE)
                 // Find source rows..
                 .where(TARGET_TABLE.NETWORK_ROUTE_POINT_EXT_ID
                                .eq(STAGING_TABLE.NETWORK_ROUTE_POINT_EXT_ID))
                 // .. with updated fields
                 .and(
                         (TARGET_TABLE.NETWORK_ROUTE_POINT_ORDER.ne(STAGING_TABLE.NETWORK_ROUTE_POINT_ORDER))
                 )
                 .returningResult(TARGET_TABLE.NETWORK_ROUTE_POINT_ID)
                 .fetch()
                 .stream()
                 .map(row -> RoutePointPK.of(row.value1()))
                 .collect(HashSet.collector());
    }

    protected Set<RoutePointPK> insert() {
        return db.insertInto(TARGET_TABLE)
                 .columns(TARGET_TABLE.NETWORK_ROUTE_POINT_EXT_ID,
                          TARGET_TABLE.NETWORK_ROUTE_DIRECTION_ID,
                          TARGET_TABLE.INFRASTRUCTURE_NODE,
                          TARGET_TABLE.NETWORK_ROUTE_POINT_ORDER)
                 .select(db.select(STAGING_TABLE.NETWORK_ROUTE_POINT_EXT_ID,
                                   ROUTE_DIRECTIONS_TABLE.NETWORK_ROUTE_DIRECTION_ID,
                                   NODES_TABLE.INFRASTRUCTURE_NODE_ID,
                                   STAGING_TABLE.NETWORK_ROUTE_POINT_ORDER)
                           .from(STAGING_TABLE)
                           .innerJoin(ROUTE_DIRECTIONS_TABLE).on(ROUTE_DIRECTIONS_TABLE.NETWORK_ROUTE_DIRECTION_EXT_ID.eq(STAGING_TABLE.NETWORK_ROUTE_DIRECTION_EXT_ID))
                           // At the moment there are about 150 route points, which are discarded because they
                           // refer to non-existing nodes
                           .innerJoin(NODES_TABLE).on(NODES_TABLE.INFRASTRUCTURE_NODE_EXT_ID.eq(STAGING_TABLE.INFRASTRUCTURE_NODE_EXT_ID))
                           .whereNotExists(selectOne()
                                                   .from(TARGET_TABLE)
                                                   .where(TARGET_TABLE.NETWORK_ROUTE_POINT_EXT_ID.eq(STAGING_TABLE.NETWORK_ROUTE_POINT_EXT_ID))))
                 .returningResult(TARGET_TABLE.NETWORK_ROUTE_POINT_ID)
                 .fetch()
                 .stream()
                 .map(row -> RoutePointPK.of(row.value1()))
                 .collect(HashSet.collector());
    }
}
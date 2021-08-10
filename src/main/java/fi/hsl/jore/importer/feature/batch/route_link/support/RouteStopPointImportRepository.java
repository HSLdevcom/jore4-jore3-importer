package fi.hsl.jore.importer.feature.batch.route_link.support;

import fi.hsl.jore.importer.feature.batch.common.AbstractImportRepository;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.ImportableRouteStopPoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.generated.RouteStopPointPK;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutePoints;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteStopPoints;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteStopPointsStaging;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.jooq.impl.DSL.selectOne;

@Repository
public class RouteStopPointImportRepository
        extends AbstractImportRepository<ImportableRouteStopPoint, RouteStopPointPK>
        implements IRouteStopPointImportRepository {

    private static final NetworkRouteStopPointsStaging STAGING_TABLE = NetworkRouteStopPointsStaging.NETWORK_ROUTE_STOP_POINTS_STAGING;
    private static final NetworkRouteStopPoints TARGET_TABLE = NetworkRouteStopPoints.NETWORK_ROUTE_STOP_POINTS;
    private static final NetworkRoutePoints ROUTE_POINTS_TABLE = NetworkRoutePoints.NETWORK_ROUTE_POINTS;

    private final DSLContext db;

    @Autowired
    public RouteStopPointImportRepository(final DSLContext db) {
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
    public void submitToStaging(final Iterable<? extends ImportableRouteStopPoint> points) {
        final BatchBindStep batch = db.batch(db.insertInto(STAGING_TABLE,
                                                           STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_EXT_ID,
                                                           STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_ORDER,
                                                           STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_HASTUS_POINT,
                                                           STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_TIMETABLE_COLUMN)
                                               .values((String) null, null, null, null));

        points.forEach(point ->
                               batch.bind(
                                       point.externalId().value(),
                                       point.orderNumber(),
                                       point.hastusStopPoint(),
                                       point.timetableColumn().orElse(null)
                               ));

        batch.execute();
    }

    protected Set<RouteStopPointPK> delete() {
        return db.deleteFrom(TARGET_TABLE)
                 // Find rows which are missing from the latest dataset
                 .whereNotExists(selectOne()
                                         .from(STAGING_TABLE)
                                         .where(STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_EXT_ID.eq(TARGET_TABLE.NETWORK_ROUTE_STOP_POINT_EXT_ID)))
                 .returningResult(TARGET_TABLE.NETWORK_ROUTE_POINT_ID)
                 .fetch()
                 .stream()
                 .map(row -> RouteStopPointPK.of(row.value1()))
                 .collect(HashSet.collector());
    }

    protected Set<RouteStopPointPK> update() {
        return db.update(TARGET_TABLE)
                 // What fields to update
                 .set(TARGET_TABLE.NETWORK_ROUTE_STOP_POINT_ORDER,
                      STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_ORDER)
                 .set(TARGET_TABLE.NETWORK_ROUTE_STOP_POINT_HASTUS_POINT,
                      STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_HASTUS_POINT)
                 .set(TARGET_TABLE.NETWORK_ROUTE_STOP_POINT_TIMETABLE_COLUMN,
                      STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_TIMETABLE_COLUMN)
                 .from(STAGING_TABLE)
                 // Find source rows..
                 .where(TARGET_TABLE.NETWORK_ROUTE_STOP_POINT_EXT_ID
                                .eq(STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_EXT_ID))
                 // .. with updated fields
                 .and(
                         (TARGET_TABLE.NETWORK_ROUTE_STOP_POINT_ORDER.ne(STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_ORDER))
                                 .or(TARGET_TABLE.NETWORK_ROUTE_STOP_POINT_HASTUS_POINT.ne(STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_HASTUS_POINT))
                                 // timetable may be null => use distinct from
                                 .or(TARGET_TABLE.NETWORK_ROUTE_STOP_POINT_TIMETABLE_COLUMN.isDistinctFrom(STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_TIMETABLE_COLUMN))
                 )
                 .returningResult(TARGET_TABLE.NETWORK_ROUTE_POINT_ID)
                 .fetch()
                 .stream()
                 .map(row -> RouteStopPointPK.of(row.value1()))
                 .collect(HashSet.collector());
    }

    protected Set<RouteStopPointPK> insert() {
        return db.insertInto(TARGET_TABLE)
                 .columns(TARGET_TABLE.NETWORK_ROUTE_POINT_ID,
                          TARGET_TABLE.NETWORK_ROUTE_STOP_POINT_EXT_ID,
                          TARGET_TABLE.NETWORK_ROUTE_STOP_POINT_ORDER,
                          TARGET_TABLE.NETWORK_ROUTE_STOP_POINT_HASTUS_POINT,
                          TARGET_TABLE.NETWORK_ROUTE_STOP_POINT_TIMETABLE_COLUMN)
                 .select(db.select(ROUTE_POINTS_TABLE.NETWORK_ROUTE_POINT_ID,
                                   STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_EXT_ID,
                                   STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_ORDER,
                                   STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_HASTUS_POINT,
                                   STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_TIMETABLE_COLUMN)
                           .from(STAGING_TABLE)
                           // Note that the stop point ext id is the same as the referenced route point ext id
                           .innerJoin(ROUTE_POINTS_TABLE).on(ROUTE_POINTS_TABLE.NETWORK_ROUTE_POINT_EXT_ID.eq(STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_EXT_ID))
                           .whereNotExists(selectOne()
                                                   .from(TARGET_TABLE)
                                                   .where(TARGET_TABLE.NETWORK_ROUTE_STOP_POINT_EXT_ID.eq(STAGING_TABLE.NETWORK_ROUTE_STOP_POINT_EXT_ID))))
                 .returningResult(TARGET_TABLE.NETWORK_ROUTE_POINT_ID)
                 .fetch()
                 .stream()
                 .map(row -> RouteStopPointPK.of(row.value1()))
                 .collect(HashSet.collector());
    }
}
package fi.hsl.jore.importer.feature.network.route_stop_point.repository;


import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.PersistableRouteStopPoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.RouteStopPoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.generated.RouteStopPointPK;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteStopPoints;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteStopPointsWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRouteStopPointsRecord;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RouteStopPointRepository
        implements IRouteStopPointTestRepository {

    private static final NetworkRouteStopPoints POINTS = NetworkRouteStopPoints.NETWORK_ROUTE_STOP_POINTS;
    private static final NetworkRouteStopPointsWithHistory HISTORY_VIEW = NetworkRouteStopPointsWithHistory.NETWORK_ROUTE_STOP_POINTS_WITH_HISTORY;
    private static final TableField<NetworkRouteStopPointsRecord, UUID> PRIMARY_KEY = POINTS.NETWORK_ROUTE_POINT_ID;

    private final DSLContext db;

    @Autowired
    public RouteStopPointRepository(final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public RouteStopPointPK insert(final PersistableRouteStopPoint point) {
        final NetworkRouteStopPointsRecord r = db.newRecord(POINTS);

        r.setNetworkRouteStopPointExtId(point.externalId().value());
        r.setNetworkRouteStopPointOrder(point.orderNumber());
        r.setNetworkRouteStopPointHastusPoint(point.hastusStopPoint());
        r.setNetworkRouteStopPointTimetableColumn(point.timetableColumn().orElse(null));

        r.store();

        return RouteStopPointPK.of(r.getNetworkRoutePointId());
    }

    @Override
    @Transactional
    public List<RouteStopPointPK> insert(final List<PersistableRouteStopPoint> entities) {
        return entities.map(this::insert);
    }

    @Override
    @Transactional
    public List<RouteStopPointPK> insert(final PersistableRouteStopPoint... entities) {
        return insert(List.of(entities));
    }

    @Override
    @Transactional
    public RouteStopPointPK update(final RouteStopPoint point) {
        final NetworkRouteStopPointsRecord r =
                Optional.ofNullable(db.selectFrom(POINTS)
                                      .where(PRIMARY_KEY.eq(point.pk().value()))
                                      .fetchAny())
                        .orElseThrow();

        r.setNetworkRouteStopPointExtId(point.externalId().value());
        r.setNetworkRouteStopPointOrder(point.orderNumber());
        r.setNetworkRouteStopPointHastusPoint(point.hastusStopPoint());
        r.setNetworkRouteStopPointTimetableColumn(point.timetableColumn().orElse(null));

        r.store();

        return RouteStopPointPK.of(r.getNetworkRoutePointId());
    }

    @Override
    @Transactional
    public List<RouteStopPointPK> update(final List<RouteStopPoint> entities) {
        return entities.map(this::update);
    }

    @Override
    @Transactional
    public List<RouteStopPointPK> update(final RouteStopPoint... entities) {
        return update(List.of(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouteStopPoint> findById(final RouteStopPointPK id) {
        return db.selectFrom(POINTS)
                 .where(PRIMARY_KEY.eq(id.value()))
                 .fetchStream()
                 .map(RouteStopPoint::from)
                 .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouteStopPoint> findByExternalId(final ExternalId externalId) {
        return db.selectFrom(POINTS)
                 .where(POINTS.NETWORK_ROUTE_STOP_POINT_EXT_ID.eq(externalId.value()))
                 .fetchStream()
                 .map(RouteStopPoint::from)
                 .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteStopPoint> findAll() {
        return db.selectFrom(POINTS)
                 .fetchStream()
                 .map(RouteStopPoint::from)
                 .collect(List.collector());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<RouteStopPointPK> findAllIds() {
        return db.select(PRIMARY_KEY)
                 .from(POINTS)
                 .fetchStream()
                 .map(row -> RouteStopPointPK.of(row.value1()))
                 .collect(HashSet.collector());
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        //noinspection ConstantConditions
        return db.selectCount()
                 .from(POINTS)
                 .fetchOne(0, int.class);
    }

    @Override
    @Transactional(readOnly = true)
    public int countHistory() {
        //noinspection ConstantConditions
        return db.selectCount()
                 .from(HISTORY_VIEW)
                 .fetchOne(0, int.class);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean empty() {
        return count() == 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emptyHistory() {
        return countHistory() == 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteStopPoint> findFromHistory() {
        return db.selectFrom(HISTORY_VIEW)
                 .orderBy(HISTORY_VIEW.NETWORK_ROUTE_STOP_POINT_SYS_PERIOD.asc())
                 .fetchStream()
                 .map(RouteStopPoint::from)
                 .collect(List.collector());
    }
}

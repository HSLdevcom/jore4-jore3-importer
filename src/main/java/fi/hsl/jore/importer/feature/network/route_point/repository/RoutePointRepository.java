package fi.hsl.jore.importer.feature.network.route_point.repository;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.route_point.dto.PersistableRoutePoint;
import fi.hsl.jore.importer.feature.network.route_point.dto.RoutePoint;
import fi.hsl.jore.importer.feature.network.route_point.dto.generated.RoutePointPK;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutePoints;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutePointsWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRoutePointsRecord;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RoutePointRepository implements IRoutePointTestRepository {

    private static final NetworkRoutePoints POINTS = NetworkRoutePoints.NETWORK_ROUTE_POINTS;
    private static final NetworkRoutePointsWithHistory HISTORY_VIEW =
            NetworkRoutePointsWithHistory.NETWORK_ROUTE_POINTS_WITH_HISTORY;
    private static final TableField<NetworkRoutePointsRecord, UUID> PRIMARY_KEY = POINTS.NETWORK_ROUTE_POINT_ID;

    private final DSLContext db;

    @Autowired
    public RoutePointRepository(@Qualifier("importerDsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public RoutePointPK insert(final PersistableRoutePoint point) {
        final NetworkRoutePointsRecord r = db.newRecord(POINTS);

        r.setNetworkRoutePointExtId(point.externalId().value());
        r.setInfrastructureNode(point.node().value());
        r.setNetworkRouteDirectionId(point.routeDirection().value());
        r.setNetworkRoutePointOrder(point.orderNumber());

        r.store();

        return RoutePointPK.of(r.getNetworkRoutePointId());
    }

    @Override
    @Transactional
    public List<RoutePointPK> insert(final List<PersistableRoutePoint> entities) {
        return entities.stream().map(this::insert).toList();
    }

    @Override
    @Transactional
    public List<RoutePointPK> insert(final PersistableRoutePoint... entities) {
        return insert(List.of(entities));
    }

    @Override
    @Transactional
    public RoutePointPK update(final RoutePoint point) {
        final NetworkRoutePointsRecord r = Optional.ofNullable(db.selectFrom(POINTS)
                        .where(PRIMARY_KEY.eq(point.pk().value()))
                        .fetchAny())
                .orElseThrow();

        r.setNetworkRoutePointExtId(point.externalId().value());
        r.setInfrastructureNode(point.node().value());
        r.setNetworkRouteDirectionId(point.routeDirection().value());
        r.setNetworkRoutePointOrder(point.orderNumber());

        r.store();

        return RoutePointPK.of(r.getNetworkRoutePointId());
    }

    @Override
    @Transactional
    public List<RoutePointPK> update(final List<RoutePoint> entities) {
        return entities.stream().map(this::update).toList();
    }

    @Override
    @Transactional
    public List<RoutePointPK> update(final RoutePoint... entities) {
        return update(List.of(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoutePoint> findById(final RoutePointPK id) {
        return db.selectFrom(POINTS)
                .where(PRIMARY_KEY.eq(id.value()))
                .fetchStream()
                .map(RoutePoint::from)
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoutePoint> findByExternalId(final ExternalId externalId) {
        return db.selectFrom(POINTS)
                .where(POINTS.NETWORK_ROUTE_POINT_EXT_ID.eq(externalId.value()))
                .fetchStream()
                .map(RoutePoint::from)
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoutePoint> findAll() {
        return db.selectFrom(POINTS).fetchStream().map(RoutePoint::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<RoutePointPK> findAllIds() {
        return db.select(PRIMARY_KEY)
                .from(POINTS)
                .fetchStream()
                .map(row -> RoutePointPK.of(row.value1()))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        //noinspection ConstantConditions
        return db.selectCount().from(POINTS).fetchOne(0, int.class);
    }

    @Override
    @Transactional(readOnly = true)
    public int countHistory() {
        //noinspection ConstantConditions
        return db.selectCount().from(HISTORY_VIEW).fetchOne(0, int.class);
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
    public List<RoutePoint> findFromHistory() {
        return db.selectFrom(HISTORY_VIEW)
                .orderBy(HISTORY_VIEW.NETWORK_ROUTE_POINT_SYS_PERIOD.asc())
                .fetchStream()
                .map(RoutePoint::from)
                .toList();
    }
}

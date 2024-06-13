package fi.hsl.jore.importer.feature.network.route_direction.repository;

import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.route_direction.dto.PersistableRouteDirection;
import fi.hsl.jore.importer.feature.network.route_direction.dto.RouteDirection;
import fi.hsl.jore.importer.feature.network.route_direction.dto.generated.RouteDirectionPK;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirections;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirectionsWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRouteDirectionsRecord;
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
public class RouteDirectionRepository implements IRouteDirectionTestRepository {

    private static final NetworkRouteDirections DIRECTION = NetworkRouteDirections.NETWORK_ROUTE_DIRECTIONS;
    private static final NetworkRouteDirectionsWithHistory HISTORY_VIEW =
            NetworkRouteDirectionsWithHistory.NETWORK_ROUTE_DIRECTIONS_WITH_HISTORY;
    private static final TableField<NetworkRouteDirectionsRecord, UUID> PRIMARY_KEY =
            DIRECTION.NETWORK_ROUTE_DIRECTION_ID;

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    @Autowired
    public RouteDirectionRepository(
            @Qualifier("importerDsl") final DSLContext db, final IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter = jsonbConverter;
    }

    @Override
    @Transactional
    public RouteDirectionPK insert(final PersistableRouteDirection routeDirection) {
        final NetworkRouteDirectionsRecord r = db.newRecord(DIRECTION);

        r.setNetworkRouteDirectionExtId(routeDirection.externalId().value());
        r.setNetworkRouteId(routeDirection.routeId().value());
        r.setNetworkRouteDirectionType(routeDirection.direction().label());
        r.setNetworkRouteDirectionName(jsonbConverter.asJson(routeDirection.name()));
        r.setNetworkRouteDirectionNameShort(jsonbConverter.asJson(routeDirection.nameShort()));
        r.setNetworkRouteDirectionOrigin(jsonbConverter.asJson(routeDirection.origin()));
        r.setNetworkRouteDirectionDestination(jsonbConverter.asJson(routeDirection.destination()));
        r.setNetworkRouteDirectionValidDateRange(routeDirection.validTime());

        r.store();

        return RouteDirectionPK.of(r.getNetworkRouteDirectionId());
    }

    @Override
    @Transactional
    public List<RouteDirectionPK> insert(final List<PersistableRouteDirection> entities) {
        return entities.stream().map(this::insert).toList();
    }

    @Override
    @Transactional
    public List<RouteDirectionPK> insert(final PersistableRouteDirection... entities) {
        return insert(List.of(entities));
    }

    @Override
    @Transactional
    public RouteDirectionPK update(final RouteDirection routeDirection) {
        final NetworkRouteDirectionsRecord r = Optional.ofNullable(db.selectFrom(DIRECTION)
                        .where(PRIMARY_KEY.eq(routeDirection.pk().value()))
                        .fetchAny())
                .orElseThrow();

        r.setNetworkRouteDirectionName(jsonbConverter.asJson(routeDirection.name()));
        r.setNetworkRouteDirectionNameShort(jsonbConverter.asJson(routeDirection.nameShort()));
        r.setNetworkRouteDirectionOrigin(jsonbConverter.asJson(routeDirection.origin()));
        r.setNetworkRouteDirectionDestination(jsonbConverter.asJson(routeDirection.destination()));
        r.setNetworkRouteDirectionValidDateRange(routeDirection.validTime());

        r.store();

        return RouteDirectionPK.of(r.getNetworkRouteDirectionId());
    }

    @Override
    @Transactional
    public List<RouteDirectionPK> update(final List<RouteDirection> entities) {
        return entities.stream().map(this::update).toList();
    }

    @Override
    @Transactional
    public List<RouteDirectionPK> update(final RouteDirection... entities) {
        return update(List.of(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouteDirection> findById(final RouteDirectionPK id) {
        return db.selectFrom(DIRECTION)
                .where(PRIMARY_KEY.eq(id.value()))
                .fetchStream()
                .map(record -> RouteDirection.from(record, jsonbConverter))
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouteDirection> findByExternalId(final ExternalId externalId) {
        return db.selectFrom(DIRECTION)
                .where(DIRECTION.NETWORK_ROUTE_DIRECTION_EXT_ID.eq(externalId.value()))
                .fetchStream()
                .map(record -> RouteDirection.from(record, jsonbConverter))
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteDirection> findAll() {
        return db.selectFrom(DIRECTION)
                .fetchStream()
                .map(record -> RouteDirection.from(record, jsonbConverter))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<RouteDirectionPK> findAllIds() {
        return db.select(PRIMARY_KEY)
                .from(DIRECTION)
                .fetchStream()
                .map(row -> RouteDirectionPK.of(row.value1()))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        //noinspection ConstantConditions
        return db.selectCount().from(DIRECTION).fetchOne(0, int.class);
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
    public List<RouteDirection> findFromHistory() {
        return db.selectFrom(HISTORY_VIEW)
                .orderBy(HISTORY_VIEW.NETWORK_ROUTE_DIRECTION_SYS_PERIOD.asc())
                .fetchStream()
                .map(record -> RouteDirection.from(record, jsonbConverter))
                .toList();
    }
}

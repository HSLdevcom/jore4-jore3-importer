package fi.hsl.jore.importer.feature.network.route.repository;


import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.route.dto.PersistableRoute;
import fi.hsl.jore.importer.feature.network.route.dto.Route;
import fi.hsl.jore.importer.feature.network.route.dto.generated.RoutePK;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutes;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutesWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRoutesRecord;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RouteRepository
        implements IRouteTestRepository {

    private static final NetworkRoutes ROUTE = NetworkRoutes.NETWORK_ROUTES;
    private static final NetworkRoutesWithHistory HISTORY_VIEW = NetworkRoutesWithHistory.NETWORK_ROUTES_WITH_HISTORY;
    private static final TableField<NetworkRoutesRecord, UUID> PRIMARY_KEY = ROUTE.NETWORK_ROUTE_ID;

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    @Autowired
    public RouteRepository(@Qualifier("importerDsl") final DSLContext db,
                           final IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter = jsonbConverter;
    }

    @Override
    @Transactional
    public RoutePK insert(final PersistableRoute route) {
        final NetworkRoutesRecord r = db.newRecord(ROUTE);

        r.setNetworkRouteExtId(route.externalId().value());
        r.setNetworkLineId(route.line().value());
        r.setNetworkRouteNumber(route.routeNumber());
        r.setNetworkRouteName(jsonbConverter.asJson(route.name()));

        r.store();

        return RoutePK.of(r.getNetworkRouteId());
    }

    @Override
    @Transactional
    public List<RoutePK> insert(final List<PersistableRoute> entities) {
        return entities.map(this::insert);
    }

    @Override
    @Transactional
    public List<RoutePK> insert(final PersistableRoute... entities) {
        return insert(List.of(entities));
    }

    @Override
    @Transactional
    public RoutePK update(final Route route) {
        final NetworkRoutesRecord r =
                Optional.ofNullable(db.selectFrom(ROUTE)
                                      .where(PRIMARY_KEY.eq(route.pk().value()))
                                      .fetchAny())
                        .orElseThrow();

        r.setNetworkRouteName(jsonbConverter.asJson(route.name()));

        r.store();

        return RoutePK.of(r.getNetworkRouteId());
    }

    @Override
    @Transactional
    public List<RoutePK> update(final List<Route> entities) {
        return entities.map(this::update);
    }

    @Override
    @Transactional
    public List<RoutePK> update(final Route... entities) {
        return update(List.of(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Route> findById(final RoutePK id) {
        return db.selectFrom(ROUTE)
                 .where(PRIMARY_KEY.eq(id.value()))
                 .fetchStream()
                 .map(record -> Route.from(record, jsonbConverter))
                 .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Route> findByExternalId(final ExternalId externalId) {
        return db.selectFrom(ROUTE)
                 .where(ROUTE.NETWORK_ROUTE_EXT_ID.eq(externalId.value()))
                 .fetchStream()
                 .map(record -> Route.from(record, jsonbConverter))
                 .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> findAll() {
        return db.selectFrom(ROUTE)
                 .fetchStream()
                 .map(record -> Route.from(record, jsonbConverter))
                 .collect(List.collector());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<RoutePK> findAllIds() {
        return db.select(PRIMARY_KEY)
                 .from(ROUTE)
                 .fetchStream()
                 .map(row -> RoutePK.of(row.value1()))
                 .collect(HashSet.collector());
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        //noinspection ConstantConditions
        return db.selectCount()
                 .from(ROUTE)
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
    public List<Route> findFromHistory() {
        return db.selectFrom(HISTORY_VIEW)
                 .orderBy(HISTORY_VIEW.NETWORK_ROUTE_SYS_PERIOD.asc())
                 .fetchStream()
                 .map(record -> Route.from(record, jsonbConverter))
                 .collect(List.collector());
    }
}

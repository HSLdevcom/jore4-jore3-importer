package fi.hsl.jore.importer.feature.network.route_link.repository;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.route_link.dto.PersistableRouteLink;
import fi.hsl.jore.importer.feature.network.route_link.dto.RouteLink;
import fi.hsl.jore.importer.feature.network.route_link.dto.generated.RouteLinkPK;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteLinks;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteLinksWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRouteLinksRecord;
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
public class RouteLinkRepository implements IRouteLinkTestRepository {

    private static final NetworkRouteLinks LINKS = NetworkRouteLinks.NETWORK_ROUTE_LINKS;
    private static final NetworkRouteLinksWithHistory HISTORY_VIEW =
            NetworkRouteLinksWithHistory.NETWORK_ROUTE_LINKS_WITH_HISTORY;
    private static final TableField<NetworkRouteLinksRecord, UUID> PRIMARY_KEY = LINKS.NETWORK_ROUTE_LINK_ID;

    private final DSLContext db;

    @Autowired
    public RouteLinkRepository(@Qualifier("importerDsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public RouteLinkPK insert(final PersistableRouteLink link) {
        final NetworkRouteLinksRecord r = db.newRecord(LINKS);

        r.setNetworkRouteLinkExtId(link.externalId().value());
        r.setInfrastructureLinkId(link.link().value());
        r.setNetworkRouteDirectionId(link.routeDirection().value());
        r.setNetworkRouteLinkOrder(link.orderNumber());

        r.store();

        return RouteLinkPK.of(r.getNetworkRouteLinkId());
    }

    @Override
    @Transactional
    public List<RouteLinkPK> insert(final List<PersistableRouteLink> entities) {
        return entities.stream().map(this::insert).toList();
    }

    @Override
    @Transactional
    public List<RouteLinkPK> insert(final PersistableRouteLink... entities) {
        return insert(List.of(entities));
    }

    @Override
    @Transactional
    public RouteLinkPK update(final RouteLink link) {
        final NetworkRouteLinksRecord r = Optional.ofNullable(db.selectFrom(LINKS)
                        .where(PRIMARY_KEY.eq(link.pk().value()))
                        .fetchAny())
                .orElseThrow();

        r.setNetworkRouteLinkExtId(link.externalId().value());
        r.setInfrastructureLinkId(link.link().value());
        r.setNetworkRouteDirectionId(link.routeDirection().value());
        r.setNetworkRouteLinkOrder(link.orderNumber());

        r.store();

        return RouteLinkPK.of(r.getNetworkRouteLinkId());
    }

    @Override
    @Transactional
    public List<RouteLinkPK> update(final List<RouteLink> entities) {
        return entities.stream().map(this::update).toList();
    }

    @Override
    @Transactional
    public List<RouteLinkPK> update(final RouteLink... entities) {
        return update(List.of(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouteLink> findById(final RouteLinkPK id) {
        return db.selectFrom(LINKS)
                .where(PRIMARY_KEY.eq(id.value()))
                .fetchStream()
                .map(RouteLink::from)
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouteLink> findByExternalId(final ExternalId externalId) {
        return db.selectFrom(LINKS)
                .where(LINKS.NETWORK_ROUTE_LINK_EXT_ID.eq(externalId.value()))
                .fetchStream()
                .map(RouteLink::from)
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteLink> findAll() {
        return db.selectFrom(LINKS).fetchStream().map(RouteLink::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<RouteLinkPK> findAllIds() {
        return db.select(PRIMARY_KEY)
                .from(LINKS)
                .fetchStream()
                .map(row -> RouteLinkPK.of(row.value1()))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        //noinspection ConstantConditions
        return db.selectCount().from(LINKS).fetchOne(0, int.class);
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
    public List<RouteLink> findFromHistory() {
        return db.selectFrom(HISTORY_VIEW)
                .orderBy(HISTORY_VIEW.NETWORK_ROUTE_LINK_SYS_PERIOD.asc())
                .fetchStream()
                .map(RouteLink::from)
                .toList();
    }
}

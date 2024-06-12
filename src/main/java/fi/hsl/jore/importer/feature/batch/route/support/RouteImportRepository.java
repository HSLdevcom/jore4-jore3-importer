package fi.hsl.jore.importer.feature.batch.route.support;

import static org.jooq.impl.DSL.selectOne;

import fi.hsl.jore.importer.feature.batch.common.AbstractImportRepository;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.network.route.dto.Jore3Route;
import fi.hsl.jore.importer.feature.network.route.dto.generated.RoutePK;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLines;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutes;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutesStaging;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RouteImportRepository extends AbstractImportRepository<Jore3Route, RoutePK>
        implements IRouteImportRepository {

    private static final NetworkRoutesStaging STAGING_TABLE =
            NetworkRoutesStaging.NETWORK_ROUTES_STAGING;
    private static final NetworkRoutes TARGET_TABLE = NetworkRoutes.NETWORK_ROUTES;
    private static final NetworkLines LINES_TABLE = NetworkLines.NETWORK_LINES;

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    @Autowired
    public RouteImportRepository(
            @Qualifier("importerDsl") final DSLContext db, final IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter = jsonbConverter;
    }

    @Override
    @Transactional
    public void clearStagingTable() {
        db.truncate(STAGING_TABLE).execute();
    }

    @Override
    @Transactional
    public void submitToStaging(final Iterable<? extends Jore3Route> routes) {
        final BatchBindStep batch =
                db.batch(
                        db.insertInto(
                                        STAGING_TABLE,
                                        STAGING_TABLE.NETWORK_ROUTE_EXT_ID,
                                        STAGING_TABLE.NETWORK_LINE_EXT_ID,
                                        STAGING_TABLE.NETWORK_ROUTE_NUMBER,
                                        STAGING_TABLE.NETWORK_ROUTE_HIDDEN_VARIANT,
                                        STAGING_TABLE.NETWORK_ROUTE_NAME,
                                        STAGING_TABLE.NETWORK_ROUTE_LEGACY_HSL_MUNICIPALITY_CODE)
                                .values((String) null, null, null, null, null, null));

        routes.forEach(
                route ->
                        batch.bind(
                                route.externalId().value(),
                                route.lineId().value(),
                                route.routeNumber(),
                                route.hiddenVariant().orElse(null),
                                jsonbConverter.asJson(route.name()),
                                route.legacyHslMunicipalityCode().name()));

        if (batch.size() > 0) {
            batch.execute();
        }
    }

    protected Set<RoutePK> delete() {
        return db
                .deleteFrom(TARGET_TABLE)
                // Find rows which are missing from the latest dataset
                .whereNotExists(
                        selectOne()
                                .from(STAGING_TABLE)
                                .where(
                                        STAGING_TABLE.NETWORK_ROUTE_EXT_ID.eq(
                                                TARGET_TABLE.NETWORK_ROUTE_EXT_ID)))
                .returningResult(TARGET_TABLE.NETWORK_ROUTE_ID)
                .fetch()
                .stream()
                .map(row -> RoutePK.of(row.value1()))
                .collect(HashSet.collector());
    }

    protected Set<RoutePK> update() {
        return db
                .update(TARGET_TABLE)
                // Only the name of the route is updated because changing
                // the other field values would either change the line of
                // the route or change the identity of the route.
                // Also, the value of the route_number column is parsed
                // from the value of the network_route_ext_id column. Thus,
                // both of them must be updated at the same time.
                .set(TARGET_TABLE.NETWORK_ROUTE_NAME, STAGING_TABLE.NETWORK_ROUTE_NAME)
                .from(STAGING_TABLE)
                // Find source rows..
                .where(TARGET_TABLE.NETWORK_ROUTE_EXT_ID.eq(STAGING_TABLE.NETWORK_ROUTE_EXT_ID))
                // .. with updated fields
                .and(TARGET_TABLE.NETWORK_ROUTE_NAME.ne(STAGING_TABLE.NETWORK_ROUTE_NAME))
                .returningResult(TARGET_TABLE.NETWORK_ROUTE_ID)
                .fetch()
                .stream()
                .map(row -> RoutePK.of(row.value1()))
                .collect(HashSet.collector());
    }

    protected Set<RoutePK> insert() {
        return db
                .insertInto(TARGET_TABLE)
                .columns(
                        TARGET_TABLE.NETWORK_ROUTE_EXT_ID,
                        TARGET_TABLE.NETWORK_LINE_ID,
                        TARGET_TABLE.NETWORK_ROUTE_NUMBER,
                        TARGET_TABLE.NETWORK_ROUTE_HIDDEN_VARIANT,
                        TARGET_TABLE.NETWORK_ROUTE_NAME,
                        TARGET_TABLE.NETWORK_ROUTE_LEGACY_HSL_MUNICIPALITY_CODE)
                .select(
                        db.select(
                                        STAGING_TABLE.NETWORK_ROUTE_EXT_ID,
                                        LINES_TABLE.NETWORK_LINE_ID,
                                        STAGING_TABLE.NETWORK_ROUTE_NUMBER,
                                        STAGING_TABLE.NETWORK_ROUTE_HIDDEN_VARIANT,
                                        STAGING_TABLE.NETWORK_ROUTE_NAME,
                                        STAGING_TABLE.NETWORK_ROUTE_LEGACY_HSL_MUNICIPALITY_CODE)
                                .from(STAGING_TABLE)
                                .leftJoin(LINES_TABLE)
                                .on(
                                        LINES_TABLE.NETWORK_LINE_EXT_ID.eq(
                                                STAGING_TABLE.NETWORK_LINE_EXT_ID))
                                .whereNotExists(
                                        selectOne()
                                                .from(TARGET_TABLE)
                                                .where(
                                                        TARGET_TABLE.NETWORK_ROUTE_EXT_ID.eq(
                                                                STAGING_TABLE
                                                                        .NETWORK_ROUTE_EXT_ID))))
                .returningResult(TARGET_TABLE.NETWORK_ROUTE_ID)
                .fetch()
                .stream()
                .map(row -> RoutePK.of(row.value1()))
                .collect(HashSet.collector());
    }
}

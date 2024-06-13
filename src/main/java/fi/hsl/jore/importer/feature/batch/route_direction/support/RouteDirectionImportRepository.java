package fi.hsl.jore.importer.feature.batch.route_direction.support;

import static org.jooq.impl.DSL.selectOne;

import fi.hsl.jore.importer.feature.batch.common.AbstractImportRepository;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.network.route_direction.dto.Jore3RouteDirection;
import fi.hsl.jore.importer.feature.network.route_direction.dto.PersistableJourneyPatternIdMapping;
import fi.hsl.jore.importer.feature.network.route_direction.dto.PersistableRouteIdMapping;
import fi.hsl.jore.importer.feature.network.route_direction.dto.generated.RouteDirectionPK;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirections;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirectionsStaging;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutes;
import java.util.Set;
import java.util.stream.Collectors;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RouteDirectionImportRepository extends AbstractImportRepository<Jore3RouteDirection, RouteDirectionPK>
        implements IRouteDirectionImportRepository {

    private static final NetworkRouteDirectionsStaging STAGING_TABLE =
            NetworkRouteDirectionsStaging.NETWORK_ROUTE_DIRECTIONS_STAGING;
    private static final NetworkRouteDirections TARGET_TABLE = NetworkRouteDirections.NETWORK_ROUTE_DIRECTIONS;
    private static final NetworkRoutes ROUTES_TABLE = NetworkRoutes.NETWORK_ROUTES;

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    @Autowired
    public RouteDirectionImportRepository(
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
    public void submitToStaging(final Iterable<? extends Jore3RouteDirection> routeDirections) {
        final BatchBindStep batch = db.batch(db.insertInto(
                        STAGING_TABLE,
                        STAGING_TABLE.NETWORK_ROUTE_DIRECTION_EXT_ID,
                        STAGING_TABLE.NETWORK_ROUTE_EXT_ID,
                        STAGING_TABLE.NETWORK_ROUTE_DIRECTION_TYPE,
                        STAGING_TABLE.NETWORK_ROUTE_DIRECTION_LENGTH,
                        STAGING_TABLE.NETWORK_ROUTE_DIRECTION_NAME,
                        STAGING_TABLE.NETWORK_ROUTE_DIRECTION_NAME_SHORT,
                        STAGING_TABLE.NETWORK_ROUTE_DIRECTION_ORIGIN,
                        STAGING_TABLE.NETWORK_ROUTE_DIRECTION_DESTINATION,
                        STAGING_TABLE.NETWORK_ROUTE_DIRECTION_VALID_DATE_RANGE)
                .values((String) null, null, null, null, null, null, null, null, null));

        routeDirections.forEach(routeDirection -> batch.bind(
                routeDirection.externalId().value(),
                routeDirection.routeId().value(),
                routeDirection.direction().label(),
                routeDirection.lengthMeters().orElse(null),
                jsonbConverter.asJson(routeDirection.name()),
                jsonbConverter.asJson(routeDirection.nameShort()),
                jsonbConverter.asJson(routeDirection.origin()),
                jsonbConverter.asJson(routeDirection.destination()),
                routeDirection.validTime()));

        if (batch.size() > 0) {
            batch.execute();
        }
    }

    protected Set<RouteDirectionPK> delete() {
        return db
                .deleteFrom(TARGET_TABLE)
                // Find rows which are missing from the latest dataset
                .whereNotExists(selectOne()
                        .from(STAGING_TABLE)
                        .where(STAGING_TABLE.NETWORK_ROUTE_DIRECTION_EXT_ID.eq(
                                TARGET_TABLE.NETWORK_ROUTE_DIRECTION_EXT_ID)))
                .returningResult(TARGET_TABLE.NETWORK_ROUTE_DIRECTION_ID)
                .fetch()
                .stream()
                .map(row -> RouteDirectionPK.of(row.value1()))
                .collect(Collectors.toSet());
    }

    protected Set<RouteDirectionPK> update() {
        return db
                .update(TARGET_TABLE)
                // What fields to update
                .set(TARGET_TABLE.NETWORK_ROUTE_DIRECTION_LENGTH, STAGING_TABLE.NETWORK_ROUTE_DIRECTION_LENGTH)
                .set(TARGET_TABLE.NETWORK_ROUTE_DIRECTION_NAME, STAGING_TABLE.NETWORK_ROUTE_DIRECTION_NAME)
                .set(TARGET_TABLE.NETWORK_ROUTE_DIRECTION_NAME_SHORT, STAGING_TABLE.NETWORK_ROUTE_DIRECTION_NAME_SHORT)
                .set(TARGET_TABLE.NETWORK_ROUTE_DIRECTION_ORIGIN, STAGING_TABLE.NETWORK_ROUTE_DIRECTION_ORIGIN)
                .set(
                        TARGET_TABLE.NETWORK_ROUTE_DIRECTION_DESTINATION,
                        STAGING_TABLE.NETWORK_ROUTE_DIRECTION_DESTINATION)
                .set(
                        TARGET_TABLE.NETWORK_ROUTE_DIRECTION_VALID_DATE_RANGE,
                        STAGING_TABLE.NETWORK_ROUTE_DIRECTION_VALID_DATE_RANGE)
                .from(STAGING_TABLE)
                // Find source rows..
                .where(TARGET_TABLE.NETWORK_ROUTE_DIRECTION_EXT_ID.eq(STAGING_TABLE.NETWORK_ROUTE_DIRECTION_EXT_ID))
                // .. with updated fields
                .and((TARGET_TABLE.NETWORK_ROUTE_DIRECTION_LENGTH.ne(STAGING_TABLE.NETWORK_ROUTE_DIRECTION_LENGTH))
                        .or(TARGET_TABLE.NETWORK_ROUTE_DIRECTION_NAME.ne(STAGING_TABLE.NETWORK_ROUTE_DIRECTION_NAME))
                        .or(TARGET_TABLE.NETWORK_ROUTE_DIRECTION_NAME_SHORT.ne(
                                STAGING_TABLE.NETWORK_ROUTE_DIRECTION_NAME_SHORT))
                        .or(TARGET_TABLE.NETWORK_ROUTE_DIRECTION_ORIGIN.ne(
                                STAGING_TABLE.NETWORK_ROUTE_DIRECTION_ORIGIN))
                        .or(TARGET_TABLE.NETWORK_ROUTE_DIRECTION_DESTINATION.ne(
                                STAGING_TABLE.NETWORK_ROUTE_DIRECTION_DESTINATION))
                        .or(TARGET_TABLE.NETWORK_ROUTE_DIRECTION_VALID_DATE_RANGE.ne(
                                STAGING_TABLE.NETWORK_ROUTE_DIRECTION_VALID_DATE_RANGE)))
                .returningResult(TARGET_TABLE.NETWORK_ROUTE_DIRECTION_ID)
                .fetch()
                .stream()
                .map(row -> RouteDirectionPK.of(row.value1()))
                .collect(Collectors.toSet());
    }

    protected Set<RouteDirectionPK> insert() {
        return db
                .insertInto(TARGET_TABLE)
                .columns(
                        TARGET_TABLE.NETWORK_ROUTE_DIRECTION_EXT_ID,
                        TARGET_TABLE.NETWORK_ROUTE_ID,
                        TARGET_TABLE.NETWORK_ROUTE_DIRECTION_TYPE,
                        TARGET_TABLE.NETWORK_ROUTE_DIRECTION_LENGTH,
                        TARGET_TABLE.NETWORK_ROUTE_DIRECTION_NAME,
                        TARGET_TABLE.NETWORK_ROUTE_DIRECTION_NAME_SHORT,
                        TARGET_TABLE.NETWORK_ROUTE_DIRECTION_ORIGIN,
                        TARGET_TABLE.NETWORK_ROUTE_DIRECTION_DESTINATION,
                        TARGET_TABLE.NETWORK_ROUTE_DIRECTION_VALID_DATE_RANGE)
                .select(db.select(
                                STAGING_TABLE.NETWORK_ROUTE_DIRECTION_EXT_ID,
                                ROUTES_TABLE.NETWORK_ROUTE_ID,
                                STAGING_TABLE.NETWORK_ROUTE_DIRECTION_TYPE,
                                STAGING_TABLE.NETWORK_ROUTE_DIRECTION_LENGTH,
                                STAGING_TABLE.NETWORK_ROUTE_DIRECTION_NAME,
                                STAGING_TABLE.NETWORK_ROUTE_DIRECTION_NAME_SHORT,
                                STAGING_TABLE.NETWORK_ROUTE_DIRECTION_ORIGIN,
                                STAGING_TABLE.NETWORK_ROUTE_DIRECTION_DESTINATION,
                                STAGING_TABLE.NETWORK_ROUTE_DIRECTION_VALID_DATE_RANGE)
                        .from(STAGING_TABLE)
                        .leftJoin(ROUTES_TABLE)
                        .on(ROUTES_TABLE.NETWORK_ROUTE_EXT_ID.eq(STAGING_TABLE.NETWORK_ROUTE_EXT_ID))
                        .whereNotExists(selectOne()
                                .from(TARGET_TABLE)
                                .where(TARGET_TABLE.NETWORK_ROUTE_DIRECTION_EXT_ID.eq(
                                        STAGING_TABLE.NETWORK_ROUTE_DIRECTION_EXT_ID))))
                .returningResult(TARGET_TABLE.NETWORK_ROUTE_DIRECTION_ID)
                .fetch()
                .stream()
                .map(row -> RouteDirectionPK.of(row.value1()))
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public void setJourneyPatternJore4Ids(final Iterable<PersistableJourneyPatternIdMapping> idMappings) {
        db.batched(c -> {
            idMappings.forEach(idMapping -> {
                c.dsl()
                        .update(TARGET_TABLE)
                        .set(TARGET_TABLE.JOURNEY_PATTERN_JORE4_ID, idMapping.journeyPatternId())
                        .where(TARGET_TABLE.NETWORK_ROUTE_DIRECTION_ID.eq(idMapping.routeDirectionId()))
                        .execute();
            });
        });
    }

    @Transactional
    @Override
    public void setRouteJore4Ids(final Iterable<PersistableRouteIdMapping> idMappings) {
        db.batched(c -> {
            idMappings.forEach(idMapping -> {
                c.dsl()
                        .update(TARGET_TABLE)
                        .set(TARGET_TABLE.NETWORK_ROUTE_JORE4_ID, idMapping.jore4Id())
                        .where(TARGET_TABLE.NETWORK_ROUTE_DIRECTION_ID.eq(idMapping.routeDirectionId()))
                        .execute();
            });
        });
    }
}

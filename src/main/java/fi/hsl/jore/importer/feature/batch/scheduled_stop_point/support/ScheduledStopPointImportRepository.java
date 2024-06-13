package fi.hsl.jore.importer.feature.batch.scheduled_stop_point.support;

import static fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodes.INFRASTRUCTURE_NODES;
import static fi.hsl.jore.importer.jooq.network.tables.NetworkPlaces.NETWORK_PLACES;

import fi.hsl.jore.importer.feature.batch.common.AbstractImportRepository;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.Jore3ScheduledStopPoint;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.PersistableScheduledStopPointIdMapping;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.generated.ScheduledStopPointPK;
import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPoints;
import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPointsStaging;
import java.util.Set;
import java.util.stream.Collectors;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ScheduledStopPointImportRepository
        extends AbstractImportRepository<Jore3ScheduledStopPoint, ScheduledStopPointPK>
        implements IScheduledStopPointImportRepository {

    private static final ScheduledStopPointsStaging STAGING_TABLE =
            ScheduledStopPointsStaging.SCHEDULED_STOP_POINTS_STAGING;
    private static final ScheduledStopPoints TARGET_TABLE = ScheduledStopPoints.SCHEDULED_STOP_POINTS;

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    public ScheduledStopPointImportRepository(@Qualifier("importerDsl") DSLContext db, IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter = jsonbConverter;
    }

    @Transactional
    @Override
    public void clearStagingTable() {
        db.truncate(STAGING_TABLE).execute();
    }

    @Transactional
    @Override
    protected Set<ScheduledStopPointPK> delete() {
        return db
                .deleteFrom(TARGET_TABLE)
                .whereNotExists(db.selectOne()
                        .from(STAGING_TABLE)
                        .where(STAGING_TABLE.SCHEDULED_STOP_POINT_EXT_ID.eq(TARGET_TABLE.SCHEDULED_STOP_POINT_EXT_ID)))
                .returningResult(TARGET_TABLE.SCHEDULED_STOP_POINT_ID)
                .fetch()
                .stream()
                .map(row -> ScheduledStopPointPK.of(row.value1()))
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    protected Set<ScheduledStopPointPK> insert() {
        return db
                .insertInto(TARGET_TABLE)
                .columns(
                        TARGET_TABLE.SCHEDULED_STOP_POINT_EXT_ID,
                        TARGET_TABLE.INFRASTRUCTURE_NODE_ID,
                        TARGET_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER,
                        TARGET_TABLE.SCHEDULED_STOP_POINT_NAME,
                        TARGET_TABLE.SCHEDULED_STOP_POINT_SHORT_ID,
                        TARGET_TABLE.NETWORK_PLACE_ID,
                        TARGET_TABLE.USAGE_IN_ROUTES)
                .select(db.select(
                                STAGING_TABLE.SCHEDULED_STOP_POINT_EXT_ID,
                                INFRASTRUCTURE_NODES.INFRASTRUCTURE_NODE_ID,
                                STAGING_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER,
                                STAGING_TABLE.SCHEDULED_STOP_POINT_NAME,
                                STAGING_TABLE.SCHEDULED_STOP_POINT_SHORT_ID,
                                NETWORK_PLACES.NETWORK_PLACE_ID,
                                STAGING_TABLE.USAGE_IN_ROUTES)
                        .from(STAGING_TABLE)

                        // An infrastructure node and a scheduled stop point use the same external identifier
                        // which is called soltunnus in Jore3 database. You can verify this by taking a look
                        // at the jr_solmu and jr_pysakki tables found from the Jore3 database.
                        .join(INFRASTRUCTURE_NODES)
                        .on(INFRASTRUCTURE_NODES.INFRASTRUCTURE_NODE_EXT_ID.eq(
                                STAGING_TABLE.SCHEDULED_STOP_POINT_EXT_ID))

                        // Use left-join because all stop points are not associated with timing places.
                        .leftJoin(NETWORK_PLACES)
                        .on(NETWORK_PLACES.NETWORK_PLACE_EXT_ID.eq(STAGING_TABLE.NETWORK_PLACE_EXT_ID))
                        .whereNotExists(db.selectOne()
                                .from(TARGET_TABLE)
                                .where(TARGET_TABLE.SCHEDULED_STOP_POINT_EXT_ID.eq(
                                        STAGING_TABLE.SCHEDULED_STOP_POINT_EXT_ID))))
                .returningResult(TARGET_TABLE.SCHEDULED_STOP_POINT_ID)
                .fetch()
                .stream()
                .map(row -> ScheduledStopPointPK.of(row.value1()))
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    protected Set<ScheduledStopPointPK> update() {
        return db
                .update(TARGET_TABLE)
                .set(TARGET_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER, STAGING_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER)
                .set(TARGET_TABLE.SCHEDULED_STOP_POINT_NAME, STAGING_TABLE.SCHEDULED_STOP_POINT_NAME)
                .set(TARGET_TABLE.SCHEDULED_STOP_POINT_SHORT_ID, STAGING_TABLE.SCHEDULED_STOP_POINT_SHORT_ID)
                .set(TARGET_TABLE.NETWORK_PLACE_ID, NETWORK_PLACES.NETWORK_PLACE_ID)
                .set(TARGET_TABLE.USAGE_IN_ROUTES, STAGING_TABLE.USAGE_IN_ROUTES)
                .from(STAGING_TABLE
                        .leftJoin(NETWORK_PLACES)
                        .on(NETWORK_PLACES.NETWORK_PLACE_EXT_ID.eq(STAGING_TABLE.NETWORK_PLACE_EXT_ID)))
                .where(TARGET_TABLE
                        .SCHEDULED_STOP_POINT_EXT_ID
                        .eq(STAGING_TABLE.SCHEDULED_STOP_POINT_EXT_ID)
                        // A scheduled stop point is updated if:
                        // Its name was changed
                        .and(TARGET_TABLE
                                .SCHEDULED_STOP_POINT_NAME
                                .notEqual(STAGING_TABLE.SCHEDULED_STOP_POINT_NAME)

                                // ELY number was changed.
                                .or(TARGET_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER.isDistinctFrom(
                                        STAGING_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER))

                                // Short ID was changed.
                                .or(TARGET_TABLE.SCHEDULED_STOP_POINT_SHORT_ID.isDistinctFrom(
                                        STAGING_TABLE.SCHEDULED_STOP_POINT_SHORT_ID))

                                // Place ID was changed.
                                .or(TARGET_TABLE.NETWORK_PLACE_ID.isDistinctFrom(NETWORK_PLACES.NETWORK_PLACE_ID))

                                // Usage in routes was changed.
                                .or(TARGET_TABLE.USAGE_IN_ROUTES.notEqual(STAGING_TABLE.USAGE_IN_ROUTES))))
                .returningResult(TARGET_TABLE.SCHEDULED_STOP_POINT_ID)
                .fetch()
                .stream()
                .map(row -> ScheduledStopPointPK.of(row.value1()))
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public void submitToStaging(Iterable<? extends Jore3ScheduledStopPoint> items) {
        final BatchBindStep batch = db.batch(db.insertInto(
                        STAGING_TABLE,
                        STAGING_TABLE.SCHEDULED_STOP_POINT_EXT_ID,
                        STAGING_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER,
                        STAGING_TABLE.SCHEDULED_STOP_POINT_NAME,
                        STAGING_TABLE.SCHEDULED_STOP_POINT_SHORT_ID,
                        STAGING_TABLE.NETWORK_PLACE_EXT_ID,
                        STAGING_TABLE.USAGE_IN_ROUTES)
                .values(null, null, null, null, null, 0));

        items.forEach(item -> batch.bind(
                item.externalId().value(),
                item.elyNumber().orElse(null),
                jsonbConverter.asJson(item.name()),
                item.shortId().orElse(null),
                item.placeExternalId().map(ExternalId::value).orElse(null),
                item.usageInRoutes()));

        if (batch.size() > 0) {
            batch.execute();
        }
    }

    @Transactional
    @Override
    public void setJore4Ids(final Iterable<PersistableScheduledStopPointIdMapping> idMappings) {
        db.batched(c -> {
            idMappings.forEach(idMapping -> {
                c.dsl()
                        .update(TARGET_TABLE)
                        .set(TARGET_TABLE.SCHEDULED_STOP_POINT_JORE4_ID, idMapping.jore4Id())
                        .where(TARGET_TABLE.SCHEDULED_STOP_POINT_EXT_ID.eq(idMapping.externalId()))
                        .execute();
            });
        });
    }
}

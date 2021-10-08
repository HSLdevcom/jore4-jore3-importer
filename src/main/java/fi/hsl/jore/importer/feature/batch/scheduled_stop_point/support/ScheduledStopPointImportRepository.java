package fi.hsl.jore.importer.feature.batch.scheduled_stop_point.support;

import fi.hsl.jore.importer.feature.batch.common.AbstractImportRepository;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ImportableScheduledStopPoint;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.generated.ScheduledStopPointPK;
import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPoints;
import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPointsStaging;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodes.INFRASTRUCTURE_NODES;
import static fi.hsl.jore.importer.util.PostgisUtil.geometryEquals;

@Repository
public class ScheduledStopPointImportRepository
        extends AbstractImportRepository<ImportableScheduledStopPoint, ScheduledStopPointPK>
        implements IScheduledStopPointImportRepository {

    private static final ScheduledStopPointsStaging STAGING_TABLE = ScheduledStopPointsStaging.SCHEDULED_STOP_POINTS_STAGING;
    private static final ScheduledStopPoints TARGET_TABLE = ScheduledStopPoints.SCHEDULED_STOP_POINTS;

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    public ScheduledStopPointImportRepository(DSLContext db, IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter= jsonbConverter;
    }

    @Transactional
    @Override
    public void clearStagingTable() {
        db.truncate(STAGING_TABLE).execute();
    }

    @Transactional
    @Override
    protected Set<ScheduledStopPointPK> delete() {
        return db.deleteFrom(TARGET_TABLE)
                .whereNotExists(
                        db.selectOne()
                                .from(STAGING_TABLE)
                                .where(STAGING_TABLE.SCHEDULED_STOP_POINT_EXT_ID.eq(TARGET_TABLE.SCHEDULED_STOP_POINT_EXT_ID))
                )
                .returningResult(TARGET_TABLE.SCHEDULED_STOP_POINT_ID)
                .fetch()
                .stream()
                .map(row -> ScheduledStopPointPK.of(row.value1()))
                .collect(HashSet.collector());
    }

    @Transactional
    @Override
    protected Set<ScheduledStopPointPK> insert() {
        return db.insertInto(TARGET_TABLE)
                .columns(
                        TARGET_TABLE.SCHEDULED_STOP_POINT_EXT_ID,
                        TARGET_TABLE.INFRASTRUCTURE_NODE_ID,
                        TARGET_TABLE.SCHEDULED_STOP_POINT_NAME,
                        TARGET_TABLE.SCHEDULED_STOP_POINT_LOCATION
                )
                .select(
                        db.select(
                                STAGING_TABLE.SCHEDULED_STOP_POINT_EXT_ID,
                                INFRASTRUCTURE_NODES.INFRASTRUCTURE_NODE_ID,
                                STAGING_TABLE.SCHEDULED_STOP_POINT_NAME,
                                STAGING_TABLE.SCHEDULED_STOP_POINT_LOCATION
                        )
                                .from(STAGING_TABLE)
                                .join(INFRASTRUCTURE_NODES).on(INFRASTRUCTURE_NODES.INFRASTRUCTURE_NODE_EXT_ID.eq(STAGING_TABLE.SCHEDULED_STOP_POINT_EXT_ID))
                                .whereNotExists(
                                        db.selectOne()
                                                .from(TARGET_TABLE)
                                                .where(TARGET_TABLE.SCHEDULED_STOP_POINT_EXT_ID.eq(STAGING_TABLE.SCHEDULED_STOP_POINT_EXT_ID))
                                )
                )
                .returningResult(TARGET_TABLE.SCHEDULED_STOP_POINT_ID)
                .fetch()
                .stream()
                .map(row -> ScheduledStopPointPK.of(row.value1()))
                .collect(HashSet.collector());
    }

    @Transactional
    @Override
    protected Set<ScheduledStopPointPK> update() {
        return db.update(TARGET_TABLE)
                .set(TARGET_TABLE.SCHEDULED_STOP_POINT_LOCATION, STAGING_TABLE.SCHEDULED_STOP_POINT_LOCATION)
                .set(TARGET_TABLE.SCHEDULED_STOP_POINT_NAME, STAGING_TABLE.SCHEDULED_STOP_POINT_NAME)
                .from(STAGING_TABLE)
                .where(TARGET_TABLE.SCHEDULED_STOP_POINT_EXT_ID.eq(STAGING_TABLE.SCHEDULED_STOP_POINT_EXT_ID)
                        .and(
                                geometryEquals(TARGET_TABLE.SCHEDULED_STOP_POINT_LOCATION, STAGING_TABLE.SCHEDULED_STOP_POINT_LOCATION).not()
                                .or(TARGET_TABLE.SCHEDULED_STOP_POINT_NAME.notEqual(STAGING_TABLE.SCHEDULED_STOP_POINT_NAME))
                        )
                )
                .returningResult(TARGET_TABLE.SCHEDULED_STOP_POINT_ID)
                .fetch()
                .stream()
                .map(row -> ScheduledStopPointPK.of(row.value1()))
                .collect(HashSet.collector());
    }

    @Transactional
    @Override
    public void submitToStaging(Iterable<? extends ImportableScheduledStopPoint> items) {
        final BatchBindStep batch = db.batch(
                db.insertInto(STAGING_TABLE,
                        STAGING_TABLE.SCHEDULED_STOP_POINT_EXT_ID,
                        STAGING_TABLE.SCHEDULED_STOP_POINT_NAME,
                        STAGING_TABLE.SCHEDULED_STOP_POINT_LOCATION
                )
                        .values((String) null, null, null)
        );

        items.forEach(item -> batch.bind(
                item.externalId().value(),
                jsonbConverter.asJson(item.name()),
                item.location()
        ));

        batch.execute();
    }
}

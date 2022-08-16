package fi.hsl.jore.importer.feature.batch.scheduled_stop_point.support;

import fi.hsl.jore.importer.feature.batch.common.AbstractImportRepository;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ImportableScheduledStopPoint;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.PersistableScheduledStopPointIdMapping;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.generated.ScheduledStopPointPK;
import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPoints;
import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPointsStaging;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodes.INFRASTRUCTURE_NODES;

@Repository
public class ScheduledStopPointImportRepository
        extends AbstractImportRepository<ImportableScheduledStopPoint, ScheduledStopPointPK>
        implements IScheduledStopPointImportRepository {

    private static final ScheduledStopPointsStaging STAGING_TABLE = ScheduledStopPointsStaging.SCHEDULED_STOP_POINTS_STAGING;
    private static final ScheduledStopPoints TARGET_TABLE = ScheduledStopPoints.SCHEDULED_STOP_POINTS;

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    public ScheduledStopPointImportRepository(@Qualifier("importerDsl") DSLContext db, IJsonbConverter jsonbConverter) {
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
                        TARGET_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER,
                        TARGET_TABLE.SCHEDULED_STOP_POINT_NAME,
                        TARGET_TABLE.SCHEDULED_STOP_POINT_SHORT_ID,
                        TARGET_TABLE.USAGE_IN_ROUTES
                )
                .select(
                        db.select(
                                STAGING_TABLE.SCHEDULED_STOP_POINT_EXT_ID,
                                INFRASTRUCTURE_NODES.INFRASTRUCTURE_NODE_ID,
                                STAGING_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER,
                                STAGING_TABLE.SCHEDULED_STOP_POINT_NAME,
                                STAGING_TABLE.SCHEDULED_STOP_POINT_SHORT_ID,
                                STAGING_TABLE.USAGE_IN_ROUTES
                        )
                                .from(STAGING_TABLE)
                                //An infrastructure node and a scheduled stop point use the same external identifier
                                //which is called soltunnus in Jore3 database. You can verify this by taking a look
                                //at the jr_solmu and jr_pysakki tables found from the Jore3 database.
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
                .set(TARGET_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER, STAGING_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER)
                .set(TARGET_TABLE.SCHEDULED_STOP_POINT_NAME, STAGING_TABLE.SCHEDULED_STOP_POINT_NAME)
                .set(TARGET_TABLE.SCHEDULED_STOP_POINT_SHORT_ID, STAGING_TABLE.SCHEDULED_STOP_POINT_SHORT_ID)
                .set(TARGET_TABLE.USAGE_IN_ROUTES, STAGING_TABLE.USAGE_IN_ROUTES)
                .from(STAGING_TABLE)
                .where(
                        TARGET_TABLE.SCHEDULED_STOP_POINT_EXT_ID.eq(STAGING_TABLE.SCHEDULED_STOP_POINT_EXT_ID)
                                //A scheduled stop point is updated if:
                                //Its name was changed
                                .and(TARGET_TABLE.SCHEDULED_STOP_POINT_NAME.notEqual(STAGING_TABLE.SCHEDULED_STOP_POINT_NAME)
                                        //Ely number is null in the target table but not in the staging table
                                        .or(TARGET_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER.isNull()
                                                .and(STAGING_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER.isNotNull())
                                        )
                                        //Ely number isn't null in the target table but is null in the staging table
                                        .or(TARGET_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER.isNotNull()
                                                .and(STAGING_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER.isNull())
                                        )
                                        //Ely number was changed
                                        .or(TARGET_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER
                                                .notEqual(STAGING_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER)
                                        )
                                        //Short id is null in the target table but not null in the staging table
                                        .or(TARGET_TABLE.SCHEDULED_STOP_POINT_SHORT_ID.isNull()
                                                .and(STAGING_TABLE.SCHEDULED_STOP_POINT_SHORT_ID.isNotNull())
                                        )
                                        //Short id isn't null in the target table but is null in the staging table
                                        .or(TARGET_TABLE.SCHEDULED_STOP_POINT_SHORT_ID.isNotNull()
                                                .and(STAGING_TABLE.SCHEDULED_STOP_POINT_SHORT_ID.isNull())
                                        )
                                        //Short id was changed
                                        .or(TARGET_TABLE.SCHEDULED_STOP_POINT_SHORT_ID
                                                .notEqual(STAGING_TABLE.SCHEDULED_STOP_POINT_SHORT_ID))
                                        //Usage in routes was changed
                                        .or(TARGET_TABLE.USAGE_IN_ROUTES
                                                .notEqual(STAGING_TABLE.USAGE_IN_ROUTES))
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
                        STAGING_TABLE.SCHEDULED_STOP_POINT_ELY_NUMBER,
                        STAGING_TABLE.SCHEDULED_STOP_POINT_NAME,
                        STAGING_TABLE.SCHEDULED_STOP_POINT_SHORT_ID,
                        STAGING_TABLE.USAGE_IN_ROUTES
                )
                        .values((String) null, null, null, null, 0)
        );

        items.forEach(item -> batch.bind(
                item.externalId().value(),
                item.elyNumber().orElse(null),
                jsonbConverter.asJson(item.name()),
                item.shortId().orElse(null),
                item.usageInRoutes()
        ));

        batch.execute();
    }

    @Transactional
    @Override
    public void setTransmodelIds(final List<PersistableScheduledStopPointIdMapping> idMappings) {
        db.batched(c -> {
            idMappings.forEach(idMapping -> {
                c.dsl().update(TARGET_TABLE)
                        .set(TARGET_TABLE.SCHEDULED_STOP_POINT_TRANSMODEL_ID, idMapping.transmodelId())
                        .where(TARGET_TABLE.SCHEDULED_STOP_POINT_EXT_ID.eq(idMapping.externalId()))
                        .execute();
            });
        });
    }
}

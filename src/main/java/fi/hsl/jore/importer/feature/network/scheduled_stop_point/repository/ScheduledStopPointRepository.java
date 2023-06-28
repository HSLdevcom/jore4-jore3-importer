package fi.hsl.jore.importer.feature.network.scheduled_stop_point.repository;

import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.PersistableScheduledStopPoint;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ScheduledStopPoint;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.generated.ScheduledStopPointPK;
import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPoints;
import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPointsWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.records.ScheduledStopPointsRecord;
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
public class ScheduledStopPointRepository implements IScheduledStopPointTestRepository {

    private static final ScheduledStopPoints SCHEDULED_STOP_POINT = ScheduledStopPoints.SCHEDULED_STOP_POINTS;
    private static final ScheduledStopPointsWithHistory HISTORY_VIEW = ScheduledStopPointsWithHistory.SCHEDULED_STOP_POINTS_WITH_HISTORY;
    private static final TableField<ScheduledStopPointsRecord, UUID> PRIMARY_KEY = SCHEDULED_STOP_POINT.SCHEDULED_STOP_POINT_ID;

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    @Autowired
    ScheduledStopPointRepository(@Qualifier("importerDsl") DSLContext db, IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter = jsonbConverter;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ScheduledStopPoint> findAll() {
        return db.selectFrom(SCHEDULED_STOP_POINT)
                .fetchStream()
                .map(record -> ScheduledStopPoint.from(record, jsonbConverter))
                .collect(List.collector());
    }

    @Transactional(readOnly = true)
    @Override
    public Set<ScheduledStopPointPK> findAllIds() {
        return db.select(PRIMARY_KEY)
                .from(SCHEDULED_STOP_POINT)
                .fetchStream()
                .map(record -> ScheduledStopPointPK.of(record.value1()))
                .collect(HashSet.collector());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ScheduledStopPoint> findById(final ScheduledStopPointPK id) {
        return db.selectFrom(SCHEDULED_STOP_POINT)
                .where(PRIMARY_KEY.eq(id.value()))
                .fetchStream()
                .map(record -> ScheduledStopPoint.from(record, jsonbConverter))
                .findFirst();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ScheduledStopPoint> findByExternalId(final ExternalId id) {
        return db.selectFrom(SCHEDULED_STOP_POINT)
                .where(SCHEDULED_STOP_POINT.SCHEDULED_STOP_POINT_EXT_ID.eq(id.value()))
                .fetchStream()
                .map(record -> ScheduledStopPoint.from(record, jsonbConverter))
                .findFirst();
    }

    @Transactional(readOnly = true)
    @Override
    public int count() {
        //noinspection ConstantConditions
        return db.selectCount()
                .from(SCHEDULED_STOP_POINT)
                .fetchOne(0, int.class);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean empty() {
        return count() == 0;
    }

    @Transactional
    @Override
    public ScheduledStopPointPK insert(final PersistableScheduledStopPoint entity) {
        final ScheduledStopPointsRecord record = db.newRecord(SCHEDULED_STOP_POINT);

        record.setScheduledStopPointExtId(entity.externalId().value());
        record.setScheduledStopPointElyNumber(entity.elyNumber().orElse(null));
        record.setScheduledStopPointName(jsonbConverter.asJson(entity.name()));
        record.setScheduledStopPointShortId(entity.shortId().orElse(null));
        record.setHastusPlaceId(entity.placeExternalId().orElse(null));

        record.store();

        return ScheduledStopPointPK.of(record.getScheduledStopPointId());
    }

    @Transactional
    @Override
    public List<ScheduledStopPointPK> insert(final List<PersistableScheduledStopPoint> entities) {
        return entities.map(this::insert);
    }

    @Transactional
    @Override
    public List<ScheduledStopPointPK> insert(final PersistableScheduledStopPoint... entities) {
        return insert(List.of(entities));
    }

    @Transactional
    @Override
    public ScheduledStopPointPK update(final ScheduledStopPoint scheduledStopPoint) {
        final ScheduledStopPointsRecord record = Optional.ofNullable(
                db.selectFrom(SCHEDULED_STOP_POINT)
                        .where(PRIMARY_KEY.eq(scheduledStopPoint.pk().value()))
                        .fetchAny()
        ).orElseThrow();

        record.setScheduledStopPointElyNumber(scheduledStopPoint.elyNumber().orElse(null));
        record.setScheduledStopPointName(jsonbConverter.asJson(scheduledStopPoint.name()));
        record.setScheduledStopPointShortId(scheduledStopPoint.shortId().orElse(null));
        record.setHastusPlaceId(scheduledStopPoint.placeExternalId().orElse(null));

        record.store();

        return ScheduledStopPointPK.of(record.getScheduledStopPointId());
    }

    @Transactional
    @Override
    public List<ScheduledStopPointPK> update(final List<ScheduledStopPoint> scheduledStopPoints) {
        return scheduledStopPoints.map(this::update);
    }

    @Transactional
    @Override
    public List<ScheduledStopPointPK> update(final ScheduledStopPoint... scheduledStopPoints) {
        return update(List.of(scheduledStopPoints));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ScheduledStopPoint> findFromHistory() {
        return db.selectFrom(HISTORY_VIEW)
                .orderBy(HISTORY_VIEW.SCHEDULED_STOP_POINT_SYS_PERIOD.asc())
                .fetchStream()
                .map(record -> ScheduledStopPoint.from(record, jsonbConverter))
                .collect(List.collector());
    }

    @Transactional(readOnly = true)
    @Override
    public int countHistory() {
        //noinspection ConstantConditions
        return db.selectCount()
                .from(HISTORY_VIEW)
                .fetchOne(0, int.class);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean emptyHistory() {
        return countHistory() == 0;
    }
}

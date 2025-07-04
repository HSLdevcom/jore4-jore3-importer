package fi.hsl.jore.importer.feature.batch.stop_place.support;

import fi.hsl.jore.importer.feature.batch.common.AbstractImportRepository;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.stops.stop_place.dto.Jore3StopPlace;
import fi.hsl.jore.importer.feature.stops.stop_place.dto.generated.StopPlacePK;
import fi.hsl.jore.importer.jooq.stops.tables.StopPlaces;
import fi.hsl.jore.importer.jooq.stops.tables.StopPlacesStaging;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class StopPlaceImportRepository
    extends AbstractImportRepository<Jore3StopPlace, StopPlacePK>
    implements IStopPlaceImportRepository {

    private static final StopPlacesStaging STAGING_TABLE =
            StopPlacesStaging.STOP_PLACES_STAGING;
    private static final StopPlaces TARGET_TABLE = StopPlaces.STOP_PLACES;

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    public StopPlaceImportRepository(@Qualifier("importerDsl") DSLContext db, IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter = jsonbConverter;
    }

    @Override
    protected Set<StopPlacePK> insert() {
        return db
                .insertInto(TARGET_TABLE)
                .columns(
                        TARGET_TABLE.STOPS_STOP_PLACE_EXT_ID,
                        TARGET_TABLE.STOPS_STOP_PLACE_NAME,
                        TARGET_TABLE.STOPS_STOP_PLACE_LONG_NAME,
                        TARGET_TABLE.STOPS_STOP_PLACE_LOCATION)
                .select(
                        db.select(
                            STAGING_TABLE.STOPS_STOP_PLACE_EXT_ID,
                            STAGING_TABLE.STOPS_STOP_PLACE_NAME,
                            STAGING_TABLE.STOPS_STOP_PLACE_LONG_NAME,
                            STAGING_TABLE.STOPS_STOP_PLACE_LOCATION)
                        .from(STAGING_TABLE)
                        .whereNotExists(
                                db.selectOne()
                                        .from(TARGET_TABLE)
                                        .where(TARGET_TABLE.STOPS_STOP_PLACE_EXT_ID.eq(STAGING_TABLE.STOPS_STOP_PLACE_EXT_ID))
                        ))
                .returningResult(TARGET_TABLE.STOPS_STOP_PLACE_ID)
                .fetch()
                .stream()
                .map(row -> StopPlacePK.of(row.value1()))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    protected Set<StopPlacePK> update() {
        return db
                .update(TARGET_TABLE)
                .set(TARGET_TABLE.STOPS_STOP_PLACE_NAME, STAGING_TABLE.STOPS_STOP_PLACE_NAME)
                .set(TARGET_TABLE.STOPS_STOP_PLACE_LONG_NAME, STAGING_TABLE.STOPS_STOP_PLACE_LONG_NAME)
                .set(TARGET_TABLE.STOPS_STOP_PLACE_LOCATION, STAGING_TABLE.STOPS_STOP_PLACE_LOCATION)
                .from(STAGING_TABLE)
                .where(
                        TARGET_TABLE.STOPS_STOP_PLACE_EXT_ID
                                .eq(STAGING_TABLE.STOPS_STOP_PLACE_EXT_ID)
                                .and(
                                        TARGET_TABLE.STOPS_STOP_PLACE_NAME.notEqual(STAGING_TABLE.STOPS_STOP_PLACE_NAME)
                                    .or(TARGET_TABLE.STOPS_STOP_PLACE_LONG_NAME.notEqual(STAGING_TABLE.STOPS_STOP_PLACE_LONG_NAME))
                                    .or(TARGET_TABLE.STOPS_STOP_PLACE_LOCATION.notEqual(STAGING_TABLE.STOPS_STOP_PLACE_LOCATION))
                                )
                        )
                .returningResult(TARGET_TABLE.STOPS_STOP_PLACE_ID)
                .fetch()
                .stream()
                .map(row -> StopPlacePK.of(row.value1()))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    protected Set<StopPlacePK> delete() {
        return db
                .deleteFrom(TARGET_TABLE)
                .whereNotExists(db.selectOne()
                        .from(STAGING_TABLE)
                        .where(STAGING_TABLE.STOPS_STOP_PLACE_EXT_ID.eq(TARGET_TABLE.STOPS_STOP_PLACE_EXT_ID)))
                .returningResult(TARGET_TABLE.STOPS_STOP_PLACE_ID)
                .fetch()
                .stream()
                .map(row -> StopPlacePK.of(row.value1()))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Transactional
    @Override
    public void setJore4Ids() {
        // TODO: Add ids when implemented
    }

    @Transactional
    @Override
    public void submitToStaging(Iterable<? extends Jore3StopPlace> items) {
        final BatchBindStep batch = db.batch(db.insertInto(
                STAGING_TABLE,
                STAGING_TABLE.STOPS_STOP_PLACE_EXT_ID,
                STAGING_TABLE.STOPS_STOP_PLACE_NAME,
                STAGING_TABLE.STOPS_STOP_PLACE_LONG_NAME,
                STAGING_TABLE.STOPS_STOP_PLACE_LOCATION)
                .values((String) null, null, null, null));

        items.forEach(item -> batch.bind(
                item.externalId().value(),
                jsonbConverter.asJson(item.name()),
                jsonbConverter.asJson(item.longName()),
                jsonbConverter.asJson(item.location())
        ));

        if (batch.size() > 0) {
            batch.execute();
        }
    }

    @Transactional
    @Override
    public void clearStagingTable() {
        db.truncate(STAGING_TABLE).execute();
    }
}

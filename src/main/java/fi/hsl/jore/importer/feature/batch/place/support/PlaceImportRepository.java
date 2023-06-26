package fi.hsl.jore.importer.feature.batch.place.support;

import fi.hsl.jore.importer.feature.batch.common.AbstractImportRepository;
import fi.hsl.jore.importer.feature.network.place.dto.PersistablePlace;
import fi.hsl.jore.importer.feature.network.place.dto.generated.PlacePK;
import fi.hsl.jore.importer.jooq.network.tables.NetworkPlaces;
import fi.hsl.jore.importer.jooq.network.tables.NetworkPlacesStaging;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.jooq.impl.DSL.selectOne;

@Repository
public class PlaceImportRepository
        extends AbstractImportRepository<PersistablePlace, PlacePK>
        implements IPlaceImportRepository {

    private static final NetworkPlacesStaging STAGING_TABLE = NetworkPlacesStaging.NETWORK_PLACES_STAGING;
    private static final NetworkPlaces TARGET_TABLE = NetworkPlaces.NETWORK_PLACES;

    private final DSLContext db;

    @Autowired
    public PlaceImportRepository(@Qualifier("importerDsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public void clearStagingTable() {
        db.truncate(STAGING_TABLE).execute();
    }

    @Override
    @Transactional
    public void submitToStaging(final Iterable<? extends PersistablePlace> places) {
        final BatchBindStep batch = db.batch(db.insertInto(
                        STAGING_TABLE,
                        STAGING_TABLE.NETWORK_PLACE_EXT_ID,
                        STAGING_TABLE.NETWORK_PLACE_NAME)
                .values((String) null, null));

        places.forEach(place -> batch.bind(place.externalId().value(),
                place.name()
        ));

        batch.execute();
    }

    protected Set<PlacePK> insert() {
        return db.insertInto(TARGET_TABLE)
                .columns(TARGET_TABLE.NETWORK_PLACE_EXT_ID,
                        TARGET_TABLE.NETWORK_PLACE_NAME)
                .select(db.select(STAGING_TABLE.NETWORK_PLACE_EXT_ID,
                                STAGING_TABLE.NETWORK_PLACE_NAME)
                        .from(STAGING_TABLE)
                        .whereNotExists(selectOne()
                                .from(TARGET_TABLE)
                                .where(TARGET_TABLE.NETWORK_PLACE_EXT_ID.eq(STAGING_TABLE.NETWORK_PLACE_EXT_ID))))
                .returningResult(TARGET_TABLE.NETWORK_PLACE_ID)
                .fetch()
                .stream()
                .map(row -> PlacePK.of(row.value1()))
                .collect(HashSet.collector());
    }

    protected Set<PlacePK> update() {
        return db.update(TARGET_TABLE)
                .set(TARGET_TABLE.NETWORK_PLACE_NAME, STAGING_TABLE.NETWORK_PLACE_NAME)
                .from(STAGING_TABLE)
                // Find source rows..
                .where(TARGET_TABLE.NETWORK_PLACE_EXT_ID
                        .eq(STAGING_TABLE.NETWORK_PLACE_EXT_ID))
                // .. with updated fields
                .and(TARGET_TABLE.NETWORK_PLACE_NAME.ne(STAGING_TABLE.NETWORK_PLACE_NAME))
                .returningResult(TARGET_TABLE.NETWORK_PLACE_ID)
                .fetch()
                .stream()
                .map(row -> PlacePK.of(row.value1()))
                .collect(HashSet.collector());
    }

    protected Set<PlacePK> delete() {
        return db.deleteFrom(TARGET_TABLE)
                // Find rows which are missing from the latest dataset
                .whereNotExists(selectOne()
                        .from(STAGING_TABLE)
                        .where(STAGING_TABLE.NETWORK_PLACE_EXT_ID.eq(TARGET_TABLE.NETWORK_PLACE_EXT_ID)))
                .returningResult(TARGET_TABLE.NETWORK_PLACE_ID)
                .fetch()
                .stream()
                .map(row -> PlacePK.of(row.value1()))
                .collect(HashSet.collector());
    }
}

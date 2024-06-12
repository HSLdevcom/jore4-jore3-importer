package fi.hsl.jore.importer.feature.jore4.repository;

import static fi.hsl.jore.jore4.jooq.timing_pattern.Tables.TIMING_PLACE;

import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4TimingPlace;
import java.util.UUID;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class Jore4TimingPlaceRepository implements IJore4TimingPlaceRepository {

    private final DSLContext db;

    private final IJsonbConverter jsonbConverter;

    @Autowired
    public Jore4TimingPlaceRepository(
            @Qualifier("jore4Dsl") final DSLContext db, final IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter = jsonbConverter;
    }

    @Transactional
    @Override
    public void insert(final Iterable<? extends Jore4TimingPlace> timingPlaces) {
        BatchBindStep batch = db.batch(
                db.insertInto(TIMING_PLACE, TIMING_PLACE.TIMING_PLACE_ID, TIMING_PLACE.LABEL, TIMING_PLACE.DESCRIPTION)
                        .values((UUID) null, null, null));

        for (final Jore4TimingPlace timingPlace : timingPlaces) {
            batch = batch.bind(
                    timingPlace.timingPlaceId(),
                    timingPlace.timingPlaceLabel(),
                    jsonbConverter.asJson(timingPlace.timingPlaceName()));
        }

        if (batch.size() > 0) {
            batch.execute();
        }
    }
}

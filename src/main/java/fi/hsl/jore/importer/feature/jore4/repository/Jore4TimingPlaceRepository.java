package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4TimingPlace;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static fi.hsl.jore.jore4.jooq.timing_pattern.Tables.TIMING_PLACE;

@Repository
public class Jore4TimingPlaceRepository implements IJore4TimingPlaceRepository {

    private final DSLContext db;

    @Autowired
    public Jore4TimingPlaceRepository(@Qualifier("jore4Dsl") final DSLContext db) {
        this.db = db;
    }

    @Transactional
    @Override
    public void insert(final List<? extends Jore4TimingPlace> timingPlaces) {
        if (!timingPlaces.isEmpty()) {
            final BatchBindStep batch = db.batch(db.insertInto(
                                                           TIMING_PLACE,
                                                           TIMING_PLACE.TIMING_PLACE_ID,
                                                           TIMING_PLACE.LABEL
                                                   )
                                                   .values((UUID) null, null));

            timingPlaces.forEach(timingPlace -> batch.bind(
                    timingPlace.timingPlaceId(),
                    timingPlace.timingPlaceLabel()
            ));

            batch.execute();
        }
    }
}

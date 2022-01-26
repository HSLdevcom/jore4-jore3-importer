package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelJourneyPatternStop;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static fi.hsl.jore.jore4.jooq.journey_pattern.Tables.SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN;

@Repository
public class TransmodelJourneyPatternStopRepository implements ITransmodelJourneyPatternStopRepository {

    private final DSLContext db;

    @Autowired
    public TransmodelJourneyPatternStopRepository(@Qualifier("jore4Dsl") final DSLContext db) {
        this.db = db;
    }

    @Transactional
    @Override
    public void insert(final List<? extends TransmodelJourneyPatternStop> journeyPatternStops) {
        if (!journeyPatternStops.isEmpty()) {
            final BatchBindStep batch = db.batch(db.insertInto(
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN,
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.JOURNEY_PATTERN_ID,
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_ID,
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_SEQUENCE,
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_TIMING_POINT,
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_VIA_POINT
                ).values((UUID) null, null, null, null, null)
            );

            journeyPatternStops.forEach(journeyPatternStop -> batch.bind(
                    journeyPatternStop.journeyPatternId(),
                    journeyPatternStop.scheduledStopPointId(),
                    journeyPatternStop.scheduledStopPointSequence(),
                    journeyPatternStop.isTimingPoint(),
                    journeyPatternStop.isViaPoint()
            ));

            batch.execute();
        }
    }
}

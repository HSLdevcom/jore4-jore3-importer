package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4JourneyPatternStop;
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
public class Jore4JourneyPatternStopRepository implements IJore4JourneyPatternStopRepository {

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    @Autowired
    public Jore4JourneyPatternStopRepository(@Qualifier("jore4Dsl") final DSLContext db,
                                             final IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter = jsonbConverter;
    }

    @Transactional
    @Override
    public void insert(final List<? extends Jore4JourneyPatternStop> journeyPatternStops) {
        if (!journeyPatternStops.isEmpty()) {
            final BatchBindStep batch = db.batch(db.insertInto(
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN,
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.JOURNEY_PATTERN_ID,
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_LABEL,
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_SEQUENCE,
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_USED_AS_TIMING_POINT,
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_VIA_POINT,
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.VIA_POINT_NAME_I18N,
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.VIA_POINT_SHORT_NAME_I18N
                ).values((UUID) null, null, null, null, null, null, null)
            );

            journeyPatternStops.forEach(journeyPatternStop -> batch.bind(
                    journeyPatternStop.journeyPatternId(),
                    journeyPatternStop.scheduledStopPointLabel(),
                    journeyPatternStop.scheduledStopPointSequence(),
                    journeyPatternStop.isTimingPoint(),
                    journeyPatternStop.isViaPoint(),
                    // Use the existing via name for both normal and short names
                    journeyPatternStop.viaPointNames().map(jsonbConverter::asJson).orElse(null),
                    journeyPatternStop.viaPointNames().map(jsonbConverter::asJson).orElse(null)
            ));

            batch.execute();
        }
    }
}

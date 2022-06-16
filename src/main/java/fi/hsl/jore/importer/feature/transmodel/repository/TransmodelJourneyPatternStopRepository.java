package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelJourneyPatternStop;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static fi.hsl.jore.jore4.jooq.journey_pattern.Tables.SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN;

@Repository
public class TransmodelJourneyPatternStopRepository implements ITransmodelJourneyPatternStopRepository {

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    @Autowired
    public TransmodelJourneyPatternStopRepository(@Qualifier("jore4Dsl") final DSLContext db,
                                                  final IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter = jsonbConverter;
    }

    @Transactional
    @Override
    public void insert(final List<? extends TransmodelJourneyPatternStop> journeyPatternStops) {
        if (!journeyPatternStops.isEmpty()) {
            final BatchBindStep batch = db.batch(db.insertInto(
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN,
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.JOURNEY_PATTERN_ID,
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_LABEL,
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_SEQUENCE,
                    SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_TIMING_POINT,
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
                    // TODO: fetch via point name and short name from Jore3
                    journeyPatternStop.isViaPoint() ? jsonbConverter.asJson(MultilingualString.of(Map.of("fi-FI", "Via Helsinki", "sv-FI", "Via Helsingfors"))) : null,
                    journeyPatternStop.isViaPoint() ? jsonbConverter.asJson(MultilingualString.of(Map.of("fi-FI", "Helsinki", "sv-FI", "Helsingfors"))) : null
            ));

            batch.execute();
        }
    }
}

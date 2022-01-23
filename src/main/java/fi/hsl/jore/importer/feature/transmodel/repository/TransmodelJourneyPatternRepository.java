package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelJourneyPattern;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static fi.hsl.jore.jore4.jooq.journey_pattern.Tables.JOURNEY_PATTERN_;

@Repository
public class TransmodelJourneyPatternRepository implements ITransmodelJourneyPatternRepository {

    private final DSLContext db;

    @Autowired
    public TransmodelJourneyPatternRepository(@Qualifier("jore4Dsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    public void insert(final List<? extends TransmodelJourneyPattern> journeyPatterns) {
        if (!journeyPatterns.isEmpty()) {
            final BatchBindStep batch = db.batch(db.insertInto(JOURNEY_PATTERN_,
                            JOURNEY_PATTERN_.JOURNEY_PATTERN_ID,
                            JOURNEY_PATTERN_.ON_ROUTE_ID
            ).values((UUID) null, null));

            journeyPatterns.forEach(journeyPattern -> batch.bind(
                    journeyPattern.journeyPatternId(),
                    journeyPattern.routeId()
            ));

            batch.execute();
        }
    }
}

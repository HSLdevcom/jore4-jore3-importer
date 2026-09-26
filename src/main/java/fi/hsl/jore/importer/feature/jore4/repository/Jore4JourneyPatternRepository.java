package fi.hsl.jore.importer.feature.jore4.repository;

import static fi.hsl.jore.jore4.jooq.network.Tables.JOURNEY_PATTERN;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4JourneyPattern;
import java.util.UUID;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class Jore4JourneyPatternRepository implements IJore4JourneyPatternRepository {

    private final DSLContext db;

    @Autowired
    public Jore4JourneyPatternRepository(@Qualifier("jore4Dsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    public void insert(final Iterable<? extends Jore4JourneyPattern> journeyPatterns) {
        BatchBindStep batch =
                db.batch(db.insertInto(JOURNEY_PATTERN, JOURNEY_PATTERN.JOURNEY_PATTERN_ID, JOURNEY_PATTERN.ON_ROUTE_ID)
                        .values((UUID) null, null));

        for (final Jore4JourneyPattern journeyPattern : journeyPatterns) {
            batch = batch.bind(journeyPattern.journeyPatternId(), journeyPattern.routeId());
        }

        if (batch.size() > 0) {
            batch.execute();
        }
    }
}

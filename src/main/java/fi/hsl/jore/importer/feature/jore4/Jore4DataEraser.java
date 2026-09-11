package fi.hsl.jore.importer.feature.jore4;

import static fi.hsl.jore.jore4.jooq.network.Tables.INFRASTRUCTURE_LINK_ALONG_ROUTE;
import static fi.hsl.jore.jore4.jooq.network.Tables.JOURNEY_PATTERN;
import static fi.hsl.jore.jore4.jooq.network.Tables.LINE;
import static fi.hsl.jore.jore4.jooq.network.Tables.ROUTE;
import static fi.hsl.jore.jore4.jooq.network.Tables.SCHEDULED_STOP_POINT;
import static fi.hsl.jore.jore4.jooq.network.Tables.SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN;
import static fi.hsl.jore.jore4.jooq.network.Tables.TIMING_PLACE;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Jore4DataEraser implements IJore4DataEraser {

    private final DSLContext db;

    @Autowired
    public Jore4DataEraser(@Qualifier("jore4Dsl") final DSLContext db) {
        this.db = db;
    }

    @Transactional
    @Override
    public void deleteJourneyPatterns() {
        db.truncateTable(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN)
                .restartIdentity()
                .execute();
        db.truncateTable(JOURNEY_PATTERN).restartIdentity().cascade().execute();
    }

    @Transactional
    @Override
    public void deleteRoutesAndLines() {
        db.truncateTable(INFRASTRUCTURE_LINK_ALONG_ROUTE).restartIdentity().execute();
        db.truncateTable(ROUTE).restartIdentity().cascade().execute();
        db.truncateTable(LINE).restartIdentity().cascade().execute();
    }

    @Transactional
    @Override
    public void deleteScheduledStopPointsAndTimingPlaces() {
        db.truncateTable(SCHEDULED_STOP_POINT).restartIdentity().cascade().execute();
        db.truncateTable(TIMING_PLACE).restartIdentity().cascade().execute();
    }
}

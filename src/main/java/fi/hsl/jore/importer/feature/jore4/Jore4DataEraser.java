package fi.hsl.jore.importer.feature.jore4;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static fi.hsl.jore.jore4.jooq.journey_pattern.Tables.JOURNEY_PATTERN_;
import static fi.hsl.jore.jore4.jooq.journey_pattern.Tables.SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN;
import static fi.hsl.jore.jore4.jooq.route.Tables.INFRASTRUCTURE_LINK_ALONG_ROUTE;
import static fi.hsl.jore.jore4.jooq.route.Tables.LINE;
import static fi.hsl.jore.jore4.jooq.route.Tables.ROUTE_;
import static fi.hsl.jore.jore4.jooq.service_pattern.Tables.SCHEDULED_STOP_POINT;
import static fi.hsl.jore.jore4.jooq.service_pattern.Tables.VEHICLE_MODE_ON_SCHEDULED_STOP_POINT;
import static fi.hsl.jore.jore4.jooq.timing_pattern.Tables.TIMING_PLACE;

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
        db.truncateTable(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN).restartIdentity().execute();
        db.truncateTable(JOURNEY_PATTERN_).restartIdentity().cascade().execute();
    }

    @Transactional
    @Override
    public void deleteRoutesAndLines() {
        db.truncateTable(INFRASTRUCTURE_LINK_ALONG_ROUTE).restartIdentity().execute();
        db.truncateTable(ROUTE_).restartIdentity().cascade().execute();
        db.truncateTable(LINE).restartIdentity().cascade().execute();
    }

    @Transactional
    @Override
    public void deleteScheduledStopPointsAndTimingPlaces() {
        db.truncateTable(VEHICLE_MODE_ON_SCHEDULED_STOP_POINT).restartIdentity().execute();
        db.truncateTable(SCHEDULED_STOP_POINT).restartIdentity().cascade().execute();
        db.truncateTable(TIMING_PLACE).restartIdentity().cascade().execute();
    }
}

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

@Component
public class DataEraser implements IDataEraser {

    private final DSLContext db;

    @Autowired
    public DataEraser(@Qualifier("jore4Dsl") final DSLContext db) {
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
    public void deleteScheduledStopPoints() {
        db.truncateTable(SCHEDULED_STOP_POINT).restartIdentity().cascade().execute();
    }
}

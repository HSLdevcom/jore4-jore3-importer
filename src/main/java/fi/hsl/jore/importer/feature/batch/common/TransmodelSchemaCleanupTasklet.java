package fi.hsl.jore.importer.feature.batch.common;

import org.jooq.DSLContext;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static fi.hsl.jore.jore4.jooq.internal_route.Tables.ROUTE;
import static fi.hsl.jore.jore4.jooq.internal_service_pattern.Tables.SCHEDULED_STOP_POINT;
import static fi.hsl.jore.jore4.jooq.journey_pattern.Tables.JOURNEY_PATTERN_;
import static fi.hsl.jore.jore4.jooq.journey_pattern.Tables.SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN;
import static fi.hsl.jore.jore4.jooq.route.Tables.INFRASTRUCTURE_LINK_ALONG_ROUTE;
import static fi.hsl.jore.jore4.jooq.route.Tables.LINE;
import static fi.hsl.jore.jore4.jooq.service_pattern.Tables.SCHEDULED_STOP_POINT_SERVICED_BY_VEHICLE_MODE;

/**
 * Cleans up the Jore 4 database so that new scheduled stop points,
 * lines, and routes can be inserted into the Jore 4 database.
 */
@Component
public class TransmodelSchemaCleanupTasklet implements Tasklet  {

    private final DSLContext db;

    @Autowired
    public TransmodelSchemaCleanupTasklet(@Qualifier("jore4Dsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        db.truncate(SCHEDULED_STOP_POINT_SERVICED_BY_VEHICLE_MODE).cascade().execute();
        db.truncate(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN).cascade().execute();
        db.truncate(INFRASTRUCTURE_LINK_ALONG_ROUTE).cascade().execute();
        db.truncate(LINE).cascade().execute();
        db.truncate(ROUTE).cascade().execute();
        db.truncate(JOURNEY_PATTERN_).cascade().execute();
        db.truncate(SCHEDULED_STOP_POINT).cascade().execute();
        return RepeatStatus.FINISHED;
    }
}

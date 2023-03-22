package fi.hsl.jore.importer.feature.batch.common;

import fi.hsl.jore.importer.feature.jore4.IDataEraser;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cleans up the Jore 4 database so that new scheduled stop points,
 * lines, and routes can be inserted into the Jore 4 database.
 */
@Component
public class Jore4SchemaCleanupTasklet implements Tasklet  {

    private final IDataEraser dataEraser;

    @Autowired
    public Jore4SchemaCleanupTasklet(final IDataEraser dataEraser) {
        this.dataEraser = dataEraser;
    }

    @Transactional
    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        dataEraser.deleteJourneyPatterns();
        dataEraser.deleteRoutesAndLines();
        dataEraser.deleteScheduledStopPoints();

        return RepeatStatus.FINISHED;
    }
}

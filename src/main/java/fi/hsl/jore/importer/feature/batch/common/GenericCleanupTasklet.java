package fi.hsl.jore.importer.feature.batch.common;

import fi.hsl.jore.importer.feature.common.dto.field.PK;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class GenericCleanupTasklet<ENTITY, KEY extends PK> implements Tasklet {

    private final IImportRepository<ENTITY, KEY> importRepository;

    public GenericCleanupTasklet(final IImportRepository<ENTITY, KEY> importRepository) {
        this.importRepository = importRepository;
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution,
                                final ChunkContext chunkContext) throws Exception {
        importRepository.clearStagingTable();
        return RepeatStatus.FINISHED;
    }
}

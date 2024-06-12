package fi.hsl.jore.importer.feature.batch.common;

import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.PK;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class GenericCommitTasklet<ENTITY, KEY extends PK> implements Tasklet {

    private static final Logger LOG = LoggerFactory.getLogger(GenericCommitTasklet.class);

    private final IImportRepository<ENTITY, KEY> importRepository;

    public GenericCommitTasklet(final IImportRepository<ENTITY, KEY> importRepository) {
        this.importRepository = importRepository;
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {

        final Map<RowStatus, Set<KEY>> result = importRepository.commitStagingToTarget();

        LOG.info("Committed: {}", IImportRepository.commitCounts(result));

        importRepository.clearStagingTable();

        return RepeatStatus.FINISHED;
    }
}

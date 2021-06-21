package fi.hsl.jore.importer.feature.batch.common;

import fi.hsl.jore.importer.feature.common.dto.field.PK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemWriter;

import java.util.List;


public class GenericImportWriter<ENTITY, KEY extends PK> implements ItemWriter<ENTITY> {

    private static final Logger LOG = LoggerFactory.getLogger(GenericImportWriter.class);

    private final IImportRepository<ENTITY, KEY> importRepository;

    private int counter;

    public GenericImportWriter(final IImportRepository<ENTITY, KEY> importRepository) {
        this.importRepository = importRepository;
    }

    @Override
    public void write(final List<? extends ENTITY> items) {
        counter += items.size();

        importRepository.submitToStaging(items);
    }

    @SuppressWarnings("unused")
    @AfterStep
    public ExitStatus afterStep(final StepExecution stepExecution) {
        LOG.info("Staged {} items", counter);

        counter = 0;
        return stepExecution.getExitStatus();
    }
}

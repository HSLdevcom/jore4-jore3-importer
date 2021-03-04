package fi.hsl.jore.importer.feature.batch.link_context;

import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.repository.ILinkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class LinkWriter implements ItemWriter<PersistableLink> {

    public static final int BLOCK_SIZE = 1000;

    private static final Logger LOG = LoggerFactory.getLogger(LinkWriter.class);

    private final ILinkRepository repository;

    private int counter;

    public LinkWriter(final ILinkRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(final List<? extends PersistableLink> items) {
        counter += items.size();
        repository.insertLinks(io.vavr.collection.List.ofAll(items));
    }

    @SuppressWarnings("unused")
    @AfterStep
    public ExitStatus afterStep(final StepExecution stepExecution) {
        LOG.info("Wrote {} items", counter);
        return stepExecution.getExitStatus();
    }
}

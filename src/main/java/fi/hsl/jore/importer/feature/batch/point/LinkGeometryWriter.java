package fi.hsl.jore.importer.feature.batch.point;

import fi.hsl.jore.importer.feature.batch.point.dto.LinkGeometry;
import fi.hsl.jore.importer.feature.infrastructure.link.repository.ILinkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemWriter;

import java.util.List;


public class LinkGeometryWriter implements ItemWriter<LinkGeometry> {

    private static final Logger LOG = LoggerFactory.getLogger(LinkGeometryWriter.class);
    public static final int BLOCK_SIZE = 100;

    private final ILinkRepository repository;

    private int counter;

    public LinkGeometryWriter(final ILinkRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(final List<? extends LinkGeometry> items) {
        counter += items.size();
        repository.updateLinkPoints(items);
    }

    @AfterStep
    public ExitStatus afterStep(final StepExecution stepExecution) {
        LOG.info("Wrote {} link geometries", counter);

        counter = 0;
        return stepExecution.getExitStatus();
    }
}

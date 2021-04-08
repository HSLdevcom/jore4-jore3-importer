package fi.hsl.jore.importer.feature.batch.node;

import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.repository.INodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemWriter;


public class NodeWriter implements ItemWriter<PersistableNode> {

    public static final int BLOCK_SIZE = 1000;

    private static final Logger LOG = LoggerFactory.getLogger(NodeWriter.class);

    private final INodeRepository repository;

    private int counter;

    public NodeWriter(final INodeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(final java.util.List<? extends PersistableNode> nodes) {
        counter += nodes.size();
        repository.upsert(nodes);
    }

    @SuppressWarnings("unused")
    @AfterStep
    public ExitStatus afterStep(final StepExecution stepExecution) {
        LOG.info("Wrote {} items", counter);

        counter = 0;
        return stepExecution.getExitStatus();
    }
}

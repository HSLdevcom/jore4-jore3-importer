package fi.hsl.jore.importer.feature.batch.point;

import fi.hsl.jore.importer.feature.batch.point.dto.LinkEndpoints;
import fi.hsl.jore.importer.feature.batch.point.dto.LinkPoints;
import fi.hsl.jore.importer.feature.batch.point.dto.PointRow;
import fi.hsl.jore.importer.feature.jore.entity.JrPoint;
import fi.hsl.jore.importer.feature.jore.key.JrLinkPk;
import io.vavr.collection.List;
import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Spring Batch doesn't directly support grouping items, but we can achieve that
 * by wrapping a reader within another reader.
 */
public class LinkPointReader
        implements ItemReader<LinkPoints> {

    private static final Logger LOG = LoggerFactory.getLogger(LinkPointReader.class);

    private final JdbcCursorItemReader<PointRow> delegate;

    private final AtomicReference<ReaderState> stateRef =
            new AtomicReference<>(ReaderState.init());

    private int counter;

    public LinkPointReader(final JdbcCursorItemReader<PointRow> delegate) {
        this.delegate = delegate;
    }

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        delegate.open(stepExecution.getExecutionContext());
    }

    @Override
    public LinkPoints read() throws Exception {
        while (true) {
            if (stateRef.get().exhausted()) {
                LOG.info("Delegate reader is exhausted");
                return null;
            }
            final PointRow item = delegate.read();
            final ReaderState nextState =
                    stateRef.getAndUpdate(oldState -> oldState.onItem(item));

            //noinspection VariableNotUsedInsideIf
            if (item != null) {
                counter += 1;
            }

            // Keep reading until the ReaderState produces a result
            if (nextState.result().isPresent()) {
                return nextState.result().orElseThrow();
            }
        }
    }

    @AfterStep
    public ExitStatus afterStep(final StepExecution stepExecution) {
        delegate.close();

        LOG.info("Read {} rows", counter);

        counter = 0;
        stateRef.set(ReaderState.init());

        return stepExecution.getExitStatus();
    }

    @Value.Immutable
    public interface ReaderState {

        Optional<JrLinkPk> link();

        Optional<LinkEndpoints> endpoints();

        @Value.Default
        default List<JrPoint> queue() {
            return List.empty();
        }

        @Value.Lazy
        default Optional<LinkPoints> pendingResult() {
            return (link().isPresent() && endpoints().isPresent()) ?
                    Optional.of(LinkPoints.of(link().orElseThrow(),
                                              endpoints().orElseThrow(),
                                              queue())) :
                    Optional.empty();
        }

        Optional<LinkPoints> result();

        static ReaderState init() {
            return ImmutableReaderState.builder().build();
        }

        @Value.Default
        default boolean exhausted() {
            return false;
        }

        default ReaderState onItem(@Nullable final PointRow ctx) {
            if (ctx == null) {
                // This is the last point in the database
                // -> Reader is finished and there are no more points
                return ImmutableReaderState.copyOf(this)
                                           .withExhausted(true)
                                           .withResult(pendingResult());
            }
            final JrPoint point = ctx.point();
            final LinkEndpoints endpoints = ctx.endpoints();
            if (link().isEmpty()) {
                // This is the first point in the database
                return ImmutableReaderState.copyOf(this)
                                           .withLink(point.fkLink())
                                           .withEndpoints(endpoints)
                                           .withQueue(List.of(point));
            }
            final JrLinkPk previous = link().get();
            if (previous.equals(point.fkLink())) {
                // This point belongs to the current link
                return ImmutableReaderState.copyOf(this)
                                           .withQueue(queue().append(point))
                                           .withResult(Optional.empty());
            }
            // This point belongs to a new link
            return ImmutableReaderState.copyOf(this)
                                       .withLink(point.fkLink())
                                       .withEndpoints(endpoints)
                                       .withQueue(List.of(point))
                                       .withResult(pendingResult());
        }
    }
}

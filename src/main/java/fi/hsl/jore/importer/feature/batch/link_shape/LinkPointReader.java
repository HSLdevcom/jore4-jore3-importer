package fi.hsl.jore.importer.feature.batch.link_shape;

import fi.hsl.jore.importer.feature.batch.link_shape.dto.LinkEndpoints;
import fi.hsl.jore.importer.feature.batch.link_shape.dto.LinkPoints;
import fi.hsl.jore.importer.feature.batch.link_shape.dto.PointRow;
import fi.hsl.jore.importer.feature.jore3.entity.JrPoint;
import fi.hsl.jore.importer.feature.jore3.key.JrLinkPk;
import io.vavr.collection.List;
import io.vavr.collection.SortedMap;
import io.vavr.collection.TreeMap;
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
                    stateRef.updateAndGet(oldState -> oldState.onItem(item));

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
    public interface Accumulator {

        // Store points in a map ordered by the point order
        @Value.Default
        default SortedMap<Integer, JrPoint> contents() {
            return TreeMap.empty();
        }

        // Mutator
        Accumulator withContents(SortedMap<Integer, JrPoint> contents);

        static Accumulator empty() {
            return ImmutableAccumulator.builder().build();
        }

        default Accumulator insert(final JrPoint point) {
            return withContents(contents().put(point.orderNumber(), point));
        }

        static Accumulator ofOnly(final JrPoint point) {
            return empty().insert(point);
        }

        default List<JrPoint> asList() {
            return contents().values().toList();
        }
    }

    @Value.Immutable
    public interface ReaderState {

        Optional<JrLinkPk> link();

        Optional<LinkEndpoints> endpoints();

        @Value.Default
        default Accumulator accumulator() {
            return Accumulator.empty();
        }

        @Value.Lazy
        default Optional<LinkPoints> pendingResult() {
            return (link().isPresent() && endpoints().isPresent()) ?
                    Optional.of(LinkPoints.of(link().orElseThrow(),
                                              endpoints().orElseThrow(),
                                              accumulator().asList())) :
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
                                           .withAccumulator(Accumulator.ofOnly(point));
            }
            final JrLinkPk previous = link().get();
            if (previous.equals(point.fkLink())) {
                // This point belongs to the current link
                return ImmutableReaderState.copyOf(this)
                                           .withAccumulator(accumulator().insert(point))
                                           .withResult(Optional.empty());
            }
            // This point belongs to a new link
            return ImmutableReaderState.copyOf(this)
                                       .withLink(point.fkLink())
                                       .withEndpoints(endpoints)
                                       .withAccumulator(Accumulator.ofOnly(point))
                                       .withResult(pendingResult());
        }
    }
}

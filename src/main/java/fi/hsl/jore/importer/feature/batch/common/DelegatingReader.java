package fi.hsl.jore.importer.feature.batch.common;

import jakarta.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamReader;

/**
 * Spring Batch doesn't directly support grouping items, but we can achieve that by wrapping a reader within another
 * reader.
 *
 * <p>The delegate reader produces items of type I, which the reader state consumes and periodically produces output of
 * type O.
 *
 * @param <O> The output type
 * @param <I> The input type
 */
public abstract class DelegatingReader<O, I> implements ItemReader<O> {

    private static final Logger LOG = LoggerFactory.getLogger(DelegatingReader.class);

    private final ItemStreamReader<I> delegate;

    private final AtomicReference<AbstractReaderState<O, I>> stateRef = new AtomicReference<>(null);

    private int counter;

    protected DelegatingReader(final ItemStreamReader<I> delegate) {
        this.delegate = delegate;
    }

    protected abstract AbstractReaderState<O, I> initialState();

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        stateRef.set(initialState());
        delegate.open(stepExecution.getExecutionContext());
    }

    @Override
    @Nullable
    public O read() throws Exception {
        while (true) {
            if (stateRef.get().exhausted()) {
                LOG.info("Delegate reader is exhausted");
                return null;
            }
            final I item = delegate.read();
            final AbstractReaderState<O, I> nextState = stateRef.updateAndGet(oldState -> oldState.onItem(item));

            //noinspection VariableNotUsedInsideIf
            if (item != null) {
                counter += 1;
            }

            // Keep reading until the AbstractReaderState produces a result
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
        stateRef.set(initialState());

        return stepExecution.getExitStatus();
    }

    public interface AbstractReaderState<O, I> {

        /** @return True, if the delegate is exhausted and cannot produce any more values */
        @Value.Default
        default boolean exhausted() {
            return false;
        }

        /**
         * Produces a result once the reader has consumed enough items.
         *
         * @return An optional describing the result
         */
        Optional<O> result();

        /**
         * Consume a single item.
         *
         * @param item One item read from the delegate. May be null if the delegate could not provide an item.
         * @return The next reader state
         */
        AbstractReaderState<O, I> onItem(@Nullable I item);
    }
}

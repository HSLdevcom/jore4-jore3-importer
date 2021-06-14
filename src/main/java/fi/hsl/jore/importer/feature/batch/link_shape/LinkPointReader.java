package fi.hsl.jore.importer.feature.batch.link_shape;

import fi.hsl.jore.importer.feature.batch.common.DelegatingReader;
import fi.hsl.jore.importer.feature.batch.link_shape.dto.LinkEndpoints;
import fi.hsl.jore.importer.feature.batch.link_shape.dto.LinkPoints;
import fi.hsl.jore.importer.feature.batch.link_shape.dto.PointRow;
import fi.hsl.jore.importer.feature.jore3.entity.JrPoint;
import fi.hsl.jore.importer.feature.jore3.key.JrLinkPk;
import io.vavr.collection.List;
import io.vavr.collection.SortedMap;
import io.vavr.collection.TreeMap;
import org.immutables.value.Value;
import org.springframework.batch.item.ItemStreamReader;

import javax.annotation.Nullable;
import java.util.Optional;

public class LinkPointReader
        extends DelegatingReader<LinkPoints, PointRow> {

    public LinkPointReader(final ItemStreamReader<PointRow> delegate) {
        super(delegate);
    }

    @Override
    protected AbstractReaderState<LinkPoints, PointRow> initialState() {
        return ReaderState.init();
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
    public interface ReaderState
            extends DelegatingReader.AbstractReaderState<LinkPoints, PointRow> {

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

        static ReaderState init() {
            return ImmutableReaderState.builder().build();
        }

        @Override
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

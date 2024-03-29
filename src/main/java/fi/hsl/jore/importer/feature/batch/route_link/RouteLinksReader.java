package fi.hsl.jore.importer.feature.batch.route_link;

import fi.hsl.jore.importer.feature.batch.common.DelegatingReader;
import fi.hsl.jore.importer.feature.batch.route_link.dto.LastLinkAttributes;
import fi.hsl.jore.importer.feature.batch.route_link.dto.RouteLinksAndAttributes;
import fi.hsl.jore.importer.feature.batch.route_link.dto.SingleRouteLinkAndParent;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteLink;
import fi.hsl.jore.importer.feature.jore3.key.JrRouteDirectionPk;
import io.vavr.collection.SortedMap;
import io.vavr.collection.TreeMap;
import io.vavr.collection.Vector;
import org.immutables.value.Value;
import org.springframework.batch.item.ItemStreamReader;

import javax.annotation.Nullable;
import java.util.Optional;

public class RouteLinksReader
        extends DelegatingReader<RouteLinksAndAttributes, SingleRouteLinkAndParent> {

    public RouteLinksReader(final ItemStreamReader<SingleRouteLinkAndParent> delegate) {
        super(delegate);
    }

    @Override
    protected AbstractReaderState<RouteLinksAndAttributes, SingleRouteLinkAndParent> initialState() {
        return ReaderState.init();
    }

    @Value.Immutable
    public interface Accumulator {

        // Store route links in a map ordered by the link order
        @Value.Default
        default SortedMap<Integer, JrRouteLink> contents() {
            return TreeMap.empty();
        }

        // Mutator
        Accumulator withContents(SortedMap<Integer, JrRouteLink> contents);

        static Accumulator empty() {
            return ImmutableAccumulator.builder().build();
        }

        default Accumulator insert(final JrRouteLink routeLink) {
            return withContents(contents().put(routeLink.orderNumber(), routeLink));
        }

        static Accumulator ofOnly(final JrRouteLink routeLink) {
            return empty().insert(routeLink);
        }

        default Vector<JrRouteLink> asVector() {
            return contents().values().toVector();
        }
    }

    @Value.Immutable
    public interface ReaderState
            extends AbstractReaderState<RouteLinksAndAttributes, SingleRouteLinkAndParent> {

        // Keep track of the parent route direction
        Optional<JrRouteDirectionPk> parent();

        Optional<LastLinkAttributes> attributesInParent();

        // Accumulator for the route links
        Accumulator accumulator();

        @Value.Lazy
        default Optional<RouteLinksAndAttributes> pendingResult() {
            return attributesInParent()
                    .map(attributes -> RouteLinksAndAttributes.of(
                            accumulator().asVector(),
                            attributes
                    ));
        }

        static ReaderState init() {
            return ImmutableReaderState.builder()
                                       .accumulator(Accumulator.empty())
                                       .build();
        }

        @Override
        default ReaderState onItem(@Nullable final SingleRouteLinkAndParent ctx) {
            if (ctx == null) {
                // This is the last route link in the database
                // -> Reader is finished and there are no more route links
                return ImmutableReaderState.copyOf(this)
                                           .withExhausted(true)
                                           .withResult(pendingResult());
            }
            final JrRouteLink routeLink = ctx.routeLink();
            final LastLinkAttributes attributes = ctx.attributes();
            final JrRouteDirectionPk parent = routeLink.fkRouteDirection();
            if (parent().isEmpty()) {
                // This is the first route link in the database for this route direction
                return ImmutableReaderState.copyOf(this)
                                           .withParent(parent)
                                           .withAttributesInParent(attributes)
                                           .withAccumulator(Accumulator.ofOnly(routeLink));
            }
            final JrRouteDirectionPk previous = parent().get();
            if (previous.equals(parent)) {
                // This route link belongs to the current route direction
                return ImmutableReaderState.copyOf(this)
                                           // We _must_ update the attributes, because while some of the fields (joined from the
                                           // route direction) are the same for all rows, the "node type of the last node" field
                                           // is different for each row and we only care about the last!
                                           .withAttributesInParent(attributes)
                                           .withAccumulator(accumulator().insert(routeLink))
                                           .withResult(Optional.empty());
            }
            // This route link belongs to a new route direction
            return ImmutableReaderState.copyOf(this)
                                       .withParent(parent)
                                       .withAttributesInParent(attributes)
                                       .withAccumulator(Accumulator.ofOnly(routeLink))
                                       .withResult(pendingResult());
        }
    }
}

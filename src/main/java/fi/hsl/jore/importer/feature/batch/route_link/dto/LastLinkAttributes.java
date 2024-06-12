package fi.hsl.jore.importer.feature.batch.route_link.dto;

import fi.hsl.jore.importer.feature.jore3.enumerated.NodeType;
import java.util.Optional;
import org.immutables.value.Value;

/**
 * Jore 3 models the route direction as a sequence of links (between nodes), where each link contains attributes of the
 * starting node. Because there are 1 fewer links than nodes (e.g. a route direction might contain 10 nodes with 9 links
 * in between), the attributes of the last node must be stored somewhere.
 *
 * <p>Jore 3 stores some these attributes in the parent route direction table.
 */
@Value.Immutable
public interface LastLinkAttributes {

    NodeType nodeType();

    boolean includeInTimetable();

    Optional<Integer> timetableColumn();

    static LastLinkAttributes of(
            final NodeType nodeType, final boolean includeInTimetable, final Optional<Integer> timetableColumn) {
        return ImmutableLastLinkAttributes.builder()
                .nodeType(nodeType)
                .timetableColumn(timetableColumn)
                .includeInTimetable(includeInTimetable)
                .build();
    }
}

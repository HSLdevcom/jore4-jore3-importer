package fi.hsl.jore.importer.feature.batch.route_link.dto;

import com.google.common.base.Preconditions;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteLink;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public interface RouteLinksAndAttributes {

    List<JrRouteLink> routeLinks();

    LastLinkAttributes lastLinkAttributes();

    @Value.Check
    default void checkLinks() {
        Preconditions.checkState(!routeLinks().isEmpty(), "Must contain at least one route link!");
        Preconditions.checkState(
                routeLinks().stream()
                                .map(JrRouteLink::fkRouteDirection)
                                .distinct()
                                .count()
                        == 1,
                "All route links must belong to the same route direction!");
    }

    static RouteLinksAndAttributes of(final List<JrRouteLink> routeLinks, final LastLinkAttributes lastLinkAttributes) {
        return ImmutableRouteLinksAndAttributes.builder()
                .routeLinks(routeLinks)
                .lastLinkAttributes(lastLinkAttributes)
                .build();
    }
}

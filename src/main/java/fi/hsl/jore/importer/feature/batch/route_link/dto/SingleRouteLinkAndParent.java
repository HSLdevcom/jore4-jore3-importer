package fi.hsl.jore.importer.feature.batch.route_link.dto;

import fi.hsl.jore.importer.feature.jore3.entity.JrRouteLink;
import org.immutables.value.Value;

@Value.Immutable
public interface SingleRouteLinkAndParent {

    JrRouteLink routeLink();

    LastLinkAttributes attributes();

    static SingleRouteLinkAndParent of(final JrRouteLink routeLink, final LastLinkAttributes attributes) {
        return ImmutableSingleRouteLinkAndParent.builder()
                .routeLink(routeLink)
                .attributes(attributes)
                .build();
    }
}

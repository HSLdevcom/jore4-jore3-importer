package fi.hsl.jore.importer.feature.infrastructure.node.dto;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

@Value.Immutable
public interface Jore3Node extends CommonFields<Jore3Node> {

    static Jore3Node of(final ExternalId externalId, final NodeType nodeType, final Point location) {
        return ImmutableJore3Node.builder()
                .externalId(externalId)
                .nodeType(nodeType)
                .location(location)
                .build();
    }
}

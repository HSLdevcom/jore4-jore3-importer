package fi.hsl.jore.importer.feature.infrastructure.node.dto;


import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

@Value.Immutable
public interface PersistableNode
        extends ModifiableFields<PersistableNode>,
                IHasNodeExternalId {

    static PersistableNode of(final ExternalId externalId,
                              final NodeType nodeType,
                              final Point location) {
        return ImmutablePersistableNode.builder()
                                       .externalId(externalId)
                                       .nodeType(nodeType)
                                       .location(location)
                                       .build();
    }
}

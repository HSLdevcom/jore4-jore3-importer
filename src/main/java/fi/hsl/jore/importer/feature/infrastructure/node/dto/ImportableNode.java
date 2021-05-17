package fi.hsl.jore.importer.feature.infrastructure.node.dto;


import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

@Value.Immutable
public interface ImportableNode
        extends CommonFields<ImportableNode> {

    static ImportableNode of(final ExternalId externalId,
                             final NodeType nodeType,
                             final Point location) {
        return ImmutableImportableNode.builder()
                                      .externalId(externalId)
                                      .nodeType(nodeType)
                                      .location(location)
                                      .build();
    }
}

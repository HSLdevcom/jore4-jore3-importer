package fi.hsl.jore.importer.feature.infrastructure.link.dto;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import org.immutables.value.Value;
import org.locationtech.jts.geom.LineString;

@Value.Immutable
public interface PersistableLink extends CommonFields, IHasLinkExternalId {

    NodePK fromNode();

    NodePK toNode();

    static PersistableLink of(
            final ExternalId externalId,
            final NetworkType networkType,
            final LineString geometry,
            final NodePK fromNode,
            final NodePK toNode) {
        return ImmutablePersistableLink.builder()
                .externalId(externalId)
                .networkType(networkType)
                .geometry(geometry)
                .fromNode(fromNode)
                .toNode(toNode)
                .build();
    }
}

package fi.hsl.jore.importer.feature.infrastructure.link.dto;


import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import org.immutables.value.Value;
import org.locationtech.jts.geom.LineString;

@Value.Immutable
public interface ImportableLink
        extends CommonFields,
                IHasLinkExternalId {

    ExternalId fromNode();

    ExternalId toNode();

    static ImportableLink of(final ExternalId externalId,
                             final NetworkType networkType,
                             final LineString geometry,
                             final ExternalId fromNode,
                             final ExternalId toNode) {
        return ImmutableImportableLink.builder()
                                      .externalId(externalId)
                                      .networkType(networkType)
                                      .geometry(geometry)
                                      .fromNode(fromNode)
                                      .toNode(toNode)
                                      .build();
    }
}

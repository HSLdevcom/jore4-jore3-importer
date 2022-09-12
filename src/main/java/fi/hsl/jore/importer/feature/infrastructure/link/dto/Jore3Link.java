package fi.hsl.jore.importer.feature.infrastructure.link.dto;


import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import org.immutables.value.Value;
import org.locationtech.jts.geom.LineString;

@Value.Immutable
public interface Jore3Link
        extends CommonFields,
                IHasLinkExternalId {

    ExternalId fromNode();

    ExternalId toNode();

    static Jore3Link of(final ExternalId externalId,
                        final NetworkType networkType,
                        final LineString geometry,
                        final ExternalId fromNode,
                        final ExternalId toNode) {
        return ImmutableJore3Link.builder()
                                      .externalId(externalId)
                                      .networkType(networkType)
                                      .geometry(geometry)
                                      .fromNode(fromNode)
                                      .toNode(toNode)
                                      .build();
    }
}

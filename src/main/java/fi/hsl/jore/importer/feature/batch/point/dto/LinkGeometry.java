package fi.hsl.jore.importer.feature.batch.point.dto;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.IHasLinkExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import org.immutables.value.Value;
import org.locationtech.jts.geom.LineString;

@Value.Immutable
public interface LinkGeometry extends IHasLinkExternalId {

    LineString geometry();

    static LinkGeometry of(final ExternalId externalId,
                           final NetworkType networkType,
                           final LineString geometry) {
        return ImmutableLinkGeometry.builder()
                                    .externalId(externalId)
                                    .networkType(networkType)
                                    .geometry(geometry)
                                    .build();
    }
}

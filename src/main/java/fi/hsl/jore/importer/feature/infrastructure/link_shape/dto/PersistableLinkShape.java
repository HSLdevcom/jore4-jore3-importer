package fi.hsl.jore.importer.feature.infrastructure.link_shape.dto;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import org.immutables.value.Value;
import org.locationtech.jts.geom.LineString;

@Value.Immutable
public interface PersistableLinkShape extends CommonFields<PersistableLinkShape> {

    LinkPK linkId();

    static PersistableLinkShape of(final LinkPK linkId, final ExternalId linkExternalId, final LineString geometry) {
        return ImmutablePersistableLinkShape.builder()
                .linkId(linkId)
                .linkExternalId(linkExternalId)
                .geometry(geometry)
                .build();
    }
}

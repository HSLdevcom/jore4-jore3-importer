package fi.hsl.jore.importer.feature.infrastructure.link_shape.dto;


import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import org.immutables.value.Value;
import org.locationtech.jts.geom.LineString;

@Value.Immutable
public interface PersistableLinkShape
        extends CommonFields<PersistableLinkShape> {

    LinkPK link();

    static PersistableLinkShape of(final LinkPK link,
                                   final ExternalId linkExternalId,
                                   final LineString geometry) {
        return ImmutablePersistableLinkShape.builder()
                                            .link(link)
                                            .linkExternalId(linkExternalId)
                                            .geometry(geometry)
                                            .build();
    }
}

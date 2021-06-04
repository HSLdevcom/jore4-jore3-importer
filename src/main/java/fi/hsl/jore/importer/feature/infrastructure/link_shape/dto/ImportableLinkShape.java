package fi.hsl.jore.importer.feature.infrastructure.link_shape.dto;


import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;
import org.locationtech.jts.geom.LineString;

@Value.Immutable
public interface ImportableLinkShape
        extends CommonFields<ImportableLinkShape> {

    static ImportableLinkShape of(final ExternalId linkExternalId,
                                  final LineString geometry) {
        return ImmutableImportableLinkShape.builder()
                                           .linkExternalId(linkExternalId)
                                           .geometry(geometry)
                                           .build();
    }
}

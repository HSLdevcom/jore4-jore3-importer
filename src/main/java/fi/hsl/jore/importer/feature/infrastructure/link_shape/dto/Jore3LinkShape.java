package fi.hsl.jore.importer.feature.infrastructure.link_shape.dto;


import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;
import org.locationtech.jts.geom.LineString;

@Value.Immutable
public interface Jore3LinkShape
        extends CommonFields<Jore3LinkShape> {

    static Jore3LinkShape of(final ExternalId linkExternalId,
                             final LineString geometry) {
        return ImmutableJore3LinkShape.builder()
                                           .linkExternalId(linkExternalId)
                                           .geometry(geometry)
                                           .build();
    }
}

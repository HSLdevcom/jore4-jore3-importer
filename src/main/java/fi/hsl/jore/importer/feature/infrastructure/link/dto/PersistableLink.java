package fi.hsl.jore.importer.feature.infrastructure.link.dto;


import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.generated.NetworkTypePK;
import org.immutables.value.Value;
import org.locationtech.jts.geom.LineString;

@Value.Immutable
public interface PersistableLink
        extends ModifiableFields<PersistableLink> {

    static PersistableLink of(final NetworkTypePK networkTypePk,
                              final LineString geometry) {
        return ImmutablePersistableLink.builder()
                                       .networkTypePk(networkTypePk)
                                       .geometry(geometry)
                                       .build();
    }
}

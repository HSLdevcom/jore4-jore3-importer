package fi.hsl.jore.importer.feature.infrastructure.link.dto;

import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.generated.NetworkTypePK;
import org.locationtech.jts.geom.LineString;


public interface ModifiableFields<T> {
    NetworkTypePK networkTypePk();

    LineString geometry();

    T withNetworkTypePk(NetworkTypePK networkTypePk);

    T withGeometry(LineString geometry);
}

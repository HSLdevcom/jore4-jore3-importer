package fi.hsl.jore.importer.feature.infrastructure.link.dto;

import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import org.locationtech.jts.geom.LineString;

import java.util.Optional;


public interface ModifiableFields<T> {
    NetworkType networkType();

    LineString geometry();

    Optional<LineString> points();

    T withNetworkType(NetworkType networkType);

    T withGeometry(LineString geometry);

    T withPoints(Optional<? extends LineString> points);

    T withPoints(LineString points);
}

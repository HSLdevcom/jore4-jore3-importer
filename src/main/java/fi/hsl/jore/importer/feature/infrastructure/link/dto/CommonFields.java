package fi.hsl.jore.importer.feature.infrastructure.link.dto;

import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import org.locationtech.jts.geom.LineString;

public interface CommonFields {
    NetworkType networkType();

    LineString geometry();
}

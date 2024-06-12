package fi.hsl.jore.importer.feature.infrastructure.link_shape.dto;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.locationtech.jts.geom.LineString;

public interface CommonFields<T> {

    ExternalId linkExternalId();

    LineString geometry();
}

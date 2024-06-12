package fi.hsl.jore.importer.feature.network.route.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import java.util.Optional;

public interface CommonFields<T> extends IHasExternalId {

    String routeNumber();

    MultilingualString name();

    LegacyHslMunicipalityCode legacyHslMunicipalityCode();

    Optional<Short> hiddenVariant();
}

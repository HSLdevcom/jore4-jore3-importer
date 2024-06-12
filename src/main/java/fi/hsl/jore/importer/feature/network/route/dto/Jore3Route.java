package fi.hsl.jore.importer.feature.network.route.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
public interface Jore3Route extends CommonFields<Jore3Route> {

    ExternalId lineId();

    static Jore3Route of(
            final ExternalId externalId,
            final ExternalId lineId,
            final String routeNumber,
            final Optional<Short> routeHiddenVariant,
            final MultilingualString name,
            final LegacyHslMunicipalityCode routeLegacyHslMunicipalityCode) {
        return ImmutableJore3Route.builder()
                .externalId(externalId)
                .lineId(lineId)
                .routeNumber(routeNumber)
                .hiddenVariant(routeHiddenVariant)
                .name(name)
                .legacyHslMunicipalityCode(routeLegacyHslMunicipalityCode)
                .build();
    }
}

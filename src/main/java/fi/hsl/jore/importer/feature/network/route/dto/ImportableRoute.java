package fi.hsl.jore.importer.feature.network.route.dto;


import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface ImportableRoute
        extends CommonFields<ImportableRoute> {

    ExternalId lineId();

    Optional<Short> routeHiddenVariant();

    static ImportableRoute of(final ExternalId externalId,
                              final ExternalId lineId,
                              final String routeNumber,
                              final Optional<Short> routeHiddenVariant, final MultilingualString name) {
        return ImmutableImportableRoute.builder()
                                       .externalId(externalId)
                                       .lineId(lineId)
                                       .routeNumber(routeNumber)
                                       .routeHiddenVariant(routeHiddenVariant)
                                       .name(name)
                                       .build();
    }
}

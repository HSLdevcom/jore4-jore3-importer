package fi.hsl.jore.importer.feature.network.route.dto;


import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;

@Value.Immutable
public interface ImportableRoute
        extends CommonFields<ImportableRoute> {

    ExternalId lineId();

    static ImportableRoute of(final ExternalId externalId,
                              final ExternalId lineId,
                              final String routeNumber,
                              final MultilingualString name) {
        return ImmutableImportableRoute.builder()
                                       .externalId(externalId)
                                       .lineId(lineId)
                                       .routeNumber(routeNumber)
                                       .name(name)
                                       .build();
    }
}

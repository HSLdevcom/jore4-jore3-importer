package fi.hsl.jore.importer.feature.network.route.dto;


import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;
import org.immutables.value.Value;

/**
 * The reinimilyh and reinimilyhr fields are missing
 * because they are deprecated.
 *
 * At the moment this variation of the route is intended to be used with
 * the {@link fi.hsl.jore.importer.feature.network.route.repository.IRouteTestRepository test repository}.
 */
@Value.Immutable
public interface PersistableRoute
        extends CommonFields<PersistableRoute> {

    LinePK line();

    static PersistableRoute of(final ExternalId externalId,
                               final LinePK line,
                               final String routeNumber,
                               final MultilingualString name) {
        return ImmutablePersistableRoute.builder()
                                        .externalId(externalId)
                                        .line(line)
                                        .routeNumber(routeNumber)
                                        .name(name)
                                        .build();
    }
}

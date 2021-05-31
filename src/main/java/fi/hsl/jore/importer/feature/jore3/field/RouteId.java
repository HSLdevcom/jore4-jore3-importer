package fi.hsl.jore.importer.feature.jore3.field;

import org.immutables.value.Value;

@Value.Immutable
public abstract class RouteId extends EncodedIdentifier {

    public static RouteId from(final String id) {
        return ImmutableRouteId.builder()
                               .originalValue(id)
                               .build();
    }
}

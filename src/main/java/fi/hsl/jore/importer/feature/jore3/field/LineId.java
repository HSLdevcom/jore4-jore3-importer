package fi.hsl.jore.importer.feature.jore3.field;

import org.immutables.value.Value;

@Value.Immutable
public abstract class LineId extends EncodedIdentifier {

    public static LineId from(final String id) {
        return ImmutableLineId.builder()
                              .originalValue(id)
                              .build();
    }
}

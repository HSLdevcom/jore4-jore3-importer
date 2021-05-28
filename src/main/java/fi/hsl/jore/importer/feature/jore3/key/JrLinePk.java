package fi.hsl.jore.importer.feature.jore3.key;


import fi.hsl.jore.importer.feature.jore3.field.LineId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasLineId;
import org.immutables.value.Value;

@Value.Immutable
public interface JrLinePk extends IHasLineId {
    static JrLinePk of(final LineId lineId) {
        return ImmutableJrLinePk.builder()
                                .lineId(lineId)
                                .build();
    }
}

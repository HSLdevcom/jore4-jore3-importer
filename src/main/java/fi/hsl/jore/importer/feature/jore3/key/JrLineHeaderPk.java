package fi.hsl.jore.importer.feature.jore3.key;

import fi.hsl.jore.importer.feature.jore3.field.LineId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasDuration;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasLineId;
import org.immutables.value.Value;

import java.time.LocalDate;

@Value.Immutable
public interface JrLineHeaderPk
        extends IHasLineId,
                IHasDuration {
    static JrLineHeaderPk of(final LineId lineId,
                             final LocalDate validFrom,
                             final LocalDate validTo) {
        return ImmutableJrLineHeaderPk.builder()
                                      .lineId(lineId)
                                      .validFrom(validFrom)
                                      .validTo(validTo)
                                      .build();
    }
}

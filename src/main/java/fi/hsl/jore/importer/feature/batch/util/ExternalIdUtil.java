package fi.hsl.jore.importer.feature.batch.util;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.jore3.field.TransitType;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;

public final class ExternalIdUtil {

    private ExternalIdUtil() {
    }

    public static ExternalId forLink(final TransitType transitType,
                                     final NodeId from,
                                     final NodeId to) {
        return ExternalId.of(String.format("%d-%s-%s",
                                           transitType.value(),
                                           from.value(),
                                           to.value()));
    }
}

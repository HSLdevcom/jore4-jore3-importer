package fi.hsl.jore.importer.feature.common.dto.mixin;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;

public interface IHasValidDates {
    DateRange validTime();
}

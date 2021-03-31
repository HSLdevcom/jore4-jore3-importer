package fi.hsl.jore.importer.feature.common.dto.mixin;

import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;

public interface IHasSystemTime {
    TimeRange systemTime();

    default boolean alive() {
        return !systemTime().range().hasUpperBound();
    }
}

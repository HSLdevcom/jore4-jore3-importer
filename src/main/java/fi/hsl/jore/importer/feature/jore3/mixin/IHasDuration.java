package fi.hsl.jore.importer.feature.jore3.mixin;

import java.time.LocalDate;

public interface IHasDuration {
    LocalDate validFrom();

    LocalDate validTo();
}

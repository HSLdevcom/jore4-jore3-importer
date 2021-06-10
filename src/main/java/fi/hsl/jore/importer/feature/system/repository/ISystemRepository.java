package fi.hsl.jore.importer.feature.system.repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public interface ISystemRepository {
    Instant currentTimestamp();

    LocalDate currentDate();

    LocalTime currentTime();
}

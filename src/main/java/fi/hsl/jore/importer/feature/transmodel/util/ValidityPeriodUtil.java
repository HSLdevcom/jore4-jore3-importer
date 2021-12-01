package fi.hsl.jore.importer.feature.transmodel.util;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Provides static utility methods which are used to construct
 * the start and end timestamps of a validity period.
 */
public final class ValidityPeriodUtil {

    /**
     * Prevent instantiation.
     */
    private ValidityPeriodUtil() {}

    /**
     * Constructs the start time of a validity period.
     * @param   validityPeriod  The validity period.
     * @return  An optional which contains the start time of a validity period.
     *          If the {@code startDay} is {@code null}, this method returns an
     *          empty optional.
     */
    public static Optional<LocalDateTime> determineValidityPeriodStartTime(final Range<LocalDate> validityPeriod) {
        if (!validityPeriod.hasLowerBound()) {
            return Optional.empty();
        }

        final LocalDate startDay  = determineStartDay(validityPeriod);
        return Optional.of(LocalDateTime.of(startDay, LocalTime.of(4, 30)));
    }

    private static LocalDate determineStartDay(final Range<LocalDate> validityPeriod) {
        final LocalDate lowerEndPoint = validityPeriod.lowerEndpoint();

        if (validityPeriod.lowerBoundType() == BoundType.CLOSED) {
            return lowerEndPoint;
        }
        else {
            return lowerEndPoint.plusDays(1);
        }
    }

    /**
     * Constructs the end time of a validity period.
     * @param   validityPeriod  The validity period.
     * @return  An optional which cotnains the end time of a validity period.
     *          If the {@code startDay} is {@code null}, this method returns an
     *          empty optional.
     */
    public static Optional<LocalDateTime> constructValidityPeriodEndTime(final Range<LocalDate> validityPeriod) {
        if (!validityPeriod.hasUpperBound()) {
            return Optional.empty();
        }

        final LocalDate endDay = determineEndDay(validityPeriod);
        return Optional.of(LocalDateTime.of(endDay, LocalTime.of( 4, 29, 59)));
    }

    private static LocalDate determineEndDay(final Range<LocalDate> validityPeriod) {
        final LocalDate upperEndPoint = validityPeriod.upperEndpoint();

        if (validityPeriod.upperBoundType() == BoundType.CLOSED) {
            return upperEndPoint.plusDays(1);
        }
        else {
            return upperEndPoint;
        }
    }
}

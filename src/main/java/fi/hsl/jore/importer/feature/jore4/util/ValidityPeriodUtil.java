package fi.hsl.jore.importer.feature.jore4.util;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;

import java.time.LocalDate;
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
    public static Optional<LocalDate> constructValidityPeriodStartDay(final Range<LocalDate> validityPeriod) {
        return Optional.of(validityPeriod)
                .filter(Range::hasLowerBound)
                .map(ValidityPeriodUtil::determineStartDay);
    }

    private static LocalDate determineStartDay(final Range<LocalDate> validityPeriod) {
        final LocalDate lowerEndPoint = validityPeriod.lowerEndpoint();
        return validityPeriod.lowerBoundType() == BoundType.CLOSED ? lowerEndPoint : lowerEndPoint.plusDays(1);
    }

    /**
     * Constructs the end time of a validity period.
     * @param   validityPeriod  The validity period.
     * @return  An optional which cotnains the end time of a validity period.
     *          If the {@code startDay} is {@code null}, this method returns an
     *          empty optional.
     */
    public static Optional<LocalDate> constructValidityPeriodEndDay(final Range<LocalDate> validityPeriod) {
        return Optional.of(validityPeriod)
                .filter(Range::hasUpperBound)
                .map(ValidityPeriodUtil::determineEndDay);
    }

    private static LocalDate determineEndDay(final Range<LocalDate> validityPeriod) {
        final LocalDate upperEndPoint = validityPeriod.upperEndpoint();
        // Jore4 validity times are (unlike postgresql dateranges) closed upper bound
        return validityPeriod.upperBoundType() == BoundType.CLOSED ? upperEndPoint : upperEndPoint.minusDays(1);
    }
}

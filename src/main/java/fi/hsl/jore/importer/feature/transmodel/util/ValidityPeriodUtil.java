package fi.hsl.jore.importer.feature.transmodel.util;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import fi.hsl.jore.importer.feature.transmodel.ExportConstants;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        return Optional.of(validityPeriod)
                .filter(Range::hasLowerBound)
                .map(ValidityPeriodUtil::determineStartDay)
                .map(startDay -> LocalDateTime.of(startDay, ExportConstants.OPERATING_DAY_START_TIME));
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
    public static Optional<LocalDateTime> constructValidityPeriodEndTime(final Range<LocalDate> validityPeriod) {
        return Optional.of(validityPeriod)
                .filter(Range::hasUpperBound)
                .map(ValidityPeriodUtil::determineEndDay)
                .map(endDay -> LocalDateTime.of(endDay, ExportConstants.OPERATING_DAY_END_TIME));
    }

    private static LocalDate determineEndDay(final Range<LocalDate> validityPeriod) {
        final LocalDate upperEndPoint = validityPeriod.upperEndpoint();
        return validityPeriod.upperBoundType() == BoundType.CLOSED ? upperEndPoint.plusDays(1) : upperEndPoint;
    }
}

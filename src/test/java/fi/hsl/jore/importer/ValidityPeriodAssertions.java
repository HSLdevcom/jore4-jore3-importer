package fi.hsl.jore.importer;

import com.google.common.collect.BoundType;
import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import java.time.LocalDate;
import org.assertj.core.api.SoftAssertions;

/**
 * Provides methods which allows us to write assertions for the validity period of entities exported to Jore 4 database.
 */
public class ValidityPeriodAssertions {

    private final SoftAssertions softAssertions;
    private final DateRange validityPeriod;

    public ValidityPeriodAssertions(final SoftAssertions softAssertions, final DateRange validityPeriod) {
        this.softAssertions = softAssertions;
        this.validityPeriod = validityPeriod;
    }

    /**
     * Ensures that the validity period starts at the given date AND the given date is included in the validity period.
     *
     * @param expectedStartDate The expected start date of the validity period.
     */
    public ValidityPeriodAssertions assertThatValidityPeriodStartsAt(final LocalDate expectedStartDate) {
        softAssertions
                .assertThat(validityPeriod.range().lowerEndpoint())
                .as("validityPeriodStartDate")
                .isEqualTo(expectedStartDate);

        softAssertions
                .assertThat(validityPeriod.range().lowerBoundType())
                .as("validityPeriodStartBoundType")
                .isEqualTo(BoundType.CLOSED);

        return this;
    }

    /**
     * Ensures that the validity period ends at the given date AND the given date isn't included in the validity period.
     *
     * @param expectedEndDate The expected end date of the validity period.
     */
    public ValidityPeriodAssertions assertThatValidityPeriodEndsAt(final LocalDate expectedEndDate) {
        softAssertions
                .assertThat(validityPeriod.range().upperEndpoint())
                .as("validityPeriodEndDate")
                .isEqualTo(expectedEndDate);

        softAssertions
                .assertThat(validityPeriod.range().upperBoundType())
                .as("validityPeriodEndBoundType")
                .isEqualTo(BoundType.OPEN);

        return this;
    }
}

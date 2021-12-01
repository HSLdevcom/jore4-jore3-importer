package fi.hsl.jore.importer.feature.transmodel.util;

import com.google.common.collect.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_END_TIME;
import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_START_TIME;
import static org.assertj.core.api.Assertions.assertThat;


class ValidityPeriodUtilTest {

    private static final LocalDate END_DAY = LocalDate.of(2021, 3, 1);
    private static final LocalDate START_DAY = LocalDate.of(2021, 2, 1);

    @Nested
    @DisplayName("Determine validity period start time")
    class DetermineValidityPeriodStartTime {

        @Nested
        @DisplayName("When the validity period doesn't have start day")
        class WhenValidityPeriodDoesNotHaveStartDay {

            @Test
            @DisplayName("Should return an empty optional")
            void shouldReturnEmptyOptional() {
                final Optional<LocalDateTime> startTime = ValidityPeriodUtil.determineValidityPeriodStartTime(Range.atMost(LocalDate.EPOCH));
                assertThat(startTime).isEmpty();
            }
        }

        @Nested
        @DisplayName("When the validity period has a start day")
        class WhenValidityPeriodHasStartDay {

            @Nested
            @DisplayName("When the start day is included in the validity period")
            class WhenStartDayIsIncludedInValidityPeriod {

                private final Range<LocalDate> INPUT = Range.closed(START_DAY, END_DAY);
                private final LocalDateTime EXPECTED_START_TIME = LocalDateTime.of(
                        START_DAY,
                        OPERATING_DAY_START_TIME
                );

                @Test
                @DisplayName("Should return an optional which contains the start time")
                void shouldReturnOptionalWhichContainsStartTime() {
                    final Optional<LocalDateTime> startTime = ValidityPeriodUtil.determineValidityPeriodStartTime(INPUT);
                    assertThat(startTime).isNotEmpty();
                }

                @Test
                @DisplayName("Should return the correct start time")
                void shouldReturnCorrectStartTime() {
                    final Optional<LocalDateTime> startTime = ValidityPeriodUtil.determineValidityPeriodStartTime(INPUT);
                    assertThat(startTime).contains(EXPECTED_START_TIME);
                }
            }

            @Nested
            @DisplayName("When the start day is excluded from the validity period")
            class WhenStartDayIsExcludedFromValidityPeriod {

                private final Range<LocalDate> INPUT = Range.open(START_DAY, END_DAY);
                private final LocalDateTime EXPECTED_START_TIME = LocalDateTime.of(
                        LocalDate.of(2021, 2, 2),
                        OPERATING_DAY_START_TIME
                );

                @Test
                @DisplayName("Should return an optional which contains the start time")
                void shouldReturnOptionalWhichContainsStartTime() {
                    final Optional<LocalDateTime> startTime = ValidityPeriodUtil.determineValidityPeriodStartTime(INPUT);
                    assertThat(startTime).isNotEmpty();
                }

                @Test
                @DisplayName("Should return the correct start time")
                void shouldReturnCorrectStartTime() {
                    final Optional<LocalDateTime> startTime = ValidityPeriodUtil.determineValidityPeriodStartTime(INPUT);
                    assertThat(startTime).contains(EXPECTED_START_TIME);
                }
            }
        }
    }

    @Nested
    @DisplayName("Determine validity period end time")
    class DetermineValidityPeriodEndTime {

        @Nested
        @DisplayName("When the validity period doesn't have end day")
        class WhenValidityPeriodDoesNotHavenEndDay {

            @Test
            @DisplayName("Should return an empty optional")
            void shouldReturnEmptyOptional() {
                final Optional<LocalDateTime> endTime = ValidityPeriodUtil.constructValidityPeriodEndTime(Range.atLeast(LocalDate.EPOCH));
                assertThat(endTime).isEmpty();
            }
        }

        @Nested
        @DisplayName("When when the validity period has end day")
        class WhenValidityPeriodHasEndDay {

            @Nested
            @DisplayName("When the end day is included in the validity period")
            class WhenEndDayIsIncludedInValidityPeriod {

                private final Range<LocalDate> INPUT = Range.closed(START_DAY, END_DAY);
                private final LocalDateTime EXPECTED_END_TIME = LocalDateTime.of(
                        LocalDate.of(2021, 3, 2),
                        OPERATING_DAY_END_TIME
                );

                @Test
                @DisplayName("Should return an optional which contains the end time")
                void shouldReturnOptionalWhichContainsEndTime() {
                    final Optional<LocalDateTime> endTime = ValidityPeriodUtil.constructValidityPeriodEndTime(INPUT);
                    assertThat(endTime).isNotEmpty();
                }

                @Test
                @DisplayName("Should return the correct end time")
                void shouldReturnCorrectEndTime() {
                    final Optional<LocalDateTime> endTime = ValidityPeriodUtil.constructValidityPeriodEndTime(INPUT);
                    assertThat(endTime).contains(EXPECTED_END_TIME);
                }
            }

            @Nested
            @DisplayName("When the end day is excluded from the validity period")
            class WhenEndDayIsExcludedFromValidityPeriod {

                private final Range<LocalDate> INPUT = Range.open(START_DAY, END_DAY);
                private final LocalDateTime EXPECTED_END_TIME = LocalDateTime.of(
                        LocalDate.of(2021, 3, 1),
                        OPERATING_DAY_END_TIME
                );

                @Test
                @DisplayName("Should return an optional which contains the end time")
                void shouldReturnOptionalWhichContainsEndTime() {
                    final Optional<LocalDateTime> endTime = ValidityPeriodUtil.constructValidityPeriodEndTime(INPUT);
                    assertThat(endTime).isNotEmpty();
                }

                @Test
                @DisplayName("Should return the correct end time")
                void shouldReturnCorrectEndTime() {
                    final Optional<LocalDateTime> endTime = ValidityPeriodUtil.constructValidityPeriodEndTime(INPUT);
                    assertThat(endTime).contains(EXPECTED_END_TIME);
                }
            }
        }
    }
}

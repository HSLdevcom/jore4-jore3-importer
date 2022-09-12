package fi.hsl.jore.importer.feature.jore4.util;

import com.google.common.collect.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ValidityPeriodUtilTest {

    private static final LocalDate END_DAY = LocalDate.of(2021, 3, 1);
    private static final LocalDate START_DAY = LocalDate.of(2021, 2, 1);

    @Nested
    @DisplayName("Determine validity period start day")
    class DetermineValidityPeriodStartDay {

        @Nested
        @DisplayName("When the validity period doesn't have start day")
        class WhenValidityPeriodDoesNotHaveStartDay {

            @Test
            @DisplayName("Should return an empty optional")
            void shouldReturnEmptyOptional() {
                final Optional<LocalDate> startDay = ValidityPeriodUtil.constructValidityPeriodStartDay(Range.atMost(LocalDate.EPOCH));
                assertThat(startDay).isEmpty();
            }
        }

        @Nested
        @DisplayName("When the validity period has a start day")
        class WhenValidityPeriodHasStartDay {

            @Nested
            @DisplayName("When the start day is included in the validity period")
            class WhenStartDayIsIncludedInValidityPeriod {

                private final Range<LocalDate> INPUT = Range.closed(START_DAY, END_DAY);

                @Test
                @DisplayName("Should return an optional which contains the start day")
                void shouldReturnOptionalWhichContainsStartDay() {
                    final Optional<LocalDate> startDay = ValidityPeriodUtil.constructValidityPeriodStartDay(INPUT);
                    assertThat(startDay).isNotEmpty();
                }

                @Test
                @DisplayName("Should return the correct start day")
                void shouldReturnCorrectStartDay() {
                    final Optional<LocalDate> startDay = ValidityPeriodUtil.constructValidityPeriodStartDay(INPUT);
                    assertThat(startDay).contains(START_DAY);
                }
            }

            @Nested
            @DisplayName("When the start day is excluded from the validity period")
            class WhenStartDayIsExcludedFromValidityPeriod {

                private final Range<LocalDate> INPUT = Range.open(START_DAY, END_DAY);

                @Test
                @DisplayName("Should return an optional which contains the start day")
                void shouldReturnOptionalWhichContainsStartDay() {
                    final Optional<LocalDate> startDay = ValidityPeriodUtil.constructValidityPeriodStartDay(INPUT);
                    assertThat(startDay).isNotEmpty();
                }

                @Test
                @DisplayName("Should return the correct start day")
                void shouldReturnCorrectStartDay() {
                    final Optional<LocalDate> startDay = ValidityPeriodUtil.constructValidityPeriodStartDay(INPUT);
                    assertThat(startDay).contains(START_DAY.plusDays(1));
                }
            }
        }
    }

    @Nested
    @DisplayName("Determine validity period end day")
    class DetermineValidityPeriodEndDay {

        @Nested
        @DisplayName("When the validity period doesn't have end day")
        class WhenValidityPeriodDoesNotHaveEndDay {

            @Test
            @DisplayName("Should return an empty optional")
            void shouldReturnEmptyOptional() {
                final Optional<LocalDate> endDay = ValidityPeriodUtil.constructValidityPeriodEndDay(Range.atLeast(LocalDate.EPOCH));
                assertThat(endDay).isEmpty();
            }
        }

        @Nested
        @DisplayName("When when the validity period has end day")
        class WhenValidityPeriodHasEndDay {

            @Nested
            @DisplayName("When the end day is included in the validity period")
            class WhenEndDayIsIncludedInValidityPeriod {

                private final Range<LocalDate> INPUT = Range.closed(START_DAY, END_DAY);

                @Test
                @DisplayName("Should return an optional which contains the end day")
                void shouldReturnOptionalWhichContainsEndDay() {
                    final Optional<LocalDate> endDay = ValidityPeriodUtil.constructValidityPeriodEndDay(INPUT);
                    assertThat(endDay).isNotEmpty();
                }

                @Test
                @DisplayName("Should return the correct end day")
                void shouldReturnCorrectEndDay() {
                    final Optional<LocalDate> endDay = ValidityPeriodUtil.constructValidityPeriodEndDay(INPUT);
                    assertThat(endDay).contains(END_DAY);
                }
            }

            @Nested
            @DisplayName("When the end day is excluded from the validity period")
            class WhenEndDayIsExcludedFromValidityPeriod {

                private final Range<LocalDate> INPUT = Range.open(START_DAY, END_DAY);

                @Test
                @DisplayName("Should return an optional which contains the end day")
                void shouldReturnOptionalWhichContainsEndDay() {
                    final Optional<LocalDate> endDay = ValidityPeriodUtil.constructValidityPeriodEndDay(INPUT);
                    assertThat(endDay).isNotEmpty();
                }

                @Test
                @DisplayName("Should return the correct end day")
                void shouldReturnCorrectEndDay() {
                    final Optional<LocalDate> endDay = ValidityPeriodUtil.constructValidityPeriodEndDay(INPUT);
                    assertThat(endDay).contains(END_DAY.minusDays(1));
                }
            }
        }
    }
}

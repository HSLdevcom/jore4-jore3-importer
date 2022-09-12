package fi.hsl.jore.importer.feature.jore4.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class TimestampFactoryTest {

    private static final int ZONE_OFFSET_EET = 2;
    private static final int ZONE_OFFSET_EEST = 3;

    @Nested
    @DisplayName("Create a timestamp with an offset")
    class OffsetDateTimeFromLocalDateTime {

        @ParameterizedTest(name = "When the local time is: {0}, the timestamp with offset should be: {1}")
        @DisplayName("Should return the correct timestamp with an offset")
        @ArgumentsSource(TimestampArgumentsProvider.class)
        void shouldReturnCorrectTimestampWithOffset(final LocalDateTime input, final OffsetDateTime expectedTimestamp) {
            final OffsetDateTime actualTimestamp = TimestampFactory.offsetDateTimeFromLocalDateTime(input);
            assertThat(actualTimestamp).isEqualTo(expectedTimestamp);
        }
    }

    private static class TimestampArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(final ExtensionContext extensionContext) throws Exception {
            //The returned timestamps are valid only for Finland. Other countries
            //might have different DST schedule.
            return Stream.of(
                    //One second before moving to summer time (in 2021)
                    Arguments.of(
                            LocalDateTime.of(LocalDate.of(2021, 3, 28), LocalTime.of(3, 59, 59)),
                            OffsetDateTime.of(
                                    LocalDateTime.of(LocalDate.of(2021, 3, 28), LocalTime.of(3, 59, 59)),
                                    ZoneOffset.ofHours(ZONE_OFFSET_EET)
                            )
                    ),
                    //Summer time in 2021
                    Arguments.of(
                            LocalDateTime.of(LocalDate.of(2021, 3, 28), LocalTime.of(4, 0)),
                            OffsetDateTime.of(
                                    LocalDateTime.of(LocalDate.of(2021, 3, 28), LocalTime.of(4, 0)),
                                    ZoneOffset.ofHours(ZONE_OFFSET_EEST)
                            )
                    ),
                    //One second before moving to winter time (in 2021)
                    Arguments.of(
                            LocalDateTime.of(LocalDate.of(2021, 10, 31), LocalTime.of(3, 59, 59)),
                            OffsetDateTime.of(
                                    LocalDateTime.of(LocalDate.of(2021, 10, 31), LocalTime.of(3, 59, 59)),
                                    ZoneOffset.ofHours(ZONE_OFFSET_EEST)
                            )
                    ),
                    //Winter time in 2021
                    Arguments.of(
                            LocalDateTime.of(LocalDate.of(2021, 10, 31), LocalTime.of(4, 0)),
                            OffsetDateTime.of(
                                    LocalDateTime.of(LocalDate.of(2021, 10, 31), LocalTime.of(4, 0)),
                                    ZoneOffset.ofHours(ZONE_OFFSET_EET)
                            )
                    )
            );
        }
    }
}
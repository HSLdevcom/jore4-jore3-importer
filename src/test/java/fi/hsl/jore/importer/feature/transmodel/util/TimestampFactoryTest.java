package fi.hsl.jore.importer.feature.transmodel.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

    @Nested
    @DisplayName("Create a timestamp with an offset")
    class OffsetDateTimeFromLocalDateTime {

        private final LocalDate LOCAL_DATE = LocalDate.of(2021, 10, 31);
        private final LocalTime LOCAL_TIME = LocalTime.of(4, 0);

        @Test
        @DisplayName("Should return the correct timestamp with an offset")
        void shouldReturnCorrectTimestampWithOffset() {
            final LocalDateTime localTimestamp = LocalDateTime.of(LOCAL_DATE, LOCAL_TIME);
            final OffsetDateTime actualTimestamp = TimestampFactory.offsetDateTimeFromLocalDateTime(localTimestamp);
            assertThat(actualTimestamp).isEqualTo(
                    OffsetDateTime.of(
                            localTimestamp,
                            ZoneOffset.systemDefault().getRules().getOffset(localTimestamp)
                    )
            );
        }
    }
}


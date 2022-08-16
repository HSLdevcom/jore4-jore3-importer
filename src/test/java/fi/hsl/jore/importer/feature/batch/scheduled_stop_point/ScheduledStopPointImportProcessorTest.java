package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;


import fi.hsl.jore.importer.feature.jore3.entity.JrScheduledStopPoint;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ImportableScheduledStopPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduledStopPointImportProcessorTest {

    private static final String EXTERNAL_ID = "9899";
    private static final Long ELY_NUMBER = 1234567L;
    private static final String IMPORTER_SHORT_ID = "H1234";
    private static final String JORE3_SHORT_ID = "1234";
    private static final String JORE3_SHORT_LETTER = "H";
    private static final String FINNISH_NAME = "";
    private static final String SWEDISH_NAME = "";
    private static final Integer USAGE_IN_ROUTES = 1;

    private final ScheduledStopPointImportProcessor processor = new ScheduledStopPointImportProcessor();

    @Nested
    @DisplayName("Process")
    class Process {

        private final JrScheduledStopPoint input = JrScheduledStopPoint.of(
                NodeId.of(EXTERNAL_ID),
                Optional.of(ELY_NUMBER),
                Optional.of(FINNISH_NAME),
                Optional.of(SWEDISH_NAME),
                Optional.of(JORE3_SHORT_ID),
                Optional.of(JORE3_SHORT_LETTER),
                Optional.of(USAGE_IN_ROUTES)
        );

        @Test
        @DisplayName("Should return a scheduled stop point with the correct external id")
        void shouldReturnScheduledStopPointWithCorrectExternalId() throws Exception {
            final ImportableScheduledStopPoint returned = processor.process(input);
            assertThat(returned.externalId().value())
                    .as("externalId")
                    .isEqualTo(EXTERNAL_ID);
        }

        @Test
        @DisplayName("Should return a scheduled stop point with the correct ely number")
        void shouldReturnScheduledStopPointWithCorrectElyNumber() throws Exception {
            final ImportableScheduledStopPoint returned = processor.process(input);
            assertThat(returned.elyNumber())
                    .as("elyNumber")
                    .contains(ELY_NUMBER);
        }

        @Test
        @DisplayName("Should return a scheduled stop point with the correct finnish name")
        void shouldReturnScheduledStopPointWithCorrectFinnishName() throws Exception {
            final ImportableScheduledStopPoint returned = processor.process(input);
            final String finnishName = JoreLocaleUtil.getI18nString(returned.name(), JoreLocaleUtil.FINNISH);
            assertThat(finnishName)
                    .as("finnishName")
                    .isEqualTo(FINNISH_NAME);
        }

        @Test
        @DisplayName("Should return a scheduled stop point with the correct swedish name")
        void shouldReturnScheduledStopPointWithCorrectSwedishName() throws Exception {
            final ImportableScheduledStopPoint returned = processor.process(input);
            final String swedishName = JoreLocaleUtil.getI18nString(returned.name(), JoreLocaleUtil.SWEDISH);
            assertThat(swedishName)
                    .as("swedishName")
                    .isEqualTo(SWEDISH_NAME);
        }

        @Nested
        @DisplayName("When the short id and short letter aren't set")
        class WhenShortIdAndShortLetterAreEmpty {

            private final JrScheduledStopPoint input = JrScheduledStopPoint.of(
                    NodeId.of(EXTERNAL_ID),
                    Optional.of(ELY_NUMBER),
                    Optional.of(FINNISH_NAME),
                    Optional.of(SWEDISH_NAME),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.of(USAGE_IN_ROUTES)
            );

            @Test
            @DisplayName("Should return a scheduled stop point with empty shortId")
            void shouldReturnScheduledStopPointWithEmptyShortId() throws Exception {
                final ImportableScheduledStopPoint returned = processor.process(input);
                assertThat(returned.shortId())
                        .as("shortId")
                        .isEmpty();
            }
        }

        @Nested
        @DisplayName("When the short id is empty")
        class WhenShortIdIsEmpty {

            private final JrScheduledStopPoint input = JrScheduledStopPoint.of(
                    NodeId.of(EXTERNAL_ID),
                    Optional.of(ELY_NUMBER),
                    Optional.of(FINNISH_NAME),
                    Optional.of(SWEDISH_NAME),
                    Optional.empty(),
                    Optional.of(JORE3_SHORT_LETTER),
                    Optional.of(USAGE_IN_ROUTES)
            );

            @Test
            @DisplayName("Should return a scheduled stop point with the correct shortId")
            void shouldReturnScheduledStopPointWithCorrectShortId() throws Exception {
                final ImportableScheduledStopPoint returned = processor.process(input);
                assertThat(returned.shortId())
                        .as("shortId")
                        .contains(JORE3_SHORT_LETTER);
            }
        }

        @Nested
        @DisplayName("When the short letter is empty")
        class WhenShortLetterIsEmpty {

            private final JrScheduledStopPoint input = JrScheduledStopPoint.of(
                    NodeId.of(EXTERNAL_ID),
                    Optional.of(ELY_NUMBER),
                    Optional.of(FINNISH_NAME),
                    Optional.of(SWEDISH_NAME),
                    Optional.of(JORE3_SHORT_ID),
                    Optional.empty(),
                    Optional.of(USAGE_IN_ROUTES)
            );

            @Test
            @DisplayName("Should return a scheduled stop point with the correct shortId")
            void shouldReturnScheduledStopPointWithCorrectShortId() throws Exception {
                final ImportableScheduledStopPoint returned = processor.process(input);
                assertThat(returned.shortId())
                        .as("shortId")
                        .contains(JORE3_SHORT_ID);
            }
        }

        @Nested
        @DisplayName("When the short id and short letter are set")
        class WhenShortIdAndShortLetterAreSet {

            private final JrScheduledStopPoint input = JrScheduledStopPoint.of(
                    NodeId.of(EXTERNAL_ID),
                    Optional.of(ELY_NUMBER),
                    Optional.of(FINNISH_NAME),
                    Optional.of(SWEDISH_NAME),
                    Optional.of(JORE3_SHORT_ID),
                    Optional.of(JORE3_SHORT_LETTER),
                    Optional.of(USAGE_IN_ROUTES)
            );

            @Test
            @DisplayName("Should return a scheduled stop point with the correct shortId")
            void shouldReturnScheduledStopPointWithCorrectShortId() throws Exception {
                final ImportableScheduledStopPoint returned = processor.process(input);
                assertThat(returned.shortId())
                        .as("shortId")
                        .contains(IMPORTER_SHORT_ID);
            }
        }
    }
}

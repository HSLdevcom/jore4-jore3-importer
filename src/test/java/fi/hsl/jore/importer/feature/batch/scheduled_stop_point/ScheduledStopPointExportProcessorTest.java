package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.digiroad.service.DigiroadStopService;
import fi.hsl.jore.importer.feature.digiroad.service.TestCsvDigiroadStopServiceFactory;
import fi.hsl.jore.importer.feature.jore3.util.JoreGeometryUtil;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPoint;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPointDirection;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ImporterScheduledStopPoint;
import io.vavr.collection.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScheduledStopPointExportProcessorTest {

    private static final Long ELY_NUMBER = 1234567890L;
    private static final long UNKNOWN_ELY_NUMBER = 999999L;
    private static final String DIGIROAD_STOP_INFRA_LINK_ID = "133202";

    private static final String IMPORTER_SHORT_ID = "H1234";

    private static final String JORE_3_STOP_EXTERNAL_ID = "1234567";
    private static final String JORE_3_STOP_FINNISH_NAME = "Ullanmäki (Jore3)";
    private static final String JORE_3_STOP_SWEDISH_NAME = "Ullasbacken (Jore3)";
    private static final double JORE_3_STOP_X_COORDINATE = 25.696376131;
    private static final double JORE_3_STOP_Y_COORDINATE = 61.207149801;

    private static final int PRIORITY = 10;
    private static final LocalDate VALIDITY_PERIOD_START = LocalDate.of(1990, 1, 1);
    private static final LocalDate VALIDITY_PERIOD_END = LocalDate.of(2051, 1, 1);

    private final Jore4ScheduledStopPointDirection JORE4_STOP_POINT_DIRECTION_ON_INFRA_LINK = Jore4ScheduledStopPointDirection.BACKWARD;

    private ScheduledStopPointExportProcessor processor;

    @BeforeAll
    void configureSystemUnderTest() throws Exception {
        final DigiroadStopService digiroadStopService = TestCsvDigiroadStopServiceFactory.create();
        processor = new ScheduledStopPointExportProcessor(digiroadStopService);
    }

    @Nested
    @DisplayName("When the source stop has one ely number")
    class WhenSourceStopHasOneElyNumber {

        @Nested
        @DisplayName("When no Digiroad stop is found with the ely number of the source stop")
        class WhenNoDigiroadStopIsFoundWithElyNumberOfSourceStop {

            private final ImporterScheduledStopPoint jore3Stop = ImporterScheduledStopPoint.of(
                    List.of(ExternalId.of(JORE_3_STOP_EXTERNAL_ID)),
                    List.of(UNKNOWN_ELY_NUMBER),
                    JoreGeometryUtil.fromDbCoordinates(JORE_3_STOP_Y_COORDINATE, JORE_3_STOP_X_COORDINATE),
                    JoreLocaleUtil.createMultilingualString(JORE_3_STOP_FINNISH_NAME, JORE_3_STOP_SWEDISH_NAME),
                    Optional.of(IMPORTER_SHORT_ID)
            );

            @Test
            @DisplayName("Should return null")
            void shouldReturnNull() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output).isNull();
            }
        }

        @Nested
        @DisplayName("When a Digiroad stop is found with the ely number of the source stop")
        class WhenDigiroadStopIsFoundWithElyNumberOfSourceStop {

            private final ImporterScheduledStopPoint jore3Stop = ImporterScheduledStopPoint.of(
                    List.of(ExternalId.of(JORE_3_STOP_EXTERNAL_ID)),
                    List.of(ELY_NUMBER),
                    JoreGeometryUtil.fromDbCoordinates(JORE_3_STOP_Y_COORDINATE, JORE_3_STOP_X_COORDINATE),
                    JoreLocaleUtil.createMultilingualString(JORE_3_STOP_FINNISH_NAME, JORE_3_STOP_SWEDISH_NAME),
                    Optional.of(IMPORTER_SHORT_ID)
            );

            @Test
            @DisplayName("Should return a scheduled stop point with a generated id")
            void shouldReturnScheduledStopPointWithGeneratedId() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.scheduledStopPointId()).isNotNull();
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct external stop id")
            void shouldReturnScheduledStopPointWithCorrectExternalStopId() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.externalScheduledStopPointId()).isEqualTo(JORE_3_STOP_EXTERNAL_ID);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct external id of infrastructure link")
            void shouldReturnScheduledStopPointWithCorrectExternalInfrastructureLinkId() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.externalInfrastructureLinkId()).isEqualTo(DIGIROAD_STOP_INFRA_LINK_ID);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct stop direction")
            void shouldReturnScheduledStopPointWithCorrectStopDirection() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.directionOnInfraLink()).isEqualTo(JORE4_STOP_POINT_DIRECTION_ON_INFRA_LINK);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct label")
            void shouldReturnScheduledStopPointWithCorrectLabel() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.label()).isEqualTo(IMPORTER_SHORT_ID);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct X coordinate")
            void shouldReturnScheduledStopPointWithCorrectXCoordinate() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.measuredLocation().getX()).isEqualTo(JORE_3_STOP_X_COORDINATE);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct Y coordinate")
            void shouldReturnScheduledStopPointWithCorrectYCoordinate() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.measuredLocation().getY()).isEqualTo(JORE_3_STOP_Y_COORDINATE);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct priority")
            void shouldReturnScheduledStopPointWithCorrectPriority() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.priority()).isEqualTo(PRIORITY);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct validity period start time")
            void shouldReturnScheduledStopPointWithCorrectValidityPeriodStartTime() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.validityStart()).contains(VALIDITY_PERIOD_START);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct validity period end time")
            void shouldReturnScheduledStopPointWithCorrectValidityPeriodEndTime() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.validityEnd()).contains(VALIDITY_PERIOD_END);
            }
        }
    }

    @Nested
    @DisplayName("When the source stop has two ely numbers")
    class WhenSourceStopHasTwoElyNumbers {

        @Nested
        @DisplayName("When a Digiroad stop is found with the secoond ely number of the source stop")
        class WhenDigiroadStopIsFoundWithSecondElyNumberOfSourceStop {

            private static final String UNKNOWN_EXTERNAL_ID = "UNKNOWN";

            private final ImporterScheduledStopPoint jore3Stop = ImporterScheduledStopPoint.of(
                    List.of(ExternalId.of(UNKNOWN_EXTERNAL_ID), ExternalId.of(JORE_3_STOP_EXTERNAL_ID)),
                    List.of(UNKNOWN_ELY_NUMBER, ELY_NUMBER),
                    JoreGeometryUtil.fromDbCoordinates(JORE_3_STOP_Y_COORDINATE, JORE_3_STOP_X_COORDINATE),
                    JoreLocaleUtil.createMultilingualString(JORE_3_STOP_FINNISH_NAME, JORE_3_STOP_SWEDISH_NAME),
                    Optional.of(IMPORTER_SHORT_ID)
            );

            @Test
            @DisplayName("Should return a scheduled stop point with a generated id")
            void shouldReturnScheduledStopPointWithGeneratedId() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.scheduledStopPointId()).isNotNull();
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct external stop id")
            void shouldReturnScheduledStopPointWithCorrectExternalStopId() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.externalScheduledStopPointId()).isEqualTo(JORE_3_STOP_EXTERNAL_ID);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct external id of infrastructure link")
            void shouldReturnScheduledStopPointWithCorrectExternalInfrastructureLinkId() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.externalInfrastructureLinkId()).isEqualTo(DIGIROAD_STOP_INFRA_LINK_ID);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct stop direction")
            void shouldReturnScheduledStopPointWithCorrectStopDirection() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.directionOnInfraLink()).isEqualTo(JORE4_STOP_POINT_DIRECTION_ON_INFRA_LINK);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct label")
            void shouldReturnScheduledStopPointWithCorrectLabel() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.label()).isEqualTo(IMPORTER_SHORT_ID);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct X coordinate")
            void shouldReturnScheduledStopPointWithCorrectXCoordinate() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.measuredLocation().getX()).isEqualTo(JORE_3_STOP_X_COORDINATE);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct Y coordinate")
            void shouldReturnScheduledStopPointWithCorrectYCoordinate() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.measuredLocation().getY()).isEqualTo(JORE_3_STOP_Y_COORDINATE);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct priority")
            void shouldReturnScheduledStopPointWithCorrectPriority() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.priority()).isEqualTo(PRIORITY);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct validity period start time")
            void shouldReturnScheduledStopPointWithCorrectValidityPeriodStartTime() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.validityStart()).contains(VALIDITY_PERIOD_START);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct validity period end time")
            void shouldReturnScheduledStopPointWithCorrectValidityPeriodEndTime() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.validityEnd()).contains(VALIDITY_PERIOD_END);
            }
        }
    }
}

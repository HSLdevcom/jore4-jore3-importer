package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.digiroad.service.DigiroadStopService;
import fi.hsl.jore.importer.feature.digiroad.service.TestCsvDigiroadStopServiceFactory;
import fi.hsl.jore.importer.feature.jore3.util.JoreGeometryUtil;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ExportableScheduledStopPoint;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPoint;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPointDirection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_END_TIME;
import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_START_TIME;
import static fi.hsl.jore.importer.feature.transmodel.util.TimestampFactory.offsetDateTimeFromLocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScheduledStopPointExportProcessorTest {

    private static final String ELY_NUMBER = "1234567890";

    private static final String DIGIROAD_STOP_INFRA_LINK_ID = "133202";

    private static final String IMPORTER_SHORT_ID = "H1234";

    private static final String JORE_3_STOP_EXTERNAL_ID = "1234567";
    private static final String JORE_3_STOP_FINNISH_NAME = "Ullanmäki (Jore3)";
    private static final String JORE_3_STOP_SWEDISH_NAME = "Ullasbacken (Jore3)";
    private static final double JORE_3_STOP_X_COORDINATE = 25.696376131;
    private static final double JORE_3_STOP_Y_COORDINATE = 61.207149801;

    private static final int PRIORITY = 10;
    private static final LocalDateTime VALIDITY_PERIOD_START_TIME = LocalDateTime.of(
            LocalDate.of(1990, 1, 1),
            OPERATING_DAY_START_TIME
    );
    private static final LocalDateTime VALIDITY_PERIOD_END_TIME = LocalDateTime.of(
            LocalDate.of(2051, 1, 1),
            OPERATING_DAY_END_TIME
    );

    private final TransmodelScheduledStopPointDirection TRANSMODEL_STOP_POINT_DIRECTION_ON_INFRA_LINK = TransmodelScheduledStopPointDirection.BACKWARD;

    private ScheduledStopPointExportProcessor processor;

    @BeforeAll
    void configureSystemUnderTest() throws Exception {
        final DigiroadStopService digiroadStopService = TestCsvDigiroadStopServiceFactory.create();
        processor = new ScheduledStopPointExportProcessor(digiroadStopService);
    }

    @Nested
    @DisplayName("When the source stop has no ely number")
    class WhenSourceStopHasNoElyNumber {

        @Nested
        @DisplayName("When the ely number is missing")
        class WhenElyNumberIsMissing {

            private final ExportableScheduledStopPoint jore3Stop = ExportableScheduledStopPoint.of(
                    ExternalId.of(JORE_3_STOP_EXTERNAL_ID),
                    Optional.empty(),
                    JoreGeometryUtil.fromDbCoordinates(JORE_3_STOP_Y_COORDINATE, JORE_3_STOP_X_COORDINATE),
                    JoreLocaleUtil.createMultilingualString(JORE_3_STOP_FINNISH_NAME, JORE_3_STOP_SWEDISH_NAME),
                    Optional.of(IMPORTER_SHORT_ID)
            );

            @Test
            @DisplayName("Should return null")
            void shouldReturnNull() throws Exception {
                final TransmodelScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output).isNull();
            }
        }

        @Nested
        @DisplayName("When the ely number is an empty string")
        class WhenElyNumberIsEmptyString {

            private final ExportableScheduledStopPoint jore3Stop = ExportableScheduledStopPoint.of(
                    ExternalId.of(JORE_3_STOP_EXTERNAL_ID),
                    Optional.of(""),
                    JoreGeometryUtil.fromDbCoordinates(JORE_3_STOP_Y_COORDINATE, JORE_3_STOP_X_COORDINATE),
                    JoreLocaleUtil.createMultilingualString(JORE_3_STOP_FINNISH_NAME, JORE_3_STOP_SWEDISH_NAME),
                    Optional.of(IMPORTER_SHORT_ID)
            );

            @Test
            @DisplayName("Should return null")
            void shouldReturnNull() throws Exception {
                final TransmodelScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output).isNull();
            }
        }

        @Nested
        @DisplayName("When the ely number contains only white space")
        class WhenElyNumberContainsOnlyWhiteSpace {

            private final ExportableScheduledStopPoint jore3Stop = ExportableScheduledStopPoint.of(
                    ExternalId.of(JORE_3_STOP_EXTERNAL_ID),
                    Optional.of("     "),
                    JoreGeometryUtil.fromDbCoordinates(JORE_3_STOP_Y_COORDINATE, JORE_3_STOP_X_COORDINATE),
                    JoreLocaleUtil.createMultilingualString(JORE_3_STOP_FINNISH_NAME, JORE_3_STOP_SWEDISH_NAME),
                    Optional.of(IMPORTER_SHORT_ID)
            );

            @Test
            @DisplayName("Should return null")
            void shouldReturnNull() throws Exception {
                final TransmodelScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output).isNull();
            }
        }
    }

    @Nested
    @DisplayName("When no Digiroad stop is found with the ely number of the source stop")
    class WhenNoDigiroadStopIsFoundWithElyNumberOfSourceStop {

        private static final String UNKNOWN_ELY_NUMBER = "999999";

        private final ExportableScheduledStopPoint jore3Stop = ExportableScheduledStopPoint.of(
                ExternalId.of(JORE_3_STOP_EXTERNAL_ID),
                Optional.of(UNKNOWN_ELY_NUMBER),
                JoreGeometryUtil.fromDbCoordinates(JORE_3_STOP_Y_COORDINATE, JORE_3_STOP_X_COORDINATE),
                JoreLocaleUtil.createMultilingualString(JORE_3_STOP_FINNISH_NAME, JORE_3_STOP_SWEDISH_NAME),
                Optional.of(IMPORTER_SHORT_ID)
        );

        @Test
        @DisplayName("Should return null")
        void shouldReturnNull() throws Exception {
            final TransmodelScheduledStopPoint output = processor.process(jore3Stop);
            assertThat(output).isNull();
        }
    }

    @Nested
    @DisplayName("When a Digiroad stop is found with the ely number of the source stop")
    class WhenDigiroadStopIsFoundWithElyNumberOfSourceStop {

        private final ExportableScheduledStopPoint jore3Stop = ExportableScheduledStopPoint.of(
                ExternalId.of(JORE_3_STOP_EXTERNAL_ID),
                Optional.of(ELY_NUMBER),
                JoreGeometryUtil.fromDbCoordinates(JORE_3_STOP_Y_COORDINATE, JORE_3_STOP_X_COORDINATE),
                JoreLocaleUtil.createMultilingualString(JORE_3_STOP_FINNISH_NAME, JORE_3_STOP_SWEDISH_NAME),
                Optional.of(IMPORTER_SHORT_ID)
        );

        @Test
        @DisplayName("Should return a scheduled stop point with a generated id")
        void shouldReturnScheduledStopPointWithGeneratedId() throws Exception {
            final TransmodelScheduledStopPoint output = processor.process(jore3Stop);
            assertThat(output.scheduledStopPointId()).isNotNull();
        }

        @Test
        @DisplayName("Should return a scheduled stop point with the correct external stop id")
        void shouldReturnScheduledStopPointWithCorrectExternalStopId() throws Exception {
            final TransmodelScheduledStopPoint output = processor.process(jore3Stop);
            assertThat(output.externalScheduledStopPointId()).isEqualTo(JORE_3_STOP_EXTERNAL_ID);
        }

        @Test
        @DisplayName("Should return a scheduled stop point with the correct external infrastructure link id")
        void shouldReturnScheduledStopPointWithCorrectExternalInfrastructureLinkId() throws Exception {
            final TransmodelScheduledStopPoint output = processor.process(jore3Stop);
            assertThat(output.externalInfrastructureLinkId()).isEqualTo(DIGIROAD_STOP_INFRA_LINK_ID);
        }

        @Test
        @DisplayName("Should return a scheduled stop point with the correct stop direction")
        void shouldReturnScheduledStopPointWithCorrectStopDirection() throws Exception {
            final TransmodelScheduledStopPoint output = processor.process(jore3Stop);
            assertThat(output.directionOnInfraLink()).isEqualTo(TRANSMODEL_STOP_POINT_DIRECTION_ON_INFRA_LINK);
        }

        @Test
        @DisplayName("Should return a scheduled stop point with the correct label")
        void shouldReturnScheduledStopPointWithCorrectLabel() throws Exception {
            final TransmodelScheduledStopPoint output = processor.process(jore3Stop);
            assertThat(output.label()).isEqualTo(IMPORTER_SHORT_ID);
        }

        @Test
        @DisplayName("Should return a scheduled stop point with the correct X coordinate")
        void shouldReturnScheduledStopPointWithCorrectXCoordinate() throws Exception {
            final TransmodelScheduledStopPoint output = processor.process(jore3Stop);
            assertThat(output.measuredLocation().getX()).isEqualTo(JORE_3_STOP_X_COORDINATE);
        }

        @Test
        @DisplayName("Should return a scheduled stop point with the correct Y coordinate")
        void shouldReturnScheduledStopPointWithCorrectYCoordinate() throws Exception {
            final TransmodelScheduledStopPoint output = processor.process(jore3Stop);
            assertThat(output.measuredLocation().getY()).isEqualTo(JORE_3_STOP_Y_COORDINATE);
        }

        @Test
        @DisplayName("Should return a scheduled stop point with the correct priority")
        void shouldReturnScheduledStopPointWithCorrectPriority() throws Exception {
            final TransmodelScheduledStopPoint output = processor.process(jore3Stop);
            assertThat(output.priority()).isEqualTo(PRIORITY);
        }

        @Test
        @DisplayName("Should return a scheduled stop point with the correct validity period start time")
        void shouldReturnScheduledStopPointWithCorrectValidityPeriodStartTime() throws Exception {
            final TransmodelScheduledStopPoint output = processor.process(jore3Stop);
            assertThat(output.validityStart()).contains(offsetDateTimeFromLocalDateTime(VALIDITY_PERIOD_START_TIME));
        }

        @Test
        @DisplayName("Should return a scheduled stop point with the correct validity period end time")
        void shouldReturnScheduledStopPointWithCorrectValidityPeriodEndTime() throws Exception {
            final TransmodelScheduledStopPoint output = processor.process(jore3Stop);
            assertThat(output.validityEnd()).contains(offsetDateTimeFromLocalDateTime(VALIDITY_PERIOD_END_TIME));
        }
    }
}

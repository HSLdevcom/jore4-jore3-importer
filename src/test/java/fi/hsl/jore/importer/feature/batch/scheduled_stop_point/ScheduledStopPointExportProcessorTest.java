package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.digiroad.service.DigiroadStopService;
import fi.hsl.jore.importer.feature.digiroad.service.TestCsvDigiroadStopServiceFactory;
import fi.hsl.jore.importer.feature.jore3.util.JoreGeometryUtil;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ExportableScheduledStopPoint;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPoint;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPointDirection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScheduledStopPointExportProcessorTest {

    private final String LANGUAGE_CODE_FINNISH = "fi_FI";
    private final String LANGUAGE_CODE_SWEDISH = "sv_SE";

    private final String ELY_NUMBER = "1234567890";

    private final String DIGIROAD_STOP_INFRA_LINK_ID = "133202";

    private final String JORE_3_STOP_EXTERNAL_ID = "1234567";
    private final String JORE_3_STOP_FINNISH_NAME = "Ullanmäki (Jore3)";
    private final String JORE_3_STOP_SWEDISH_NAME = "Ullasbacken (Jore3)";
    private final double JORE_3_STOP_X_COORDINATE = 25.696376131;
    private final double JORE_3_STOP_Y_COORDINATE = 61.207149801;

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

        private final ExportableScheduledStopPoint jore3Stop = ExportableScheduledStopPoint.of(
                ExternalId.of(JORE_3_STOP_EXTERNAL_ID),
                Optional.empty(),
                JoreGeometryUtil.fromDbCoordinates(JORE_3_STOP_Y_COORDINATE, JORE_3_STOP_X_COORDINATE),
                MultilingualString.of(Map.of(
                        LANGUAGE_CODE_FINNISH,
                        JORE_3_STOP_FINNISH_NAME,
                        LANGUAGE_CODE_SWEDISH,
                        JORE_3_STOP_SWEDISH_NAME
                ))
        );

        @Test
        @DisplayName("Should return null")
        void shouldReturnNull() throws Exception {
            final TransmodelScheduledStopPoint output = processor.process(jore3Stop);
            assertThat(output).isNull();
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
                MultilingualString.of(Map.of(
                        LANGUAGE_CODE_FINNISH,
                        JORE_3_STOP_FINNISH_NAME,
                        LANGUAGE_CODE_SWEDISH,
                        JORE_3_STOP_SWEDISH_NAME
                ))
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
                MultilingualString.of(Map.of(
                        LANGUAGE_CODE_FINNISH,
                        JORE_3_STOP_FINNISH_NAME,
                        LANGUAGE_CODE_SWEDISH,
                        JORE_3_STOP_SWEDISH_NAME
                ))
        );

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
            assertThat(output.label()).isEqualTo(JORE_3_STOP_FINNISH_NAME);
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
    }
}

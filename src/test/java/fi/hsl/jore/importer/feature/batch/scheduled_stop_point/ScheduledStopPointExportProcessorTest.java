package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.digiroad.service.DigiroadStopService;
import fi.hsl.jore.importer.feature.digiroad.service.TestCsvDigiroadStopServiceFactory;
import fi.hsl.jore.importer.feature.jore3.util.JoreGeometryUtil;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ClosestInfrastructureLink;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPoint;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPointDirection;
import fi.hsl.jore.importer.feature.jore4.entity.VehicleMode;
import fi.hsl.jore.importer.feature.jore4.repository.IJore4InfrastructureNetworkRepository;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ImporterScheduledStopPoint;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.locationtech.jts.geom.Point;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScheduledStopPointExportProcessorTest {

    // Digiroad stops found from src/test/resources/csv/digiroad_stops.csv
    private static final long BUS_ELY_NUMBER = 1234567890L; // [2]
    private static final long TRAM_AND_BUS_ELY_NUMBER = 2000000001L; // [1, 2]
    private static final long TRAM_ELY_NUMBER = 2000000002L; // [1]
    private static final long BUS_WITHOUT_BRACKETS_ELY_NUMBER = 2000000003L; // 2
    private static final long UNKNOWN_ELY_NUMBER = 999999L;

    private static final String DIGIROAD_STOP_INFRA_LINK_ID = "133202";
    private static final String GENERIC_BUS = "generic_bus";
    private static final String GENERIC_TRAM = "generic_tram";

    private static final UUID BUS_LINK_ID = UUID.fromString("554c63e6-87b2-4dc8-a032-b6b0e2607696");
    private static final UUID TRAM_LINK_ID = UUID.fromString("a1b2c3d4-0000-4000-8000-000000000001");
    private static final String TRAM_EXTERNAL_LINK_ID = "tram-1";

    private static final String IMPORTER_SHORT_ID = "H1234";

    private static final String JORE_3_STOP_EXTERNAL_ID = "1234567";
    private static final String JORE_3_STOP_FINNISH_NAME = "Ullanmäki (Jore3)";
    private static final String JORE_3_STOP_SWEDISH_NAME = "Ullasbacken (Jore3)";
    private static final double JORE_3_STOP_X_COORDINATE = 25.696376131;
    private static final double JORE_3_STOP_Y_COORDINATE = 61.207149801;
    private static final String JORE_3_STOP_PLACE_EXT_ID = "1ELIEL";

    private static final int PRIORITY = 10;
    private static final LocalDate VALIDITY_PERIOD_START = LocalDate.of(1990, 1, 1);
    private static final LocalDate VALIDITY_PERIOD_END = LocalDate.of(2051, 1, 1);

    private static final Jore4ScheduledStopPointDirection DIGIROAD_DIRECTION = Jore4ScheduledStopPointDirection.FORWARD;

    private DigiroadStopService digiroadStopService;
    private IJore4InfrastructureNetworkRepository repository;
    private ScheduledStopPointExportProcessor processor;

    @BeforeAll
    void createDigiroadStopService() throws Exception {
        digiroadStopService = TestCsvDigiroadStopServiceFactory.create();
    }

    @BeforeEach
    void configureSystemUnderTest() {
        repository = mock(IJore4InfrastructureNetworkRepository.class);
        processor = new ScheduledStopPointExportProcessor(digiroadStopService, repository);
    }

    private static Point jore3Location() {
        return JoreGeometryUtil.fromDbCoordinates(JORE_3_STOP_Y_COORDINATE, JORE_3_STOP_X_COORDINATE);
    }

    private static ImporterScheduledStopPoint jore3Stop(final long... elyNumbers) {
        final List<Long> elys = Arrays.stream(elyNumbers).boxed().toList();
        final List<ExternalId> externalIds =
                elys.stream().map(ely -> ExternalId.of(JORE_3_STOP_EXTERNAL_ID)).toList();
        return ImporterScheduledStopPoint.of(
                externalIds,
                elys,
                jore3Location(),
                JoreLocaleUtil.createMultilingualString(JORE_3_STOP_FINNISH_NAME, JORE_3_STOP_SWEDISH_NAME),
                Optional.of(IMPORTER_SHORT_ID),
                Optional.of(JORE_3_STOP_PLACE_EXT_ID));
    }

    private void givenBusLinkExists() {
        given(repository.findLinkIdByExternalId(DIGIROAD_STOP_INFRA_LINK_ID, GENERIC_BUS))
                .willReturn(Optional.of(BUS_LINK_ID));
    }

    private void givenTramLink(final String linkDirection) {
        given(repository.findClosestLink(any(Point.class), eq(GENERIC_TRAM)))
                .willReturn(Optional.of(
                        Jore4ClosestInfrastructureLink.of(TRAM_LINK_ID, TRAM_EXTERNAL_LINK_ID, linkDirection, 12.5)));
    }

    private void givenTramDirection(final Jore4ScheduledStopPointDirection direction) {
        given(repository.findPointDirectionOnLink(any(Point.class), eq(TRAM_LINK_ID)))
                .willReturn(Optional.ofNullable(direction));
    }

    private static void assertBusStop(final Jore4ScheduledStopPoint output) {
        assertThat(output).isNotNull();
        assertThat(output.vehicleMode()).isEqualTo(VehicleMode.BUS);
        assertThat(output.infrastructureLinkId()).isEqualTo(BUS_LINK_ID);
        assertThat(output.externalInfrastructureLinkId()).isEqualTo(DIGIROAD_STOP_INFRA_LINK_ID);
    }

    private static void assertTramStop(
            final Jore4ScheduledStopPoint output, final Jore4ScheduledStopPointDirection expectedDirection) {
        assertThat(output).isNotNull();
        assertThat(output.vehicleMode()).isEqualTo(VehicleMode.TRAM);
        assertThat(output.infrastructureLinkId()).isEqualTo(TRAM_LINK_ID);
        assertThat(output.externalInfrastructureLinkId()).isEqualTo(TRAM_EXTERNAL_LINK_ID);
        assertThat(output.directionOnInfraLink()).isEqualTo(expectedDirection);
    }

    @Nested
    @DisplayName("When the source stop has one ely number")
    class WhenSourceStopHasOneElyNumber {

        @Nested
        @DisplayName("When no Digiroad stop is found with the ely number of the source stop")
        class WhenNoDigiroadStopIsFoundWithElyNumberOfSourceStop {

            @Test
            @DisplayName("Should return null")
            void shouldReturnNull() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop(UNKNOWN_ELY_NUMBER));
                assertThat(output).isNull();
            }
        }

        @Nested
        @DisplayName("When a bus Digiroad stop ([2]) is found with the ely number of the source stop")
        class WhenBusDigiroadStopIsFound {

            private final ImporterScheduledStopPoint jore3Stop = jore3Stop(BUS_ELY_NUMBER);

            @BeforeEach
            void givenLink() {
                givenBusLinkExists();
            }

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
            @DisplayName("Should return a bus stop on the Digiroad link")
            void shouldReturnBusStopOnDigiroadLink() throws Exception {
                assertBusStop(processor.process(jore3Stop));
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the Digiroad stop direction")
            void shouldReturnScheduledStopPointWithCorrectStopDirection() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.directionOnInfraLink()).isEqualTo(DIGIROAD_DIRECTION);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct label")
            void shouldReturnScheduledStopPointWithCorrectLabel() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.label()).isEqualTo(IMPORTER_SHORT_ID);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct coordinates")
            void shouldReturnScheduledStopPointWithCorrectCoordinates() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.measuredLocation().getX()).isEqualTo(JORE_3_STOP_X_COORDINATE);
                assertThat(output.measuredLocation().getY()).isEqualTo(JORE_3_STOP_Y_COORDINATE);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct timing place label")
            void shouldReturnScheduledStopPointWithCorrectTimingPlaceLabel() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.timingPlaceLabel()).contains(JORE_3_STOP_PLACE_EXT_ID);
            }

            @Test
            @DisplayName("Should return a scheduled stop point with the correct priority and validity period")
            void shouldReturnScheduledStopPointWithCorrectPriorityAndValidityPeriod() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);
                assertThat(output.priority()).isEqualTo(PRIORITY);
                assertThat(output.validityStart()).contains(VALIDITY_PERIOD_START);
                assertThat(output.validityEnd()).contains(VALIDITY_PERIOD_END);
            }

            @Test
            @DisplayName("Should not look for a tram link")
            void shouldNotLookForTramLink() throws Exception {
                processor.process(jore3Stop);
                verify(repository, never()).findClosestLink(any(), any());
            }
        }

        @Nested
        @DisplayName("When a bus Digiroad stop without brackets (2) is found")
        class WhenBusDigiroadStopWithoutBracketsIsFound {

            @Test
            @DisplayName("Should return a bus stop")
            void shouldReturnBusStop() throws Exception {
                givenBusLinkExists();
                assertBusStop(processor.process(jore3Stop(BUS_WITHOUT_BRACKETS_ELY_NUMBER)));
            }
        }

        @Nested
        @DisplayName("When the bus link is missing or isn't a generic_bus link")
        class WhenBusLinkIsMissing {

            @Test
            @DisplayName("Should return null")
            void shouldReturnNull() throws Exception {
                assertThat(processor.process(jore3Stop(BUS_ELY_NUMBER))).isNull();
            }
        }

        @Nested
        @DisplayName("When a tram Digiroad stop ([1]) is found")
        class WhenTramDigiroadStopIsFound {

            private final ImporterScheduledStopPoint jore3Stop = jore3Stop(TRAM_ELY_NUMBER);

            @Test
            @DisplayName("Should return a tram stop with the direction resolved from the location")
            void shouldReturnTramStopWithResolvedDirection() throws Exception {
                givenTramLink("bidirectional");
                givenTramDirection(Jore4ScheduledStopPointDirection.FORWARD);

                assertTramStop(processor.process(jore3Stop), Jore4ScheduledStopPointDirection.FORWARD);
            }

            @Test
            @DisplayName("Should use the direction of a one-way link when the direction can't be resolved")
            void shouldUseOneWayLinkDirection() throws Exception {
                givenTramLink("backward");
                givenTramDirection(null);

                assertTramStop(processor.process(jore3Stop), Jore4ScheduledStopPointDirection.BACKWARD);
            }

            @Test
            @DisplayName("Should return null when no direction can be resolved on a bidirectional link")
            void shouldReturnNullWhenNoDirectionOnBidirectionalLink() throws Exception {
                givenTramLink("bidirectional");
                givenTramDirection(null);

                assertThat(processor.process(jore3Stop)).isNull();
            }

            @Test
            @DisplayName("Should return null when no tram link is found")
            void shouldReturnNullWhenNoTramLink() throws Exception {
                assertThat(processor.process(jore3Stop)).isNull();
            }

            @Test
            @DisplayName("Should not use the Digiroad link")
            void shouldNotUseDigiroadLink() throws Exception {
                givenTramLink("bidirectional");
                givenTramDirection(Jore4ScheduledStopPointDirection.FORWARD);

                processor.process(jore3Stop);
                verify(repository, never()).findLinkIdByExternalId(any(), any());
            }
        }

        @Nested
        @DisplayName("When a tram and bus Digiroad stop ([1, 2]) is found")
        class WhenTramAndBusDigiroadStopIsFound {

            private final ImporterScheduledStopPoint jore3Stop = jore3Stop(TRAM_AND_BUS_ELY_NUMBER);

            @BeforeEach
            void givenLink() {
                givenBusLinkExists();
            }

            @Test
            @DisplayName("Should return a tram stop when the tram link resolves")
            void shouldReturnTramStop() throws Exception {
                givenTramLink("bidirectional");
                givenTramDirection(Jore4ScheduledStopPointDirection.BACKWARD);

                assertTramStop(processor.process(jore3Stop), Jore4ScheduledStopPointDirection.BACKWARD);
            }

            @Test
            @DisplayName("Should fall back to bus when no tram link is found")
            void shouldFallBackToBusWhenNoTramLink() throws Exception {
                final Jore4ScheduledStopPoint output = processor.process(jore3Stop);

                assertBusStop(output);
                assertThat(output.directionOnInfraLink()).isEqualTo(Jore4ScheduledStopPointDirection.FORWARD);
            }

            @Test
            @DisplayName("Should fall back to bus when no tram direction can be resolved on a bidirectional link")
            void shouldFallBackToBusWhenNoTramDirection() throws Exception {
                givenTramLink("bidirectional");
                givenTramDirection(null);

                assertBusStop(processor.process(jore3Stop));
            }
        }
    }

    @Nested
    @DisplayName("When the source stop has two ely numbers")
    class WhenSourceStopHasTwoElyNumbers {

        @Test
        @DisplayName("Should use the second ely number when no Digiroad stop is found with the first one")
        void shouldUseSecondElyNumberWhenFirstNotFound() throws Exception {
            givenBusLinkExists();

            final Jore4ScheduledStopPoint output = processor.process(jore3Stop(UNKNOWN_ELY_NUMBER, BUS_ELY_NUMBER));

            assertBusStop(output);
            assertThat(output.directionOnInfraLink()).isEqualTo(DIGIROAD_DIRECTION);
        }

        @Test
        @DisplayName("Should use the second ely number when the first one can't be resolved")
        void shouldUseSecondElyNumberWhenFirstCannotBeResolved() throws Exception {
            givenBusLinkExists();

            // The first stop is a tram-only stop and no tram link is found.
            final Jore4ScheduledStopPoint output = processor.process(jore3Stop(TRAM_ELY_NUMBER, BUS_ELY_NUMBER));

            assertBusStop(output);
        }
    }
}

package fi.hsl.jore.importer.feature.digiroad.service;

import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStop;
import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStopDirection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CsvDigiroadStopServiceTest {


    private CsvDigiroadStopService service;

    @BeforeAll
    void configureSystemUnderTest() throws Exception {
        service = TestCsvDigiroadStopServiceFactory.create();
    }

    @Nested
    @DisplayName("Find by national id")
    class FindByNationalId {

        @Nested
        @DisplayName("When the digiroad stop is found")
        class WhenDigiroadStopIsFound {

            private final String EXPECTED_DIGIROAD_STOP_ID = "111";
            private final String EXPECTED_DIGIROAD_LINK_ID = "133202";
            private final String NATIONAL_ID = "168626";
            private final DigiroadStopDirection EXPECTED_DIRECTION_ON_INFRALINK = DigiroadStopDirection.BACKWARD;
            private final double EXPECTED_X_COORDINATE = 24.696376131;
            private final double EXPECTED_Y_COORDINATE = 60.207149801;
            private final String EXPECTED_FINNISH_NAME = "Ullanmäki";
            private final String EXPECTED_SWEDISH_NAME = "Ullasbacken";

            @Test
            @DisplayName("Should return an optional which contains the parsed stop")
            void shouldReturnOptionalWhichContainsParsedStop() {
                Optional<DigiroadStop> container = service.findByNationalId(NATIONAL_ID);
                assertThat(container).isNotEmpty();
            }

            @Test
            @DisplayName("Should return a stop which has the correct digiroad stop id")
            void shouldReturnStopWhichContainsHasDigiroadStopId() {
                DigiroadStop stop = service.findByNationalId(NATIONAL_ID).get();
                assertThat(stop.digiroadStopId())
                        .as("externalStopId")
                        .isEqualTo(EXPECTED_DIGIROAD_STOP_ID);
            }

            @Test
            @DisplayName("Should return a stop which has the correct digiroadLinkId")
            void shouldReturnStopWhichHasCorrectDigiroadLinkId() {
                DigiroadStop stop = service.findByNationalId(NATIONAL_ID).get();
                assertThat(stop.digiroadLinkId())
                        .as("digiroadLinkId")
                        .isEqualTo(EXPECTED_DIGIROAD_LINK_ID);
            }

            @Test
            @DisplayName("Should return a stop which has the correct nationalId")
            void shouldReturnStopWhichHasCorrectElyNumber() {
                DigiroadStop stop = service.findByNationalId(NATIONAL_ID).get();
                assertThat(stop.nationalId())
                        .as("nationalId")
                        .isEqualTo(NATIONAL_ID);
            }

            @Test
            @DisplayName("Should return a stop which has the correct direction on infra link")
            void shouldReturnStopWhichHasCorrectDirectionOnInfraLink() {
                DigiroadStop stop = service.findByNationalId(NATIONAL_ID).get();
                assertThat(stop.directionOnInfraLink())
                        .as("directionOnInfraLink")
                        .isEqualTo(EXPECTED_DIRECTION_ON_INFRALINK);
            }

            @Test
            @DisplayName("Should return a stop which has the correct X coordinate")
            void shouldReturnStopWhichHasCorrectXCoordinate() {
                DigiroadStop stop = service.findByNationalId(NATIONAL_ID).get();
                assertThat(stop.location().getX())
                        .as("X Coordinate")
                        .isEqualTo(EXPECTED_X_COORDINATE);
            }

            @Test
            @DisplayName("Should return a stop which has the correct Y coordinate")
            void shouldReturnStopWhichHasCorrectYCoordinate() {
                DigiroadStop stop = service.findByNationalId(NATIONAL_ID).get();
                assertThat(stop.location().getY())
                        .as("Y Coordinate")
                        .isEqualTo(EXPECTED_Y_COORDINATE);
            }

            @Test
            @DisplayName("Should return a stop which has the correct Finnish name")
            void shouldReturnStopWhichHasCorrectFinnishName() {
                DigiroadStop stop = service.findByNationalId(NATIONAL_ID).get();
                assertThat(stop.nameFinnish())
                        .as("nameFinnish")
                        .isNotEmpty()
                        .contains(EXPECTED_FINNISH_NAME);
            }

            @Test
            @DisplayName("Should return a stop which has the correct Swedish name")
            void shouldReturnStopWhichHasCorrectSwedishName() {
                DigiroadStop stop = service.findByNationalId(NATIONAL_ID).get();
                assertThat(stop.nameSwedish())
                        .as("nameSwedish")
                        .isNotEmpty()
                        .contains(EXPECTED_SWEDISH_NAME);
            }
        }

        @Nested
        @DisplayName("When no Digiroad stop is found")
        class WhenNoDigiroadStopIsFound {

            private final String UNKNOWN_ELY_NUMBER = "9876543";

            @Test
            @DisplayName("Should return an empty optional")
            void shouldReturnEmptyOptional() {
                Optional<DigiroadStop> container = service.findByNationalId(UNKNOWN_ELY_NUMBER);
                assertThat(container).isEmpty();
            }
        }
    }
}
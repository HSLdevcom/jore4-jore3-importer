package fi.hsl.jore.importer.feature.digiroad.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStop;
import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStopDirection;
import fi.hsl.jore.importer.feature.jore4.entity.VehicleMode;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Parse Digiroad stop from a CSV line")
public class DigiroadStopFactoryTest {

    @Nested
    @DisplayName("Parse vehicle modes from pys_tyyppi")
    class ParseVehicleModes {

        @ParameterizedTest(name = "pys_tyyppi {0} -> {1}")
        @CsvSource({
            "1, TRAM",
            "2, BUS",
            "3, BUS",
            "4, BUS",
            "5, BUS",
            "6, BUS",
            "7, UNKNOWN",
            "8, BUS",
            "9, BUS",
            "10, BUS",
            "11, BUS"
        })
        @DisplayName("Should map each stop type to the correct vehicle mode")
        void shouldMapStopTypeToVehicleMode(final int stopType, final VehicleMode expected) {
            assertThat(DigiroadStopFactory.parseVehicleModes("[" + stopType + "]"))
                    .containsExactly(expected);
        }

        @Test
        @DisplayName("Should parse a list without spaces after commas")
        void shouldParseListWithoutSpaces() {
            assertThat(DigiroadStopFactory.parseVehicleModes("[1,2,7]"))
                    .containsExactly(VehicleMode.TRAM, VehicleMode.BUS, VehicleMode.UNKNOWN);
        }

        @Test
        @DisplayName("Should parse a list with spaces after commas")
        void shouldParseListWithSpaces() {
            assertThat(DigiroadStopFactory.parseVehicleModes("[1, 2, 7]"))
                    .containsExactly(VehicleMode.TRAM, VehicleMode.BUS, VehicleMode.UNKNOWN);
        }

        @ParameterizedTest(name = "pys_tyyppi \"{0}\"")
        @ValueSource(strings = {"1", " 1 ", "[1]", " [1] "})
        @DisplayName("Should parse a single value with or without brackets as a list of one")
        void shouldParseSingleValueWithOrWithoutBrackets(final String value) {
            assertThat(DigiroadStopFactory.parseVehicleModes(value)).containsExactly(VehicleMode.TRAM);
        }

        @ParameterizedTest(name = "pys_tyyppi \"{0}\"")
        @ValueSource(strings = {"", "[]", "[2", "2]", "[2,]", "[,2]", "[a]", "a", "1,2", "[0]", "0", "[12]", "12"})
        @DisplayName("Should throw an exception for an invalid value")
        void shouldThrowExceptionForInvalidValue(final String value) {
            assertThatThrownBy(() -> DigiroadStopFactory.parseVehicleModes(value))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("When the CSV line is invalid")
    class WhenCSVLineIsInvalid {

        @Nested
        @DisplayName("When the CSV line is null")
        class WhenCSVLineIsNull {

            @Test
            @DisplayName("Should throw an exception")
            void shouldThrowException() {
                assertThatThrownBy(() -> DigiroadStopFactory.fromCsvLine(null))
                        .isExactlyInstanceOf(NullPointerException.class);
            }
        }

        @Nested
        @DisplayName("When the column count of the CSV line isn't correct")
        class WhenColumnCountOfCSVLineIsNotCorrect {

            private static final String CSV_LINE = ",,,,,,,";

            @Test
            @DisplayName("Should throw an exception")
            void shouldThrowException() {
                assertThatThrownBy(() -> DigiroadStopFactory.fromCsvLine(CSV_LINE))
                        .isExactlyInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("When the digiroad stop id is empty")
        class WhenDigiroadStopIdIsEmpty {

            private static final String CSV_LINE =
                    ";133202;168626;backward;\"{\"\"type\"\": \"\"Point\"\", \"\"coordinates\"\": [24.696376131, 60.207149801]}\";Ullanmäki;Ullasbacken;[2];digiroad_r_mml";

            @Test
            @DisplayName("Should throw an exception")
            void shouldThrowException() {
                assertThatThrownBy(() -> DigiroadStopFactory.fromCsvLine(CSV_LINE))
                        .isExactlyInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("When the digiroad link id is empty")
        class WhenDigiroadLinkIdIsEmpty {

            private static final String CSV_LINE =
                    "111;;168626;backward;\"{\"\"type\"\": \"\"Point\"\", \"\"coordinates\"\": [24.696376131, 60.207149801]}\";Ullanmäki;Ullasbacken;[2];digiroad_r_mml";

            @Test
            @DisplayName("Should throw an exception")
            void shouldThrowException() {
                assertThatThrownBy(() -> DigiroadStopFactory.fromCsvLine(CSV_LINE))
                        .isExactlyInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("When the national id is empty")
        class WhenNationalIdIsEmpty {

            private static final String CSV_LINE =
                    "111;133202;;backward;\"{\"\"type\"\": \"\"Point\"\", \"\"coordinates\"\": [24.696376131, 60.207149801]}\";Ullanmäki;Ullasbacken;[2];digiroad_r_mml";

            @Test
            @DisplayName("Should throw an exception")
            void shouldThrowException() {
                assertThatThrownBy(() -> DigiroadStopFactory.fromCsvLine(CSV_LINE))
                        .isExactlyInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("When the national id contains nonnumerical characters")
        class WhenNationalIdIsContainsNonNumericalCharacters {

            private static final String CSV_LINE =
                    "111;133202;12d3;backward;\"{\"\"type\"\": \"\"Point\"\", \"\"coordinates\"\": [24.696376131, 60.207149801]}\";Ullanmäki;Ullasbacken;[2];digiroad_r_mml";

            @Test
            @DisplayName("Should throw an exception")
            void shouldThrowException() {
                assertThatThrownBy(() -> DigiroadStopFactory.fromCsvLine(CSV_LINE))
                        .isExactlyInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("When the stop direction is invalid")
        class WhenStopDirectionIsInvalid {

            private static final String CSV_LINE =
                    "111;133202;168626;invalid;\"{\"\"type\"\": \"\"Point\"\", \"\"coordinates\"\": [24.696376131, 60.207149801]}\";Ullanmäki;Ullasbacken;[2];digiroad_r_mml";

            @Test
            @DisplayName("Should throw an exception")
            void shouldThrowException() {
                assertThatThrownBy(() -> DigiroadStopFactory.fromCsvLine(CSV_LINE))
                        .isExactlyInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("When the stop location is invalid")
        class WhenStopLocationIsInvalid {

            private static final String CSV_LINE =
                    "111;133202;168626;backward;\"{\"\"type\"\": \"\"Point\"\", \"\"coordinates\"\": [24.696376131, 60.207149801])\";Ullanmäki;Ullasbacken;[2];digiroad_r_mml";

            @Test
            @DisplayName("Should throw an exception")
            void shouldThrowException() {
                assertThatThrownBy(() -> DigiroadStopFactory.fromCsvLine(CSV_LINE))
                        .isExactlyInstanceOf(RuntimeException.class);
            }
        }
    }

    @Nested
    @DisplayName("When the CSV line is valid")
    class WhenCSVLineIsValid {

        private final String EXPECTED_DIGIROAD_STOP_ID = "111";
        private final String EXPECTED_DIGIROAD_LINK_ID = "133202";
        private final int EXPECTED_NATIONAL_ID = 168626;
        private final DigiroadStopDirection EXPECTED_DIRECTION_ON_INFRALINK = DigiroadStopDirection.BACKWARD;
        private final double EXPECTED_X_COORDINATE = 24.696376131;
        private final double EXPECTED_Y_COORDINATE = 60.207149801;
        private final String EXPECTED_FINNISH_NAME = "Ullanmäki";
        private final String EXPECTED_SWEDISH_NAME = "Ullasbacken";

        @Nested
        @DisplayName("When all values are given")
        class WhenAllValuesAreGiven {

            private static final String CSV_LINE =
                    "111;133202;168626;backward;\"{\"\"type\"\": \"\"Point\"\", \"\"coordinates\"\": [24.696376131, 60.207149801]}\";Ullanmäki;Ullasbacken;[2];digiroad_r_mml";

            @Test
            @DisplayName("Should return an optional which contains the parsed stop")
            void shouldReturnOptionalWhichContainsParsedStop() {
                final Optional<DigiroadStop> container = DigiroadStopFactory.fromCsvLine(CSV_LINE);
                assertThat(container).isNotEmpty();
            }

            @Test
            @DisplayName("Should return a stop which has the correct digiroad stop id")
            void shouldReturnStopWhichHasCorrectDigiroadStopId() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.digiroadStopId()).as("digiroadStopId").isEqualTo(EXPECTED_DIGIROAD_STOP_ID);
            }

            @Test
            @DisplayName("Should return a stop which has the correct digiroadLinkId")
            void shouldReturnStopWhichHasCorrectDigiroadLinkId() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.digiroadLinkId()).as("digiroadLinkId").isEqualTo(EXPECTED_DIGIROAD_LINK_ID);
            }

            @Test
            @DisplayName("Should return a stop which has the correct national id")
            void shouldReturnStopWhichHasCorrectElyNumber() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.nationalId()).as("nationalId").isEqualTo(EXPECTED_NATIONAL_ID);
            }

            @Test
            @DisplayName("Should return a stop which has the correct direction on infra link")
            void shouldReturnStopWhichHasCorrectDirectionOnInfraLink() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.directionOnInfraLink())
                        .as("directionOnInfraLink")
                        .isEqualTo(EXPECTED_DIRECTION_ON_INFRALINK);
            }

            @Test
            @DisplayName("Should return a stop which has the correct X coordinate")
            void shouldReturnStopWhichHasCorrectXCoordinate() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.location().getX()).as("X Coordinate").isEqualTo(EXPECTED_X_COORDINATE);
            }

            @Test
            @DisplayName("Should return a stop which has the correct Y coordinate")
            void shouldReturnStopWhichHasCorrectYCoordinate() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.location().getY()).as("Y Coordinate").isEqualTo(EXPECTED_Y_COORDINATE);
            }

            @Test
            @DisplayName("Should return a stop which has the correct Finnish name")
            void shouldReturnStopWhichHasCorrectFinnishName() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.nameFinnish()).as("nameFinnish").isNotEmpty().contains(EXPECTED_FINNISH_NAME);
            }

            @Test
            @DisplayName("Should return a stop which has the correct Swedish name")
            void shouldReturnStopWhichHasCorrectSwedishName() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.nameSwedish()).as("nameSwedish").isNotEmpty().contains(EXPECTED_SWEDISH_NAME);
            }

            @Test
            @DisplayName("Should return a stop which has the correct vehicle modes")
            void shouldReturnStopWhichHasCorrectVehicleModes() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.vehicleModes()).as("vehicleModes").containsExactly(VehicleMode.BUS);
            }
        }

        @Nested
        @DisplayName("When pys_tyyppi is a single value without brackets")
        class WhenPysTyyppiIsSingleValueWithoutBrackets {

            private static final String CSV_LINE =
                    "111;133202;168626;backward;\"{\"\"type\"\": \"\"Point\"\", \"\"coordinates\"\": [24.696376131, 60.207149801]}\";Ullanmäki;Ullasbacken;1;digiroad_r_mml";

            @Test
            @DisplayName("Should return a stop which has a single vehicle mode")
            void shouldReturnStopWhichHasSingleVehicleMode() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.vehicleModes()).as("vehicleModes").containsExactly(VehicleMode.TRAM);
            }
        }

        @Nested
        @DisplayName("When names are empty strings")
        class WhenNamesAreEmptyStrings {

            private static final String CSV_LINE =
                    "111;133202;168626;backward;\"{\"\"type\"\": \"\"Point\"\", \"\"coordinates\"\": [24.696376131, 60.207149801]}\";;;[2];digiroad_r_mml";

            @Test
            @DisplayName("Should return an optional which contains the parsed stop")
            void shouldReturnOptionalWhichContainsParsedStop() {
                final Optional<DigiroadStop> container = DigiroadStopFactory.fromCsvLine(CSV_LINE);
                assertThat(container).isNotEmpty();
            }

            @Test
            @DisplayName("Should return a stop which has the correct digiroad stop id")
            void shouldReturnStopWhichContainsHasDigiroadStopId() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.digiroadStopId()).as("digiroadStopId").isEqualTo(EXPECTED_DIGIROAD_STOP_ID);
            }

            @Test
            @DisplayName("Should return a stop which has the correct digiroadLinkId")
            void shouldReturnStopWhichHasCorrectDigiroadLinkId() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.digiroadLinkId()).as("digiroadLinkId").isEqualTo(EXPECTED_DIGIROAD_LINK_ID);
            }

            @Test
            @DisplayName("Should return a stop which has the correct national id")
            void shouldReturnStopWhichHasCorrectNationalId() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.nationalId()).as("nationalId").isEqualTo(EXPECTED_NATIONAL_ID);
            }

            @Test
            @DisplayName("Should return a stop which has the correct direction on infra link")
            void shouldReturnStopWhichHasCorrectDirectionOnInfraLink() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.directionOnInfraLink())
                        .as("directionOnInfraLink")
                        .isEqualTo(EXPECTED_DIRECTION_ON_INFRALINK);
            }

            @Test
            @DisplayName("Should return a stop which has the correct X coordinate")
            void shouldReturnStopWhichHasCorrectXCoordinate() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.location().getX()).as("X Coordinate").isEqualTo(EXPECTED_X_COORDINATE);
            }

            @Test
            @DisplayName("Should return a stop which has the correct Y coordinate")
            void shouldReturnStopWhichHasCorrectYCoordinate() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.location().getY()).as("Y Coordinate").isEqualTo(EXPECTED_Y_COORDINATE);
            }

            @Test
            @DisplayName("Should return a stop which has an empty Finnish name")
            void shouldReturnStopWhichHasEmptyFinnishName() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.nameFinnish()).as("nameFinnish").isEmpty();
            }

            @Test
            @DisplayName("Should return a stop which has an empty Swedish name")
            void shouldReturnStopWhichHasEmptySwedishName() {
                final DigiroadStop stop =
                        DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.nameSwedish()).as("nameSwedish").isEmpty();
            }
        }
    }
}

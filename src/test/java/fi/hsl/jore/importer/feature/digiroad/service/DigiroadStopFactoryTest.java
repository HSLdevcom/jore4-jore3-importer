package fi.hsl.jore.importer.feature.digiroad.service;

import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStop;
import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStopDirection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Parse Digiroad stop from a CSV line")
public class DigiroadStopFactoryTest {

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
        @DisplayName("When the external stop id is empty")
        class WhenExternalStopIdIsEmpty {

            private static final String CSV_LINE = ";133202;168626;backward;\"{\"\"type\"\": \"\"Point\"\", \"\"coordinates\"\": [24.696376131, 60.207149801]}\";Ullanmäki;Ullasbacken;digiroad_r";

            @Test
            @DisplayName("Should throw an exception")
            void shouldThrowException() {
                assertThatThrownBy(() -> DigiroadStopFactory.fromCsvLine(CSV_LINE))
                        .isExactlyInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("When the external link id is empty")
        class WhenExternalLinkIdIsEmpty {

            private static final String CSV_LINE = "111;;168626;backward;\"{\"\"type\"\": \"\"Point\"\", \"\"coordinates\"\": [24.696376131, 60.207149801]}\";Ullanmäki;Ullasbacken;digiroad_r";

            @Test
            @DisplayName("Should throw an exception")
            void shouldThrowException() {
                assertThatThrownBy(() -> DigiroadStopFactory.fromCsvLine(CSV_LINE))
                        .isExactlyInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("When the ely number is empty")
        class WhenElyNumberIsEmpty {

            private static final String CSV_LINE = "111;133202;;backward;\"{\"\"type\"\": \"\"Point\"\", \"\"coordinates\"\": [24.696376131, 60.207149801]}\";Ullanmäki;Ullasbacken;digiroad_r";

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

            private static final String CSV_LINE = "111;133202;168626;invalid;\"{\"\"type\"\": \"\"Point\"\", \"\"coordinates\"\": [24.696376131, 60.207149801]}\";Ullanmäki;Ullasbacken;digiroad_r";

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

            private static final String CSV_LINE = "111;133202;168626;backward;\"{\"\"type\"\": \"\"Point\"\", \"\"coordinates\"\": [24.696376131, 60.207149801])\";Ullanmäki;Ullasbacken;digiroad_r";

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

        private final String EXPECTED_EXTERNAL_STOP_ID  = "111";
        private final String EXPECTED_EXTERNAL_LINK_ID = "133202";
        private final String EXPECTED_ELY_NUMBER = "168626";
        private final DigiroadStopDirection EXPECTED_DIRECTION_ON_INFRALINK = DigiroadStopDirection.BACKWARD;
        private final double EXPECTED_X_COORDINATE = 24.696376131;
        private final double EXPECTED_Y_COORDINATE = 60.207149801;
        private final String EXPECTED_FINNISH_NAME = "Ullanmäki";
        private final String EXPECTED_SWEDISH_NAME = "Ullasbacken";

        @Nested
        @DisplayName("When all values are given")
        class WhenAllValuesAreGiven {

            private static final String CSV_LINE = "111;133202;168626;backward;\"{\"\"type\"\": \"\"Point\"\", \"\"coordinates\"\": [24.696376131, 60.207149801]}\";Ullanmäki;Ullasbacken;digiroad_r";

            @Test
            @DisplayName("Should return an optional which contains the parsed stop")
            void shouldReturnOptionalWhichContainsParsedStop() {
                Optional<DigiroadStop> container = DigiroadStopFactory.fromCsvLine(CSV_LINE);
                assertThat(container).isNotEmpty();
            }

            @Test
            @DisplayName("Should return a stop which has the correct external stop id")
            void shouldReturnStopWhichContainsHasExternalStopId() {
                DigiroadStop stop = DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.externalStopId())
                        .as("externalStopId")
                        .isEqualTo(EXPECTED_EXTERNAL_STOP_ID);
            }

            @Test
            @DisplayName("Should return a stop which has the correct externalLinkId")
            void shouldReturnStopWhichHasCorrectExternalLinkId() {
                DigiroadStop stop = DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.externalLinkId())
                        .as("externalLinkId")
                        .isEqualTo(EXPECTED_EXTERNAL_LINK_ID);
            }

            @Test
            @DisplayName("Should return a stop which has the correct ely number")
            void shouldReturnStopWhichHasCorrectElyNumber() {
                DigiroadStop stop = DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.elyNumber())
                        .as("elyNumber")
                        .isEqualTo(EXPECTED_ELY_NUMBER);
            }

            @Test
            @DisplayName("Should return a stop which has the correct direction on infra link")
            void shouldReturnStopWhichHasCorrectDirectionOnInfraLink() {
                DigiroadStop stop = DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.directionOnInfraLink())
                        .as("directionOnInfraLink")
                        .isEqualTo(EXPECTED_DIRECTION_ON_INFRALINK);
            }

            @Test
            @DisplayName("Should return a stop which has the correct X coordinate")
            void shouldReturnStopWhichHasCorrectXCoordinate() {
                DigiroadStop stop = DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.location().getX())
                        .as("X Coordinate")
                        .isEqualTo(EXPECTED_X_COORDINATE);
            }

            @Test
            @DisplayName("Should return a stop which has the correct Y coordinate")
            void shouldReturnStopWhichHasCorrectYCoordinate() {
                DigiroadStop stop = DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.location().getY())
                        .as("Y Coordinate")
                        .isEqualTo(EXPECTED_Y_COORDINATE);
            }

            @Test
            @DisplayName("Should return a stop which has the correct Finnish name")
            void shouldReturnStopWhichHasCorrectFinnishName() {
                DigiroadStop stop = DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.nameFinnish())
                        .as("nameFinnish")
                        .isNotEmpty()
                        .contains(EXPECTED_FINNISH_NAME);
            }

            @Test
            @DisplayName("Should return a stop which has the correct Swedish name")
            void shouldReturnStopWhichHasCorrectSwedishName() {
                DigiroadStop stop = DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.nameSwedish())
                        .as("nameSwedish")
                        .isNotEmpty()
                        .contains(EXPECTED_SWEDISH_NAME);
            }
        }

        @Nested
        @DisplayName("When names are empty strings")
        class WhenNamesAreEmptyStrings {

            private static final String CSV_LINE = "111;133202;168626;backward;\"{\"\"type\"\": \"\"Point\"\", \"\"coordinates\"\": [24.696376131, 60.207149801]}\";;;digiroad_r";

            @Test
            @DisplayName("Should return an optional which contains the parsed stop")
            void shouldReturnOptionalWhichContainsParsedStop() {
                Optional<DigiroadStop> container = DigiroadStopFactory.fromCsvLine(CSV_LINE);
                assertThat(container).isNotEmpty();
            }

            @Test
            @DisplayName("Should return a stop which has the correct external stop id")
            void shouldReturnStopWhichContainsHasExternalStopId() {
                DigiroadStop stop = DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.externalStopId())
                        .as("externalStopId")
                        .isEqualTo(EXPECTED_EXTERNAL_STOP_ID);
            }

            @Test
            @DisplayName("Should return a stop which has the correct externalLinkId")
            void shouldReturnStopWhichHasCorrectExternalLinkId() {
                DigiroadStop stop = DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.externalLinkId())
                        .as("externalLinkId")
                        .isEqualTo(EXPECTED_EXTERNAL_LINK_ID);
            }

            @Test
            @DisplayName("Should return a stop which has the correct ely number")
            void shouldReturnStopWhichHasCorrectElyNumber() {
                DigiroadStop stop = DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.elyNumber())
                        .as("elyNumber")
                        .isEqualTo(EXPECTED_ELY_NUMBER);
            }

            @Test
            @DisplayName("Should return a stop which has the correct direction on infra link")
            void shouldReturnStopWhichHasCorrectDirectionOnInfraLink() {
                DigiroadStop stop = DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.directionOnInfraLink())
                        .as("directionOnInfraLink")
                        .isEqualTo(EXPECTED_DIRECTION_ON_INFRALINK);
            }

            @Test
            @DisplayName("Should return a stop which has the correct X coordinate")
            void shouldReturnStopWhichHasCorrectXCoordinate() {
                DigiroadStop stop = DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.location().getX())
                        .as("X Coordinate")
                        .isEqualTo(EXPECTED_X_COORDINATE);
            }

            @Test
            @DisplayName("Should return a stop which has the correct Y coordinate")
            void shouldReturnStopWhichHasCorrectYCoordinate() {
                DigiroadStop stop = DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.location().getY())
                        .as("Y Coordinate")
                        .isEqualTo(EXPECTED_Y_COORDINATE);
            }

            @Test
            @DisplayName("Should return a stop which has an empty Finnish name")
            void shouldReturnStopWhichHasEmptyFinnishName() {
                DigiroadStop stop = DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.nameFinnish())
                        .as("nameFinnish")
                        .isEmpty();
            }

            @Test
            @DisplayName("Should return a stop which has an empty Swedish name")
            void shouldReturnStopWhichHasEmptySwedishName() {
                DigiroadStop stop = DigiroadStopFactory.fromCsvLine(CSV_LINE).get();
                assertThat(stop.nameSwedish())
                        .as("nameSwedish")
                        .isEmpty();
            }
        }
    }
}

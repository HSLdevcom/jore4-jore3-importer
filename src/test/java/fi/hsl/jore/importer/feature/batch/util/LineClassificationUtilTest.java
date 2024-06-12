package fi.hsl.jore.importer.feature.batch.util;

import static org.assertj.core.api.Assertions.assertThat;

import fi.hsl.jore.importer.feature.jore3.enumerated.PublicTransportType;
import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore4.entity.TypeOfLine;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class LineClassificationUtilTest {

    @Nested
    @DisplayName("Resolve the type of line")
    class FromNodeType {

        @DisplayName("Should return the correct type of line value")
        @ParameterizedTest(
                name = "When the line properties are: {0}, {1}, {2}, {3} should return {4}")
        @ArgumentsSource(LineClassificationUtilArgumentsProvider.class)
        void shouldReturnCorrectRoutePointType(
                final TransitType transitType,
                final boolean isTrunkLine,
                final PublicTransportType publicTransportType,
                final String lineNumber,
                final TypeOfLine expectedOutput) {
            final TypeOfLine actual =
                    LineClassificationUtil.resolveTypeOfLine(
                            transitType, isTrunkLine, publicTransportType, lineNumber);
            assertThat(actual).isEqualTo(expectedOutput);
        }
    }

    private static class LineClassificationUtilArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(
                final ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(
                            TransitType.BUS,
                            true,
                            PublicTransportType.BUS_LOCAL,
                            "123",
                            TypeOfLine.EXPRESS_BUS_SERVICE),
                    Arguments.of(
                            TransitType.BUS,
                            false,
                            PublicTransportType.BUS_LOCAL,
                            "123",
                            TypeOfLine.SPECIAL_NEEDS_BUS),
                    Arguments.of(
                            TransitType.BUS,
                            false,
                            PublicTransportType.U_TRANSPORT,
                            "123",
                            TypeOfLine.REGIONAL_BUS_SERVICE),
                    Arguments.of(
                            TransitType.BUS,
                            false,
                            PublicTransportType.BUS_TAMPERE,
                            "123",
                            TypeOfLine.STOPPING_BUS_SERVICE),
                    Arguments.of(
                            TransitType.BUS,
                            false,
                            PublicTransportType.KIRKKONUMMI_INTERNAL,
                            "623",
                            TypeOfLine.STOPPING_BUS_SERVICE),
                    Arguments.of(
                            TransitType.TRAIN,
                            false,
                            PublicTransportType.ESPOO_SERVICE,
                            " Y",
                            TypeOfLine.SUBURBAN_RAILWAY),
                    Arguments.of(
                            TransitType.TRAIN,
                            false,
                            PublicTransportType.ESPOO_INTERNAL,
                            "P",
                            TypeOfLine.SUBURBAN_RAILWAY),
                    Arguments.of(
                            TransitType.TRAIN,
                            true,
                            PublicTransportType.VANTAA_INTERNAL,
                            "T ",
                            TypeOfLine.REGIONAL_RAIL_SERVICE),
                    Arguments.of(
                            TransitType.TRAIN,
                            false,
                            PublicTransportType.VANTAA_SERVICE,
                            "Z",
                            TypeOfLine.REGIONAL_RAIL_SERVICE),
                    Arguments.of(
                            TransitType.FERRY,
                            false,
                            PublicTransportType.HELSINKI_EARLY_MORNING,
                            "345",
                            TypeOfLine.FERRY_SERVICE),
                    Arguments.of(
                            TransitType.FERRY,
                            true,
                            PublicTransportType.OTHER_LOCAL,
                            "456",
                            TypeOfLine.FERRY_SERVICE),
                    Arguments.of(
                            TransitType.SUBWAY,
                            false,
                            PublicTransportType.HELSINKI_EARLY_MORNING,
                            "245",
                            TypeOfLine.METRO_SERVICE),
                    Arguments.of(
                            TransitType.SUBWAY,
                            true,
                            PublicTransportType.OTHER_LOCAL,
                            "156",
                            TypeOfLine.METRO_SERVICE),
                    Arguments.of(
                            TransitType.TRAM,
                            false,
                            PublicTransportType.ESPOO_SERVICE,
                            "550",
                            TypeOfLine.REGIONAL_TRAM_SERVICE),
                    Arguments.of(
                            TransitType.TRAM,
                            false,
                            PublicTransportType.ESPOO_INTERNAL,
                            "550 ",
                            TypeOfLine.REGIONAL_TRAM_SERVICE),
                    Arguments.of(
                            TransitType.TRAM,
                            true,
                            PublicTransportType.VANTAA_INTERNAL,
                            "1 ",
                            TypeOfLine.CITY_TRAM_SERVICE),
                    Arguments.of(
                            TransitType.TRAM,
                            false,
                            PublicTransportType.VANTAA_SERVICE,
                            "3",
                            TypeOfLine.CITY_TRAM_SERVICE));
        }
    }
}

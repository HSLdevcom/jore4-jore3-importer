package fi.hsl.jore.importer.feature.jore3.field;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static fi.hsl.jore.importer.feature.jore3.field.LegacyPublicTransportDestination.ESPOO;
import static fi.hsl.jore.importer.feature.jore3.field.LegacyPublicTransportDestination.HELSINKI;
import static fi.hsl.jore.importer.feature.jore3.field.LegacyPublicTransportDestination.KIRKKONUMMI_SIUNTIO;
import static fi.hsl.jore.importer.feature.jore3.field.LegacyPublicTransportDestination.LEGACY;
import static fi.hsl.jore.importer.feature.jore3.field.LegacyPublicTransportDestination.REGIONAL_VANTAA_ESPOO;
import static fi.hsl.jore.importer.feature.jore3.field.LegacyPublicTransportDestination.TESTING;
import static fi.hsl.jore.importer.feature.jore3.field.LegacyPublicTransportDestination.TRAIN_OR_METRO;
import static fi.hsl.jore.importer.feature.jore3.field.LegacyPublicTransportDestination.TUUSULA_KERAVA_SIPOO;
import static fi.hsl.jore.importer.feature.jore3.field.LegacyPublicTransportDestination.U_BUS;
import static fi.hsl.jore.importer.feature.jore3.field.LegacyPublicTransportDestination.VANTAA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class EncodedIdentifierTest {

    private static final boolean IS_PRIMARY = true;
    private static final boolean IS_VARIANT = false;

    private static final boolean IS_TEST = true;
    private static final boolean IS_PRODUCTION = false;

    private static Stream<Arguments> identifierAndPredicates() {
        return Stream.of(
                // Tikkurila - Myyrmäki
                arguments("0052B", LEGACY, "52B", IS_VARIANT, IS_PRODUCTION),
                // Raitiov. korv. bussi (this is a test line/route)
                arguments("1000 X", HELSINKI, "X", IS_PRIMARY, IS_TEST),
                // Fredrikinkatu - Kampintori - Runeberginkatu (this is a test line/route)
                arguments("1000A", HELSINKI, "A", IS_VARIANT, IS_TEST),
                // Rautatientori-Koskela (this is a test line/route)
                arguments("1000S4", HELSINKI, "S", IS_VARIANT, IS_TEST),
                // Koskelan halli - Käpylä
                arguments("1001 3", HELSINKI, "1", IS_VARIANT, IS_PRODUCTION),
                // Urheilutalo-Eira
                arguments("1001A4", HELSINKI, "1A", IS_VARIANT, IS_PRODUCTION),
                // Koskelan halli - Käpylä (this is a bus replacing the tram "3")
                arguments("1003 X", HELSINKI, "3X", IS_PRIMARY, IS_PRODUCTION),
                // suomenlinna
                arguments("1019", HELSINKI, "19", IS_PRIMARY, IS_PRODUCTION),
                // Rautatientori-Koskela
                arguments("1039", HELSINKI, "39", IS_PRIMARY, IS_PRODUCTION),
                // Kamppi-Lauttasaari-Kivenlahti
                arguments("2147KT", ESPOO, "147KT", IS_VARIANT, IS_PRODUCTION),
                // Vanttila - Kauklahti - Lasilaakso
                arguments("2166 1", ESPOO, "166", IS_VARIANT, IS_PRODUCTION),
                // helsinki kerava (this is a legacy train line with no line letter)
                arguments("3001", TRAIN_OR_METRO, "", IS_PRIMARY, IS_PRODUCTION),
                // Helsinki-Riihimäki
                arguments("3001D", TRAIN_OR_METRO, "D", IS_PRIMARY, IS_PRODUCTION),
                // (reitillä ei otsikkoa)
                arguments("3001I1", TRAIN_OR_METRO, "I", IS_VARIANT, IS_PRODUCTION),
                // Helsinki-Leppävaara
                arguments("3002A", TRAIN_OR_METRO, "A", IS_PRIMARY, IS_PRODUCTION),
                // A-juna
                arguments("3002A3", TRAIN_OR_METRO, "A", IS_VARIANT, IS_PRODUCTION),
                // Tapiola - Mellunmäki
                arguments("31M2", TRAIN_OR_METRO, "M2", IS_PRIMARY, IS_PRODUCTION),
                // Tapiola - Mellunmäki
                arguments("31M2 4", TRAIN_OR_METRO, "M2", IS_VARIANT, IS_PRODUCTION),
                // Tapiola - Mellunmäki
                arguments("31M2B4", TRAIN_OR_METRO, "M2B", IS_VARIANT, IS_PRODUCTION),
                // Myllykylän th. - Koivikko - Katriinan sairaala - Riipilä
                arguments("4001KM", VANTAA, "1KM", IS_VARIANT, IS_PRODUCTION),
                // Töölön kisahalli-Kivistö
                arguments("4431 3", VANTAA, "431", IS_VARIANT, IS_PRODUCTION),
                // Jupperi-Lähderanta-Espoon keskus
                arguments("5530 3", REGIONAL_VANTAA_ESPOO, "530", IS_VARIANT, IS_PRODUCTION),
                // Kirkkonummi-Jorvas-Hirsala
                arguments("6021SV", KIRKKONUMMI_SIUNTIO, "21SV", IS_VARIANT, IS_PRODUCTION),
                // Kivenlahti-Masala-Kirkkonummi-Kantvik-Upinniemi
                arguments("7177MB", U_BUS, "177MB", IS_VARIANT, IS_PRODUCTION),
                // Testi
                arguments("8888", TESTING, "888", IS_PRIMARY, IS_TEST),
                // Kaskela-Kytömaa-Terveyskeskus
                arguments("9008", TUUSULA_KERAVA_SIPOO, "8", IS_PRIMARY, IS_PRODUCTION),
                // Kaskela - Keravan asema
                arguments("9008TT", TUUSULA_KERAVA_SIPOO, "8TT", IS_VARIANT, IS_PRODUCTION),
                // Rautatientori - Jokivarsi - Nikkilä - Pornainen - Halkia
                arguments("9787A4", TUUSULA_KERAVA_SIPOO, "787A", IS_VARIANT, IS_PRODUCTION),
                // Rautatientori-Kuninkaanmäki-Nikkilä-Hinthaara-Porvoo
                arguments("9788KV", TUUSULA_KERAVA_SIPOO, "788KV", IS_VARIANT, IS_PRODUCTION)
        );
    }

    @ParameterizedTest
    @MethodSource("identifierAndPredicates")
    public void parseJoreIdentifiers(final String id,
                                     final LegacyPublicTransportDestination destination,
                                     final String displayId,
                                     final boolean isPrimary,
                                     final boolean isTest) {
        final LineId lineId = LineId.from(id);

        assertThat("destination is correct",
                   lineId.destination(),
                   is(destination));

        assertThat("displayed identifier is correct",
                   lineId.displayId(),
                   is(displayId));

        assertThat("priority flag is correct",
                   lineId.primary(),
                   is(isPrimary));

        assertThat("test flag is correct",
                   lineId.test(),
                   is(isTest));
    }
}

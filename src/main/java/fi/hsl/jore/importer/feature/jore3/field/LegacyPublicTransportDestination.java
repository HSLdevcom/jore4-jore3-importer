package fi.hsl.jore.importer.feature.jore3.field;

import java.util.Arrays;
import java.util.Optional;

public enum LegacyPublicTransportDestination {
    LEGACY('0', "Not used / legacy"),
    HELSINKI('1', "Helsinki"),
    ESPOO('2', "Espoo"),
    TRAIN_OR_METRO('3', "Train or metro"),
    VANTAA('4', "Vantaa"),
    REGIONAL_VANTAA_ESPOO('5', "Regional between Vantaa and Espoo"),
    KIRKKONUMMI_SIUNTIO('6', "Kirkkonummi and Siuntio"),
    U_BUS('7', "U-bus routes"),
    TESTING('8', "Testing, unused"),
    TUUSULA_KERAVA_SIPOO('9', "Tuusula, Kerava and Sipoo"),
    UNKNOWN('?', "Unknown transport destination");

    private final Character value;
    private final String description;

    LegacyPublicTransportDestination(final Character value, final String description) {
        this.value = value;
        this.description = description;
    }

    public Character getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<LegacyPublicTransportDestination> of(final Character i) {
        return Arrays.stream(values())
                .filter(destination -> destination.value.equals(i))
                .findFirst();
    }
}

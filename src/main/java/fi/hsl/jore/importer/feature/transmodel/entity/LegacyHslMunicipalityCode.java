package fi.hsl.jore.importer.feature.transmodel.entity;

import fi.hsl.jore.importer.feature.jore3.field.LegacyPublicTransportDestination;

import java.util.Arrays;

/**
 * Contains the HSL municipality code values used in Routes and Lines in the Jore 4 database.
 */
public enum LegacyHslMunicipalityCode {
    LEGACY_NOT_USED('0'),
    HELSINKI('1'),
    ESPOO('2'),
    TRAIN_OR_METRO('3'),
    VANTAA('4'),
    ESPOON_VANTAA_REGIONAL('5'),
    KIRKKONUMMI_AND_SIUNTIO('6'),
    U_LINES('7'),
    TESTING_NOT_USED('8'),
    TUUSULA_KERAVA_SIPOO('9');

    private final Character value;

    LegacyHslMunicipalityCode(final Character value) {
        this.value = value;
    }

    public Character getValue() {
        return value;
    }

    /**
     * Create a HslMunicipalityCode from LegacyPublicTransportDestination.
     *
     * @throws Error If no equivalent value is found.
     *         Notably, if called with LegacyPublicTransportDestination.UNKNOWN.
     */
    public static LegacyHslMunicipalityCode of(final LegacyPublicTransportDestination val) {
        return LegacyHslMunicipalityCode.of(val.getValue());
    }

    public static LegacyHslMunicipalityCode of(final Character val) {
         return Arrays.stream(values())
            .filter(type -> type.value.equals(val))
            .findFirst()
            .orElseThrow();
    }
}

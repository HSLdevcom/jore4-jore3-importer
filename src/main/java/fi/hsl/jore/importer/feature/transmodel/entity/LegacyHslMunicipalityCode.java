package fi.hsl.jore.importer.feature.transmodel.entity;

import fi.hsl.jore.importer.feature.jore3.field.LegacyPublicTransportDestination;

import java.util.Arrays;

/**
 * Contains the HSL municipality code values used in Routes and Lines in the Jore 4 database.
 */
public enum LegacyHslMunicipalityCode {
    LEGACY_NOT_USED('0', "legacy_not_used"),
    HELSINKI('1', "helsinki"),
    ESPOO('2', "espoo"),
    TRAIN_OR_METRO('3', "train_or_metro"),
    VANTAA('4', "vantaa"),
    ESPOON_VANTAA_REGIONAL('5', "espoon_vantaa_regional"),
    KIRKKONUMMI_AND_SIUNTIO('6', "kirkkonummi_and_siuntio"),
    U_LINES('7', "u_lines"),
    TESTING_NOT_USED('8', "testing_not_used"),
    TUUSULA_KERAVA_SIPOO('9', "tuusula_kerava_sipoo");

    private final Character value;
    private final String jore4Value;

    LegacyHslMunicipalityCode(final Character value, final String jore4Value) {
        this.value = value;
        this.jore4Value = jore4Value;
    }

    public Character getValue() {
        return value;
    }

    public String getJore4Value() {
        return jore4Value;
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

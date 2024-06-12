package fi.hsl.jore.importer.feature.jore3.enumerated;

import fi.hsl.jore.importer.feature.jore3.mapping.JoreEnumeration;
import java.util.Arrays;
import java.util.Optional;

@JoreEnumeration(name = "Tilaajaorganisaatio")
public enum ClientOrganization {
    HSL("HSL", "HSL:n sopimukset"),
    ESPOO("ESP", "Espoo"),
    HELSINKI("HKI", "Helsinki"),
    KAUNIAINEN("KAU", "Kauniainen"),
    KERAVA("KER", "Kerava"),
    VANTAA("VAN", "Vantaa"),
    YHTEISTYOVALTUUSKUNTA("YTV", "YTV"),

    UNKNOWN("?", "Unknown"),
    ;
    private final String abbreviation;
    private final String description;

    ClientOrganization(final String abbreviation, final String description) {
        this.abbreviation = abbreviation;
        this.description = description;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<ClientOrganization> of(final String abbreviation) {
        return Arrays.stream(values())
                .filter(organization -> organization.abbreviation.equals(abbreviation))
                .findFirst();
    }
}

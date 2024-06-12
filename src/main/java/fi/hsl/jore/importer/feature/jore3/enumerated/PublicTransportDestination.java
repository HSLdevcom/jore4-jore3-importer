package fi.hsl.jore.importer.feature.jore3.enumerated;

import fi.hsl.jore.importer.feature.jore3.mapping.JoreEnumeration;
import java.util.Arrays;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JoreEnumeration(name = "Joukkoliikennelaji")
public enum PublicTransportDestination {
    INTERNAL_HELSINKI(9201, "Helsingin sisäinen liikenne"),
    INTERNAL_ESPOO_KAUNIAINEN(9202, "Espoon ja Kauniaisten sisäinen liikenne"),
    INTERNAL_VANTAA(9204, "Vantaan sisäinen liikenne"),
    INTERNAL_KERAVA(9205, "Keravan sisäinen liikenne"),
    INTERNAL_KIRKKONUMMI(9206, "Kirkkonummen sisäinen liikenne"),
    INTERNAL_SIPOO(9207, "Sipoon sisäinen liikenne"),
    INTERNAL_TUUSULA(9208, "Tuusulan sisäinen liikenne"),
    INTERNAL_SIUNTIO(9209, "Siuntion sisäinen liikenne"),
    REGIONAL(9310, "Seutuliikenne"),
    REGIONAL_KERAVA(9311, "Seutuliikenne Kerava"),
    REGIONAL_KIRKKONUMMI(9312, "Seutuliikenne Kirkkonummi"),
    REGIONAL_TUUSULA(9314, "Seutuliikenne Tuusula"),
    REGIONAL_SIUNTIO(9315, "Seutuliikenne Siuntio"),
    REGIONAL_TOWARDS_ESPOO(9320, "Seutu, Espoon suunta"),
    REGIONAL_TOWARDS_VANTAA(9321, "Seutu, Vantaan suunta"),
    REGIONAL_TRANSVERSE(9322, "Seutu, poikittais"),
    LOCAL(9410, "Lähiseutuliikenne"),
    U_TRANSPORT_CAPITAL(9510, "U-liikenne PKS"),
    U_TRANSPORT_KIRKKONUMMI(9511, "U-liikenne Kirkkonummi"),
    U_TRANSPORT_SIPOO(9512, "U-liikenne Sipoo"),
    U_TRANSPORT_TUUSULA(9513, "U-liikenne Tuusula"),

    UNKNOWN(-9999, "Unknown"),
    ;

    private static final Logger LOG = LoggerFactory.getLogger(PublicTransportDestination.class);

    private final int value;
    private final String description;

    PublicTransportDestination(final int value, final String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<PublicTransportDestination> of(final int i) {
        return Arrays.stream(values()).filter(destination -> destination.value == i).findFirst();
    }

    public static Optional<PublicTransportDestination> of(final String s) {
        try {
            return of(Integer.parseInt(s));
        } catch (final NumberFormatException ignored) {
            LOG.warn("Could not parse PublicTransportDestination from '{}'", s);
            return Optional.empty();
        }
    }
}

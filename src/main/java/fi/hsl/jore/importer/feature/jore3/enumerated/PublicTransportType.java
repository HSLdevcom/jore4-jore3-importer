package fi.hsl.jore.importer.feature.jore3.enumerated;

import fi.hsl.jore.importer.feature.jore3.mapping.JoreEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;

@JoreEnumeration(name = "Joukkoliikennelaji")
public enum PublicTransportType {
    BUS(1, "Bussiliikenne"),
    TRAM(2, "Raitiovaunuliikenne"),
    ESPOO_INTERNAL(3, "Espoon sisäinen"),
    VANTAA_INTERNAL(4, "Vantaan sisäinen"),
    REGIONAL(5, "Seutuliikenne"),
    METRO(6, "Metroliikenne"),
    WATERWAY(7, "Vesiliikenne"),
    U_TRANSPORT(8, "U-liikenne"),
    OTHER_LOCAL(9, "Muu lähiliikenne"),
    INTERCITY(10, "Kaukoliikenne"),
    EXPRESS(11, "Pikavuoro"),
    TRAIN_LOCAL(12, "Lähijunaliikenne"),
    INTERCITY_VR(13, "VR:n kaukoliikenne"),
    ALL(14, "Kaikki"),
    BUS_TAMPERE(16, "TRE bussit"),
    BUS_LOCAL(21, "Lähibussiliikenne"),
    HELSINKI_EARLY_MORNING(22, "Helsingin aamuyönlinjat"),
    ESPOO_SERVICE(23, "Espoon palvelulinjat"),
    VANTAA_SERVICE(24, "Vantaan palvelulinjat"),
    EARLY_MORNING_LOCAL(25, "Aamuyön seutulinjat"),
    KIRKKONUMMI_INTERNAL(36, "Kirkkonummen sisäinen"),
    SIPOO_INTERNAL(38, "Sipoon sisäiset linjat"),
    KERAVA_INTERNAL(39, "Keravan sisäinen"),

    UNKNOWN(-9999, "Unknown"),
    ;

    private static final Logger LOG = LoggerFactory.getLogger(PublicTransportType.class);

    private final int value;
    private final String description;

    PublicTransportType(final int value,
                        final String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<PublicTransportType> of(final int i) {
        return Arrays.stream(values())
                     .filter(transportType -> transportType.value == i)
                     .findFirst();
    }

    public static Optional<PublicTransportType> of(final String s) {
        try {
            return of(Integer.parseInt(s));
        } catch (final NumberFormatException ignored) {
            LOG.warn("Could not parse TransitType from '{}'", s);
            return Optional.empty();
        }
    }
}

package fi.hsl.jore.importer.feature.infrastructure.network_type.dto;

public enum NetworkType {
    BUS("bus"),
    SUBWAY("subway"),
    TRAM("tram"),
    TRAIN("train"),
    FERRY("ferry"),
    UNKNOWN("unknown"),
    ;

    private final String label;

    NetworkType(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

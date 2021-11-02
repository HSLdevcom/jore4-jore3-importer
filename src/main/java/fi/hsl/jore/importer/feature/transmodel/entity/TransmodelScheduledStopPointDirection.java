package fi.hsl.jore.importer.feature.transmodel.entity;

/**
 * Specifies the direction of the scheduled stop point
 * on an infrastructure link.
 */
public enum TransmodelScheduledStopPointDirection {

    BACKWARD("backward"),
    FORWARD("forward");

    private final String value;

    TransmodelScheduledStopPointDirection(final String value) {
        this.value = value;
    }

    /**
     * @return The value which is inserted into the Jore 4 database.
     */
    public String getValue() {
        return value;
    }
}

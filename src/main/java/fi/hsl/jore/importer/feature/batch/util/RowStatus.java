package fi.hsl.jore.importer.feature.batch.util;

import java.util.Arrays;
import java.util.Optional;

public enum RowStatus {
    INSERTED("i"),
    UPDATED("u"),
    DELETED("d");

    private final String value;

    RowStatus(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static Optional<RowStatus> of(final String value) {
        return Arrays.stream(values())
                .filter(status -> status.value.equals(value))
                .findFirst();
    }
}

package fi.hsl.jore.importer.feature.common.style;

import org.immutables.value.Value;

public abstract class Wrapper<T> {
    @Value.Parameter
    public abstract T value();

    @Override
    public String toString() {
        return value().toString();
    }
}

package fi.hsl.jore.importer.feature.jore.mixin;

import com.google.common.base.Preconditions;
import fi.hsl.jore.importer.feature.jore.mapping.JoreColumn;
import org.immutables.value.Value;

public interface IHasCoordinates {

    @JoreColumn(name = "solx")
    double latitude();

    @JoreColumn(name = "soly")
    double longitude();

    @Value.Check
    default void checkCoordinates() {
        Preconditions.checkState(latitude() <= 90.0D);
        Preconditions.checkState(longitude() <= 180.0D);
    }
}

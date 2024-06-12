package fi.hsl.jore.importer.feature.jore3.key;

import fi.hsl.jore.importer.feature.jore3.field.generated.RouteLinkId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasRouteLinkId;
import org.immutables.value.Value;

@Value.Immutable
public interface JrRouteLinkPk extends IHasRouteLinkId {
    static JrRouteLinkPk of(final RouteLinkId id) {
        return ImmutableJrRouteLinkPk.builder().routeLinkId(id).build();
    }
}

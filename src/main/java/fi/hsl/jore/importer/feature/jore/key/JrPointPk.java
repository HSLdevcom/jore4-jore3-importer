package fi.hsl.jore.importer.feature.jore.key;


import fi.hsl.jore.importer.feature.jore.mixin.IHasPointId;
import org.immutables.value.Value;

@Value.Immutable
public interface JrPointPk extends IHasPointId {
    static JrPointPk of(final int pointId) {
        return ImmutableJrPointPk.builder()
                                 .pointId(pointId)
                                 .build();
    }
}

package fi.hsl.jore.importer.feature.common.dto.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.vavr.collection.Map;
import io.vavr.collection.TreeMap;
import org.immutables.value.Value;

import java.util.Locale;
import java.util.Optional;

@Value.Immutable
public interface MultilingualString {

    @JsonValue
    @Value.Default
    default Map<String, String> values() {
        // TreeMap -> consistent sorting order of keys
        return TreeMap.empty();
    }

    @JsonCreator
    static MultilingualString of(final java.util.Map<String, String> values) {
        return ImmutableMultilingualString.builder()
                                          .values(TreeMap.ofAll(values))
                                          .build();
    }

    static MultilingualString empty() {
        return ImmutableMultilingualString.builder()
                                          .build();
    }

    default MultilingualString with(final Locale locale,
                                    final String value) {
        return ImmutableMultilingualString.copyOf(this)
                                          .withValues(values().put(locale.toString(), value));
    }

    default MultilingualString with(final Locale locale,
                                    final Optional<String> value) {
        return value.map(v -> with(locale, v))
                    .orElse(this);
    }
}

package fi.hsl.jore.importer.feature.common.dto.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.vavr.collection.Map;
import io.vavr.collection.TreeMap;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import org.immutables.value.Value;

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
        return ImmutableMultilingualString.builder().values(TreeMap.ofAll(values)).build();
    }

    static MultilingualString ofLocaleMap(final java.util.Map<Locale, String> values) {
        return MultilingualString.of(
                values.entrySet().stream()
                        .collect(
                                Collectors.toMap(
                                        entry -> entry.getKey().toString(),
                                        java.util.Map.Entry::getValue)));
    }

    static MultilingualString empty() {
        return ImmutableMultilingualString.builder().build();
    }

    default MultilingualString with(final Locale locale, final String value) {
        return ImmutableMultilingualString.copyOf(this)
                .withValues(values().put(locale.toString(), value));
    }

    default MultilingualString with(final Locale locale, final Optional<String> value) {
        return value.map(v -> with(locale, v)).orElse(this);
    }
}

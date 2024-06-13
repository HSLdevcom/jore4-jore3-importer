package fi.hsl.jore.importer.feature.common.dto.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.immutables.value.Value;

@Value.Immutable
public interface MultilingualString {

    @JsonValue
    @Value.Default
    default SortedMap<String, String> values() {
        return Collections.emptySortedMap();
    }

    @JsonCreator
    static MultilingualString of(final Map<String, String> values) {
        return ImmutableMultilingualString.builder()
                .values(Collections.unmodifiableSortedMap(new TreeMap<>(values)))
                .build();
    }

    static MultilingualString ofLocaleMap(final Map<Locale, String> values) {
        return MultilingualString.of(values.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(entry -> entry.getKey().toString(), Entry::getValue)));
    }

    static MultilingualString empty() {
        return ImmutableMultilingualString.builder().build();
    }

    default MultilingualString with(final Locale locale, final String value) {
        final TreeMap<String, String> newValues = new TreeMap<>(values());
        newValues.put(locale.toString(), value);

        return ImmutableMultilingualString.copyOf(this).withValues(Collections.unmodifiableSortedMap(newValues));
    }

    default MultilingualString with(final Locale locale, final Optional<String> value) {
        return value.map(v -> with(locale, v)).orElse(this);
    }
}

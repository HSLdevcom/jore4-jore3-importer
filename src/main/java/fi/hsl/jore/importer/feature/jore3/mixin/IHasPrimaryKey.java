package fi.hsl.jore.importer.feature.jore3.mixin;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface IHasPrimaryKey<K> {
    K pk();

    static <K, E extends IHasPrimaryKey<K>> Map<K, E> groupByPk(final List<E> vals) {
        return vals.stream().collect(Collectors.groupingBy(IHasPrimaryKey::pk)).entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(Entry::getKey, entry -> {
                    final List<E> list = entry.getValue();
                    if (list.size() > 1) {
                        final Logger log = LoggerFactory.getLogger(IHasPrimaryKey.class);
                        log.warn("Multiple entities for key {}", entry.getKey());
                    }
                    return list.get(0);
                }));
    }
}

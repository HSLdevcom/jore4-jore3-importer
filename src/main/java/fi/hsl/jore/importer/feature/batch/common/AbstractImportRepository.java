package fi.hsl.jore.importer.feature.batch.common;

import com.google.common.base.Stopwatch;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.PK;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractImportRepository<ENTITY, KEY extends PK> implements IImportRepository<ENTITY, KEY> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractImportRepository.class);

    protected abstract Set<KEY> insert();

    protected abstract Set<KEY> update();

    protected abstract Set<KEY> delete();

    private Set<KEY> logRuntime(final Supplier<Set<KEY>> method, final String label) {
        final Stopwatch watch = Stopwatch.createStarted();
        final Set<KEY> result = method.get();
        watch.stop();
        LOG.info("{} returned {} rows and took {} ms", label, result.size(), watch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }

    @Override
    @Transactional
    public Map<RowStatus, Set<KEY>> commitStagingToTarget() {
        final Set<KEY> deleted = logRuntime(this::delete, "delete");
        final Set<KEY> updated = logRuntime(this::update, "update");
        final Set<KEY> inserted = logRuntime(this::insert, "insert");

        return Map.of(RowStatus.INSERTED, inserted, RowStatus.UPDATED, updated, RowStatus.DELETED, deleted)
                .entrySet()
                .stream()
                .filter(it -> !it.getValue().isEmpty())
                .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));
    }
}

package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.feature.batch.scheduled_stop_point.support.IScheduledStopPointImportRepository;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPoint;
import fi.hsl.jore.importer.feature.jore4.repository.IJore4ScheduledStopPointRepository;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.PersistableScheduledStopPointIdMapping;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Writes the exported scheduled stop points to the Jore 4
 * database.
 */
@Component
public class ScheduledStopPointExportWriter implements ItemWriter<Jore4ScheduledStopPoint> {

    private final IScheduledStopPointImportRepository importerRepository;
    private final IJore4ScheduledStopPointRepository jore4Repository;

    @Autowired
    public ScheduledStopPointExportWriter(final IScheduledStopPointImportRepository importerRepository,
                                          final IJore4ScheduledStopPointRepository jore4Repository) {
        this.importerRepository = importerRepository;
        this.jore4Repository = jore4Repository;
    }

    @Override
    public void write(final List<? extends Jore4ScheduledStopPoint> items) throws Exception {
        jore4Repository.insert(items);

        final io.vavr.collection.List<PersistableScheduledStopPointIdMapping> jore4IdMappings = items.stream()
                .map(item -> PersistableScheduledStopPointIdMapping.of(
                        item.externalScheduledStopPointId(),
                        item.scheduledStopPointId()
                ))
                .collect(io.vavr.collection.List.collector());
        importerRepository.setJore4Ids(jore4IdMappings);
    }
}

package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.feature.batch.scheduled_stop_point.support.IScheduledStopPointImportRepository;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.PersistableScheduledStopPointIdMapping;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPoint;
import fi.hsl.jore.importer.feature.transmodel.repository.ITransmodelScheduledStopPointRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Writes the exported scheduled stop points to the Jore 4
 * database.
 */
@Component
public class ScheduledStopPointExportWriter implements ItemWriter<TransmodelScheduledStopPoint> {

    private final IScheduledStopPointImportRepository importerRepository;
    private final ITransmodelScheduledStopPointRepository jore4Repository;

    @Autowired
    public ScheduledStopPointExportWriter(final IScheduledStopPointImportRepository importerRepository,
                                          final ITransmodelScheduledStopPointRepository jore4Repository) {
        this.importerRepository = importerRepository;
        this.jore4Repository = jore4Repository;
    }

    @Override
    public void write(final List<? extends TransmodelScheduledStopPoint> items) throws Exception {
        jore4Repository.insert(items);

        final io.vavr.collection.List<PersistableScheduledStopPointIdMapping> transmodelIdMappings = items.stream()
                .map(item -> PersistableScheduledStopPointIdMapping.of(
                        item.externalScheduledStopPointId(),
                        UUID.fromString(item.scheduledStopPointId())
                ))
                .collect(io.vavr.collection.List.collector());
        importerRepository.setTransmodelIds(transmodelIdMappings);
    }
}

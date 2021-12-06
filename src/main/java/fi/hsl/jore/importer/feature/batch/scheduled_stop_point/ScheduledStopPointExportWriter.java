package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.feature.batch.scheduled_stop_point.support.IScheduledStopPointImportRepository;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.PersistableScheduledStopPointTransmodelId;
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

        final List<PersistableScheduledStopPointTransmodelId> transmodelIds = items.stream()
                .map(item -> PersistableScheduledStopPointTransmodelId.of(
                        item.externalScheduledStopPointId(),
                        UUID.fromString(item.scheduledStopPointId())
                ))
                .collect(Collectors.toList());
        importerRepository.setTransmodelIds(transmodelIds);
    }
}

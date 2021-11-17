package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPoint;
import fi.hsl.jore.importer.feature.transmodel.repository.ITransmodelScheduledStopPointRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Writes the exported scheduled stop points to the Jore 4
 * database.
 */
@Component
public class ScheduledStopPointExportWriter implements ItemWriter<TransmodelScheduledStopPoint> {

    private final ITransmodelScheduledStopPointRepository repository;

    @Autowired
    public ScheduledStopPointExportWriter(final ITransmodelScheduledStopPointRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(final List<? extends TransmodelScheduledStopPoint> items) throws Exception {
        repository.insert(items);
    }
}

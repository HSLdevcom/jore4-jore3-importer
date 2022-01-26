package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelJourneyPatternStop;
import fi.hsl.jore.importer.feature.transmodel.repository.ITransmodelJourneyPatternStopRepository;
import fi.hsl.jore.importer.feature.transmodel.repository.TransmodelJourneyPatternStopRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JourneyPatternStopExportWriter implements ItemWriter<TransmodelJourneyPatternStop> {

    private final ITransmodelJourneyPatternStopRepository repository;

    @Autowired
    public JourneyPatternStopExportWriter(final TransmodelJourneyPatternStopRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(final List<? extends TransmodelJourneyPatternStop> items) throws Exception {
        repository.insert(items);
    }
}

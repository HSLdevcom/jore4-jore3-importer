package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4JourneyPatternStop;
import fi.hsl.jore.importer.feature.jore4.repository.IJore4JourneyPatternStopRepository;
import fi.hsl.jore.importer.feature.jore4.repository.Jore4JourneyPatternStopRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JourneyPatternStopExportWriter implements ItemWriter<Jore4JourneyPatternStop> {

    private final IJore4JourneyPatternStopRepository repository;

    @Autowired
    public JourneyPatternStopExportWriter(final Jore4JourneyPatternStopRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(final List<? extends Jore4JourneyPatternStop> items) throws Exception {
        repository.insert(items);
    }
}

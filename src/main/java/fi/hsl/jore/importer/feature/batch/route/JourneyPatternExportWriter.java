package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.batch.route.support.IRouteImportRepository;
import fi.hsl.jore.importer.feature.network.route.dto.PersistableJourneyPatternIdMapping;
import fi.hsl.jore.importer.feature.network.route.dto.PersistableRouteIdMapping;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelJourneyPattern;
import fi.hsl.jore.importer.feature.transmodel.repository.ITransmodelJourneyPatternRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class JourneyPatternExportWriter implements ItemWriter<TransmodelJourneyPattern> {

    private final IRouteImportRepository importerRepository;
    private final ITransmodelJourneyPatternRepository jore4Repository;

    @Autowired
    JourneyPatternExportWriter(final IRouteImportRepository importerRepository,
                               final ITransmodelJourneyPatternRepository jore4Repository) {
        this.importerRepository = importerRepository;
        this.jore4Repository = jore4Repository;
    }

    @Override
    public void write(List<? extends TransmodelJourneyPattern> items) throws Exception {
        jore4Repository.insert(items);

        final io.vavr.collection.List<PersistableJourneyPatternIdMapping> journeyPatternIdMappings = items.stream()
                .map(item -> PersistableJourneyPatternIdMapping.of(
                        UUID.fromString(item.routeId()),
                        UUID.fromString(item.journeyPatternId())
                ))
                .collect(io.vavr.collection.List.collector());
        importerRepository.setJourneyPatternTransmodelIds(journeyPatternIdMappings);
    }
}

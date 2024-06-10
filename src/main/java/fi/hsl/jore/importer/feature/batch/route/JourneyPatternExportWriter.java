package fi.hsl.jore.importer.feature.batch.route;

import com.google.common.collect.FluentIterable;
import fi.hsl.jore.importer.feature.batch.route_direction.support.IRouteDirectionImportRepository;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4JourneyPattern;
import fi.hsl.jore.importer.feature.jore4.repository.IJore4JourneyPatternRepository;
import fi.hsl.jore.importer.feature.network.route_direction.dto.PersistableJourneyPatternIdMapping;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JourneyPatternExportWriter implements ItemWriter<Jore4JourneyPattern> {

    private final IRouteDirectionImportRepository importerRepository;
    private final IJore4JourneyPatternRepository jore4Repository;

    @Autowired
    JourneyPatternExportWriter(final IRouteDirectionImportRepository importerRepository,
                               final IJore4JourneyPatternRepository jore4Repository) {
        this.importerRepository = importerRepository;
        this.jore4Repository = jore4Repository;
    }

    @Override
    public void write(final Chunk<? extends Jore4JourneyPattern> items) throws Exception {
        jore4Repository.insert(items);

        importerRepository.setJourneyPatternJore4Ids(
            FluentIterable
                .from(items)
                .transform(item -> PersistableJourneyPatternIdMapping.of(
                    item.routeDirectionExtId(),
                    item.journeyPatternId()
                ))
        );
    }
}

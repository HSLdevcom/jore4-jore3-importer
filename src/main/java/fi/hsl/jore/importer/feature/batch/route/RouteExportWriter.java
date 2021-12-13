package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.batch.route.support.IRouteImportRepository;
import fi.hsl.jore.importer.feature.network.route.dto.PersistableRouteIdMapping;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRoute;
import fi.hsl.jore.importer.feature.transmodel.repository.ITransmodelRouteRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RouteExportWriter implements ItemWriter<TransmodelRoute> {

    private final IRouteImportRepository importerRepository;
    private final ITransmodelRouteRepository jore4Repository;

    @Autowired
    public RouteExportWriter(final IRouteImportRepository importerRepository,
                             final ITransmodelRouteRepository jore4Repository)  {
        this.importerRepository = importerRepository;
        this.jore4Repository = jore4Repository;
    }

    @Override
    public void write(List<? extends TransmodelRoute> items) throws Exception {
        jore4Repository.insert(items);

        final io.vavr.collection.List<PersistableRouteIdMapping> transmodelIdMappings = items.stream()
                .map(item -> PersistableRouteIdMapping.of(
                        item.externalId(),
                        UUID.fromString(item.routeId()))
                )
                .collect(io.vavr.collection.List.collector());
        importerRepository.setTransmodelIds(transmodelIdMappings);
    }
}

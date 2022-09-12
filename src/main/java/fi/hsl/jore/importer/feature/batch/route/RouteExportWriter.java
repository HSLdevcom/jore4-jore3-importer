package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.batch.route_direction.support.IRouteDirectionImportRepository;
import fi.hsl.jore.importer.feature.network.route_direction.dto.PersistableRouteIdMapping;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4Route;
import fi.hsl.jore.importer.feature.jore4.repository.IJore4RouteRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteExportWriter implements ItemWriter<Jore4Route> {

    private final IRouteDirectionImportRepository importerRepository;
    private final IJore4RouteRepository jore4Repository;

    @Autowired
    public RouteExportWriter(final IRouteDirectionImportRepository importerRepository,
                             final IJore4RouteRepository jore4Repository)  {
        this.importerRepository = importerRepository;
        this.jore4Repository = jore4Repository;
    }

    @Override
    public void write(final List<? extends Jore4Route> items) throws Exception {
        jore4Repository.insert(items);

        final io.vavr.collection.List<PersistableRouteIdMapping> transmodelIdMappings = items.stream()
                .map(item -> PersistableRouteIdMapping.of(
                        item.directionExtId(),
                        item.routeId()
                ))
                .collect(io.vavr.collection.List.collector());
        importerRepository.setRouteTransmodelIds(transmodelIdMappings);
    }
}

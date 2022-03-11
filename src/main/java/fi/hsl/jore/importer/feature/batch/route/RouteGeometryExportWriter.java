package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRouteGeometry;
import fi.hsl.jore.importer.feature.transmodel.repository.ITransmodelRouteGeometryRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Inserts route geometries into the Jore 4 database.
 */
@Component
public class RouteGeometryExportWriter implements ItemWriter<TransmodelRouteGeometry> {

    private final ITransmodelRouteGeometryRepository repository;

    @Autowired
    public RouteGeometryExportWriter(final ITransmodelRouteGeometryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(final List<? extends TransmodelRouteGeometry> items) throws Exception {
        repository.insert(items);
    }
}

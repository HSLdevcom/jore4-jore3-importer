package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4RouteGeometry;
import fi.hsl.jore.importer.feature.jore4.repository.IJore4RouteGeometryRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Inserts route geometries into the Jore 4 database. */
@Component
public class RouteGeometryExportWriter implements ItemWriter<Jore4RouteGeometry> {

    private final IJore4RouteGeometryRepository repository;

    @Autowired
    public RouteGeometryExportWriter(final IJore4RouteGeometryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(final Chunk<? extends Jore4RouteGeometry> items) throws Exception {
        repository.insert(items);
    }
}

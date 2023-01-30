package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.feature.batch.line.support.ILineImportRepository;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4Line;
import fi.hsl.jore.importer.feature.jore4.repository.IJore4LineRepository;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLineIdMapping;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Writes the exported lines to the Jore 4 database.
 */
@Component
public class LineExportWriter implements ItemWriter<Jore4Line> {

    private final ILineImportRepository importerRepository;
    private final IJore4LineRepository jore4Repository;

    @Autowired
    public LineExportWriter(final ILineImportRepository importerRepository,
                            final IJore4LineRepository jore4Repository) {
        this.importerRepository = importerRepository;
        this.jore4Repository = jore4Repository;
    }

    @Override
    public void write(final List<? extends Jore4Line> items) throws Exception {
        jore4Repository.insert(items);

        final io.vavr.collection.List<PersistableLineIdMapping> transmodelIdMappings = items.stream()
                .map(item -> PersistableLineIdMapping.of(
                        item.externalLineId(),
                        item.lineId()
                ))
                .collect(io.vavr.collection.List.collector());
        importerRepository.setTransmodelIds(transmodelIdMappings);
    }
}

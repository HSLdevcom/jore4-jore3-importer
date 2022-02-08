package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.feature.batch.line.support.ILineImportRepository;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLineIdMapping;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelLine;
import fi.hsl.jore.importer.feature.transmodel.repository.ITransmodelLineRepository;
import fi.hsl.jore.importer.feature.transmodel.repository.TransmodelLineRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Writes the exported lines to the Jore 4 database.
 */
@Component
public class LineExportWriter implements ItemWriter<TransmodelLine> {

    private final ILineImportRepository importerRepository;
    private final ITransmodelLineRepository jore4Repository;

    @Autowired
    public LineExportWriter(final ILineImportRepository importerRepository,
                            final ITransmodelLineRepository jore4Repository) {
        this.importerRepository = importerRepository;
        this.jore4Repository = jore4Repository;
    }

    @Override
    public void write(final List<? extends TransmodelLine> items) throws Exception {
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

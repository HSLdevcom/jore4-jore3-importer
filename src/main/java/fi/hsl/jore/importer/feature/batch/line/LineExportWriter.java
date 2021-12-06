package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.feature.batch.line.support.ILineImportRepository;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLineTransmodelId;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelLine;
import fi.hsl.jore.importer.feature.transmodel.repository.TransmodelLineRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Writes the exported lines to the Jore 4 database.
 */
@Component
public class LineExportWriter implements ItemWriter<TransmodelLine> {

    private final ILineImportRepository importerRepository;
    private final TransmodelLineRepository jore4Repository;

    @Autowired
    public LineExportWriter(final ILineImportRepository importerRepository,
                            final TransmodelLineRepository repository) {
        this.importerRepository = importerRepository;
        this.jore4Repository = repository;
    }

    @Override
    public void write(final List<? extends TransmodelLine> items) throws Exception {
        jore4Repository.insert(items);

        final List<PersistableLineTransmodelId> transmodelIds = items.stream()
                .map(item -> PersistableLineTransmodelId.of(
                        item.externalLineId(),
                        UUID.fromString(item.lineId())
                ))
                .collect(Collectors.toList());
        importerRepository.setTransmodelIds(transmodelIds);
    }
}

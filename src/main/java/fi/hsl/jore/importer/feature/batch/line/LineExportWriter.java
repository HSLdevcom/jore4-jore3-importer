package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelLine;
import fi.hsl.jore.importer.feature.transmodel.repository.TransmodelLineRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Writes the exported lines to the Jore 4 database.
 */
@Component
public class LineExportWriter implements ItemWriter<TransmodelLine> {

    private final TransmodelLineRepository repository;

    @Autowired
    public LineExportWriter(final TransmodelLineRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(final List<? extends TransmodelLine> items) throws Exception {
        repository.insert(items);
    }
}

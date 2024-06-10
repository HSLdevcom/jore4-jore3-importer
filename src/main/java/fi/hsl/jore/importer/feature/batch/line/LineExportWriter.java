package fi.hsl.jore.importer.feature.batch.line;

import com.google.common.collect.FluentIterable;
import fi.hsl.jore.importer.feature.batch.line_header.support.ILineHeaderImportRepository;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4Line;
import fi.hsl.jore.importer.feature.jore4.repository.IJore4LineRepository;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLineIdMapping;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Writes the exported lines to the Jore 4 database.
 */
@Component
public class LineExportWriter implements ItemWriter<Jore4Line> {

    private final IJore4LineRepository jore4LineRepository;
    private final ILineHeaderImportRepository importerLineHeaderRepository;

    @Autowired
    public LineExportWriter(final IJore4LineRepository jore4LineRepository,
                            final ILineHeaderImportRepository importerLineHeaderRepository) {
        this.jore4LineRepository = jore4LineRepository;
        this.importerLineHeaderRepository = importerLineHeaderRepository;
    }

    @Override
    public void write(final Chunk<? extends Jore4Line> items) throws Exception {
        jore4LineRepository.insert(items);

        importerLineHeaderRepository.setJore4Ids(
            FluentIterable
                .from(items)
                .transform(item -> PersistableLineIdMapping.of(
                    item.externalIdOfLineHeader(),
                    item.lineId()
                )
        ));
    }
}

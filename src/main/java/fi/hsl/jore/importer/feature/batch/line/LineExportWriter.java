package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.feature.batch.line.support.ILineImportRepository;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4Line;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4LineExternalId;
import fi.hsl.jore.importer.feature.jore4.repository.IJore4LineExternalIdRepository;
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
    private final IJore4LineRepository jore4LineRepository;
    private final IJore4LineExternalIdRepository jore4LineExternalIdRepository;

    @Autowired
    public LineExportWriter(final ILineImportRepository importerRepository,
                            final IJore4LineRepository jore4LineRepository,
                            final IJore4LineExternalIdRepository jore4LineExternalIdRepository) {
        this.importerRepository = importerRepository;
        this.jore4LineRepository = jore4LineRepository;
        this.jore4LineExternalIdRepository = jore4LineExternalIdRepository;
    }

    @Override
    public void write(final List<? extends Jore4Line> items) throws Exception {
        /**
         * Insert related line_external_ids first so that lines use them and don't get new ids generated.
         *
         * Note on id terminology, on some relevant ids:
         * - Importer database (imported from Jore 3): external_id (the primary key) and export_id (used eg. in exports)
         * - Jore 4: line_external_id
         * Importantly, the Jore 4 line_external_id is __NOT the same__ as Jore 3 external_id.
         * Instead, the Jore 4 line_external_id is set based on Jore 3 export_id.
         * The Jore 3 external_id is not exported to Jore 4 at all (we just need it here to write Jore 4 ids back to importer database).
         * In short, Jore 3 export_id -> Jore 4 line_external_id
         */
        final io.vavr.collection.List<Jore4LineExternalId> jore4LineExternalIds = items.stream()
                .map(item -> Jore4LineExternalId.of(
                        item.label(),
                        item.exportId() // Yes, this is correct.
                ))
                .collect(io.vavr.collection.List.collector());
        jore4LineExternalIdRepository.insert(jore4LineExternalIds.toJavaList());

        jore4LineRepository.insert(items);

        final io.vavr.collection.List<PersistableLineIdMapping> jore4IdMappings = items.stream()
                .map(item -> PersistableLineIdMapping.of(
                        item.externalLineId(),
                        item.lineId()
                ))
                .collect(io.vavr.collection.List.collector());
        importerRepository.setJore4Ids(jore4IdMappings);
    }
}

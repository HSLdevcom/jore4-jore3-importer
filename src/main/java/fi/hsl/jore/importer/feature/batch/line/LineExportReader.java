package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.feature.batch.util.ResourceUtil;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.network.line.dto.ImporterLine;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Reads the lines which are imported to Jore 4 from the import
 * schemas which are found from the database of this Spring Boot
 * application.
 */
@Component
public class LineExportReader {

    private static final String NAME = "lineExportReader";

    private final DataSource dataSource;
    private final IJsonbConverter jsonConverter;
    private final String sql;

    @Autowired
    public LineExportReader(@Qualifier("importerDataSource") final DataSource dataSource,
                            final IJsonbConverter jsonConverter,
                            @Value(LineExportMapper.SQL_PATH) final Resource sqlResource) {
        this.dataSource = dataSource;
        this.jsonConverter = jsonConverter;
        this.sql = ResourceUtil.fromResource(sqlResource);
    }

    public JdbcCursorItemReader<ImporterLine> build() {
        // The default fetch size seems to be 128 items
        return new JdbcCursorItemReaderBuilder<ImporterLine>()
                .dataSource(dataSource)
                .name(NAME)
                .sql(sql)
                .rowMapper(new LineExportMapper(jsonConverter))
                .build();
    }
}

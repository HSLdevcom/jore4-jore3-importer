package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.batch.util.ResourceUtil;
import fi.hsl.jore.importer.feature.network.route.dto.ImporterJourneyPattern;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class JourneyPatternExportReader {

    private static final String NAME = "journeyPatternExportReader";

    private final DataSource dataSource;
    private final String sql;

    @Autowired
    public JourneyPatternExportReader(@Qualifier("importerDataSource") final DataSource dataSource,
                                      @Value(JourneyPatternExportMapper.SQL_PATH) final Resource sqlResource) {
        this.dataSource = dataSource;
        this.sql = ResourceUtil.fromResource(sqlResource);
    }

    public JdbcCursorItemReader<ImporterJourneyPattern> build() {
        // The default fetch size seems to be 128 items
        return new JdbcCursorItemReaderBuilder<ImporterJourneyPattern>()
                .dataSource(dataSource)
                .name(NAME)
                .sql(sql)
                .rowMapper(new JourneyPatternExportMapper())
                .build();
    }
}

package fi.hsl.jore.importer.feature.batch.stop_place;

import fi.hsl.jore.importer.feature.batch.util.ResourceUtil;
import fi.hsl.jore.importer.feature.jore3.entity.JrStopPlace;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class StopPlaceImportReader {

    private static final String NAME = "stopPlaceReader";

    private final DataSource sourceDataSource;
    private final String sql;

    @Autowired
    public StopPlaceImportReader(
            @Qualifier("sourceDataSource") final DataSource dataSource,
            @Value("") final Resource sqlResource
    ) {
        this.sourceDataSource = dataSource;
        this.sql = ResourceUtil.fromResource(sqlResource);
    }

    public JdbcCursorItemReader<JrStopPlace> build() {
        return new JdbcCursorItemReaderBuilder<JrStopPlace>()
                .dataSource(sourceDataSource)
                .name(NAME)
                .sql(sql)
                .rowMapper(new StopPlaceImportMapper())
                .build();
    }
}

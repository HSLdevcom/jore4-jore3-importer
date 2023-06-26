package fi.hsl.jore.importer.feature.batch.place;

import fi.hsl.jore.importer.feature.batch.util.ResourceUtil;
import fi.hsl.jore.importer.feature.jore3.entity.JrPlace;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class PlaceImportRowReader {

    private static final String NAME = "placeRowReader";

    private final DataSource sourceDataSource;
    private final String sql;

    @Autowired
    public PlaceImportRowReader(@Qualifier("sourceDataSource") final DataSource sourceDataSource,
                                @Value(PlaceImportRowMapper.SQL_PATH) final Resource sqlResource) {
        this.sourceDataSource = sourceDataSource;
        this.sql = ResourceUtil.fromResource(sqlResource);
    }

    public JdbcCursorItemReader<JrPlace> build() {
        // The default fetch size seems to be 128 items
        return new JdbcCursorItemReaderBuilder<JrPlace>()
                .dataSource(sourceDataSource)
                .name(NAME)
                .sql(sql)
                .rowMapper(new PlaceImportRowMapper())
                .build();
    }
}

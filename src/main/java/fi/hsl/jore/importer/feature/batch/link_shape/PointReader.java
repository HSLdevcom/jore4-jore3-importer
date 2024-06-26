package fi.hsl.jore.importer.feature.batch.link_shape;

import fi.hsl.jore.importer.feature.batch.link_shape.dto.PointRow;
import fi.hsl.jore.importer.feature.batch.util.ResourceUtil;
import javax.sql.DataSource;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class PointReader {

    private static final String NAME = "pointReader";

    private final DataSource sourceDataSource;
    private final String sql;

    @Autowired
    public PointReader(
            @Qualifier("sourceDataSource") final DataSource sourceDataSource,
            @Value(PointRowMapper.SQL_PATH) final Resource sqlResource) {
        this.sourceDataSource = sourceDataSource;
        sql = ResourceUtil.fromResource(sqlResource);
    }

    public JdbcCursorItemReader<PointRow> build() {
        // The default fetch size seems to be 128 items
        return new JdbcCursorItemReaderBuilder<PointRow>()
                .dataSource(sourceDataSource)
                .name(NAME)
                .sql(sql)
                .rowMapper(new PointRowMapper())
                .build();
    }
}

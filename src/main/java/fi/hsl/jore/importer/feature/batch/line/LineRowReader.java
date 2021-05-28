package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.feature.batch.util.ResourceUtil;
import fi.hsl.jore.importer.feature.jore3.entity.JrLine;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class LineRowReader {

    private static final String NAME = "lineRowReader";

    private final DataSource sourceDataSource;
    private final String sql;

    @Autowired
    public LineRowReader(@Qualifier("sourceDataSource") final DataSource sourceDataSource,
                         @Value(LineRowMapper.SQL_PATH) final Resource sqlResource) {
        this.sourceDataSource = sourceDataSource;
        this.sql = ResourceUtil.fromResource(sqlResource);
    }

    public JdbcCursorItemReader<JrLine> build() {
        // The default fetch size seems to be 128 items
        return new JdbcCursorItemReaderBuilder<JrLine>()
                .dataSource(sourceDataSource)
                .name(NAME)
                .sql(sql)
                .rowMapper(new LineRowMapper())
                .build();
    }
}

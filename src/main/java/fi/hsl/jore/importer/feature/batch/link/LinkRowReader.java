package fi.hsl.jore.importer.feature.batch.link;

import fi.hsl.jore.importer.feature.batch.link.dto.LinkRow;
import fi.hsl.jore.importer.feature.util.ResourceUtil;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class LinkRowReader {

    private static final String NAME = "linkRowReader";

    private final DataSource sourceDataSource;
    private final String sql;

    @Autowired
    public LinkRowReader(@Qualifier("sourceDataSource") final DataSource sourceDataSource,
                         @Value(LinkRowMapper.SQL_PATH) final Resource sqlResource) {
        this.sourceDataSource = sourceDataSource;
        this.sql = ResourceUtil.fromResource(sqlResource);
    }

    public JdbcCursorItemReader<LinkRow> build() {
        // The default fetch size seems to be 128 items
        return new JdbcCursorItemReaderBuilder<LinkRow>()
                .dataSource(sourceDataSource)
                .name(NAME)
                .sql(sql)
                .rowMapper(new LinkRowMapper())
                .build();
    }
}

package fi.hsl.jore.importer.feature.batch.route_link;

import fi.hsl.jore.importer.feature.batch.route_link.dto.SingleRouteLinkAndParent;
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
public class RouteLinkReader {

    private static final String NAME = "routeLinkReader";

    private final DataSource sourceDataSource;
    private final String sql;

    @Autowired
    public RouteLinkReader(
            @Qualifier("sourceDataSource") final DataSource sourceDataSource,
            @Value(RouteLinkRowMapper.SQL_PATH) final Resource sqlResource) {
        this.sourceDataSource = sourceDataSource;
        sql = ResourceUtil.fromResource(sqlResource);
    }

    public JdbcCursorItemReader<SingleRouteLinkAndParent> build() {
        // The default fetch size seems to be 128 items
        return new JdbcCursorItemReaderBuilder<SingleRouteLinkAndParent>()
                .dataSource(sourceDataSource)
                .name(NAME)
                .sql(sql)
                .rowMapper(new RouteLinkRowMapper())
                .build();
    }
}

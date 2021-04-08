package fi.hsl.jore.importer.feature.batch.node;

import fi.hsl.jore.importer.feature.jore.entity.JrNode;
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
public class NodeReader {

    private static final String NAME = "nodeReader";

    private final DataSource sourceDataSource;
    private final String sql;

    @Autowired
    public NodeReader(@Qualifier("sourceDataSource") final DataSource sourceDataSource,
                      @Value(NodeRowMapper.SQL_PATH) final Resource sqlResource) {
        this.sourceDataSource = sourceDataSource;
        sql = ResourceUtil.fromResource(sqlResource);
    }

    public JdbcCursorItemReader<JrNode> build() {
        // The default fetch size seems to be 128 items
        return new JdbcCursorItemReaderBuilder<JrNode>()
                .dataSource(sourceDataSource)
                .name(NAME)
                .sql(sql)
                .rowMapper(new NodeRowMapper())
                .build();
    }
}

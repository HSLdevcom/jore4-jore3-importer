package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.batch.util.ResourceUtil;
import fi.hsl.jore.importer.feature.jore3.entity.JrRoute;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class RouteReader {

    private static final String NAME = "routeReader";

    private final DataSource sourceDataSource;
    private final String sql;

    @Autowired
    public RouteReader(@Qualifier("sourceDataSource") final DataSource sourceDataSource,
                       @Value(RouteMapper.SQL_PATH) final Resource sqlResource) {
        this.sourceDataSource = sourceDataSource;
        this.sql = ResourceUtil.fromResource(sqlResource);
    }

    public JdbcCursorItemReader<JrRoute> build() {
        // The default fetch size seems to be 128 items
        return new JdbcCursorItemReaderBuilder<JrRoute>()
                .dataSource(sourceDataSource)
                .name(NAME)
                .sql(sql)
                .rowMapper(new RouteMapper())
                .build();
    }
}

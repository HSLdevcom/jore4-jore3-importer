package fi.hsl.jore.importer.feature.batch.route_direction;

import fi.hsl.jore.importer.feature.batch.util.ResourceUtil;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteDirection;
import javax.sql.DataSource;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class RouteDirectionReader {

    private static final String NAME = "routeDirectionReader";

    private final DataSource sourceDataSource;
    private final String sql;

    @Autowired
    public RouteDirectionReader(
            @Qualifier("sourceDataSource") final DataSource sourceDataSource,
            @Value(RouteDirectionMapper.SQL_PATH) final Resource sqlResource) {
        this.sourceDataSource = sourceDataSource;
        this.sql = ResourceUtil.fromResource(sqlResource);
    }

    public JdbcCursorItemReader<JrRouteDirection> build() {
        // The default fetch size seems to be 128 items
        return new JdbcCursorItemReaderBuilder<JrRouteDirection>()
                .dataSource(sourceDataSource)
                .name(NAME)
                .sql(sql)
                .rowMapper(new RouteDirectionMapper())
                .build();
    }
}

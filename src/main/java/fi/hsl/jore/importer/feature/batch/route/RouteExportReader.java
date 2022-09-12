package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.batch.util.ResourceUtil;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.network.route.dto.ImporterRoute;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class RouteExportReader {

    private static final String NAME = "routeExportReader";

    private final DataSource dataSource;
    private final IJsonbConverter jsonConverter;
    private final String sql;

    @Autowired
    public RouteExportReader(@Qualifier("importerDataSource") final DataSource dataSource,
                            final IJsonbConverter jsonConverter,
                            @Value(RouteExportMapper.SQL_PATH) final Resource sqlResource) {
        this.dataSource = dataSource;
        this.jsonConverter = jsonConverter;
        this.sql = ResourceUtil.fromResource(sqlResource);
    }

    public JdbcCursorItemReader<ImporterRoute> build() {
        // The default fetch size seems to be 128 items
        return new JdbcCursorItemReaderBuilder<ImporterRoute>()
                .dataSource(dataSource)
                .name(NAME)
                .sql(sql)
                .rowMapper(new RouteExportMapper(jsonConverter))
                .build();
    }
}

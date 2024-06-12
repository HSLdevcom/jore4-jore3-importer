package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.batch.util.ResourceUtil;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRouteGeometry;
import javax.sql.DataSource;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Provides a static factory method which creates an {@link org.springframework.batch.item.ItemReader} object that reads
 * the information of the exported route geometries from the importer's database.
 */
@Component
public class RouteGeometryExportReader {

    private static final String NAME = "routeGeometryExportReader";

    private final DataSource dataSource;
    private final String sql;

    @Autowired
    public RouteGeometryExportReader(
            @Qualifier("importerDataSource") final DataSource dataSource,
            @Value(RouteGeometryExportMapper.SQL_PATH) final Resource sqlResource) {
        this.dataSource = dataSource;
        this.sql = ResourceUtil.fromResource(sqlResource);
    }

    public JdbcCursorItemReader<ImporterRouteGeometry> build() {
        // The default fetch size seems to be 128 items
        return new JdbcCursorItemReaderBuilder<ImporterRouteGeometry>()
                .dataSource(dataSource)
                .name(NAME)
                .sql(sql)
                .rowMapper(new RouteGeometryExportMapper())
                .build();
    }
}

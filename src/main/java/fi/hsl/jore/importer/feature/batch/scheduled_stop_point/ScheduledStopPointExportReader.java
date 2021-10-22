package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.hsl.jore.importer.feature.batch.util.ResourceUtil;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ExportableScheduledStopPoint;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Reads the imported scheduled stop points from the import
 * schemas which are found from the database of this Spring
 * Boot application.
 */
@Component
public class ScheduledStopPointExportReader {

    private static final String NAME = "stopPointExportReader";

    private final DataSource dataSource;
    private final ObjectMapper objectMapper;
    private final String sql;

    @Autowired
    public ScheduledStopPointExportReader(@Qualifier("destinationDataSource") final DataSource dataSource,
                                          final ObjectMapper objectMapper,
                                          @Value(ScheduledStopPointExportMapper.SQL_PATH) final Resource sqlResource) {
        this.dataSource = dataSource;
        this.objectMapper = objectMapper;
        this.sql = ResourceUtil.fromResource(sqlResource);
    }

    public JdbcCursorItemReader<ExportableScheduledStopPoint> build() {
        // The default fetch size seems to be 128 items
        return new JdbcCursorItemReaderBuilder<ExportableScheduledStopPoint>()
                .dataSource(dataSource)
                .name(NAME)
                .sql(sql)
                .rowMapper(new ScheduledStopPointExportMapper(objectMapper))
                .build();
    }
}

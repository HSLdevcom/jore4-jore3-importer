package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.feature.batch.util.ResourceUtil;
import fi.hsl.jore.importer.feature.jore3.entity.JrScheduledStopPoint;
import javax.sql.DataSource;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/** Reads the imported scheduled stop points from the source database. */
@Component
public class ScheduledStopPointImportReader {

    private static final String NAME = "stopPointReader";

    private final DataSource sourceDataSource;
    private final String sql;

    @Autowired
    public ScheduledStopPointImportReader(
            @Qualifier("sourceDataSource") final DataSource sourceDataSource,
            @Value(ScheduledStopPointImportMapper.SQL_PATH) final Resource sqlResource) {
        this.sourceDataSource = sourceDataSource;
        this.sql = ResourceUtil.fromResource(sqlResource);
    }

    public JdbcCursorItemReader<JrScheduledStopPoint> build() {
        // The default fetch size seems to be 128 items
        return new JdbcCursorItemReaderBuilder<JrScheduledStopPoint>()
                .dataSource(sourceDataSource)
                .name(NAME)
                .sql(sql)
                .rowMapper(new ScheduledStopPointImportMapper())
                .build();
    }
}

package fi.hsl.jore.importer.feature.batch.scheduled_stop_point.timing_place;

import fi.hsl.jore.importer.feature.batch.util.ResourceUtil;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.timing_place.ImporterTimingPlace;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Reads Hastus place IDs from scheduled stop points from the import schemas
 * which are found from the database of this Spring Boot application.
 */
@Component
public class TimingPlaceExportReader {

    private static final String NAME = "timingPlaceExportReader";

    private final DataSource dataSource;
    private final String sql;

    @Autowired
    public TimingPlaceExportReader(@Qualifier("importerDataSource") final DataSource dataSource,
                                   @Value(TimingPlaceExportMapper.SQL_PATH) final Resource sqlResource) {
        this.dataSource = dataSource;
        this.sql = ResourceUtil.fromResource(sqlResource);
    }

    public JdbcCursorItemReader<ImporterTimingPlace> build() {
        // The default fetch size seems to be 128 items.
        return new JdbcCursorItemReaderBuilder<ImporterTimingPlace>()
                .dataSource(dataSource)
                .name(NAME)
                .sql(sql)
                .rowMapper(new TimingPlaceExportMapper())
                .build();
    }
}

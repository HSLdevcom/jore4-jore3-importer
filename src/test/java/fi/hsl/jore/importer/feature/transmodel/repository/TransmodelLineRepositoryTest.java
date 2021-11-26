package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelLine;
import fi.hsl.jore.importer.feature.transmodel.entity.VehicleMode;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static fi.hsl.jore.jore4.jooq.route.Tables.LINE;
import static org.assertj.db.api.Assertions.assertThat;

@IntTest
class TransmodelLineRepositoryTest {

    private static final String EXPECTED_JORE4_NAME = "{\"fi_FI\":\"Vantaanportti-Lentoasema-Kerava\",\"sv_SE\":\"Vandaporten-Flygstationen-Kervo\"}";
    private static final String EXPECTED_JORE4_SHORT_NAME = "{\"fi_FI\":\"Vantaanp-Kerava\",\"sv_SE\":\"Vandap-Kervo\"}";

    private static final String FINNISH_NAME = "Vantaanportti-Lentoasema-Kerava";
    private static final String FINNISH_SHORT_NAME = "Vantaanp-Kerava";
    private static final String SWEDISH_NAME = "Vandaporten-Flygstationen-Kervo";
    private static final String SWEDISH_SHORT_NAME = "Vandap-Kervo";

    private static final VehicleMode PRIMARY_VEHICLE_MODE = VehicleMode.BUS;

    private final TransmodelLineRepository repository;
    private final Table targetTable;

    @Autowired
    TransmodelLineRepositoryTest(final TransmodelLineRepository repository,
                                 @Qualifier("jore4DataSource") final DataSource targetDataSource ) {
        this.repository = repository;
        this.targetTable = new Table(targetDataSource, "route.line");
    }

    @Nested
    @DisplayName("Insert line into the database")
    @Sql(
            scripts = {
                    "/sql/transmodel/drop_tables.sql",
                    "/sql/transmodel/populate_vehicle_modes.sql"
            },
            config = @SqlConfig(dataSource = "jore4DataSource")
    )
    class InsertLineIntoDatabase {

        private final TransmodelLine INPUT = TransmodelLine.of(
                JoreLocaleUtil.createMultilingualString(FINNISH_NAME, SWEDISH_NAME),
                JoreLocaleUtil.createMultilingualString(FINNISH_SHORT_NAME, SWEDISH_SHORT_NAME),
                PRIMARY_VEHICLE_MODE
        );

        @Test
        @DisplayName("Should insert one line into the database")
        void shouldInsertOneLineIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable).hasNumberOfRows(1);
        }

        @Test
        @DisplayName("Should insert the correct name into the database")
        void shouldInsertCorrectNameIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(LINE.NAME_I18N.getName())
                    .isEqualTo(EXPECTED_JORE4_NAME);
        }

        @Test
        @DisplayName("Should insert the correct short name into the database")
        void shouldInsertCorrectShortNameIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(LINE.SHORT_NAME_I18N.getName())
                    .isEqualTo(EXPECTED_JORE4_SHORT_NAME);
        }

        @Test
        @DisplayName("Should insert the correct primary vehicle mode into the database")
        void shouldInsertCorrectPrimaryVehicleModeIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(LINE.PRIMARY_VEHICLE_MODE.getName())
                    .isEqualTo(PRIMARY_VEHICLE_MODE.getValue());
        }
    }
}

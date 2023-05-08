package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4Line;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4LineExternalId;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.jore4.entity.TypeOfLine;
import fi.hsl.jore.importer.feature.jore4.entity.VehicleMode;
import org.assertj.core.api.Assertions;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import javax.sql.DataSource;
import java.util.List;

import static fi.hsl.jore.jore4.jooq.route.Tables.LINE_EXTERNAL_ID;
import static org.assertj.db.api.Assertions.assertThat;

@IntTest
class Jore4LineExternalIdRepositoryTest {

    private static final String LABEL = "1";
    private static final Short EXTERNAL_LINE_ID = 1001;

    private final Jore4LineExternalIdRepository repository;
    private final Table targetTable;

    @Autowired
    Jore4LineExternalIdRepositoryTest(final Jore4LineExternalIdRepository repository,
                            @Qualifier("jore4DataSource") final DataSource targetDataSource) {
        this.repository = repository;
        this.targetTable = new Table(targetDataSource, "route.line_external_id");
    }

    @Nested
    @DisplayName("Insert line_external_id into the database")
    @Sql(
            scripts = {
                    "/sql/jore4/drop_tables.sql"
            },
            config = @SqlConfig(dataSource = "jore4DataSource")
    )
    class InsertLineIntoDatabase {

        private final Jore4LineExternalId INPUT = Jore4LineExternalId.of(
                LABEL,
                EXTERNAL_LINE_ID
        );

        @Test
        @DisplayName("Should insert one line_external_id into the database")
        void shouldInsertOneLineIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable).hasNumberOfRows(1);
        }

        @Test
        @DisplayName("Should insert the correct label into the database")
        void shouldInsertCorrectLabelIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(LINE_EXTERNAL_ID.LABEL.getName())
                    .isEqualTo(LABEL);
        }

        @Test
        @DisplayName("Should insert the correct external_id into the database")
        void shouldInsertCorrectExternalIdIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(LINE_EXTERNAL_ID.EXTERNAL_ID.getName())
                    .isEqualTo(EXTERNAL_LINE_ID);
        }

        @Test
        @DisplayName("Should do nothing if the line_external_id already exists in the database")
        void shouldDoNothingOnConflict() {
            repository.insert(List.of(INPUT));
            repository.insert(List.of(INPUT));
            repository.insert(List.of(INPUT));

            assertThat(targetTable).hasNumberOfRows(1);
        }
    }
}

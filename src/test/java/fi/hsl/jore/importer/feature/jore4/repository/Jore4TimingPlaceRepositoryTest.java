package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4TimingPlace;
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
import java.util.UUID;

import static fi.hsl.jore.importer.TestJsonUtil.equalJson;
import static fi.hsl.jore.jore4.jooq.timing_pattern.Tables.TIMING_PLACE;
import static org.assertj.db.api.Assertions.assertThat;

@IntTest
public class Jore4TimingPlaceRepositoryTest {

    private static final UUID TIMING_PLACE_ID = UUID.fromString("b5f56d68-c4cf-11ed-afa1-0242ac120002");
    private static final String TIMING_PLACE_LABEL = "1ELIEL";
    private static final String TIMING_PLACE_NAME = "Elielinaukio";

    private static final String EXPECTED_RESULTING_TIMING_PLACE_NAME = "{\"fi_FI\":\"" + TIMING_PLACE_NAME + "\"}";

    private final IJore4TimingPlaceRepository repository;
    private final Table targetTable;

    @Autowired
    Jore4TimingPlaceRepositoryTest(final IJore4TimingPlaceRepository repository,
                                   @Qualifier("jore4DataSource") final DataSource targetDataSource) {

        this.repository = repository;
        this.targetTable = new Table(targetDataSource, "timing_pattern.timing_place");
    }

    @Nested
    @DisplayName("Insert line into the database")
    @Sql(
            scripts = {
                    "/sql/jore4/drop_tables.sql"
            },
            config = @SqlConfig(dataSource = "jore4DataSource", transactionManager = "jore4TransactionManager")
    )
    class InsertLineIntoDatabase {

        private final Jore4TimingPlace INPUT = Jore4TimingPlace.of(
                TIMING_PLACE_ID,
                TIMING_PLACE_LABEL,
                MultilingualString.empty().with(JoreLocaleUtil.FINNISH, TIMING_PLACE_NAME));

        @Test
        @DisplayName("Should insert one timing place into the database")
        void shouldInsertOneLineIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable).hasNumberOfRows(1);
        }

        @Test
        @DisplayName("Should insert the correct ID to the database")
        void shouldInsertCorrectIdIntoDatabase() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(TIMING_PLACE.TIMING_PLACE_ID.getName())
                    .isEqualTo(TIMING_PLACE_ID);
        }

        @Test
        @DisplayName("Should insert the correct label into the database")
        void shouldInsertCorrectLabelIntoDatabase() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(TIMING_PLACE.LABEL.getName())
                    .isEqualTo(TIMING_PLACE_LABEL);
        }

        @Test
        @DisplayName("Should insert the correct name into the database")
        void shouldInsertCorrectNameIntoDatabase() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(TIMING_PLACE.DESCRIPTION.getName())
                    .is(equalJson(EXPECTED_RESULTING_TIMING_PLACE_NAME));
        }
    }
}

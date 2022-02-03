package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelJourneyPattern;
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

import static fi.hsl.jore.jore4.jooq.journey_pattern.Tables.JOURNEY_PATTERN_;
import static org.assertj.db.api.Assertions.assertThat;

@IntTest
class TransmodelJourneyPatternRepositoryTest {

    private final TransmodelJourneyPatternRepository repository;
    private final Table targetTable;

    @Autowired
    public TransmodelJourneyPatternRepositoryTest(@Qualifier("jore4DataSource") final DataSource targetDataSource,
                                                  final TransmodelJourneyPatternRepository repository) {
        this.repository = repository;
        this.targetTable = new Table(targetDataSource, "journey_pattern.journey_pattern");
    }

    @Nested
    @DisplayName("Insert journey pattern into the database")
    @Sql(
            scripts = {
                    "/sql/transmodel/drop_tables.sql",
                    "/sql/transmodel/populate_vehicle_modes.sql",
                    "/sql/transmodel/populate_infrastructure_links.sql",
                    "/sql/transmodel/populate_lines.sql",
                    "/sql/transmodel/populate_scheduled_stop_points.sql",
                    "/sql/transmodel/populate_routes.sql"
            },
            config = @SqlConfig(dataSource = "jore4DataSource")
    )
    class InsertJourneyPatternIntoDatabase {

        private static final String JOURNEY_PATTERN_ID = "c5d767fa-400c-45db-bc01-9efd18bad212";
        private static final String ROUTE_ID = "5bfa9a65-c80f-4af8-be95-8370cb12df50";

        private final TransmodelJourneyPattern INPUT = TransmodelJourneyPattern.of(
                JOURNEY_PATTERN_ID,
                ROUTE_ID
        );

        @Test
        @DisplayName("Should insert one journey pattern into the database")
        void shouldInsertOneJourneyPatternIntoDatabase() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable).hasNumberOfRows(1);
        }

        @Test
        @DisplayName("Should save a new journey pattern with the correct id")
        void shouldSaveNewJourneyPatternWithCorrectId() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(JOURNEY_PATTERN_.JOURNEY_PATTERN_ID.getName())
                    .isEqualTo(JOURNEY_PATTERN_ID);
        }

        @Test
        @DisplayName("Should save a new journey pattern with the correct route id")
        void shouldSaveNewJourneyPatternWithCorrectRouteId() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(JOURNEY_PATTERN_.ON_ROUTE_ID.getName())
                    .isEqualTo(ROUTE_ID);
        }
    }
}
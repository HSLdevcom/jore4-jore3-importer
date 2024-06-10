package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4JourneyPattern;
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

import static fi.hsl.jore.jore4.jooq.journey_pattern.Tables.JOURNEY_PATTERN_;
import static org.assertj.db.api.Assertions.assertThat;

@IntTest
class Jore4JourneyPatternRepositoryTest {

    private final Jore4JourneyPatternRepository repository;
    private final Table targetTable;

    @Autowired
    public Jore4JourneyPatternRepositoryTest(@Qualifier("jore4DataSource") final DataSource targetDataSource,
                                             final Jore4JourneyPatternRepository repository) {
        this.repository = repository;
        this.targetTable = new Table(targetDataSource, "journey_pattern.journey_pattern");
    }

    @Nested
    @DisplayName("Insert journey pattern into the database")
    @Sql(
            scripts = {
                    "/sql/jore4/drop_tables.sql",
                    "/sql/jore4/populate_infrastructure_links.sql",
                    "/sql/jore4/populate_timing_places.sql",
                    "/sql/jore4/populate_scheduled_stop_points.sql",
                    "/sql/jore4/populate_lines.sql",
                    "/sql/jore4/populate_routes.sql"
            },
            config = @SqlConfig(dataSource = "jore4DataSource", transactionManager = "jore4TransactionManager")
    )
    class InsertJourneyPatternIntoDatabase {

        private final UUID JOURNEY_PATTERN_ID = UUID.fromString("c5d767fa-400c-45db-bc01-9efd18bad212");
        private final UUID ROUTE_DIRECTION_EXT_ID = UUID.fromString("4aaa41fa-e3ff-4591-a3f5-958f832405af");
        private final UUID ROUTE_ID = UUID.fromString("5bfa9a65-c80f-4af8-be95-8370cb12df50");

        private final Jore4JourneyPattern INPUT = Jore4JourneyPattern.of(
                JOURNEY_PATTERN_ID,
                ROUTE_DIRECTION_EXT_ID,
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

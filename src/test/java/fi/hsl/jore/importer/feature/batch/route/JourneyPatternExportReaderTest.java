package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.network.route.dto.ImporterJourneyPattern;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntTest
class JourneyPatternExportReaderTest {

    private final JdbcCursorItemReader<ImporterJourneyPattern> reader;

    @Autowired
    JourneyPatternExportReaderTest(final JourneyPatternExportReader reader) {
        this.reader = reader.build();
    }

    @BeforeEach
    void openReader() {
        this.reader.open(new ExecutionContext());
    }

    @AfterEach
    void closeReader() {
        this.reader.close();
    }

    @Nested
    @DisplayName("When the source table is empty")
    @Sql(scripts = "/sql/importer/drop_tables.sql")
    class WhenSourceTablesAreEmpty {

        @Test
        @DisplayName("The first invocation of the read() method must return null")
        void firstInvocationOfReadMethodMustReturnNull() throws Exception {
            final ImporterJourneyPattern found = reader.read();
            assertThat(found).isNull();
        }
    }

    @Nested
    @DisplayName("When the source table has one route")
    @Sql(scripts = {
            "/sql/importer/drop_tables.sql",
            "/sql/importer/populate_infrastructure_nodes.sql",
            "/sql/importer/populate_lines_with_jore4_ids.sql",
            "/sql/importer/populate_routes.sql",
            "/sql/importer/populate_route_directions_with_jore4_ids.sql"
    })
    @ExtendWith(SoftAssertionsExtension.class)
    class WhenSourceTableHasOneRoute {

        private final UUID EXPECTED_ROUTE_DIRECTION_ID = UUID.fromString("6f93fa6b-8a19-4b98-bd84-b8409e670c70");
        private final UUID EXPECTED_ROUTE_JORE4_ID = UUID.fromString("5bfa9a65-c80f-4af8-be95-8370cb12df50");

        @Test
        @DisplayName("The first invocation of the read() method must return the found journey pattern")
        void firstInvocationOfReadMethodMustReturnFoundJourneyPattern(final SoftAssertions softAssertions) throws Exception {
            final ImporterJourneyPattern first = reader.read();

            softAssertions.assertThat(first.routeDirectionId())
                    .as("routeDirectionId")
                    .isEqualTo(EXPECTED_ROUTE_DIRECTION_ID);

            softAssertions.assertThat(first.routeJore4Id())
                    .as("routeJore4Id")
                    .isEqualTo(EXPECTED_ROUTE_JORE4_ID);
        }


        @Test
        @DisplayName("The second invocation of the read() method must return null")
        void secondInvocationOfReadMethodMustReturnNull() throws Exception {
            //The first invocation returns the journey pattern found from the database.
            final ImporterJourneyPattern first = reader.read();
            assertThat(first).isNotNull();

            //Because there are no more journey patterns, this invocation must return null.
            final ImporterJourneyPattern second = reader.read();
            assertThat(second).isNull();
        }
    }
}

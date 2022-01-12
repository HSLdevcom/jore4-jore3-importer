package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.network.route.dto.ExportableJourneyPattern;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntTest
class JourneyPatternExportReaderTest {

    private final JdbcCursorItemReader<ExportableJourneyPattern> reader;

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
    @Sql(scripts = "/sql/destination/drop_tables.sql")
    class WhenSourceTablesAreEmpty {

        @Test
        @DisplayName("The first invocation of the read() method must return null")
        void firstInvocationOfReadMethodMustReturnNull() throws Exception {
            final ExportableJourneyPattern found = reader.read();
            assertThat(found).isNull();
        }
    }

    @Nested
    @DisplayName("When the source table has one route")
    @Sql(scripts = {
            "/sql/destination/drop_tables.sql",
            "/sql/destination/populate_infrastructure_nodes.sql",
            "/sql/destination/populate_lines_with_transmodel_ids.sql",
            "/sql/destination/populate_routes_with_transmodel_ids.sql"
    })
    class WhenSourceTableHasOneRoute {

        private final UUID EXPECTED_ROUTE_TRANSMODEL_ID = UUID.fromString("5bfa9a65-c80f-4af8-be95-8370cb12df50");

        @Test
        @DisplayName("The first invocation of the read() method must return the found journey pattern")
        void firstInvocationOfReadMethodMustReturnFoundJourneyPattern() throws Exception {
            final ExportableJourneyPattern first = reader.read();
            assertThat(first.routeTransmodelId()).isEqualTo(EXPECTED_ROUTE_TRANSMODEL_ID);
        }


        @Test
        @DisplayName("The second invocation of the read() method must return null")
        void secondInvocationOfReadMethodMustReturnNull() throws Exception {
            //The first invocation returns the journey pattern found from the database.
            final ExportableJourneyPattern first = reader.read();
            assertThat(first).isNotNull();

            //Because there are no more journey patterns, this invocation must return null.
            final ExportableJourneyPattern second = reader.read();
            assertThat(second).isNull();
        }
    }
}

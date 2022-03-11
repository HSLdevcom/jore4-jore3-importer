package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRouteGeometry;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRouteInfrastructureLink;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

import static fi.hsl.jore.jore4.jooq.route.Tables.INFRASTRUCTURE_LINK_ALONG_ROUTE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.db.api.Assertions.assertThat;

@IntTest
class TransmodelRouteGeometryRepositoryTest {

    private final TransmodelRouteGeometryRepository repository;
    private final Table targetTable;


    @Autowired
    TransmodelRouteGeometryRepositoryTest(@Qualifier("jore4DataSource") final DataSource targetDataSource,
                                          final TransmodelRouteGeometryRepository repository) {
        this.repository = repository;
        this.targetTable = new Table(targetDataSource, "route.infrastructure_link_along_route");
    }

    @Nested
    @DisplayName("insert route geometries into the database")
    @Sql(
            scripts = {
                    "/sql/transmodel/drop_tables.sql",
                    "/sql/transmodel/populate_vehicle_modes.sql",
                    "/sql/transmodel/populate_infrastructure_links.sql",
                    "/sql/transmodel/populate_lines.sql",
                    "/sql/transmodel/populate_scheduled_stop_points.sql",
                    "/sql/transmodel/populate_routes_without_infrastructure_links.sql"
            },
            config = @SqlConfig(dataSource = "jore4DataSource")
    )
    class Insert {

        @Nested
        @DisplayName("When no route geometries are given as argument")
        class WhenNoRouteGeometriesAreGivenAsArgument {

            @Test
            @DisplayName("Shouldn't insert any rows into the target table")
            void shouldNotInsertAnyRowsIntoTargetTable() {
                repository.insert(List.of());
                assertThat(targetTable).isEmpty();
            }
        }

        @Nested
        @DisplayName("When one route geometry is given as argument")
        class WhenOneRouteGeometryIsGivenAsArgument {

            private final UUID ROUTE_ID = UUID.fromString("5bfa9a65-c80f-4af8-be95-8370cb12df50");
            private final UUID INFRASTRUCTURE_LINK_ID = UUID.fromString("554c63e6-87b2-4dc8-a032-b6b0e2607696");
            private static final String INFRASTRUCTURE_LINK_EXT_ID = "133202";
            private static final boolean INFRASTRUCTURE_LINK_IS_TRAVERSAL_FORWARDS = true;
            private static final int INFRASTRUCTURE_LINK_SEQUENCE = 0;
            private static final String INFRASTRUCTURE_LINK_SOURCE = "digiroad_r";

            private TransmodelRouteGeometry input;

            @BeforeEach
            void createInput() {
                final io.vavr.collection.List<TransmodelRouteInfrastructureLink> infrastructureLinks = io.vavr.collection.List.of(
                        TransmodelRouteInfrastructureLink.of(
                                INFRASTRUCTURE_LINK_SOURCE,
                                INFRASTRUCTURE_LINK_EXT_ID,
                                INFRASTRUCTURE_LINK_SEQUENCE,
                                INFRASTRUCTURE_LINK_IS_TRAVERSAL_FORWARDS
                        )
                );
                input = TransmodelRouteGeometry.of(
                        ROUTE_ID,
                        infrastructureLinks
                );
            }

            @Test
            @DisplayName("Should insert one row into the target table")
            void shouldInsertOneRowIntoTargetTable() {
                repository.insert(List.of(input));
                assertThat(targetTable).hasNumberOfRows(1);
            }

            @Test
            @DisplayName("Should save a new route infrastructure link with the correct route id")
            void shouldSaveNewRouteInfrastructureLinkWithCorrectRouteId() {
                repository.insert(List.of(input));
                assertThat(targetTable)
                        .row()
                        .value(INFRASTRUCTURE_LINK_ALONG_ROUTE.ROUTE_ID.getName())
                        .isEqualTo(ROUTE_ID);
            }

            @Test
            @DisplayName("Should save a new route infrastructure link with the correct infrastructure link id")
            void shouldSaveNewRouteInfrastructureLinkWithCorrectInfrastructureLinkId() {
                repository.insert(List.of(input));
                assertThat(targetTable)
                        .row()
                        .value(INFRASTRUCTURE_LINK_ALONG_ROUTE.INFRASTRUCTURE_LINK_ID.getName())
                        .isEqualTo(INFRASTRUCTURE_LINK_ID);
            }

            @Test
            @DisplayName("Should save a new route infrastructure link with the correct infrastructure link sequence")
            void shouldSaveNewRouteInfrastructureLinkWithCorrectInfrastructureLinkSequence() {
                repository.insert(List.of(input));
                assertThat(targetTable)
                        .row()
                        .value(INFRASTRUCTURE_LINK_ALONG_ROUTE.INFRASTRUCTURE_LINK_SEQUENCE.getName())
                        .isEqualTo(INFRASTRUCTURE_LINK_SEQUENCE);
            }

            @Test
            @DisplayName("Should save a new route infrastructure link with correct traversal direction")
            void shouldSaveNewRouteInfrastructureLinkWithCorrectTraversalDirection() {
                repository.insert(List.of(input));
                assertThat(targetTable)
                        .row()
                        .value(INFRASTRUCTURE_LINK_ALONG_ROUTE.IS_TRAVERSAL_FORWARDS.getName())
                        .isEqualTo(INFRASTRUCTURE_LINK_IS_TRAVERSAL_FORWARDS);
            }
        }
    }
}

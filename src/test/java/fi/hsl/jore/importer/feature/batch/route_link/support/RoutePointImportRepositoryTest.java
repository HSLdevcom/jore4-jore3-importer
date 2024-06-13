package fi.hsl.jore.importer.feature.batch.route_link.support;

import static fi.hsl.jore.importer.util.JoreCollectionUtils.getFirst;
import static org.assertj.core.api.Assertions.assertThat;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.network.route_point.dto.RoutePoint;
import fi.hsl.jore.importer.feature.network.route_point.dto.generated.RoutePointPK;
import fi.hsl.jore.importer.feature.network.route_point.repository.IRoutePointTestRepository;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@IntTest
class RoutePointImportRepositoryTest {

    private final IRoutePointImportRepository importRepository;
    private final IRoutePointTestRepository targetRepository;

    @Autowired
    RoutePointImportRepositoryTest(
            final IRoutePointImportRepository importRepository, final IRoutePointTestRepository targetRepository) {
        this.importRepository = importRepository;
        this.targetRepository = targetRepository;
    }

    @Nested
    @DisplayName("Move data from the staging table to the target table")
    class CommitStagingToTarget {

        @Nested
        @DisplayName("When the staging table has no rows")
        class WhenStagingTableHasNoRows {

            @Nested
            @DisplayName("When the target table has no rows")
            @Sql(scripts = "/sql/importer/drop_tables.sql")
            class WhenTargetTableHasNoRows {

                @BeforeEach
                public void beforeEach() {
                    assertThat(targetRepository.empty())
                            .overridingErrorMessage("Target repository should be empty at the start of the test")
                            .isTrue();
                }

                @Test
                @DisplayName("Should return an empty result")
                void shouldReturnEmptyResult() {
                    assertThat(importRepository.commitStagingToTarget()).isEmpty();
                }

                @Test
                @DisplayName("Shouldn't perform any operations to the target table")
                void shouldNotPerformAnyOperationsToTargetTable() {
                    assertThat(targetRepository.empty())
                            .overridingErrorMessage("Expected that the target table is empty but it was not")
                            .isTrue();
                }
            }

            @Nested
            @DisplayName("When the target table has one row")
            @ExtendWith(SoftAssertionsExtension.class)
            @Sql(
                    scripts = {
                        "/sql/importer/drop_tables.sql",
                        "/sql/importer/populate_infrastructure_nodes.sql",
                        "/sql/importer/populate_lines.sql",
                        "/sql/importer/populate_routes.sql",
                        "/sql/importer/populate_route_directions.sql",
                        "/sql/importer/populate_route_points.sql"
                    })
            class WhenTargetTableHasOneRow {

                @Test
                @DisplayName("Should delete the existing row from the target table")
                void shouldDeleteExistingRowFromTargetTable(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RoutePointPK>> result = importRepository.commitStagingToTarget();

                    softAssertions
                            .assertThat(result.keySet())
                            .overridingErrorMessage(
                                    "Expected that only delete query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.DELETED);

                    final Set<RoutePointPK> idsOfDeletedRows = result.get(RowStatus.DELETED);

                    softAssertions
                            .assertThat(idsOfDeletedRows)
                            .overridingErrorMessage(
                                    "Expected that only one id was returned but found: %d", idsOfDeletedRows.size())
                            .hasSize(1);

                    softAssertions
                            .assertThat(targetRepository.empty())
                            .overridingErrorMessage("Expected that the target table is empty but it was not")
                            .isTrue();
                }
            }
        }

        @Nested
        @DisplayName("When the staging table has rows")
        @ExtendWith(SoftAssertionsExtension.class)
        class WhenStagingTableHasRows {

            private final UUID EXPECTED_NETWORK_ROUTE_POINT_ID =
                    UUID.fromString("00000cc9-d691-492e-b55c-294b903fca33");
            private final UUID EXPECTED_INFRASTRUCTURE_NODE_ID =
                    UUID.fromString("00002c7a-bd85-43ed-afb9-389b498aaa06");
            private final UUID EXPECTED_NETWORK_ROUTE_DIRECTION_ID =
                    UUID.fromString("6f93fa6b-8a19-4b98-bd84-b8409e670c70");
            private final String EXPECTED_NETWORK_ROUTE_POINT_EXT_ID = "1234528-1113227";
            private final int EXPECTED_NETWORK_ROUTE_POINT_ORDER_NUMBER = 162;

            @Nested
            @DisplayName("When the target table is empty")
            @Sql(
                    scripts = {
                        "/sql/importer/drop_tables.sql",
                        "/sql/importer/populate_infrastructure_nodes.sql",
                        "/sql/importer/populate_lines.sql",
                        "/sql/importer/populate_routes.sql",
                        "/sql/importer/populate_route_directions.sql",
                        "/sql/importer/populate_route_points_staging.sql",
                    })
            class WhenTargetTableIsEmpty {

                @BeforeEach
                public void beforeEach() {
                    assertThat(targetRepository.empty())
                            .overridingErrorMessage("Target repository should be empty at the start of the test")
                            .isTrue();
                }

                @Test
                @DisplayName("Should insert one new row into the target table")
                void shouldInsertNewRowIntoTargetTable(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RoutePointPK>> result = importRepository.commitStagingToTarget();

                    softAssertions
                            .assertThat(result.keySet())
                            .overridingErrorMessage(
                                    "Expected that only insert query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.INSERTED);

                    final Set<RoutePointPK> insertedIds = result.get(RowStatus.INSERTED);

                    softAssertions
                            .assertThat(insertedIds)
                            .overridingErrorMessage(
                                    "Expected that only one id was returned but found: %d", insertedIds.size())
                            .hasSize(1);
                }

                @Test
                @DisplayName("Should return the id of the inserted route point")
                void shouldReturnIdOfInsertedRoutePoint() {
                    final Map<RowStatus, Set<RoutePointPK>> result = importRepository.commitStagingToTarget();

                    final RoutePointPK id = getFirst(result.get(RowStatus.INSERTED));
                    final Set<RoutePointPK> dbIds = targetRepository.findAllIds();
                    assertThat(dbIds)
                            .overridingErrorMessage(
                                    "Expected the database to contain row with id: %s but found: %s", id, dbIds)
                            .containsOnly(id);
                }

                @Test
                @DisplayName("Should insert a new route point to the target table")
                void shouldInsertNewRoutePointToTargetTable(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RoutePointPK>> result = importRepository.commitStagingToTarget();
                    final RoutePointPK id = getFirst(result.get(RowStatus.INSERTED));

                    final RoutePoint inserted = targetRepository.findById(id).get();

                    softAssertions
                            .assertThat(inserted.externalId().value())
                            .overridingErrorMessage(
                                    "Expected the route point ext id to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_POINT_EXT_ID,
                                    inserted.externalId().value())
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_POINT_EXT_ID);

                    softAssertions
                            .assertThat(inserted.routeDirection().value())
                            .overridingErrorMessage(
                                    "Expected the route point direction id to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_DIRECTION_ID,
                                    inserted.routeDirection().value())
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_DIRECTION_ID);

                    softAssertions
                            .assertThat(inserted.node().value())
                            .overridingErrorMessage(
                                    "Expected the infrastructure node id to be: %s but was: %s",
                                    EXPECTED_INFRASTRUCTURE_NODE_ID,
                                    inserted.node().value())
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_NODE_ID);

                    softAssertions
                            .assertThat(inserted.orderNumber())
                            .overridingErrorMessage(
                                    "Expected the order number to be: %d but was: %d",
                                    EXPECTED_NETWORK_ROUTE_POINT_ORDER_NUMBER, inserted.orderNumber())
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_POINT_ORDER_NUMBER);
                }
            }

            @Nested
            @DisplayName("When the target table contains the imported route point")
            @ExtendWith(SoftAssertionsExtension.class)
            @Sql(
                    scripts = {
                        "/sql/importer/drop_tables.sql",
                        "/sql/importer/populate_infrastructure_nodes.sql",
                        "/sql/importer/populate_lines.sql",
                        "/sql/importer/populate_routes.sql",
                        "/sql/importer/populate_route_directions.sql",
                        "/sql/importer/populate_route_points_staging.sql",
                        "/sql/importer/populate_route_points.sql"
                    })
            class WhenTargetTableContainsImportedRoutePoint {

                @Test
                @DisplayName("Should update the information of the existing row")
                void shouldUpdateInformationOfExistingRow(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RoutePointPK>> result = importRepository.commitStagingToTarget();

                    softAssertions
                            .assertThat(result.keySet())
                            .overridingErrorMessage(
                                    "Expected that only update query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.UPDATED);

                    final Set<RoutePointPK> idsOfUpdatedRows = result.get(RowStatus.UPDATED);

                    softAssertions
                            .assertThat(idsOfUpdatedRows)
                            .overridingErrorMessage(
                                    "Expected that only one id was returned but found: %d", idsOfUpdatedRows.size())
                            .hasSize(1);

                    final RoutePointPK idOfUpdatedRow =
                            idsOfUpdatedRows.iterator().next();
                    softAssertions
                            .assertThat(idOfUpdatedRow.value())
                            .overridingErrorMessage(
                                    "Expected the id of updated row to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_POINT_ID, idOfUpdatedRow.value())
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_POINT_ID);
                }

                @Test
                @DisplayName("Should update the order number of the existing route point")
                void shouldUpdateOrderNumberOfExistingRoutePoint(SoftAssertions softAssertions) {
                    importRepository.commitStagingToTarget();

                    final RoutePoint updated = targetRepository
                            .findById(RoutePointPK.of(EXPECTED_NETWORK_ROUTE_POINT_ID))
                            .get();

                    softAssertions
                            .assertThat(updated.externalId().value())
                            .overridingErrorMessage(
                                    "Expected the route point ext id to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_POINT_EXT_ID,
                                    updated.externalId().value())
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_POINT_EXT_ID);

                    softAssertions
                            .assertThat(updated.routeDirection().value())
                            .overridingErrorMessage(
                                    "Expected the route point direction id to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_DIRECTION_ID,
                                    updated.routeDirection().value())
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_DIRECTION_ID);

                    softAssertions
                            .assertThat(updated.node().value())
                            .overridingErrorMessage(
                                    "Expected the infrastructure node id to be: %s but was: %s",
                                    EXPECTED_INFRASTRUCTURE_NODE_ID,
                                    updated.node().value())
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_NODE_ID);

                    softAssertions
                            .assertThat(updated.orderNumber())
                            .overridingErrorMessage(
                                    "Expected the order number to be: %d but was: %d",
                                    EXPECTED_NETWORK_ROUTE_POINT_ORDER_NUMBER, updated.orderNumber())
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_POINT_ORDER_NUMBER);
                }
            }
        }
    }
}

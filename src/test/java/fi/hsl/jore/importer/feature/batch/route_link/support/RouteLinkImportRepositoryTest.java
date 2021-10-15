package fi.hsl.jore.importer.feature.batch.route_link.support;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.network.route_link.dto.RouteLink;
import fi.hsl.jore.importer.feature.network.route_link.dto.generated.RouteLinkPK;
import fi.hsl.jore.importer.feature.network.route_link.repository.IRouteLinkTestRepository;
import fi.hsl.jore.importer.feature.network.route_point.dto.generated.RoutePointPK;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntTest
class RouteLinkImportRepositoryTest {

    private final IRouteLinkImportRepository importRepository;
    private final IRouteLinkTestRepository targetRepository;

    @Autowired
    RouteLinkImportRepositoryTest(final IRouteLinkImportRepository importRepository,
                                  final IRouteLinkTestRepository targetRepository) {
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
            @Sql(scripts = "/sql/destination/drop_tables.sql")
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
            @Sql(scripts = {
                    "/sql/destination/drop_tables.sql",
                    "/sql/destination/populate_infrastructure_nodes.sql",
                    "/sql/destination/populate_infrastructure_links.sql",
                    "/sql/destination/populate_lines.sql",
                    "/sql/destination/populate_routes.sql",
                    "/sql/destination/populate_route_directions.sql",
                    "/sql/destination/populate_route_points.sql",
                    "/sql/destination/populate_route_links.sql"
            })
            class WhenTargetTableHasOneRow {

                @Test
                @DisplayName("Should delete the existing row from the target table")
                void shouldDeleteExistingRowFromTargetTable(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RouteLinkPK>> result = importRepository.commitStagingToTarget();

                    softAssertions.assertThat(result.keySet())
                            .overridingErrorMessage("Expected that only delete query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.DELETED);

                    final Set<RouteLinkPK> idsOfDeletedRows = result.get(RowStatus.DELETED).get();

                    softAssertions.assertThat(idsOfDeletedRows)
                            .overridingErrorMessage(
                                    "Expected that only one id was returned but found: %d",
                                    idsOfDeletedRows.size()
                            )
                            .hasSize(1);

                    softAssertions.assertThat(targetRepository.empty())
                            .overridingErrorMessage("Expected that the target table is empty but it was not")
                            .isTrue();
                }
            }
        }

        @Nested
        @DisplayName("When the staging table has rows")
        @ExtendWith(SoftAssertionsExtension.class)
        class WhenStagingTableHasRows {

            private final UUID EXPECTED_NETWORK_ROUTE_LINK_ID = UUID.fromString("00000cc9-d691-492e-b55c-294b903fca33");
            private final UUID EXPECTED_NETWORK_ROUTE_DIRECTION_ID = UUID.fromString("6f93fa6b-8a19-4b98-bd84-b8409e670c70");
            private final UUID EXPECTED_INFRASTRUCTURE_LINK_ID = UUID.fromString("00018613-e3a1-41be-9ba0-a95f61d70aec");
            private final String EXPECTED_NETWORK_ROUTE_LINK_EXT_ID = "1111111";
            private final int EXPECTED_NETWORK_ROUTE_LINK_ORDER_NUMBER = 10;

            @Nested
            @DisplayName("When the target table is empty")
            @Sql(scripts = {
                    "/sql/destination/drop_tables.sql",
                    "/sql/destination/populate_infrastructure_nodes.sql",
                    "/sql/destination/populate_infrastructure_links.sql",
                    "/sql/destination/populate_lines.sql",
                    "/sql/destination/populate_routes.sql",
                    "/sql/destination/populate_route_directions.sql",
                    "/sql/destination/populate_route_points.sql",
                    "/sql/destination/populate_route_links_staging.sql"
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
                    final Map<RowStatus, Set<RouteLinkPK>> result = importRepository.commitStagingToTarget();

                    softAssertions.assertThat(result.keySet())
                            .overridingErrorMessage("Expected that only insert query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.INSERTED);

                    final Set<RouteLinkPK> insertedIds = result.get(RowStatus.INSERTED).get();

                    softAssertions.assertThat(insertedIds)
                            .overridingErrorMessage(
                                    "Expected that only one id was returned but found: %d",
                                    insertedIds.size()
                            )
                            .hasSize(1);
                }

                @Test
                @DisplayName("Should return the id of the inserted route link")
                void shouldReturnIdOfInsertedRouteLink() {
                    final Map<RowStatus, Set<RouteLinkPK>> result = importRepository.commitStagingToTarget();

                    final RouteLinkPK id = result.get(RowStatus.INSERTED).get().get();
                    final Set<RouteLinkPK> dbIds = targetRepository.findAllIds();
                    assertThat(dbIds)
                            .overridingErrorMessage(
                                    "Expected the database to contain row with id: %s but found: %s",
                                    id,
                                    dbIds
                            )
                            .containsOnly(id);
                }

                @Test
                @DisplayName("Should insert a new route link into the target table")
                void shouldInsertNewRouteLinkIntoTargetTable(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RouteLinkPK>> result = importRepository.commitStagingToTarget();
                    final RouteLinkPK id = result.get(RowStatus.INSERTED).get().get();

                    RouteLink inserted = targetRepository.findById(id).get();

                    softAssertions.assertThat(inserted.routeDirection().value())
                            .overridingErrorMessage(
                                    "Expected the network route direction id to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_DIRECTION_ID,
                                    inserted.routeDirection().value()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_DIRECTION_ID);

                    softAssertions.assertThat(inserted.externalId().value())
                            .overridingErrorMessage(
                                    "Expected the network route link ext id to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_LINK_EXT_ID,
                                    inserted.externalId().value()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_LINK_EXT_ID);

                    softAssertions.assertThat(inserted.link().value())
                            .overridingErrorMessage(
                                    "Expected the infrastructure link id to be: %s but was: %s",
                                    EXPECTED_INFRASTRUCTURE_LINK_ID,
                                    inserted.link().value()
                            )
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ID);

                    softAssertions.assertThat(inserted.orderNumber())
                            .overridingErrorMessage(
                                    "Expected the route link order number to be: %d but was: %d",
                                    EXPECTED_NETWORK_ROUTE_LINK_ORDER_NUMBER,
                                    inserted.orderNumber()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_LINK_ORDER_NUMBER);
                }
            }

            @Nested
            @DisplayName("When the target table is empty")
            @Sql(scripts = {
                    "/sql/destination/drop_tables.sql",
                    "/sql/destination/populate_infrastructure_nodes.sql",
                    "/sql/destination/populate_infrastructure_links.sql",
                    "/sql/destination/populate_lines.sql",
                    "/sql/destination/populate_routes.sql",
                    "/sql/destination/populate_route_directions.sql",
                    "/sql/destination/populate_route_points.sql",
                    "/sql/destination/populate_route_links.sql",
                    "/sql/destination/populate_route_links_staging.sql"
            })
            class WhenTargetTableContainsImportedRouteLink {

                @Test
                @DisplayName("Should update the information of the existing row")
                void shouldUpdateInformationOfExistingRow(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RouteLinkPK>> result = importRepository.commitStagingToTarget();

                    softAssertions.assertThat(result.keySet())
                            .overridingErrorMessage("Expected that only update query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.UPDATED);

                    final Set<RouteLinkPK> idsOfUpdatedRows = result.get(RowStatus.UPDATED).get();

                    softAssertions.assertThat(idsOfUpdatedRows)
                            .overridingErrorMessage(
                                    "Expected that only one id was returned but found: %d",
                                    idsOfUpdatedRows.size()
                            )
                            .hasSize(1);

                    final RouteLinkPK idOfUpdatedRow = idsOfUpdatedRows.iterator().next();
                    softAssertions.assertThat(idOfUpdatedRow.value())
                            .overridingErrorMessage(
                                    "Expected the id of updated row to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_LINK_ID,
                                    idOfUpdatedRow.value()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_LINK_ID);
                }

                @Test
                @DisplayName("Should update the order number of the existing network route link")
                void shouldUpdateOrderNumberOfExistingNetworkRouteLink(SoftAssertions softAssertions) {
                    importRepository.commitStagingToTarget();
                    final RouteLink inserted = targetRepository.findById(RouteLinkPK.of(EXPECTED_NETWORK_ROUTE_LINK_ID)).get();

                    softAssertions.assertThat(inserted.routeDirection().value())
                            .overridingErrorMessage(
                                    "Expected the network route direction id to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_DIRECTION_ID,
                                    inserted.routeDirection().value()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_DIRECTION_ID);

                    softAssertions.assertThat(inserted.externalId().value())
                            .overridingErrorMessage(
                                    "Expected the network route link ext id to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_LINK_EXT_ID,
                                    inserted.externalId().value()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_LINK_EXT_ID);

                    softAssertions.assertThat(inserted.link().value())
                            .overridingErrorMessage(
                                    "Expected the infrastructure link id to be: %s but was: %s",
                                    EXPECTED_INFRASTRUCTURE_LINK_ID,
                                    inserted.link().value()
                            )
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ID);

                    softAssertions.assertThat(inserted.orderNumber())
                            .overridingErrorMessage(
                                    "Expected the route link order number to be: %d but was: %d",
                                    EXPECTED_NETWORK_ROUTE_LINK_ORDER_NUMBER,
                                    inserted.orderNumber()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_LINK_ORDER_NUMBER);
                }
            }
        }
    }
}
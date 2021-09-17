package fi.hsl.jore.importer.feature.batch.route.support;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.network.route.dto.Route;
import fi.hsl.jore.importer.feature.network.route.dto.generated.RoutePK;
import fi.hsl.jore.importer.feature.network.route.repository.IRouteTestRepository;
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
class RouteImportRepositoryTest {

    private final IRouteImportRepository importRepository;
    private final IRouteTestRepository targetRepository;

    @Autowired
    RouteImportRepositoryTest(IRouteImportRepository importRepository,
                              IRouteTestRepository targetRepository) {
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
                    "/sql/destination/populate_lines.sql",
                    "/sql/destination/populate_routes.sql"
            })
            class WhenTargetTableHasOneRow {

                @Test
                @DisplayName("Should delete the existing row from the target table")
                void shouldDeleteExistingRowFromTargetTable(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RoutePK>> result = importRepository.commitStagingToTarget();

                    softAssertions.assertThat(result.keySet())
                            .overridingErrorMessage("Expected that only delete query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.DELETED);

                    final Set<RoutePK> idsOfDeletedRows = result.get(RowStatus.DELETED).get();

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

            @Nested
            @DisplayName("When the target table is empty")
            @Sql(scripts = {
                    "/sql/destination/drop_tables.sql",
                    "/sql/destination/populate_lines.sql",
                    "/sql/destination/populate_routes_staging.sql"
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
                    final Map<RowStatus, Set<RoutePK>> result = importRepository.commitStagingToTarget();

                    softAssertions.assertThat(result.keySet())
                            .overridingErrorMessage("Expected that only insert query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.INSERTED);

                    final Set<RoutePK> insertedIds = result.get(RowStatus.INSERTED).get();

                    softAssertions.assertThat(insertedIds)
                            .overridingErrorMessage(
                                    "Expected that only one id was returned but found: %d",
                                    insertedIds.size()
                            )
                            .hasSize(1);
                }

                @Test
                @DisplayName("Should return the id of the inserted route")
                void shouldReturnIdOfInsertedRoute() {
                    final Map<RowStatus, Set<RoutePK>> result = importRepository.commitStagingToTarget();

                    RoutePK id = result.get(RowStatus.INSERTED).get().get();
                    Set<RoutePK> dbIds = targetRepository.findAllIds();
                    assertThat(dbIds)
                            .overridingErrorMessage(
                                    "Expected the database to contain row with id: %s but found: %s",
                                    id,
                                    dbIds
                            )
                            .containsOnly(id);
                }
            }

            @Nested
            @DisplayName("When the target table contains the imported route")
            @ExtendWith(SoftAssertionsExtension.class)
            @Sql(scripts = {
                    "/sql/destination/drop_tables.sql",
                    "/sql/destination/populate_lines.sql",
                    "/sql/destination/populate_routes_staging.sql",
                    "/sql/destination/populate_routes.sql"
            })
            class WhenTargetTableContainsImportedRoute {

                private final UUID EXPECTED_NETWORK_ROUTE_ID = UUID.fromString("484d89ae-f365-4c9b-bb1a-8f7b783e95f3");
                private final UUID EXPECTED_NETWORK_LINE_ID = UUID.fromString("579db108-1f52-4364-9815-5f17c84ce3fb");
                private final String EXPECTED_NETWORK_ROUTE_EXT_ID = "1001";
                private final String EXPECTED_NETWORK_ROUTE_NUMBER = "1";
                private final String EXPECTED_FINNISH_ROUTE_NAME = "Keskustori - Etelä-Hervanta";
                private final String EXPECTED_SWEDISH_ROUTE_NAME = "Central torget - Södra Hervanta";

                private final String LOCALE_FI_FI = "fi_FI";
                private final String LOCALE_SV_SE = "sv_SE";

                @Test
                @DisplayName("Should update the information of the existing row")
                void shouldUpdateInformationOfExistingRow(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RoutePK>> result = importRepository.commitStagingToTarget();

                    softAssertions.assertThat(result.keySet())
                            .overridingErrorMessage("Expected that only update query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.UPDATED);

                    final Set<RoutePK> idsOfUpdatedRows = result.get(RowStatus.UPDATED).get();

                    softAssertions.assertThat(idsOfUpdatedRows)
                            .overridingErrorMessage(
                                    "Expected that only one id was returned but found: %d",
                                    idsOfUpdatedRows.size()
                            )
                            .hasSize(1);
                }

                @Test
                @DisplayName("Should update only the route name of the existing route")
                void shouldUpdateOnlyRouteNameOfExistingRoute(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RoutePK>> result = importRepository.commitStagingToTarget();

                    RoutePK id = result.get(RowStatus.UPDATED).get().get();
                    Route updated = targetRepository.findById(id).get();

                    softAssertions.assertThat(updated.pk().value())
                            .overridingErrorMessage(
                                    "Expected the network route id to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_ID,
                                    updated.pk().value()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_ID);

                    softAssertions.assertThat(updated.line().value())
                            .overridingErrorMessage(
                                    "Expected the network line id to be: %s but was: %s",
                                    EXPECTED_NETWORK_LINE_ID,
                                    updated.line().value()
                            )
                            .isEqualTo(EXPECTED_NETWORK_LINE_ID);

                    softAssertions.assertThat(updated.externalId().value())
                            .overridingErrorMessage(
                                    "Expected the network route ext id to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_EXT_ID,
                                    updated.externalId().value()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_EXT_ID);

                    softAssertions.assertThat(updated.routeNumber())
                            .overridingErrorMessage(
                                    "Expected the route number to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_NUMBER,
                                    updated.routeNumber()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_NUMBER);

                    MultilingualString routeName = updated.name();

                    String finnishRouteName = routeName.values().get(LOCALE_FI_FI).get();
                    softAssertions.assertThat(finnishRouteName)
                            .overridingErrorMessage(
                                    "Expected that the updated Finnish route name to be: %s but was: %s",
                                    EXPECTED_FINNISH_ROUTE_NAME,
                                    finnishRouteName
                            )
                            .isEqualTo(EXPECTED_FINNISH_ROUTE_NAME);

                    String swedishRouteName = routeName.values().get(LOCALE_SV_SE).get();
                    softAssertions.assertThat(swedishRouteName)
                            .overridingErrorMessage(
                                    "Expected that the updated Swedish route name to be: %s but was: %s",
                                    EXPECTED_SWEDISH_ROUTE_NAME,
                                    swedishRouteName
                            )
                            .isEqualTo(EXPECTED_SWEDISH_ROUTE_NAME);
                }
            }
        }
    }
}

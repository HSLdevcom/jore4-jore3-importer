package fi.hsl.jore.importer.feature.batch.route_link.support;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.RouteStopPoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.generated.RouteStopPointPK;
import fi.hsl.jore.importer.feature.network.route_stop_point.repository.IRouteStopPointTestRepository;
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
class RouteStopPointImportRepositoryTest {

    private final IRouteStopPointImportRepository importRepository;
    private final IRouteStopPointTestRepository targetRepository;

    @Autowired
    RouteStopPointImportRepositoryTest(final IRouteStopPointImportRepository importRepository,
                                       final IRouteStopPointTestRepository targetRepository) {
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
                    "/sql/destination/populate_lines.sql",
                    "/sql/destination/populate_routes.sql",
                    "/sql/destination/populate_route_directions.sql",
                    "/sql/destination/populate_route_points.sql",
                    "/sql/destination/populate_route_stop_points.sql"
            })
            class WhenTargetTableHasOneRow {

                @Test
                @DisplayName("Should delete the existing row from the target table")
                void shouldDeleteExistingRowFromTargetTable(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RouteStopPointPK>> result = importRepository.commitStagingToTarget();

                    softAssertions.assertThat(result.keySet())
                            .overridingErrorMessage("Expected that only delete query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.DELETED);

                    final Set<RouteStopPointPK> idsOfDeletedRows = result.get(RowStatus.DELETED).get();

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

            private final UUID EXPECTED_NETWORK_ROUTE_POINT_ID = UUID.fromString("00000cc9-d691-492e-b55c-294b903fca33");
            private final String EXPECTED_NETWORK_ROUTE_STOP_POINT_EXT_ID = "1234528-1113227";
            private final int EXPECTED_NETWORK_ROUTE_STOP_POINT_ORDER_NUMBER = 6;
            private final boolean EXPECTED_NETWORK_ROUTE_STOP_POINT_HASTUS_POINT = true;
            private final int EXPECTED_NETWORK_ROUTE_STOP_POINT_TIMETABLE_COLUMN = 12;

            @Nested
            @DisplayName("When the target table is empty")
            @Sql(scripts = {
                    "/sql/destination/drop_tables.sql",
                    "/sql/destination/populate_infrastructure_nodes.sql",
                    "/sql/destination/populate_lines.sql",
                    "/sql/destination/populate_routes.sql",
                    "/sql/destination/populate_route_directions.sql",
                    "/sql/destination/populate_route_points.sql",
                    "/sql/destination/populate_route_stop_points_staging.sql"
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
                    final Map<RowStatus, Set<RouteStopPointPK>> result = importRepository.commitStagingToTarget();

                    softAssertions.assertThat(result.keySet())
                            .overridingErrorMessage("Expected that only insert query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.INSERTED);

                    final Set<RouteStopPointPK> insertedIds = result.get(RowStatus.INSERTED).get();

                    softAssertions.assertThat(insertedIds)
                            .overridingErrorMessage(
                                    "Expected that only one id was returned but found: %d",
                                    insertedIds.size()
                            )
                            .hasSize(1);
                }

                @Test
                @DisplayName("Should return the id of the inserted route stop point")
                void shouldReturnIdOfInsertedRouteStopPoint() {
                    final Map<RowStatus, Set<RouteStopPointPK>> result = importRepository.commitStagingToTarget();

                    final RouteStopPointPK id = result.get(RowStatus.INSERTED).get().get();
                    final Set<RouteStopPointPK> dbIds = targetRepository.findAllIds();
                    assertThat(dbIds)
                            .overridingErrorMessage(
                                    "Expected the database to contain row with id: %s but found: %s",
                                    id,
                                    dbIds
                            )
                            .containsOnly(id);
                }

                @Test
                @DisplayName("Should insert a new route point to the target table")
                void shouldInsertNewRoutePointToTargetTable(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RouteStopPointPK>> result = importRepository.commitStagingToTarget();
                    final RouteStopPointPK id = result.get(RowStatus.INSERTED).get().get();

                    final RouteStopPoint inserted = targetRepository.findById(id).get();

                    softAssertions.assertThat(inserted.externalId().value())
                            .overridingErrorMessage(
                                    "Expected the network route stop point ext id to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_STOP_POINT_EXT_ID,
                                    inserted.externalId().value()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_STOP_POINT_EXT_ID);

                    softAssertions.assertThat(inserted.orderNumber())
                            .overridingErrorMessage(
                                    "Expected the network route stop point order number to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_STOP_POINT_ORDER_NUMBER,
                                    inserted.orderNumber()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_STOP_POINT_ORDER_NUMBER);

                    softAssertions.assertThat(inserted.hastusStopPoint())
                            .overridingErrorMessage(
                                    "Expected the network route point hastus point to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_STOP_POINT_HASTUS_POINT,
                                    inserted.hastusStopPoint()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_STOP_POINT_HASTUS_POINT);

                    softAssertions.assertThat(inserted.timetableColumn().get())
                            .overridingErrorMessage(
                                    "Expected the network route stop point timetable column to be: %d but was %d",
                                    EXPECTED_NETWORK_ROUTE_STOP_POINT_TIMETABLE_COLUMN,
                                    inserted.timetableColumn().get()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_STOP_POINT_TIMETABLE_COLUMN);
                }
            }

            @Nested
            @DisplayName("When the target table contains the imported route stop point")
            @ExtendWith(SoftAssertionsExtension.class)
            @Sql(scripts = {
                    "/sql/destination/drop_tables.sql",
                    "/sql/destination/populate_infrastructure_nodes.sql",
                    "/sql/destination/populate_lines.sql",
                    "/sql/destination/populate_routes.sql",
                    "/sql/destination/populate_route_directions.sql",
                    "/sql/destination/populate_route_points.sql",
                    "/sql/destination/populate_route_stop_points_staging.sql",
                    "/sql/destination/populate_route_stop_points.sql"
            })
            class WhenTargetTableContainsImportedRouteStopPoint {

                @Test
                @DisplayName("Should update the information of the existing row")
                void shouldUpdateInformationOfExistingRow(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RouteStopPointPK>> result = importRepository.commitStagingToTarget();

                    softAssertions.assertThat(result.keySet())
                            .overridingErrorMessage("Expected that only update query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.UPDATED);

                    final Set<RouteStopPointPK> idsOfUpdatedRows = result.get(RowStatus.UPDATED).get();

                    softAssertions.assertThat(idsOfUpdatedRows)
                            .overridingErrorMessage(
                                    "Expected that only one id was returned but found: %d",
                                    idsOfUpdatedRows.size()
                            )
                            .hasSize(1);

                    final RouteStopPointPK idOfUpdatedRow = idsOfUpdatedRows.iterator().next();
                    softAssertions.assertThat(idOfUpdatedRow.value())
                            .overridingErrorMessage(
                                    "Expected the id of updated row to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_POINT_ID,
                                    idOfUpdatedRow.value()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_POINT_ID);
                }

                @Test
                @DisplayName("Should update the information of existing network route stop point")
                void shouldUpdateInformationOfExistingNetworkRouteStopPoint(SoftAssertions softAssertions) {
                    importRepository.commitStagingToTarget();

                    final RouteStopPoint updated = targetRepository.findById(RouteStopPointPK.of(EXPECTED_NETWORK_ROUTE_POINT_ID)).get();

                    softAssertions.assertThat(updated.externalId().value())
                            .overridingErrorMessage(
                                    "Expected the network route stop point ext id to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_STOP_POINT_EXT_ID,
                                    updated.externalId().value()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_STOP_POINT_EXT_ID);

                    softAssertions.assertThat(updated.orderNumber())
                            .overridingErrorMessage(
                                    "Expected the network route stop point order number to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_STOP_POINT_ORDER_NUMBER,
                                    updated.orderNumber()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_STOP_POINT_ORDER_NUMBER);

                    softAssertions.assertThat(updated.hastusStopPoint())
                            .overridingErrorMessage(
                                    "Expected the network route point hastus point to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_STOP_POINT_HASTUS_POINT,
                                    updated.hastusStopPoint()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_STOP_POINT_HASTUS_POINT);

                    softAssertions.assertThat(updated.timetableColumn().get())
                            .overridingErrorMessage(
                                    "Expected the network route stop point timetable column to be: %d but was %d",
                                    EXPECTED_NETWORK_ROUTE_STOP_POINT_TIMETABLE_COLUMN,
                                    updated.timetableColumn().get()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_STOP_POINT_TIMETABLE_COLUMN);
                }
            }
        }
    }
}

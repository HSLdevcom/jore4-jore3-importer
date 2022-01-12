package fi.hsl.jore.importer.feature.batch.route.support;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.route.dto.PersistableJourneyPatternIdMapping;
import fi.hsl.jore.importer.feature.network.route.dto.PersistableRouteIdMapping;
import fi.hsl.jore.importer.feature.network.route.dto.Route;
import fi.hsl.jore.importer.feature.network.route.dto.generated.RoutePK;
import fi.hsl.jore.importer.feature.network.route.repository.IRouteTestRepository;
import io.vavr.collection.List;
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
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntTest
class RouteImportRepositoryTest {

    private final IRouteImportRepository importRepository;
    private final IRouteTestRepository targetRepository;

    @Autowired
    RouteImportRepositoryTest(final IRouteImportRepository importRepository,
                              final IRouteTestRepository targetRepository) {
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

            private final UUID EXPECTED_NETWORK_LINE_ID = UUID.fromString("579db108-1f52-4364-9815-5f17c84ce3fb");
            private final String EXPECTED_NETWORK_ROUTE_EXT_ID = "1001";
            private final String EXPECTED_NETWORK_ROUTE_NUMBER = "1";
            private final String EXPECTED_FINNISH_ROUTE_NAME = "Keskustori - Etelä-Hervanta";
            private final String EXPECTED_SWEDISH_ROUTE_NAME = "Central torget - Södra Hervanta";

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

                    final RoutePK id = result.get(RowStatus.INSERTED).get().get();
                    final Set<RoutePK> dbIds = targetRepository.findAllIds();
                    assertThat(dbIds)
                            .overridingErrorMessage(
                                    "Expected the database to contain row with id: %s but found: %s",
                                    id,
                                    dbIds
                            )
                            .containsOnly(id);
                }

                @Test
                @DisplayName("Should insert the imported row into the target table")
                void shouldInsertImportedRowIntoTargetTable(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RoutePK>> result = importRepository.commitStagingToTarget();
                    final RoutePK id = result.get(RowStatus.INSERTED).get().get();

                    final Route inserted = targetRepository.findById(id).get();

                    softAssertions.assertThat(inserted.line().value())
                            .as("network line id")
                            .isEqualTo(EXPECTED_NETWORK_LINE_ID);

                    softAssertions.assertThat(inserted.externalId().value())
                            .as("network route ext id")
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_EXT_ID);

                    softAssertions.assertThat(inserted.routeNumber())
                            .as("route number")
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_NUMBER);

                    final String finnishRouteName = JoreLocaleUtil.getI18nString(inserted.name(), JoreLocaleUtil.FINNISH);
                    softAssertions.assertThat(finnishRouteName)
                            .as("updated Finnish route name")
                            .isEqualTo(EXPECTED_FINNISH_ROUTE_NAME);

                    final String swedishRouteName = JoreLocaleUtil.getI18nString(inserted.name(), JoreLocaleUtil.SWEDISH);
                    softAssertions.assertThat(swedishRouteName)
                            .as("updated Swedish route name")
                            .isEqualTo(EXPECTED_SWEDISH_ROUTE_NAME);

                    softAssertions.assertThat(inserted.transmodelId())
                            .as("transmodelId")
                            .isEmpty();
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
                    importRepository.commitStagingToTarget();
                    final Route updated = targetRepository.findById(RoutePK.of(EXPECTED_NETWORK_ROUTE_ID)).get();

                    softAssertions.assertThat(updated.line().value())
                            .as("network line id")
                            .isEqualTo(EXPECTED_NETWORK_LINE_ID);

                    softAssertions.assertThat(updated.externalId().value())
                            .as("network route ext id")
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_EXT_ID);

                    softAssertions.assertThat(updated.routeNumber())
                            .as("route number")
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_NUMBER);

                    final String finnishRouteName = JoreLocaleUtil.getI18nString(updated.name(), JoreLocaleUtil.FINNISH);
                    softAssertions.assertThat(finnishRouteName)
                            .as("updated Finnish route name")
                            .isEqualTo(EXPECTED_FINNISH_ROUTE_NAME);

                    final String swedishRouteName = JoreLocaleUtil.getI18nString(updated.name(), JoreLocaleUtil.SWEDISH);
                    softAssertions.assertThat(swedishRouteName)
                            .as("updated Swedish route name")
                            .isEqualTo(EXPECTED_SWEDISH_ROUTE_NAME);

                    softAssertions.assertThat(updated.transmodelId())
                            .as("transmodelId")
                            .isEmpty();
                }
            }
        }
    }

    @Nested
    @DisplayName("Set the transmodel ids of existing routes")
    @Sql(scripts = {
            "/sql/destination/drop_tables.sql",
            "/sql/destination/populate_lines.sql",
            "/sql/destination/populate_routes.sql"
    })
    class SetRouteTransmodelIds {

        private final ExternalId EXT_ID = ExternalId.of("1001");
        private final UUID TRANSMODEL_ID = UUID.fromString("51f2686b-166c-4157-bd70-647337e44c8c");

        @Nested
        @DisplayName("When the route isn't found")
        class WhenRouteIsNotFound {

            private static final String UNKNOWN_EXT_ID = "999999999";
            private List<PersistableRouteIdMapping> INPUT = List.of(PersistableRouteIdMapping.of(
                    UNKNOWN_EXT_ID,
                    TRANSMODEL_ID
            ));

            @Test
            @DisplayName("Shouldn't update the transmodel id of the existing route")
            void shouldNotUpdateTransmodelIdOfExistingRoute() {
                importRepository.setRouteTransmodelIds(INPUT);
                final Route route = targetRepository.findByExternalId(EXT_ID).get();
                assertThat(route.transmodelId()).isEmpty();
            }
        }

        @Nested
        @DisplayName("When the route is found")
        class WhenRouteIsFound {

            private List<PersistableRouteIdMapping> INPUT = List.of(PersistableRouteIdMapping.of(
                    EXT_ID.value(),
                    TRANSMODEL_ID
            ));

            @Test
            @DisplayName("Should update the transmodel id of the existing route")
            void shouldUpdateTransmodelIdOfExistingRoute() {
                importRepository.setRouteTransmodelIds(INPUT);
                final Route route = targetRepository.findByExternalId(EXT_ID).get();
                assertThat(route.transmodelId()).contains(TRANSMODEL_ID);
            }
        }
    }

    @Nested
    @DisplayName("Set the transmodel ids of journey patterns")
    @Sql(scripts = {
            "/sql/destination/drop_tables.sql",
            "/sql/destination/populate_lines.sql",
            "/sql/destination/populate_routes_with_transmodel_ids.sql"
    })
    class SetJourneyPatternTransmodelIds {

        private final UUID JOURNEY_PATTERN_TRANSMODEL_ID = UUID.fromString("8b6a8f9c-64b2-4853-9160-d5eb46836fb7");
        private final UUID ROUTE_TRANSMODEL_ID = UUID.fromString("5bfa9a65-c80f-4af8-be95-8370cb12df50");
        private final UUID ROUTE_ID = UUID.fromString("484d89ae-f365-4c9b-bb1a-8f7b783e95f3");

        @Nested
        @DisplayName("When the route isn't found")
        class WhenRouteIsNotFound {

            private final UUID UNKNOWN_ROUTE_TRANSMODEL_ID = UUID.fromString("ddd49284-ba94-4eab-8071-ffc9f346b274");

            private final PersistableJourneyPatternIdMapping INPUT = PersistableJourneyPatternIdMapping.of(UNKNOWN_ROUTE_TRANSMODEL_ID,
                    JOURNEY_PATTERN_TRANSMODEL_ID
            );

            @Test
            @DisplayName("Shouldn't update the journey pattern id of an existing route")
            void shouldNotUpdateJourneyPatternIdOfExistingRoute() {
                importRepository.setJourneyPatternTransmodelIds(List.of(INPUT));

                final Route route = targetRepository.findById(RoutePK.of(ROUTE_ID)).get();
                assertThat(route.journeyPatternTransmodelId()).isEmpty();
            }
        }

        @Nested
        @DisplayName("When the route is found")
        class WhenRouteIsFound {

            private final PersistableJourneyPatternIdMapping INPUT = PersistableJourneyPatternIdMapping.of(ROUTE_TRANSMODEL_ID,
                    JOURNEY_PATTERN_TRANSMODEL_ID
            );

            @Test
            @DisplayName("Should update the journey pattern id of an existing route")
            void shouldUpdateJourneyPatternIdOfExistingRoute() {
                importRepository.setJourneyPatternTransmodelIds(List.of(INPUT));

                final Route route = targetRepository.findById(RoutePK.of(ROUTE_ID)).get();
                assertThat(route.journeyPatternTransmodelId()).contains(JOURNEY_PATTERN_TRANSMODEL_ID);
            }
        }
    }
}

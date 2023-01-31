package fi.hsl.jore.importer.feature.batch.route_direction.support;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.direction_type.field.DirectionType;
import fi.hsl.jore.importer.feature.network.route_direction.dto.PersistableJourneyPatternIdMapping;
import fi.hsl.jore.importer.feature.network.route_direction.dto.PersistableRouteIdMapping;
import fi.hsl.jore.importer.feature.network.route_direction.dto.RouteDirection;
import fi.hsl.jore.importer.feature.network.route_direction.dto.generated.RouteDirectionPK;
import fi.hsl.jore.importer.feature.network.route_direction.repository.IRouteDirectionTestRepository;
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
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntTest
class RouteDirectionImportRepositoryTest {

    private final IRouteDirectionImportRepository importRepository;
    private final IRouteDirectionTestRepository targetRepository;

    @Autowired
    RouteDirectionImportRepositoryTest(final IRouteDirectionImportRepository importRepository,
                                       final IRouteDirectionTestRepository targetRepository) {
        this.importRepository = importRepository;
        this.targetRepository = targetRepository;
    }

    @Nested
    @DisplayName("Move data from the staging table to the target table")
    class CommitStagingToTarget {

        private final UUID EXPECTED_NETWORK_ROUTE_DIRECTION_ID = UUID.fromString("6f93fa6b-8a19-4b98-bd84-b8409e670c70");
        private final String EXPECTED_NETWORK_ROUTE_DIRECTION_EXT_ID = "1001-2-20211004";
        private final UUID EXPECTED_NETWORK_ROUTE_ID = UUID.fromString("484d89ae-f365-4c9b-bb1a-8f7b783e95f3");
        private final DirectionType EXPECTED_NETWORK_ROUTE_DIRECTION_TYPE = DirectionType.INBOUND;
        private final int EXPECTED_NETWORK_ROUTE_DIRECTION_LENGTH = 10700;
        private final String EXPECTED_FINNISH_NAME = "Keskustori - Kaleva - Etelä-Hervanta";
        private final String EXPECTED_SWEDISH_NAME = "Central torget - Kaleva - Södra Hervanta";
        private final String EXPECTED_FINNISH_NAME_SHORT = "Keskustori-Etelä-Hervanta";
        private final String EXPECTED_SWEDISH_NAME_SHORT = "Central torget-Södra Hervanta";
        private final String EXPECTED_FINNISH_ORIGIN = "Keskustori";
        private final String EXPECTED_SWEDISH_ORIGIN = "Central torget";
        private final String EXPECTED_FINNISH_DESTINATION = "Etelä-Hervanta";
        private final String EXPECTED_SWEDISH_DESTINATION = "Södra Hervanta";

        //The expected date range is: [2021-01-01..2022-01-01). We need pass a weird end date to the between() method
        //because its implementation moves the end day forward by one day.
        private final DateRange EXPECTED_VALID_DATE_RANGE = DateRange.between(LocalDate.parse("2021-01-01"), LocalDate.parse("2021-12-31"));

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
                    "/sql/destination/populate_routes.sql",
                    "/sql/destination/populate_route_directions.sql"
            })
            class WhenTargetTableHasOneRow {

                @Test
                @DisplayName("Should delete the existing row from the target table")
                void shouldDeleteExistingRowFromTargetTable(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RouteDirectionPK>> result = importRepository.commitStagingToTarget();

                    softAssertions.assertThat(result.keySet())
                            .overridingErrorMessage("Expected that only delete query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.DELETED);

                    final Set<RouteDirectionPK> idsOfDeletedRows = result.get(RowStatus.DELETED).get();

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
                    "/sql/destination/populate_routes.sql",
                    "/sql/destination/populate_route_directions_staging.sql"
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
                    final Map<RowStatus, Set<RouteDirectionPK>> result = importRepository.commitStagingToTarget();

                    softAssertions.assertThat(result.keySet())
                            .overridingErrorMessage("Expected that only insert query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.INSERTED);

                    final Set<RouteDirectionPK> insertedIds = result.get(RowStatus.INSERTED).get();

                    softAssertions.assertThat(insertedIds)
                            .overridingErrorMessage(
                                    "Expected that only one id was returned but found: %d",
                                    insertedIds.size()
                            )
                            .hasSize(1);
                }

                @Test
                @DisplayName("Should return the id of the inserted route direction")
                void shouldReturnIdOfInsertedRouteDirection() {
                    final Map<RowStatus, Set<RouteDirectionPK>> result = importRepository.commitStagingToTarget();

                    final RouteDirectionPK id = result.get(RowStatus.INSERTED).get().get();
                    final Set<RouteDirectionPK> dbIds = targetRepository.findAllIds();
                    assertThat(dbIds)
                            .overridingErrorMessage(
                                    "Expected the database to contain row with id: %s but found: %s",
                                    id,
                                    dbIds
                            )
                            .containsOnly(id);
                }

                @Test
                @DisplayName("Should insert a new route direction to the target table")
                void shouldInsertNewRouteDirectionToTargetTable(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RouteDirectionPK>> result = importRepository.commitStagingToTarget();
                    final RouteDirectionPK id = result.get(RowStatus.INSERTED).get().get();

                    final RouteDirection inserted = targetRepository.findById(id).get();

                    softAssertions.assertThat(inserted.routeId().value())
                            .overridingErrorMessage(
                                    "Expected the network route id to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_ID,
                                    inserted.routeId().value()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_ID);

                    softAssertions.assertThat(inserted.direction())
                            .overridingErrorMessage(
                                    "Expected the direction type to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_DIRECTION_TYPE,
                                    inserted.direction()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_DIRECTION_TYPE);

                    softAssertions.assertThat(inserted.lengthMeters().get())
                            .overridingErrorMessage(
                                    "Expected the length of the route direction to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_DIRECTION_LENGTH,
                                    inserted.lengthMeters().get()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_DIRECTION_LENGTH);

                    softAssertions.assertThat(inserted.externalId().value())
                            .overridingErrorMessage(
                                    "Expected the route direction external id to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_DIRECTION_EXT_ID,
                                    inserted.externalId().value()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_DIRECTION_EXT_ID);

                    final String actualFinnishName = JoreLocaleUtil.getI18nString(inserted.name(), JoreLocaleUtil.FINNISH);
                    softAssertions.assertThat(actualFinnishName)
                            .overridingErrorMessage(
                                    "Expected the Finnish name to be: %s but was: %s",
                                    EXPECTED_FINNISH_NAME,
                                    actualFinnishName
                            )
                            .isEqualTo(EXPECTED_FINNISH_NAME);

                    final String actualSwedishName = JoreLocaleUtil.getI18nString(inserted.name(), JoreLocaleUtil.SWEDISH);
                    softAssertions.assertThat(actualSwedishName)
                            .overridingErrorMessage(
                                    "Expected the Swedish name to be: %s but was: %s",
                                    EXPECTED_SWEDISH_NAME,
                                    actualSwedishName
                            )
                            .isEqualTo(EXPECTED_SWEDISH_NAME);

                    final String actualFinnishNameShort = JoreLocaleUtil.getI18nString(inserted.nameShort(), JoreLocaleUtil.FINNISH);
                    softAssertions.assertThat(actualFinnishNameShort)
                            .overridingErrorMessage(
                                    "Expected the Finnish short name to be: %s but was: %s",
                                    EXPECTED_FINNISH_NAME_SHORT,
                                    actualFinnishNameShort
                            )
                            .isEqualTo(EXPECTED_FINNISH_NAME_SHORT);

                    final String actualSwedishNameShort = JoreLocaleUtil.getI18nString(inserted.nameShort(), JoreLocaleUtil.SWEDISH);
                    softAssertions.assertThat(actualSwedishNameShort)
                            .overridingErrorMessage(
                                    "Expected the Swedish short name to be: %s but was: %s",
                                    EXPECTED_SWEDISH_NAME_SHORT,
                                    actualSwedishNameShort
                            )
                            .isEqualTo(EXPECTED_SWEDISH_NAME_SHORT);

                    final String actualFinnishOrigin = JoreLocaleUtil.getI18nString(inserted.origin(), JoreLocaleUtil.FINNISH);
                    softAssertions.assertThat(actualFinnishOrigin)
                            .overridingErrorMessage(
                                    "Expected the Finnish origin to be: %s but was: %s",
                                    EXPECTED_FINNISH_ORIGIN,
                                    actualFinnishOrigin
                            )
                            .isEqualTo(EXPECTED_FINNISH_ORIGIN);

                    final String actualSwedishOrigin = JoreLocaleUtil.getI18nString(inserted.origin(), JoreLocaleUtil.SWEDISH);
                    softAssertions.assertThat(actualSwedishOrigin)
                            .overridingErrorMessage(
                                    "Expected the Swedish origin to be: %s but was: %s",
                                    EXPECTED_SWEDISH_ORIGIN,
                                    actualSwedishOrigin
                            )
                            .isEqualTo(EXPECTED_SWEDISH_ORIGIN);

                    final String actualFinnishDestination = JoreLocaleUtil.getI18nString(inserted.destination(), JoreLocaleUtil.FINNISH);
                    softAssertions.assertThat(actualFinnishDestination)
                            .overridingErrorMessage(
                                    "Expected the Finnish destination to be: %s but was: %s",
                                    EXPECTED_FINNISH_DESTINATION,
                                    actualFinnishDestination
                            )
                            .isEqualTo(EXPECTED_FINNISH_DESTINATION);

                    final String actualSwedishDestination = JoreLocaleUtil.getI18nString(inserted.destination(), JoreLocaleUtil.SWEDISH);
                    softAssertions.assertThat(actualSwedishDestination)
                            .overridingErrorMessage(
                                    "Expected the Swedish destination to be: %s but was: %s",
                                    EXPECTED_SWEDISH_DESTINATION,
                                    actualSwedishDestination
                            )
                            .isEqualTo(EXPECTED_SWEDISH_DESTINATION);

                    softAssertions.assertThat(inserted.validTime())
                            .overridingErrorMessage(
                                    "Expected the valid data range to be: %s but was: %s",
                                    EXPECTED_VALID_DATE_RANGE,
                                    inserted.validTime()
                            )
                            .isEqualTo(EXPECTED_VALID_DATE_RANGE);
                }
            }

            @Nested
            @DisplayName("When the target table contains the imported route direction")
            @ExtendWith(SoftAssertionsExtension.class)
            @Sql(scripts = {
                    "/sql/destination/drop_tables.sql",
                    "/sql/destination/populate_lines.sql",
                    "/sql/destination/populate_routes.sql",
                    "/sql/destination/populate_route_directions_staging.sql",
                    "/sql/destination/populate_route_directions.sql"
            })
            class WhenTargetTableContainsImportedRouteDirection {

                @Test
                @DisplayName("Should update the information of the existing row")
                void shouldUpdateInformationOfExistingRow(SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<RouteDirectionPK>> result = importRepository.commitStagingToTarget();

                    softAssertions.assertThat(result.keySet())
                            .overridingErrorMessage("Expected that only update query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.UPDATED);

                    final Set<RouteDirectionPK> idsOfUpdatedRows = result.get(RowStatus.UPDATED).get();

                    softAssertions.assertThat(idsOfUpdatedRows)
                            .overridingErrorMessage(
                                    "Expected that only one id was returned but found: %d",
                                    idsOfUpdatedRows.size()
                            )
                            .hasSize(1);
                }

                @Test
                @DisplayName("Should update the information of the route direction found from the target table")
                void shouldUpdateInformationOfRouteDirectionFoundFromTargetTable(SoftAssertions softAssertions) {
                    importRepository.commitStagingToTarget();
                    final RouteDirection updated = targetRepository.findById(RouteDirectionPK.of(EXPECTED_NETWORK_ROUTE_DIRECTION_ID)).get();

                    softAssertions.assertThat(updated.routeId().value())
                            .overridingErrorMessage(
                                    "Expected the network route id to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_ID,
                                    updated.routeId().value()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_ID);

                    softAssertions.assertThat(updated.direction())
                            .overridingErrorMessage(
                                    "Expected the direction type to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_DIRECTION_TYPE,
                                    updated.direction()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_DIRECTION_TYPE);

                    softAssertions.assertThat(updated.lengthMeters().get())
                            .overridingErrorMessage(
                                    "Expected the length of the route direction to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_DIRECTION_LENGTH,
                                    updated.lengthMeters().get()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_DIRECTION_LENGTH);

                    softAssertions.assertThat(updated.externalId().value())
                            .overridingErrorMessage(
                                    "Expected the route direction external id to be: %s but was: %s",
                                    EXPECTED_NETWORK_ROUTE_DIRECTION_EXT_ID,
                                    updated.externalId().value()
                            )
                            .isEqualTo(EXPECTED_NETWORK_ROUTE_DIRECTION_EXT_ID);

                    final String actualFinnishName = JoreLocaleUtil.getI18nString(updated.name(), JoreLocaleUtil.FINNISH);
                    softAssertions.assertThat(actualFinnishName)
                            .overridingErrorMessage(
                                    "Expected the Finnish name to be: %s but was: %s",
                                    EXPECTED_FINNISH_NAME,
                                    actualFinnishName
                            )
                            .isEqualTo(EXPECTED_FINNISH_NAME);

                    final String actualSwedishName = JoreLocaleUtil.getI18nString(updated.name(), JoreLocaleUtil.SWEDISH);
                    softAssertions.assertThat(actualSwedishName)
                            .overridingErrorMessage(
                                    "Expected the Swedish name to be: %s but was: %s",
                                    EXPECTED_SWEDISH_NAME,
                                    actualSwedishName
                            )
                            .isEqualTo(EXPECTED_SWEDISH_NAME);

                    final String actualFinnishNameShort = JoreLocaleUtil.getI18nString(updated.nameShort(), JoreLocaleUtil.FINNISH);
                    softAssertions.assertThat(actualFinnishNameShort)
                            .overridingErrorMessage(
                                    "Expected the Finnish short name to be: %s but was: %s",
                                    EXPECTED_FINNISH_NAME_SHORT,
                                    actualFinnishNameShort
                            )
                            .isEqualTo(EXPECTED_FINNISH_NAME_SHORT);

                    final String actualSwedishNameShort = JoreLocaleUtil.getI18nString(updated.nameShort(), JoreLocaleUtil.SWEDISH);
                    softAssertions.assertThat(actualSwedishNameShort)
                            .overridingErrorMessage(
                                    "Expected the Swedish short name to be: %s but was: %s",
                                    EXPECTED_SWEDISH_NAME_SHORT,
                                    actualSwedishNameShort
                            )
                            .isEqualTo(EXPECTED_SWEDISH_NAME_SHORT);

                    final String actualFinnishOrigin = JoreLocaleUtil.getI18nString(updated.origin(), JoreLocaleUtil.FINNISH);
                    softAssertions.assertThat(actualFinnishOrigin)
                            .overridingErrorMessage(
                                    "Expected the Finnish origin to be: %s but was: %s",
                                    EXPECTED_FINNISH_ORIGIN,
                                    actualFinnishOrigin
                            )
                            .isEqualTo(EXPECTED_FINNISH_ORIGIN);

                    final String actualSwedishOrigin = JoreLocaleUtil.getI18nString(updated.origin(), JoreLocaleUtil.SWEDISH);
                    softAssertions.assertThat(actualSwedishOrigin)
                            .overridingErrorMessage(
                                    "Expected the Swedish origin to be: %s but was: %s",
                                    EXPECTED_SWEDISH_ORIGIN,
                                    actualSwedishOrigin
                            )
                            .isEqualTo(EXPECTED_SWEDISH_ORIGIN);

                    final String actualFinnishDestination = JoreLocaleUtil.getI18nString(updated.destination(), JoreLocaleUtil.FINNISH);
                    softAssertions.assertThat(actualFinnishDestination)
                            .overridingErrorMessage(
                                    "Expected the Finnish destination to be: %s but was: %s",
                                    EXPECTED_FINNISH_DESTINATION,
                                    actualFinnishDestination
                            )
                            .isEqualTo(EXPECTED_FINNISH_DESTINATION);

                    final String actualSwedishDestination = JoreLocaleUtil.getI18nString(updated.destination(), JoreLocaleUtil.SWEDISH);
                    softAssertions.assertThat(actualSwedishDestination)
                            .overridingErrorMessage(
                                    "Expected the Swedish destination to be: %s but was: %s",
                                    EXPECTED_SWEDISH_DESTINATION,
                                    actualSwedishDestination
                            )
                            .isEqualTo(EXPECTED_SWEDISH_DESTINATION);

                    softAssertions.assertThat(updated.validTime())
                            .overridingErrorMessage(
                                    "Expected the valid data range to be: %s but was: %s",
                                    EXPECTED_VALID_DATE_RANGE,
                                    updated.validTime()
                            )
                            .isEqualTo(EXPECTED_VALID_DATE_RANGE);
                }
            }
        }
    }

    @Nested
    @DisplayName("Set journey pattern Jore 4 ids")
    @Sql(scripts = {
            "/sql/destination/drop_tables.sql",
            "/sql/destination/populate_lines.sql",
            "/sql/destination/populate_routes.sql",
            "/sql/destination/populate_route_directions.sql"
    })
    class SetJourneyPatternJore4Ids {

        private final UUID ROUTE_DIRECTION_ID = UUID.fromString("6f93fa6b-8a19-4b98-bd84-b8409e670c70");
        private final UUID JOURNEY_PATTERN_JORE4_ID = UUID.fromString("b97185ef-0c27-4548-ac38-3d2cdd0023ac");


        @Nested
        @DisplayName("When the route direction isn't found")
        class WhenRouteDirectionIsNotFound {

            private final UUID UNKNOWN_ROUTE_DIRECTION_ID = UUID.fromString("038174b2-985f-47f7-9676-83cf0b944d88");
            private final List<PersistableJourneyPatternIdMapping> INPUT = List.of(PersistableJourneyPatternIdMapping.of(
                    UNKNOWN_ROUTE_DIRECTION_ID,
                    JOURNEY_PATTERN_JORE4_ID
            ));

            @Test
            @DisplayName("Should'nt update the journey pattern id of the existing route direction")
            void shouldNotUpdateJourneyPatternIdOfExistingRouteDirection() {
                importRepository.setJourneyPatternJore4Ids(INPUT);

                final RouteDirection routeDirection = targetRepository.findById(RouteDirectionPK.of(ROUTE_DIRECTION_ID)).get();
                assertThat(routeDirection.journeyPatternJore4Id()).isEmpty();
            }
        }

        @Nested
        @DisplayName("When the route direction is found")
        class WhenRouteDirectionIsFound {

            private final List<PersistableJourneyPatternIdMapping> INPUT = List.of(PersistableJourneyPatternIdMapping.of(
                    ROUTE_DIRECTION_ID,
                    JOURNEY_PATTERN_JORE4_ID
            ));

            @Test
            @DisplayName("Should update the journey pattern id of the existing route direction")
            void shouldUpdateJourneyPatternIdOfExistingRouteDirection() {
                importRepository.setJourneyPatternJore4Ids(INPUT);

                final RouteDirection routeDirection = targetRepository.findById(RouteDirectionPK.of(ROUTE_DIRECTION_ID)).get();
                assertThat(routeDirection.journeyPatternJore4Id()).contains(JOURNEY_PATTERN_JORE4_ID);
            }
        }
    }

    @Nested
    @DisplayName("Set route Jore 4 ids")
    @Sql(scripts = {
            "/sql/destination/drop_tables.sql",
            "/sql/destination/populate_lines.sql",
            "/sql/destination/populate_routes.sql",
            "/sql/destination/populate_route_directions.sql"
    })
    class SetRouteJore4Ids {

        private final UUID ROUTE_DIRECTION_ID = UUID.fromString("6f93fa6b-8a19-4b98-bd84-b8409e670c70");
        private final UUID JORE4_ID = UUID.fromString("51f2686b-166c-4157-bd70-647337e44c8c");

        @Nested
        @DisplayName("When the route direction isn't found")
        class WhenRouteDirectionIsNotFound {

            private final UUID UNKNOWN_ROUTE_DIRECTION_ID = UUID.fromString("038174b2-985f-47f7-9676-83cf0b944d88");
            private List<PersistableRouteIdMapping> INPUT = List.of(PersistableRouteIdMapping.of(
                    UNKNOWN_ROUTE_DIRECTION_ID,
                    JORE4_ID
            ));

            @Test
            @DisplayName("Shouldn't update the Jore 4 id of the existing route direction")
            void shouldNotUpdateJore4IdOfExistingRouteDirection() {
                importRepository.setRouteJore4Ids(INPUT);

                final RouteDirection routeDirection = targetRepository.findById(RouteDirectionPK.of(ROUTE_DIRECTION_ID)).get();
                assertThat(routeDirection.routeJore4Id()).isEmpty();
            }
        }

        @Nested
        @DisplayName("When the route direction is found")
        class WhenRouteDirectionIsFound {

            private List<PersistableRouteIdMapping> INPUT = List.of(PersistableRouteIdMapping.of(
                    ROUTE_DIRECTION_ID,
                    JORE4_ID
            ));

            @Test
            @DisplayName("Should update the Jore 4 id of the existing route direction")
            void shouldUpdateJore4IdOfExistingRouteDirection() {
                importRepository.setRouteJore4Ids(INPUT);

                final RouteDirection routeDirection = targetRepository.findById(RouteDirectionPK.of(ROUTE_DIRECTION_ID)).get();
                assertThat(routeDirection.routeJore4Id()).contains(JORE4_ID);
            }
        }
    }
}

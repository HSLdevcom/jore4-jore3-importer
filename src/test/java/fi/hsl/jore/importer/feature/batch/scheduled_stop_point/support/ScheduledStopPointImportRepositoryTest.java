package fi.hsl.jore.importer.feature.batch.scheduled_stop_point.support;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.PersistableScheduledStopPointIdMapping;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ScheduledStopPoint;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.generated.ScheduledStopPointPK;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.repository.IScheduledStopPointTestRepository;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntTest
public class ScheduledStopPointImportRepositoryTest {

    private static final String EXTERNAL_ID = "1000003";

    private final IScheduledStopPointImportRepository importRepository;
    private final IScheduledStopPointTestRepository targetRepository;

    @Autowired
    ScheduledStopPointImportRepositoryTest(final IScheduledStopPointImportRepository importRepository,
                                           final IScheduledStopPointTestRepository targetRepository) {
        this.importRepository = importRepository;
        this.targetRepository = targetRepository;
    }

    @Nested
    @DisplayName("Move data from the staging table to the target table")
    class CommitStagingToTarget {

        private final UUID EXPECTED_ID = UUID.fromString("058a63b3-365b-4676-af51-809bef577cdd");
        private final UUID EXPECTED_INFRASTRUCTURE_NODE_ID = UUID.fromString("cc11a5db-2ae7-4220-adfe-aca5d6620909");
        private final long EXPECTED_ELY_NUMBER = 1234567890L;
        private static final String EXPECTED_FINNISH_NAME = "Yliopisto";
        private static final String EXPECTED_SWEDISH_NAME = "Universitetet";
        private static final String EXPECTED_SHORT_ID = "H1234";
        private static final String EXPECTED_HASTUS_PLACE_ID = "1MARIA";

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
            @Sql(scripts = {
                    "/sql/importer/drop_tables.sql",
                    "/sql/importer/populate_infrastructure_nodes.sql",
                    "/sql/importer/populate_scheduled_stop_points.sql"
            })
            class WhenTargetTableHasOneRow {

                @Test
                @DisplayName("Should delete the existing row from the target table")
                void shouldDeleteExistingRowFromTargetTable(final SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<ScheduledStopPointPK>> result = importRepository.commitStagingToTarget();

                    softAssertions.assertThat(result.keySet())
                            .overridingErrorMessage("Expected that only delete query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.DELETED);

                    final Set<ScheduledStopPointPK> idsOfDeletedRows = result.get(RowStatus.DELETED).get();

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
                    "/sql/importer/drop_tables.sql",
                    "/sql/importer/populate_infrastructure_nodes.sql",
                    "/sql/importer/populate_scheduled_stop_points_staging.sql"
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
                void shouldInsertNewRowIntoTargetTable(final SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<ScheduledStopPointPK>> result = importRepository.commitStagingToTarget();

                    softAssertions.assertThat(result.keySet())
                            .overridingErrorMessage("Expected that only insert query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.INSERTED);

                    final Set<ScheduledStopPointPK> insertedIds = result.get(RowStatus.INSERTED).get();

                    softAssertions.assertThat(insertedIds)
                            .overridingErrorMessage(
                                    "Expected that only one id was returned but found: %d",
                                    insertedIds.size()
                            )
                            .hasSize(1);
                }

                @Test
                @DisplayName("Should return the id of the inserted scheduled stop point")
                void shouldReturnIdOfInsertedScheduledStop() {
                    final Map<RowStatus, Set<ScheduledStopPointPK>> result = importRepository.commitStagingToTarget();

                    final ScheduledStopPointPK id = result.get(RowStatus.INSERTED).get().get();
                    final Set<ScheduledStopPointPK> dbIds = targetRepository.findAllIds();
                    assertThat(dbIds)
                            .overridingErrorMessage(
                                    "Expected the database to contain row with id: %s but found: %s",
                                    id,
                                    dbIds
                            )
                            .containsOnly(id);
                }

                @Test
                @DisplayName("Should insert a new scheduled stop point into the target table")
                void shouldInsertNewScheduledStopPointIntoTargetTable(final SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<ScheduledStopPointPK>> result = importRepository.commitStagingToTarget();
                    final ScheduledStopPointPK id = result.get(RowStatus.INSERTED).get().get();

                    final ScheduledStopPoint inserted = targetRepository.findById(id).get();

                    softAssertions.assertThat(inserted.externalId().value())
                            .as("external id")
                            .isEqualTo(EXTERNAL_ID);

                    softAssertions.assertThat(inserted.node().value())
                            .as("infrastructure node id")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_NODE_ID);

                    softAssertions.assertThat(inserted.elyNumber())
                            .as("elyNumber")
                            .contains(EXPECTED_ELY_NUMBER);

                    final String finnishName = JoreLocaleUtil.getI18nString(inserted.name(), JoreLocaleUtil.FINNISH);
                    softAssertions.assertThat(finnishName)
                            .as("finnish name")
                            .isEqualTo(EXPECTED_FINNISH_NAME);

                    final String swedishName = JoreLocaleUtil.getI18nString(inserted.name(), JoreLocaleUtil.SWEDISH);
                    softAssertions.assertThat(swedishName)
                            .as("swedish name")
                            .isEqualTo(EXPECTED_SWEDISH_NAME);

                    softAssertions.assertThat(inserted.shortId())
                            .as("shortId")
                            .contains(EXPECTED_SHORT_ID);

                    softAssertions.assertThat(inserted.hastusPlaceId())
                            .as("hastusPlaceId")
                            .contains(EXPECTED_HASTUS_PLACE_ID);
                }
            }

            @Nested
            @DisplayName("When the target table contains the imported scheduled stop point")
            @ExtendWith(SoftAssertionsExtension.class)
            @Sql(scripts = {
                    "/sql/importer/drop_tables.sql",
                    "/sql/importer/populate_infrastructure_nodes.sql",
                    "/sql/importer/populate_scheduled_stop_points.sql",
                    "/sql/importer/populate_scheduled_stop_points_staging.sql"
            })
            class WhenTargetTableContainsImportedScheduledStopPoint {

                @Test
                @DisplayName("Should update the information of the existing row")
                void shouldUpdateInformationOfExistingRow(final SoftAssertions softAssertions) {
                    final Map<RowStatus, Set<ScheduledStopPointPK>> result = importRepository.commitStagingToTarget();

                    softAssertions.assertThat(result.keySet())
                            .overridingErrorMessage("Expected that only update query was invoked but found: %s", result.keySet())
                            .containsOnly(RowStatus.UPDATED);

                    final Set<ScheduledStopPointPK> idsOfUpdatedRows = result.get(RowStatus.UPDATED).get();

                    softAssertions.assertThat(idsOfUpdatedRows)
                            .overridingErrorMessage(
                                    "Expected that only one id was returned but found: %d",
                                    idsOfUpdatedRows.size()
                            )
                            .hasSize(1);
                }

                @Test
                @DisplayName("Should update the information of the found scheduled stop point")
                void shouldUpdateInformationOFoundScheduledStopPoint(final SoftAssertions softAssertions) {
                    importRepository.commitStagingToTarget();
                    final ScheduledStopPoint updated = targetRepository.findById(ScheduledStopPointPK.of(EXPECTED_ID)).get();

                    softAssertions.assertThat(updated.pk().value())
                            .as("id")
                            .isEqualTo(EXPECTED_ID);

                    softAssertions.assertThat(updated.externalId().value())
                            .as("external id")
                            .isEqualTo(EXTERNAL_ID);

                    softAssertions.assertThat(updated.node().value())
                            .as("infrastructure node id")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_NODE_ID);

                    softAssertions.assertThat(updated.elyNumber())
                            .as("elyNumber")
                            .contains(EXPECTED_ELY_NUMBER);

                    final String finnishName = JoreLocaleUtil.getI18nString(updated.name(), JoreLocaleUtil.FINNISH);
                    softAssertions.assertThat(finnishName)
                            .as("finnish name")
                            .isEqualTo(EXPECTED_FINNISH_NAME);

                    final String swedishName = JoreLocaleUtil.getI18nString(updated.name(), JoreLocaleUtil.SWEDISH);
                    softAssertions.assertThat(swedishName)
                            .as("swedish name")
                            .isEqualTo(EXPECTED_SWEDISH_NAME);

                    softAssertions.assertThat(updated.shortId())
                            .as("shortId")
                            .contains(EXPECTED_SHORT_ID);

                    softAssertions.assertThat(updated.hastusPlaceId())
                            .as("hastusPlaceId")
                            .contains(EXPECTED_HASTUS_PLACE_ID);
                }
            }
        }
    }

    @Nested
    @DisplayName("Set the Jore 4 ids of scheduled stop points")
    @Sql(scripts = {
            "/sql/importer/drop_tables.sql",
            "/sql/importer/populate_infrastructure_nodes.sql",
            "/sql/importer/populate_scheduled_stop_points.sql"
    })
    class SetJore4Ids {

        private final UUID JORE4_ID = UUID.fromString("51f2686b-166c-4157-bd70-647337e44c8c");

        @Nested
        @DisplayName("When the updated scheduled stop point isn't found")
        class WhenUpdatedScheduledStopPointIsNotFound {

            private static final String UNKNOWN_EXT_ID = "999999999";
            private List<PersistableScheduledStopPointIdMapping> INPUT = List.of(
                    PersistableScheduledStopPointIdMapping.of(UNKNOWN_EXT_ID, JORE4_ID)
            );

            @Test
            @DisplayName("Shouldn't update the Jore 4 id of the existing scheduled stop point")
            void shouldNotUpdateJore4IdOfExistingScheduledStopPoint() {
                importRepository.setJore4Ids(INPUT);

                final ScheduledStopPoint updated = targetRepository.findByExternalId(ExternalId.of(EXTERNAL_ID)).get();
                assertThat(updated.jore4Id()).isEmpty();
            }
        }

        @Nested
        @DisplayName("When the updated scheduled stop point is found")
        class WhenUpdatedScheduledStopPointIsFound {

            private List<PersistableScheduledStopPointIdMapping> INPUT = List.of(
                    PersistableScheduledStopPointIdMapping.of(EXTERNAL_ID, JORE4_ID)
            );

            @Test
            @DisplayName("Should update the Jore 4 id of the existing scheduled stop point")
            void shouldUpdateJore4IdOfExistingScheduledStopPoint() {
                importRepository.setJore4Ids(INPUT);

                final ScheduledStopPoint updated = targetRepository.findByExternalId(ExternalId.of(EXTERNAL_ID)).get();
                assertThat(updated.jore4Id()).contains(JORE4_ID);
            }
        }
    }
}

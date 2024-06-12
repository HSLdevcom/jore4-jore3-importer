package fi.hsl.jore.importer.feature.batch.place.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.jore3.field.generated.PlaceId;
import fi.hsl.jore.importer.feature.network.place.dto.PersistablePlace;
import fi.hsl.jore.importer.feature.network.place.dto.Place;
import fi.hsl.jore.importer.feature.network.place.dto.generated.PlacePK;
import fi.hsl.jore.importer.feature.network.place.repository.IPlaceTestRepository;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@IntTest
public class PlaceImportRepositoryTest {

    private final IPlaceImportRepository importRepository;
    private final IPlaceTestRepository targetRepository;

    public PlaceImportRepositoryTest(
            @Autowired final IPlaceImportRepository importRepository,
            @Autowired final IPlaceTestRepository targetRepository) {
        this.importRepository = importRepository;
        this.targetRepository = targetRepository;
    }

    @Nested
    @DisplayName("Commit staging to the target table")
    @Sql(scripts = "/sql/importer/drop_tables.sql")
    class CommitStagingToTarget {

        private static final String PLACE_ID = "1KALA";
        private static final String PLACE_NAME = "Kalasatama";
        private static final String PLACE_NEW_NAME = "Kalasatama2";
        private final ExternalId EXT_ID = ExternalIdUtil.forPlace(PlaceId.of(PLACE_ID));

        @BeforeEach
        public void beforeEach() {
            assertThat(
                    "Target repository should be empty at the start of the test", targetRepository.empty(), is(true));
        }

        @Test
        public void whenNoStagedRowsAndCommit_thenReturnEmptyResult() {
            assertThat(importRepository.commitStagingToTarget(), is(HashMap.empty()));
        }

        @Test
        public void whenNewStagedRowsAndCommit_andTargetDbEmpty_thenReturnResultWithInsertedId() {
            importRepository.submitToStaging(List.of(PersistablePlace.of(EXT_ID, PLACE_NAME)));

            final Map<RowStatus, Set<PlacePK>> result = importRepository.commitStagingToTarget();

            assertThat("Only INSERTs should occur", result.keySet(), is(HashSet.of(RowStatus.INSERTED)));

            final Set<PlacePK> ids = result.get(RowStatus.INSERTED).get();

            assertThat("Only a single row is inserted", ids.size(), is(1));

            final PlacePK id = ids.get();

            assertThat("Target repository should contain a single row", targetRepository.count(), is(1));

            assertThat(
                    "Target repository (including history) should contain a single row",
                    targetRepository.countHistory(),
                    is(1));

            final Optional<Place> maybePlace = targetRepository.findById(id);
            assertThat("The inserted row is found in the target repository", maybePlace.isPresent(), is(true));
        }

        @Test
        public void whenNoStagedRowsAndCommit_andTargetNotEmpty_thenReturnResultWithDeletedId() {
            final PlacePK existingId = targetRepository.insert(PersistablePlace.of(EXT_ID, PLACE_NAME));

            final Map<RowStatus, Set<PlacePK>> result = importRepository.commitStagingToTarget();

            assertThat("Only DELETEs should occur", result.keySet(), is(HashSet.of(RowStatus.DELETED)));

            final Set<PlacePK> ids = result.get(RowStatus.DELETED).get();

            assertThat("Only a single row is deleted", ids, is(HashSet.of(existingId)));

            assertThat("Target repository should be empty after import", targetRepository.empty(), is(true));

            assertThat(
                    "Target repository (including history) should contain a single row",
                    targetRepository.countHistory(),
                    is(1));
        }

        @Test
        public void whenNewStagedRowsAndCommit_andTargetNotEmpty_thenReturnResultWithUpdatedId() {
            targetRepository.insert(PersistablePlace.of(EXT_ID, PLACE_NAME));

            importRepository.submitToStaging(List.of(PersistablePlace.of(EXT_ID, PLACE_NEW_NAME)));

            final Map<RowStatus, Set<PlacePK>> result = importRepository.commitStagingToTarget();

            assertThat("Only UPDATEs should occur", result.keySet(), is(HashSet.of(RowStatus.UPDATED)));

            final Set<PlacePK> ids = result.get(RowStatus.UPDATED).get();

            assertThat("Only a single row is updated", ids.size(), is(1));

            final PlacePK id = ids.get();

            assertThat("Target repository should contain a single row", targetRepository.count(), is(1));

            assertThat(
                    "Target repository including history should contain two rows",
                    targetRepository.countHistory(),
                    is(2));

            final Optional<Place> maybePlace = targetRepository.findById(id);
            assertThat("The updated row is found in the target repository", maybePlace.isPresent(), is(true));

            assertThat(
                    "The name of updated row should be changed as expected in the target repository",
                    maybePlace.get().name(),
                    is(PLACE_NEW_NAME));
        }
    }
}

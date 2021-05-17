package fi.hsl.jore.importer.feature.batch.link.support;

import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.ImportableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.feature.infrastructure.link.repository.ILinkRepository;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.util.GeometryUtil;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LinkImportRepositoryTest extends IntegrationTest {

    private static final LineString LINE_1 = GeometryUtil.toLineString(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.168988620, 24.949328727, 0),
            new Coordinate(60.168896355, 24.945549266, 0)
    );

    private static final LineString LINE_2 = GeometryUtil.toLineString(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.158988620, 24.939328727, 0),
            new Coordinate(60.158896355, 24.935549266, 0)
    );

    private final ILinkImportRepository importRepository;
    private final ILinkRepository targetRepository;

    public LinkImportRepositoryTest(@Autowired final ILinkImportRepository importRepository,
                                    @Autowired final ILinkRepository targetRepository) {
        this.importRepository = importRepository;
        this.targetRepository = targetRepository;
    }

    @Test
    public void whenNoStagedRowsAndCommit_thenReturnEmptyResult() {
        assertThat(importRepository.commitStagingToTarget(),
                   is(HashMap.empty()));
    }

    @Test
    public void whenNewStagedRowsAndCommit_andTargetDbEmpty_thenReturnResultWithInsertedId() {
        assertThat("Target repository should be empty before import",
                   targetRepository.empty(),
                   is(true));

        importRepository.submitToStaging(
                List.of(ImportableLink.of(ExternalId.of("a"), NetworkType.ROAD, LINE_1))
        );

        final Map<RowStatus, Set<LinkPK>> result = importRepository.commitStagingToTarget();

        assertThat("Only INSERTs should occur",
                   result.keySet(),
                   is(HashSet.of(RowStatus.INSERTED)));

        final Set<LinkPK> ids = result.get(RowStatus.INSERTED).get();

        assertThat("Only a single row is inserted",
                   ids.size(),
                   is(1));

        assertThat("Target repository should contain only the inserted row",
                   targetRepository.findAllIds(),
                   is(ids));
    }

    @Test
    public void whenStagedRowsWithChangesAndCommit_andTargetNotEmpty_thenReturnResultWithUpdatedId() {
        assertThat("Target repository should be empty before import",
                   targetRepository.empty(),
                   is(true));

        final List<LinkPK> existing = targetRepository.upsert(
                List.of(PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, LINE_1))
        );

        final LinkPK existingId = existing.get(0);

        assertThat("Target repository should now contain a single row",
                   targetRepository.findAllIds(),
                   is(HashSet.of(existingId)));

        importRepository.submitToStaging(
                List.of(ImportableLink.of(ExternalId.of("a"), NetworkType.ROAD, LINE_2))
        );

        final Map<RowStatus, Set<LinkPK>> result = importRepository.commitStagingToTarget();

        assertThat("Only UPDATEs should occur",
                   result.keySet(),
                   is(HashSet.of(RowStatus.UPDATED)));

        final Set<LinkPK> ids = result.get(RowStatus.UPDATED).get();

        assertThat("Only a single row is updated",
                   ids,
                   is(HashSet.of(existingId)));

        assertThat("Target repository should still contain a single row",
                   targetRepository.findAllIds(),
                   is(ids));

        final Optional<Link> maybeLink = targetRepository.findById(existingId);
        assertThat("The updated row is found in the target repository",
                   maybeLink.isPresent(),
                   is(true));

        assertThat("The updated rows location was changed",
                   maybeLink.get().geometry(),
                   is(LINE_2));
    }

    @Test
    public void whenStagedRowsWithNoChangesAndCommit_andTargetNotEmpty_thenReturnEmptyResult() {
        assertThat("Target repository should be empty before import",
                   targetRepository.empty(),
                   is(true));

        final PersistableLink sourceLink = PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, LINE_1);

        final List<LinkPK> existing = targetRepository.upsert(
                List.of(sourceLink)
        );

        final LinkPK existingId = existing.get(0);

        assertThat("Target repository should now contain a single row",
                   targetRepository.findAllIds(),
                   is(HashSet.of(existingId)));

        // We submit the same link
        importRepository.submitToStaging(
                List.of(ImportableLink.of(sourceLink.externalId(),
                                          sourceLink.networkType(),
                                          sourceLink.geometry()))
        );

        final Map<RowStatus, Set<LinkPK>> result = importRepository.commitStagingToTarget();

        assertThat("No operations should occur",
                   result.keySet(),
                   is(HashSet.empty()));

        assertThat("Target repository should still contain a single row",
                   targetRepository.findAllIds(),
                   is(HashSet.of(existingId)));

        final Link link = targetRepository.findById(existingId).orElseThrow();

        assertThat("The target row was not changed",
                   link.geometry(),
                   is(LINE_1));
    }

    @Test
    public void whenStagedRowsAndCommit_andTargetContainsExtraRows_thenReturnResultWithDeletedId() {
        assertThat("Target repository should be empty before import",
                   targetRepository.empty(),
                   is(true));

        // Insert two links into the target table (as if imported previously)
        final PersistableLink firstLink = PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, LINE_1);
        final PersistableLink secondLink = PersistableLink.of(ExternalId.of("b"), NetworkType.ROAD, LINE_2);

        final List<LinkPK> existing = targetRepository.upsert(
                List.of(firstLink,
                        secondLink)
        );

        final LinkPK firstId = existing.get(0);
        final LinkPK secondId = existing.get(1);

        // We submit only the latter link as-is, simulating the case where the first link is removed at the source
        importRepository.submitToStaging(
                List.of(ImportableLink.of(secondLink.externalId(),
                                          secondLink.networkType(),
                                          secondLink.geometry()))
        );

        final Map<RowStatus, Set<LinkPK>> result = importRepository.commitStagingToTarget();

        assertThat("Only DELETEs should occur",
                   result.keySet(),
                   is(HashSet.of(RowStatus.DELETED)));

        final Set<LinkPK> ids = result.get(RowStatus.DELETED).get();

        assertThat("Only a single row is deleted",
                   ids,
                   is(HashSet.of(firstId)));

        assertThat("Target repository should contain the second link",
                   targetRepository.findAllIds(),
                   is(HashSet.of(secondId)));
    }
}

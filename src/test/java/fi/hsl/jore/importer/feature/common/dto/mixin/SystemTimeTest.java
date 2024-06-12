package fi.hsl.jore.importer.feature.common.dto.mixin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.google.common.collect.Range;
import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.TestGeometryUtil;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.ImmutableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.Node;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.feature.infrastructure.node.repository.INodeTestRepository;
import fi.hsl.jore.importer.feature.system.repository.ISystemRepository;
import io.vavr.collection.List;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

public class SystemTimeTest extends IntegrationTest {

    // Let's use Nodes as an example entity with system time
    private final INodeTestRepository nodeRepository;

    private final ISystemRepository systemRepository;

    private static final Point GEOM = TestGeometryUtil.randomPoint();
    private static final Point GEOM2 = TestGeometryUtil.randomPoint();
    private static final Point GEOM3 = TestGeometryUtil.randomPoint();

    public SystemTimeTest(
            @Autowired final INodeTestRepository nodeRepository, @Autowired final ISystemRepository systemRepository) {
        this.nodeRepository = nodeRepository;
        this.systemRepository = systemRepository;
    }

    @Test
    public void testSystemTimeAfterInsert() {
        final Instant before = systemRepository.currentTimestamp();

        nodeRepository.insert(PersistableNode.of(ExternalId.of("a"), NodeType.CROSSROADS, GEOM));

        final Instant after = systemRepository.currentTimestamp();

        assertThat("Sanity check: inserting the entity was not instantaneous", after.isAfter(before));

        final List<? extends IHasSystemTime> entities = nodeRepository.findAll();

        assertThat("Only one alive entity should exist", entities.size(), is(1));

        assertThat("Only one (alive) entity should exist (including history) ", nodeRepository.countHistory(), is(1));

        final IHasSystemTime insertedEntity = entities.get(0);
        final Range<Instant> systemTime = insertedEntity.systemTime().range();

        assertThat("Entity system time shouldn't be empty", systemTime.isEmpty(), is(false));
        assertThat(
                "Entity system time should have a starting point (= last modified or creation time)",
                systemTime.hasLowerBound(),
                is(true));
        assertThat(
                "Entity system time should not have an end point (as it's not yet deleted)",
                systemTime.hasUpperBound(),
                is(false));
        assertThat(
                "Entity system time should not contain timestamps before the entity was created/modified",
                systemTime.contains(before),
                is(false));
        assertThat(
                "Entity system time should contain timestamps after the entity was created",
                systemTime.contains(after),
                is(true));
    }

    @Test
    public void testSystemTimeAfterUpdate() {
        final Instant before = systemRepository.currentTimestamp();

        final NodePK id = nodeRepository.insert(PersistableNode.of(ExternalId.of("a"), NodeType.CROSSROADS, GEOM));
        final Node node = nodeRepository.findById(id).orElseThrow();

        final Instant afterFirstInsert = systemRepository.currentTimestamp();

        nodeRepository.update(ImmutableNode.copyOf(node).withLocation(GEOM2));

        final Instant afterSecondInsert = systemRepository.currentTimestamp();

        nodeRepository.update(ImmutableNode.copyOf(node).withLocation(GEOM3));

        final Instant afterThirdInsert = systemRepository.currentTimestamp();

        final List<? extends IHasSystemTime> entities = nodeRepository.findAll();

        assertThat("Only one alive entity should exist", entities.size(), is(1));

        final List<? extends IHasSystemTime> allEntities = nodeRepository.findFromHistory();

        assertThat("History should contain the latest version and the two earlier ones", allEntities.size(), is(3));

        final Range<Instant> currentEntitySystemTime =
                allEntities.get(2).systemTime().range();
        final Range<Instant> middleEntitySystemTime =
                allEntities.get(1).systemTime().range();
        final Range<Instant> oldestEntitySystemTime =
                allEntities.get(0).systemTime().range();

        assertThat(
                "Current entity system time should not have an end point (as it's not yet deleted/modified)",
                currentEntitySystemTime.hasUpperBound(),
                is(false));
        assertThat(
                "Middle entity system time should have an end point (it's not alive)",
                middleEntitySystemTime.hasUpperBound(),
                is(true));
        assertThat(
                "Oldest entity system time should have an end point (it's not alive)",
                oldestEntitySystemTime.hasUpperBound(),
                is(true));

        assertThat(
                "Current entity system time should have a lower bound",
                currentEntitySystemTime.hasLowerBound(),
                is(true));
        assertThat(
                "Middle entity system time should have a lower bound",
                middleEntitySystemTime.hasLowerBound(),
                is(true));
        assertThat(
                "Oldest entity system time should have a lower bound",
                oldestEntitySystemTime.hasLowerBound(),
                is(true));

        assertThat(oldestEntitySystemTime.contains(before), is(false));
        assertThat(oldestEntitySystemTime.contains(afterFirstInsert), is(true));
        assertThat(oldestEntitySystemTime.contains(afterSecondInsert), is(false));
        assertThat(oldestEntitySystemTime.contains(afterThirdInsert), is(false));

        assertThat(
                "The latter entity owns the modification timestamp",
                middleEntitySystemTime.contains(oldestEntitySystemTime.upperEndpoint()),
                is(true));

        assertThat(middleEntitySystemTime.contains(before), is(false));
        assertThat(middleEntitySystemTime.contains(afterFirstInsert), is(false));
        assertThat(middleEntitySystemTime.contains(afterSecondInsert), is(true));
        assertThat(middleEntitySystemTime.contains(afterThirdInsert), is(false));

        assertThat(
                "The latter entity owns the modification timestamp",
                currentEntitySystemTime.contains(middleEntitySystemTime.upperEndpoint()),
                is(true));

        assertThat(currentEntitySystemTime.contains(before), is(false));
        assertThat(currentEntitySystemTime.contains(afterFirstInsert), is(false));
        assertThat(currentEntitySystemTime.contains(afterSecondInsert), is(false));
        assertThat(currentEntitySystemTime.contains(afterThirdInsert), is(true));
    }
}

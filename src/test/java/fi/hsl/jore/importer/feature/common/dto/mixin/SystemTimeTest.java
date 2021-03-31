package fi.hsl.jore.importer.feature.common.dto.mixin;

import com.google.common.collect.Range;
import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.repository.INodeRepository;
import fi.hsl.jore.importer.feature.util.GeometryUtil;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SystemTimeTest extends IntegrationTest {

    // Let's use Nodes as an example entity with system time
    private final INodeRepository nodeRepository;

    private static final Point GEOM = GeometryUtil.toPoint(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.168988620, 24.949328727, 0)
    );

    private static final Point GEOM2 = GeometryUtil.toPoint(
            GeometryUtil.SRID_WGS84,
            new Coordinate(61.168988620, 25.949328727, 0)
    );

    private static final Point GEOM3 = GeometryUtil.toPoint(
            GeometryUtil.SRID_WGS84,
            new Coordinate(62.168988620, 26.949328727, 0)
    );

    public SystemTimeTest(@Autowired final INodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    @Test
    public void testSystemTimeAfterInsert() {
        final Instant before = Instant.now();

        nodeRepository.upsert(List.of(
                PersistableNode.of(ExternalId.of("a"), NodeType.CROSSROADS, GEOM)
        ));

        final Instant after = Instant.now();

        final List<? extends IHasSystemTime> entities = nodeRepository.findAll();

        assertThat("Only one alive entity should exist",
                   entities.size(),
                   is(1));

        assertThat("Only one (alive) entity should exist (including history) ",
                   nodeRepository.countHistory(),
                   is(1));

        final IHasSystemTime insertedEntity = entities.get(0);
        final Range<Instant> systemTime = insertedEntity.systemTime().range();

        assertThat("Entity system time shouldn't be empty",
                   systemTime.isEmpty(),
                   is(false));
        assertThat("Entity system time should have a starting point (= last modified or creation time)",
                   systemTime.hasLowerBound(),
                   is(true));
        assertThat("Entity system time should not have an end point (as it's not yet deleted)",
                   systemTime.hasUpperBound(),
                   is(false));
        assertThat("Entity system time should not contain timestamps before the entity was created/modified",
                   systemTime.contains(before),
                   is(false));
        assertThat("Entity system time should contain timestamps after the entity was created",
                   systemTime.contains(after),
                   is(true));
    }

    @Test
    public void testSystemTimeAfterUpdate() {
        final Instant before = Instant.now();

        nodeRepository.upsert(List.of(
                PersistableNode.of(ExternalId.of("a"), NodeType.CROSSROADS, GEOM)
        ));

        final Instant afterFirstInsert = Instant.now();

        nodeRepository.upsert(List.of(
                PersistableNode.of(ExternalId.of("a"), NodeType.CROSSROADS, GEOM2)
        ));

        final Instant afterSecondInsert = Instant.now();

        nodeRepository.upsert(List.of(
                PersistableNode.of(ExternalId.of("a"), NodeType.CROSSROADS, GEOM3)
        ));

        final Instant afterThirdInsert = Instant.now();


        final List<? extends IHasSystemTime> entities = nodeRepository.findAll();

        assertThat("Only one alive entity should exist",
                   entities.size(),
                   is(1));

        final List<? extends IHasSystemTime> allEntities = nodeRepository.findFromHistory();

        assertThat("History should contain the latest version and the two earlier ones",
                   allEntities.size(),
                   is(3));

        final IHasSystemTime currentEntity = allEntities.get(2);
        final IHasSystemTime middleEntity = allEntities.get(1);
        final IHasSystemTime oldestEntity = allEntities.get(0);

        assertThat("Current entity system time should not have an end point (as it's not yet deleted/modified)",
                   currentEntity.systemTime().range().hasUpperBound(),
                   is(false));
        assertThat("Middle entity system time should have an end point (it's not alive)",
                   middleEntity.systemTime().range().hasUpperBound(),
                   is(true));
        assertThat("Oldest entity system time should have an end point (it's not alive)",
                   oldestEntity.systemTime().range().hasUpperBound(),
                   is(true));

        assertThat("Current entity system time should have a lower bound",
                   currentEntity.systemTime().range().hasLowerBound(),
                   is(true));
        assertThat("Middle entity system time should have a lower bound",
                   middleEntity.systemTime().range().hasLowerBound(),
                   is(true));
        assertThat("Oldest entity system time should have a lower bound",
                   oldestEntity.systemTime().range().hasLowerBound(),
                   is(true));

        assertThat(oldestEntity.systemTime().range().contains(before),
                   is(false));
        assertThat(oldestEntity.systemTime().range().contains(afterFirstInsert),
                   is(true));
        assertThat(oldestEntity.systemTime().range().contains(afterSecondInsert),
                   is(false));
        assertThat(oldestEntity.systemTime().range().contains(afterThirdInsert),
                   is(false));

        assertThat("The latter entity owns the modification timestamp",
                   middleEntity.systemTime().range().contains(oldestEntity.systemTime().range().upperEndpoint()),
                   is(true));

        assertThat(middleEntity.systemTime().range().contains(before),
                   is(false));
        assertThat(middleEntity.systemTime().range().contains(afterFirstInsert),
                   is(false));
        assertThat(middleEntity.systemTime().range().contains(afterSecondInsert),
                   is(true));
        assertThat(middleEntity.systemTime().range().contains(afterThirdInsert),
                   is(false));

        assertThat("The latter entity owns the modification timestamp",
                   currentEntity.systemTime().range().contains(middleEntity.systemTime().range().upperEndpoint()),
                   is(true));

        assertThat(currentEntity.systemTime().range().contains(before),
                   is(false));
        assertThat(currentEntity.systemTime().range().contains(afterFirstInsert),
                   is(false));
        assertThat(currentEntity.systemTime().range().contains(afterSecondInsert),
                   is(false));
        assertThat(currentEntity.systemTime().range().contains(afterThirdInsert),
                   is(true));
    }
}

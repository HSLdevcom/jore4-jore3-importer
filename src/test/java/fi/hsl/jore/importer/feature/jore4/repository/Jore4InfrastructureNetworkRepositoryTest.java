package fi.hsl.jore.importer.feature.jore4.repository;

import static org.assertj.core.api.Assertions.assertThat;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.jore3.util.JoreGeometryUtil;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ClosestInfrastructureLink;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPointDirection;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

@IntTest
@Sql(
        scripts = {
            "/sql/jore4/drop_tables.sql",
            "/sql/jore4/populate_infrastructure_links.sql",
            "/sql/jore4/populate_tram_infrastructure_links.sql"
        },
        config = @SqlConfig(dataSource = "jore4DataSource", transactionManager = "jore4TransactionManager"))
class Jore4InfrastructureNetworkRepositoryTest {

    private static final String GENERIC_BUS = "generic_bus";
    private static final String GENERIC_TRAM = "generic_tram";

    private static final String BUS_LINK_EXTERNAL_ID = "133202";
    private static final UUID BUS_LINK_ID = UUID.fromString("554c63e6-87b2-4dc8-a032-b6b0e2607696");

    // See populate_tram_infrastructure_links.sql: a one-way (forward) tram link from (24.94, 60.17) to (24.95, 60.17).
    private static final String TRAM_LINK_EXTERNAL_ID = "tram-1";
    private static final UUID TRAM_LINK_ID = UUID.fromString("a1b2c3d4-0000-4000-8000-000000000001");
    private static final double TRAM_LINK_LONGITUDE = 24.945;
    private static final double TRAM_LINK_LATITUDE = 60.17;

    // One degree of latitude is roughly 111 km, so 0.0001 degrees is roughly 11 metres.
    private static final Point POINT_11M_NORTH_OF_TRAM_LINK = point(TRAM_LINK_LATITUDE + 0.0001);
    private static final Point POINT_11M_SOUTH_OF_TRAM_LINK = point(TRAM_LINK_LATITUDE - 0.0001);
    // The start vertex of the link. A vertex is used instead of an intermediate point, because a point with the same
    // latitude between the vertices may not be exactly on the link after projecting to a metric coordinate system.
    private static final Point POINT_ON_TRAM_LINK = JoreGeometryUtil.fromDbCoordinates(TRAM_LINK_LATITUDE, 24.94);
    private static final Point POINT_78M_SOUTH_OF_TRAM_LINK = point(TRAM_LINK_LATITUDE - 0.0007);
    private static final Point POINT_167M_SOUTH_OF_TRAM_LINK = point(TRAM_LINK_LATITUDE - 0.0015);

    private final Jore4InfrastructureNetworkRepository repository;

    @Autowired
    Jore4InfrastructureNetworkRepositoryTest(final Jore4InfrastructureNetworkRepository repository) {
        this.repository = repository;
    }

    private static Point point(final double latitude) {
        return JoreGeometryUtil.fromDbCoordinates(latitude, TRAM_LINK_LONGITUDE);
    }

    @Nested
    @DisplayName("Find link id by external id")
    class FindLinkIdByExternalId {

        @Test
        @DisplayName("Should return the link id when the link has the given vehicle submode")
        void shouldReturnLinkIdWhenSubmodeMatches() {
            assertThat(repository.findLinkIdByExternalId(BUS_LINK_EXTERNAL_ID, GENERIC_BUS))
                    .contains(BUS_LINK_ID);
        }

        @Test
        @DisplayName("Should return an empty optional when the link doesn't have the given vehicle submode")
        void shouldReturnEmptyWhenSubmodeDoesNotMatch() {
            assertThat(repository.findLinkIdByExternalId(BUS_LINK_EXTERNAL_ID, GENERIC_TRAM))
                    .isEmpty();
        }

        @Test
        @DisplayName("Should return an empty optional when no link is found with the external id")
        void shouldReturnEmptyWhenLinkNotFound() {
            assertThat(repository.findLinkIdByExternalId("unknown", GENERIC_BUS))
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("Find closest link")
    class FindClosestLink {

        @Test
        @DisplayName("Should return the closest link with the given vehicle submode")
        void shouldReturnClosestLinkWithSubmode() {
            // The bus link in populate_tram_infrastructure_links.sql is closer to this point than the tram link.
            final Optional<Jore4ClosestInfrastructureLink> link =
                    repository.findClosestLink(POINT_11M_NORTH_OF_TRAM_LINK, GENERIC_TRAM);

            assertThat(link).isPresent();
            assertThat(link.get().infrastructureLinkId()).isEqualTo(TRAM_LINK_ID);
            assertThat(link.get().externalLinkId()).isEqualTo(TRAM_LINK_EXTERNAL_ID);
            assertThat(link.get().direction()).isEqualTo("forward");
            assertThat(link.get().oneWayDirection()).contains(Jore4ScheduledStopPointDirection.FORWARD);
            assertThat(link.get().distanceInMeters()).isBetween(10.0, 12.5);
        }

        @Test
        @DisplayName("Should return a link which is 50-100 metres away")
        void shouldReturnLinkWithin100Metres() {
            final Optional<Jore4ClosestInfrastructureLink> link =
                    repository.findClosestLink(POINT_78M_SOUTH_OF_TRAM_LINK, GENERIC_TRAM);

            assertThat(link).isPresent();
            assertThat(link.get().infrastructureLinkId()).isEqualTo(TRAM_LINK_ID);
        }

        @Test
        @DisplayName("Should return an empty optional when no link is found within 100 metres")
        void shouldReturnEmptyWhenNoLinkWithin100Metres() {
            assertThat(repository.findClosestLink(POINT_167M_SOUTH_OF_TRAM_LINK, GENERIC_TRAM))
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("Find point direction on link")
    class FindPointDirectionOnLink {

        @Test
        @DisplayName("Should return forward for a point on the right side of the link")
        void shouldReturnForwardForPointOnRightSide() {
            assertThat(repository.findPointDirectionOnLink(POINT_11M_SOUTH_OF_TRAM_LINK, TRAM_LINK_ID))
                    .contains(Jore4ScheduledStopPointDirection.FORWARD);
        }

        @Test
        @DisplayName("Should return backward for a point on the left side of the link")
        void shouldReturnBackwardForPointOnLeftSide() {
            assertThat(repository.findPointDirectionOnLink(POINT_11M_NORTH_OF_TRAM_LINK, TRAM_LINK_ID))
                    .contains(Jore4ScheduledStopPointDirection.BACKWARD);
        }

        @Test
        @DisplayName("Should return an empty optional for a point on the link")
        void shouldReturnEmptyForPointOnLink() {
            assertThat(repository.findPointDirectionOnLink(POINT_ON_TRAM_LINK, TRAM_LINK_ID))
                    .isEmpty();
        }

        @Test
        @DisplayName("Should resolve the direction of a point which is 50-100 metres away from the link")
        void shouldResolveDirectionWithin100Metres() {
            assertThat(repository.findPointDirectionOnLink(POINT_78M_SOUTH_OF_TRAM_LINK, TRAM_LINK_ID))
                    .contains(Jore4ScheduledStopPointDirection.FORWARD);
        }

        @Test
        @DisplayName("Should return an empty optional for a point which is more than 100 metres away from the link")
        void shouldReturnEmptyForPointFurtherThan100Metres() {
            assertThat(repository.findPointDirectionOnLink(POINT_167M_SOUTH_OF_TRAM_LINK, TRAM_LINK_ID))
                    .isEmpty();
        }
    }
}

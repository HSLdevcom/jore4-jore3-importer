package fi.hsl.jore.importer.feature.jore4.repository;

import static fi.hsl.jore.jore4.jooq.infrastructure_network.Tables.INFRASTRUCTURE_LINK;
import static fi.hsl.jore.jore4.jooq.infrastructure_network.Tables.VEHICLE_SUBMODE_ON_INFRASTRUCTURE_LINK;

import fi.hsl.jore.importer.config.jooq.converter.geometry.PointConverter;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ClosestInfrastructureLink;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPointDirection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.jooq.DSLContext;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * Reads the infrastructure network from the Jore 4 database.
 *
 * <p>The database functions <code>resolve_point_to_closest_link</code> and <code>find_point_direction_on_link</code>
 * are excluded from the jOOQ code generation, so they are called with plain SQL. Points are bound as PostGIS EWKT.
 */
@Repository
public class Jore4InfrastructureNetworkRepository implements IJore4InfrastructureNetworkRepository {

    private static final String SQL_FIND_CLOSEST_LINK = """
            WITH point_of_interest AS (SELECT ?::geography AS geog)
            SELECT link.infrastructure_link_id,
                   link.external_link_id,
                   link.direction,
                   ST_Distance(point_of_interest.geog, link.shape) AS distance
            FROM point_of_interest
            CROSS JOIN LATERAL infrastructure_network.resolve_point_to_closest_link(point_of_interest.geog, ?) link
            """;

    private static final String SQL_FIND_POINT_DIRECTION_ON_LINK = """
            SELECT direction.value
            FROM infrastructure_network.find_point_direction_on_link(?::geography, ?::uuid, ?::double precision) direction
            """;

    private final DSLContext db;

    @Autowired
    public Jore4InfrastructureNetworkRepository(@Qualifier("jore4Dsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    public Optional<UUID> findLinkIdByExternalId(final String externalLinkId, final String vehicleSubmode) {
        return db.select(INFRASTRUCTURE_LINK.INFRASTRUCTURE_LINK_ID)
                .from(INFRASTRUCTURE_LINK)
                .join(VEHICLE_SUBMODE_ON_INFRASTRUCTURE_LINK)
                .on(VEHICLE_SUBMODE_ON_INFRASTRUCTURE_LINK.INFRASTRUCTURE_LINK_ID.eq(
                        INFRASTRUCTURE_LINK.INFRASTRUCTURE_LINK_ID))
                .where(INFRASTRUCTURE_LINK.EXTERNAL_LINK_ID.eq(externalLinkId))
                .and(VEHICLE_SUBMODE_ON_INFRASTRUCTURE_LINK.VEHICLE_SUBMODE.eq(vehicleSubmode))
                .fetchOptionalInto(UUID.class);
    }

    @Override
    public Optional<Jore4ClosestInfrastructureLink> findClosestLink(final Point point, final String vehicleSubmode) {
        return db.fetchOptional(SQL_FIND_CLOSEST_LINK, toEwkt(point), vehicleSubmode)
                .map(record -> Jore4ClosestInfrastructureLink.of(
                        record.get("infrastructure_link_id", UUID.class),
                        record.get("external_link_id", String.class),
                        record.get("direction", String.class),
                        record.get("distance", Double.class)));
    }

    @Override
    public Optional<Jore4ScheduledStopPointDirection> findPointDirectionOnLink(
            final Point point, final UUID infrastructureLinkId) {
        return db.fetchOptional(
                        SQL_FIND_POINT_DIRECTION_ON_LINK,
                        toEwkt(point),
                        infrastructureLinkId.toString(),
                        SEARCH_RADIUS_METERS)
                .flatMap(record -> Jore4ScheduledStopPointDirection.fromValue(record.get("value", String.class)));
    }

    private static String toEwkt(final Point point) {
        return (String) Objects.requireNonNull(PointConverter.INSTANCE.to(point));
    }
}

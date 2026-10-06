package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4ClosestInfrastructureLink;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPointDirection;
import java.util.Optional;
import java.util.UUID;
import org.locationtech.jts.geom.Point;

/** Declares read operations for the infrastructure network found from the Jore 4 database. */
public interface IJore4InfrastructureNetworkRepository {

    /**
     * The search radius (in metres) used both when resolving the closest infrastructure link and when resolving the
     * direction of a point on a link. The radius of the closest link search is fixed to 100 metres by the database
     * function, and the direction search uses the same radius so that every resolved link can produce a direction.
     */
    double SEARCH_RADIUS_METERS = 100.0;

    /**
     * Finds the id of the infrastructure link which has the given external id and is traversable by the given vehicle
     * submode.
     */
    Optional<UUID> findLinkIdByExternalId(String externalLinkId, String vehicleSubmode);

    /**
     * Finds the infrastructure link closest to the given point, within {@link #SEARCH_RADIUS_METERS}, which is
     * traversable by the given vehicle submode.
     */
    Optional<Jore4ClosestInfrastructureLink> findClosestLink(Point point, String vehicleSubmode);

    /**
     * Resolves the direction of the given point relative to the given infrastructure link, within
     * {@link #SEARCH_RADIUS_METERS}. Returns an empty optional if the direction cannot be resolved, e.g. because the
     * point is on the link, on both sides of it or too far away.
     */
    Optional<Jore4ScheduledStopPointDirection> findPointDirectionOnLink(Point point, UUID infrastructureLinkId);
}

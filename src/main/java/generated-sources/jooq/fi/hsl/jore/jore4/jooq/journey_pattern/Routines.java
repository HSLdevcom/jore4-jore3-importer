/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.journey_pattern;


import fi.hsl.jore.jore4.jooq.journey_pattern.routines.CheckRouteJourneyPatternRefs;
import fi.hsl.jore.jore4.jooq.journey_pattern.tables.CheckInfraLinkStopRefsWithNewScheduledStopPoint;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;


/**
 * Convenience access to all stored procedures and functions in journey_pattern.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Routines {

    /**
     * @deprecated Unknown data type. Please define an explicit {@link
     * org.jooq.Binding} to specify how this type should be handled. Deprecation
     * can be turned off using {@literal <deprecationOnUnknownTypes/>} in your
     * code generator configuration.
     */
    @Deprecated
    public static Boolean checkRouteJourneyPatternRefs(
          Configuration configuration
        , UUID filterJourneyPatternId
        , UUID filterRouteId
        , UUID replaceScheduledStopPointId
        , UUID newLocatedOnInfrastructureLinkId
        , Object newMeasuredLocation
        , String newDirection
        , String newLabel
        , OffsetDateTime newValidityStart
        , OffsetDateTime newValidityEnd
    ) {
        CheckRouteJourneyPatternRefs f = new CheckRouteJourneyPatternRefs();
        f.setFilterJourneyPatternId(filterJourneyPatternId);
        f.setFilterRouteId(filterRouteId);
        f.setReplaceScheduledStopPointId(replaceScheduledStopPointId);
        f.setNewLocatedOnInfrastructureLinkId(newLocatedOnInfrastructureLinkId);
        f.setNewMeasuredLocation(newMeasuredLocation);
        f.setNewDirection(newDirection);
        f.setNewLabel(newLabel);
        f.setNewValidityStart(newValidityStart);
        f.setNewValidityEnd(newValidityEnd);

        f.execute(configuration);
        return f.getReturnValue();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link
     * org.jooq.Binding} to specify how this type should be handled. Deprecation
     * can be turned off using {@literal <deprecationOnUnknownTypes/>} in your
     * code generator configuration.
     */
    @Deprecated
    public static Field<Boolean> checkRouteJourneyPatternRefs(
          UUID filterJourneyPatternId
        , UUID filterRouteId
        , UUID replaceScheduledStopPointId
        , UUID newLocatedOnInfrastructureLinkId
        , Object newMeasuredLocation
        , String newDirection
        , String newLabel
        , OffsetDateTime newValidityStart
        , OffsetDateTime newValidityEnd
    ) {
        CheckRouteJourneyPatternRefs f = new CheckRouteJourneyPatternRefs();
        f.setFilterJourneyPatternId(filterJourneyPatternId);
        f.setFilterRouteId(filterRouteId);
        f.setReplaceScheduledStopPointId(replaceScheduledStopPointId);
        f.setNewLocatedOnInfrastructureLinkId(newLocatedOnInfrastructureLinkId);
        f.setNewMeasuredLocation(newMeasuredLocation);
        f.setNewDirection(newDirection);
        f.setNewLabel(newLabel);
        f.setNewValidityStart(newValidityStart);
        f.setNewValidityEnd(newValidityEnd);

        return f.asField();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link
     * org.jooq.Binding} to specify how this type should be handled. Deprecation
     * can be turned off using {@literal <deprecationOnUnknownTypes/>} in your
     * code generator configuration.
     */
    @Deprecated
    public static Field<Boolean> checkRouteJourneyPatternRefs(
          Field<UUID> filterJourneyPatternId
        , Field<UUID> filterRouteId
        , Field<UUID> replaceScheduledStopPointId
        , Field<UUID> newLocatedOnInfrastructureLinkId
        , Field<Object> newMeasuredLocation
        , Field<String> newDirection
        , Field<String> newLabel
        , Field<OffsetDateTime> newValidityStart
        , Field<OffsetDateTime> newValidityEnd
    ) {
        CheckRouteJourneyPatternRefs f = new CheckRouteJourneyPatternRefs();
        f.setFilterJourneyPatternId(filterJourneyPatternId);
        f.setFilterRouteId(filterRouteId);
        f.setReplaceScheduledStopPointId(replaceScheduledStopPointId);
        f.setNewLocatedOnInfrastructureLinkId(newLocatedOnInfrastructureLinkId);
        f.setNewMeasuredLocation(newMeasuredLocation);
        f.setNewDirection(newDirection);
        f.setNewLabel(newLabel);
        f.setNewValidityStart(newValidityStart);
        f.setNewValidityEnd(newValidityEnd);

        return f.asField();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link
     * org.jooq.Binding} to specify how this type should be handled. Deprecation
     * can be turned off using {@literal <deprecationOnUnknownTypes/>} in your
     * code generator configuration.
     */
    @Deprecated
    public static Result<Record> checkInfraLinkStopRefsWithNewScheduledStopPoint(
          Configuration configuration
        , UUID replaceScheduledStopPointId
        , UUID newLocatedOnInfrastructureLinkId
        , Object newMeasuredLocation
        , String newDirection
        , String newLabel
        , OffsetDateTime newValidityStart
        , OffsetDateTime newValidityEnd
    ) {
        return configuration.dsl().selectFrom(fi.hsl.jore.jore4.jooq.journey_pattern.tables.CheckInfraLinkStopRefsWithNewScheduledStopPoint.CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT.call(
              replaceScheduledStopPointId
            , newLocatedOnInfrastructureLinkId
            , newMeasuredLocation
            , newDirection
            , newLabel
            , newValidityStart
            , newValidityEnd
        )).fetch();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link
     * org.jooq.Binding} to specify how this type should be handled. Deprecation
     * can be turned off using {@literal <deprecationOnUnknownTypes/>} in your
     * code generator configuration.
     */
    @Deprecated
    public static CheckInfraLinkStopRefsWithNewScheduledStopPoint checkInfraLinkStopRefsWithNewScheduledStopPoint(
          UUID replaceScheduledStopPointId
        , UUID newLocatedOnInfrastructureLinkId
        , Object newMeasuredLocation
        , String newDirection
        , String newLabel
        , OffsetDateTime newValidityStart
        , OffsetDateTime newValidityEnd
    ) {
        return fi.hsl.jore.jore4.jooq.journey_pattern.tables.CheckInfraLinkStopRefsWithNewScheduledStopPoint.CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT.call(
            replaceScheduledStopPointId,
            newLocatedOnInfrastructureLinkId,
            newMeasuredLocation,
            newDirection,
            newLabel,
            newValidityStart,
            newValidityEnd
        );
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link
     * org.jooq.Binding} to specify how this type should be handled. Deprecation
     * can be turned off using {@literal <deprecationOnUnknownTypes/>} in your
     * code generator configuration.
     */
    @Deprecated
    public static CheckInfraLinkStopRefsWithNewScheduledStopPoint checkInfraLinkStopRefsWithNewScheduledStopPoint(
          Field<UUID> replaceScheduledStopPointId
        , Field<UUID> newLocatedOnInfrastructureLinkId
        , Field<Object> newMeasuredLocation
        , Field<String> newDirection
        , Field<String> newLabel
        , Field<OffsetDateTime> newValidityStart
        , Field<OffsetDateTime> newValidityEnd
    ) {
        return fi.hsl.jore.jore4.jooq.journey_pattern.tables.CheckInfraLinkStopRefsWithNewScheduledStopPoint.CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT.call(
            replaceScheduledStopPointId,
            newLocatedOnInfrastructureLinkId,
            newMeasuredLocation,
            newDirection,
            newLabel,
            newValidityStart,
            newValidityEnd
        );
    }
}

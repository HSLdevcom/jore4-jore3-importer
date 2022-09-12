/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.journey_pattern;


import fi.hsl.jore.jore4.jooq.journey_pattern.tables.CheckInfraLinkStopRefsWithNewScheduledStopPoint;
import fi.hsl.jore.jore4.jooq.journey_pattern.tables.JourneyPattern;
import fi.hsl.jore.jore4.jooq.journey_pattern.tables.ScheduledStopPointInJourneyPattern;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;


/**
 * Convenience access to all tables in journey_pattern.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table
     * <code>journey_pattern.check_infra_link_stop_refs_with_new_scheduled_stop_point</code>.
     */
    public static final CheckInfraLinkStopRefsWithNewScheduledStopPoint CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT = CheckInfraLinkStopRefsWithNewScheduledStopPoint.CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT;

    /**
     * @deprecated Unknown data type. Please define an explicit {@link
     * org.jooq.Binding} to specify how this type should be handled. Deprecation
     * can be turned off using {@literal <deprecationOnUnknownTypes/>} in your
     * code generator configuration.
     */
    @Deprecated
    public static Result<Record> CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT(
          Configuration configuration
        , UUID replaceScheduledStopPointId
        , UUID newLocatedOnInfrastructureLinkId
        , Object newMeasuredLocation
        , String newDirection
        , String newLabel
        , OffsetDateTime newValidityStart
        , OffsetDateTime newValidityEnd
        , Integer newPriority
    ) {
        return configuration.dsl().selectFrom(fi.hsl.jore.jore4.jooq.journey_pattern.tables.CheckInfraLinkStopRefsWithNewScheduledStopPoint.CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT.call(
              replaceScheduledStopPointId
            , newLocatedOnInfrastructureLinkId
            , newMeasuredLocation
            , newDirection
            , newLabel
            , newValidityStart
            , newValidityEnd
            , newPriority
        )).fetch();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link
     * org.jooq.Binding} to specify how this type should be handled. Deprecation
     * can be turned off using {@literal <deprecationOnUnknownTypes/>} in your
     * code generator configuration.
     */
    @Deprecated
    public static CheckInfraLinkStopRefsWithNewScheduledStopPoint CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT(
          UUID replaceScheduledStopPointId
        , UUID newLocatedOnInfrastructureLinkId
        , Object newMeasuredLocation
        , String newDirection
        , String newLabel
        , OffsetDateTime newValidityStart
        , OffsetDateTime newValidityEnd
        , Integer newPriority
    ) {
        return fi.hsl.jore.jore4.jooq.journey_pattern.tables.CheckInfraLinkStopRefsWithNewScheduledStopPoint.CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT.call(
            replaceScheduledStopPointId,
            newLocatedOnInfrastructureLinkId,
            newMeasuredLocation,
            newDirection,
            newLabel,
            newValidityStart,
            newValidityEnd,
            newPriority
        );
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link
     * org.jooq.Binding} to specify how this type should be handled. Deprecation
     * can be turned off using {@literal <deprecationOnUnknownTypes/>} in your
     * code generator configuration.
     */
    @Deprecated
    public static CheckInfraLinkStopRefsWithNewScheduledStopPoint CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT(
          Field<UUID> replaceScheduledStopPointId
        , Field<UUID> newLocatedOnInfrastructureLinkId
        , Field<Object> newMeasuredLocation
        , Field<String> newDirection
        , Field<String> newLabel
        , Field<OffsetDateTime> newValidityStart
        , Field<OffsetDateTime> newValidityEnd
        , Field<Integer> newPriority
    ) {
        return fi.hsl.jore.jore4.jooq.journey_pattern.tables.CheckInfraLinkStopRefsWithNewScheduledStopPoint.CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT.call(
            replaceScheduledStopPointId,
            newLocatedOnInfrastructureLinkId,
            newMeasuredLocation,
            newDirection,
            newLabel,
            newValidityStart,
            newValidityEnd,
            newPriority
        );
    }

    /**
     * The journey patterns, i.e. the ordered lists of stops and timing points
     * along routes:
     * https://www.transmodel-cen.eu/model/index.htm?goto=2:3:1:813
     */
    public static final JourneyPattern JOURNEY_PATTERN_ = JourneyPattern.JOURNEY_PATTERN_;

    /**
     * The scheduled stop points that form the journey pattern, in order:
     * https://www.transmodel-cen.eu/model/index.htm?goto=2:3:1:813 . For HSL,
     * all timing points are stops, hence journey pattern instead of service
     * pattern.
     */
    public static final ScheduledStopPointInJourneyPattern SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN = ScheduledStopPointInJourneyPattern.SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN;
}

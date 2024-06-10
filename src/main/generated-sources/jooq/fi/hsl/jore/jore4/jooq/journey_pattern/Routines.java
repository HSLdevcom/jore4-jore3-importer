/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.journey_pattern;


import fi.hsl.jore.jore4.jooq.journey_pattern.routines.CreateVerifyInfraLinkStopRefsQueueTempTable;
import fi.hsl.jore.jore4.jooq.journey_pattern.routines.InfraLinkStopRefsAlreadyVerified;
import fi.hsl.jore.jore4.jooq.journey_pattern.tables.CheckInfraLinkStopRefsWithNewScheduledStopPoint;
import fi.hsl.jore.jore4.jooq.journey_pattern.tables.GetBrokenRouteCheckFilters;
import fi.hsl.jore.jore4.jooq.journey_pattern.tables.GetBrokenRouteJourneyPatterns;
import fi.hsl.jore.jore4.jooq.journey_pattern.tables.MaximumPriorityValiditySpans;

import java.time.LocalDate;
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
     * Call
     * <code>journey_pattern.create_verify_infra_link_stop_refs_queue_temp_table</code>
     */
    public static void createVerifyInfraLinkStopRefsQueueTempTable(
          Configuration configuration
    ) {
        CreateVerifyInfraLinkStopRefsQueueTempTable p = new CreateVerifyInfraLinkStopRefsQueueTempTable();

        p.execute(configuration);
    }

    /**
     * Call <code>journey_pattern.infra_link_stop_refs_already_verified</code>
     */
    public static Boolean infraLinkStopRefsAlreadyVerified(
          Configuration configuration
    ) {
        InfraLinkStopRefsAlreadyVerified f = new InfraLinkStopRefsAlreadyVerified();

        f.execute(configuration);
        return f.getReturnValue();
    }

    /**
     * Get <code>journey_pattern.infra_link_stop_refs_already_verified</code> as
     * a field.
     */
    public static Field<Boolean> infraLinkStopRefsAlreadyVerified() {
        InfraLinkStopRefsAlreadyVerified f = new InfraLinkStopRefsAlreadyVerified();

        return f.asField();
    }

    /**
     * @deprecated Unknown data type. Parameter type or return type is unknown.
     * If this is a qualified, user-defined type, it may have been excluded from
     * code generation. If this is a built-in type, you can define an explicit
     * {@link org.jooq.Binding} to specify how this type should be handled.
     * Deprecation can be turned off using {@literal
     * <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static Result<Record> checkInfraLinkStopRefsWithNewScheduledStopPoint(
          Configuration configuration
        , UUID replaceScheduledStopPointId
        , UUID newLocatedOnInfrastructureLinkId
        , Object newMeasuredLocation
        , String newDirection
        , String newLabel
        , LocalDate newValidityStart
        , LocalDate newValidityEnd
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
     * @deprecated Unknown data type. Parameter type or return type is unknown.
     * If this is a qualified, user-defined type, it may have been excluded from
     * code generation. If this is a built-in type, you can define an explicit
     * {@link org.jooq.Binding} to specify how this type should be handled.
     * Deprecation can be turned off using {@literal
     * <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static CheckInfraLinkStopRefsWithNewScheduledStopPoint checkInfraLinkStopRefsWithNewScheduledStopPoint(
          UUID replaceScheduledStopPointId
        , UUID newLocatedOnInfrastructureLinkId
        , Object newMeasuredLocation
        , String newDirection
        , String newLabel
        , LocalDate newValidityStart
        , LocalDate newValidityEnd
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
     * @deprecated Unknown data type. Parameter type or return type is unknown.
     * If this is a qualified, user-defined type, it may have been excluded from
     * code generation. If this is a built-in type, you can define an explicit
     * {@link org.jooq.Binding} to specify how this type should be handled.
     * Deprecation can be turned off using {@literal
     * <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static CheckInfraLinkStopRefsWithNewScheduledStopPoint checkInfraLinkStopRefsWithNewScheduledStopPoint(
          Field<UUID> replaceScheduledStopPointId
        , Field<UUID> newLocatedOnInfrastructureLinkId
        , Field<Object> newMeasuredLocation
        , Field<String> newDirection
        , Field<String> newLabel
        , Field<LocalDate> newValidityStart
        , Field<LocalDate> newValidityEnd
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
     * Call <code>journey_pattern.get_broken_route_check_filters</code>.
     */
    public static Result<Record> getBrokenRouteCheckFilters(
          Configuration configuration
        , UUID[] filterRouteIds
    ) {
        return configuration.dsl().selectFrom(fi.hsl.jore.jore4.jooq.journey_pattern.tables.GetBrokenRouteCheckFilters.GET_BROKEN_ROUTE_CHECK_FILTERS.call(
              filterRouteIds
        )).fetch();
    }

    /**
     * Get <code>journey_pattern.get_broken_route_check_filters</code> as a
     * table.
     */
    public static GetBrokenRouteCheckFilters getBrokenRouteCheckFilters(
          UUID[] filterRouteIds
    ) {
        return fi.hsl.jore.jore4.jooq.journey_pattern.tables.GetBrokenRouteCheckFilters.GET_BROKEN_ROUTE_CHECK_FILTERS.call(
            filterRouteIds
        );
    }

    /**
     * Get <code>journey_pattern.get_broken_route_check_filters</code> as a
     * table.
     */
    public static GetBrokenRouteCheckFilters getBrokenRouteCheckFilters(
          Field<UUID[]> filterRouteIds
    ) {
        return fi.hsl.jore.jore4.jooq.journey_pattern.tables.GetBrokenRouteCheckFilters.GET_BROKEN_ROUTE_CHECK_FILTERS.call(
            filterRouteIds
        );
    }

    /**
     * @deprecated Unknown data type. Parameter type or return type is unknown.
     * If this is a qualified, user-defined type, it may have been excluded from
     * code generation. If this is a built-in type, you can define an explicit
     * {@link org.jooq.Binding} to specify how this type should be handled.
     * Deprecation can be turned off using {@literal
     * <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static Result<Record> getBrokenRouteJourneyPatterns(
          Configuration configuration
        , UUID[] filterRouteIds
        , UUID replaceScheduledStopPointId
        , UUID newLocatedOnInfrastructureLinkId
        , Object newMeasuredLocation
        , String newDirection
        , String newLabel
        , LocalDate newValidityStart
        , LocalDate newValidityEnd
        , Integer newPriority
    ) {
        return configuration.dsl().selectFrom(fi.hsl.jore.jore4.jooq.journey_pattern.tables.GetBrokenRouteJourneyPatterns.GET_BROKEN_ROUTE_JOURNEY_PATTERNS.call(
              filterRouteIds
            , replaceScheduledStopPointId
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
     * @deprecated Unknown data type. Parameter type or return type is unknown.
     * If this is a qualified, user-defined type, it may have been excluded from
     * code generation. If this is a built-in type, you can define an explicit
     * {@link org.jooq.Binding} to specify how this type should be handled.
     * Deprecation can be turned off using {@literal
     * <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static GetBrokenRouteJourneyPatterns getBrokenRouteJourneyPatterns(
          UUID[] filterRouteIds
        , UUID replaceScheduledStopPointId
        , UUID newLocatedOnInfrastructureLinkId
        , Object newMeasuredLocation
        , String newDirection
        , String newLabel
        , LocalDate newValidityStart
        , LocalDate newValidityEnd
        , Integer newPriority
    ) {
        return fi.hsl.jore.jore4.jooq.journey_pattern.tables.GetBrokenRouteJourneyPatterns.GET_BROKEN_ROUTE_JOURNEY_PATTERNS.call(
            filterRouteIds,
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
     * @deprecated Unknown data type. Parameter type or return type is unknown.
     * If this is a qualified, user-defined type, it may have been excluded from
     * code generation. If this is a built-in type, you can define an explicit
     * {@link org.jooq.Binding} to specify how this type should be handled.
     * Deprecation can be turned off using {@literal
     * <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static GetBrokenRouteJourneyPatterns getBrokenRouteJourneyPatterns(
          Field<UUID[]> filterRouteIds
        , Field<UUID> replaceScheduledStopPointId
        , Field<UUID> newLocatedOnInfrastructureLinkId
        , Field<Object> newMeasuredLocation
        , Field<String> newDirection
        , Field<String> newLabel
        , Field<LocalDate> newValidityStart
        , Field<LocalDate> newValidityEnd
        , Field<Integer> newPriority
    ) {
        return fi.hsl.jore.jore4.jooq.journey_pattern.tables.GetBrokenRouteJourneyPatterns.GET_BROKEN_ROUTE_JOURNEY_PATTERNS.call(
            filterRouteIds,
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
     * @deprecated Unknown data type. Parameter type or return type is unknown.
     * If this is a qualified, user-defined type, it may have been excluded from
     * code generation. If this is a built-in type, you can define an explicit
     * {@link org.jooq.Binding} to specify how this type should be handled.
     * Deprecation can be turned off using {@literal
     * <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static Result<Record> maximumPriorityValiditySpans(
          Configuration configuration
        , String entityType
        , String[] filterRouteLabels
        , LocalDate filterValidityStart
        , LocalDate filterValidityEnd
        , Integer upperPriorityLimit
        , UUID replaceScheduledStopPointId
        , UUID newScheduledStopPointId
        , UUID newLocatedOnInfrastructureLinkId
        , Object newMeasuredLocation
        , String newDirection
        , String newLabel
        , LocalDate newValidityStart
        , LocalDate newValidityEnd
        , Integer newPriority
    ) {
        return configuration.dsl().selectFrom(fi.hsl.jore.jore4.jooq.journey_pattern.tables.MaximumPriorityValiditySpans.MAXIMUM_PRIORITY_VALIDITY_SPANS.call(
              entityType
            , filterRouteLabels
            , filterValidityStart
            , filterValidityEnd
            , upperPriorityLimit
            , replaceScheduledStopPointId
            , newScheduledStopPointId
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
     * @deprecated Unknown data type. Parameter type or return type is unknown.
     * If this is a qualified, user-defined type, it may have been excluded from
     * code generation. If this is a built-in type, you can define an explicit
     * {@link org.jooq.Binding} to specify how this type should be handled.
     * Deprecation can be turned off using {@literal
     * <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static MaximumPriorityValiditySpans maximumPriorityValiditySpans(
          String entityType
        , String[] filterRouteLabels
        , LocalDate filterValidityStart
        , LocalDate filterValidityEnd
        , Integer upperPriorityLimit
        , UUID replaceScheduledStopPointId
        , UUID newScheduledStopPointId
        , UUID newLocatedOnInfrastructureLinkId
        , Object newMeasuredLocation
        , String newDirection
        , String newLabel
        , LocalDate newValidityStart
        , LocalDate newValidityEnd
        , Integer newPriority
    ) {
        return fi.hsl.jore.jore4.jooq.journey_pattern.tables.MaximumPriorityValiditySpans.MAXIMUM_PRIORITY_VALIDITY_SPANS.call(
            entityType,
            filterRouteLabels,
            filterValidityStart,
            filterValidityEnd,
            upperPriorityLimit,
            replaceScheduledStopPointId,
            newScheduledStopPointId,
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
     * @deprecated Unknown data type. Parameter type or return type is unknown.
     * If this is a qualified, user-defined type, it may have been excluded from
     * code generation. If this is a built-in type, you can define an explicit
     * {@link org.jooq.Binding} to specify how this type should be handled.
     * Deprecation can be turned off using {@literal
     * <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static MaximumPriorityValiditySpans maximumPriorityValiditySpans(
          Field<String> entityType
        , Field<String[]> filterRouteLabels
        , Field<LocalDate> filterValidityStart
        , Field<LocalDate> filterValidityEnd
        , Field<Integer> upperPriorityLimit
        , Field<UUID> replaceScheduledStopPointId
        , Field<UUID> newScheduledStopPointId
        , Field<UUID> newLocatedOnInfrastructureLinkId
        , Field<Object> newMeasuredLocation
        , Field<String> newDirection
        , Field<String> newLabel
        , Field<LocalDate> newValidityStart
        , Field<LocalDate> newValidityEnd
        , Field<Integer> newPriority
    ) {
        return fi.hsl.jore.jore4.jooq.journey_pattern.tables.MaximumPriorityValiditySpans.MAXIMUM_PRIORITY_VALIDITY_SPANS.call(
            entityType,
            filterRouteLabels,
            filterValidityStart,
            filterValidityEnd,
            upperPriorityLimit,
            replaceScheduledStopPointId,
            newScheduledStopPointId,
            newLocatedOnInfrastructureLinkId,
            newMeasuredLocation,
            newDirection,
            newLabel,
            newValidityStart,
            newValidityEnd,
            newPriority
        );
    }
}

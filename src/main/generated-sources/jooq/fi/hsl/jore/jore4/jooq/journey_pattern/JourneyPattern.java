/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.journey_pattern;


import fi.hsl.jore.jore4.jooq.DefaultCatalog;
import fi.hsl.jore.jore4.jooq.journey_pattern.tables.CheckInfraLinkStopRefsWithNewScheduledStopPoint;
import fi.hsl.jore.jore4.jooq.journey_pattern.tables.GetBrokenRouteCheckFilters;
import fi.hsl.jore.jore4.jooq.journey_pattern.tables.GetBrokenRouteJourneyPatterns;
import fi.hsl.jore.jore4.jooq.journey_pattern.tables.MaximumPriorityValiditySpans;
import fi.hsl.jore.jore4.jooq.journey_pattern.tables.ScheduledStopPointInJourneyPattern;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Catalog;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JourneyPattern extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>journey_pattern</code>
     */
    public static final JourneyPattern JOURNEY_PATTERN = new JourneyPattern();

    /**
     * The table <code>journey_pattern.check_infra_link_stop_refs_with_new_scheduled_stop_point</code>.
     */
    public final CheckInfraLinkStopRefsWithNewScheduledStopPoint CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT = CheckInfraLinkStopRefsWithNewScheduledStopPoint.CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT;

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static Result<Record> CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT(
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
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static CheckInfraLinkStopRefsWithNewScheduledStopPoint CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT(
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
              replaceScheduledStopPointId
            , newLocatedOnInfrastructureLinkId
            , newMeasuredLocation
            , newDirection
            , newLabel
            , newValidityStart
            , newValidityEnd
            , newPriority
        );
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static CheckInfraLinkStopRefsWithNewScheduledStopPoint CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT(
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
              replaceScheduledStopPointId
            , newLocatedOnInfrastructureLinkId
            , newMeasuredLocation
            , newDirection
            , newLabel
            , newValidityStart
            , newValidityEnd
            , newPriority
        );
    }

    /**
     * The table <code>journey_pattern.get_broken_route_check_filters</code>.
     */
    public final GetBrokenRouteCheckFilters GET_BROKEN_ROUTE_CHECK_FILTERS = GetBrokenRouteCheckFilters.GET_BROKEN_ROUTE_CHECK_FILTERS;

    /**
     * Call <code>journey_pattern.get_broken_route_check_filters</code>.
     */
    public static Result<Record> GET_BROKEN_ROUTE_CHECK_FILTERS(
          Configuration configuration
        , UUID[] filterRouteIds
    ) {
        return configuration.dsl().selectFrom(fi.hsl.jore.jore4.jooq.journey_pattern.tables.GetBrokenRouteCheckFilters.GET_BROKEN_ROUTE_CHECK_FILTERS.call(
              filterRouteIds
        )).fetch();
    }

    /**
     * Get <code>journey_pattern.get_broken_route_check_filters</code> as a table.
     */
    public static GetBrokenRouteCheckFilters GET_BROKEN_ROUTE_CHECK_FILTERS(
          UUID[] filterRouteIds
    ) {
        return fi.hsl.jore.jore4.jooq.journey_pattern.tables.GetBrokenRouteCheckFilters.GET_BROKEN_ROUTE_CHECK_FILTERS.call(
              filterRouteIds
        );
    }

    /**
     * Get <code>journey_pattern.get_broken_route_check_filters</code> as a table.
     */
    public static GetBrokenRouteCheckFilters GET_BROKEN_ROUTE_CHECK_FILTERS(
          Field<UUID[]> filterRouteIds
    ) {
        return fi.hsl.jore.jore4.jooq.journey_pattern.tables.GetBrokenRouteCheckFilters.GET_BROKEN_ROUTE_CHECK_FILTERS.call(
              filterRouteIds
        );
    }

    /**
     * The table <code>journey_pattern.get_broken_route_journey_patterns</code>.
     */
    public final GetBrokenRouteJourneyPatterns GET_BROKEN_ROUTE_JOURNEY_PATTERNS = GetBrokenRouteJourneyPatterns.GET_BROKEN_ROUTE_JOURNEY_PATTERNS;

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static Result<Record> GET_BROKEN_ROUTE_JOURNEY_PATTERNS(
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
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static GetBrokenRouteJourneyPatterns GET_BROKEN_ROUTE_JOURNEY_PATTERNS(
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
              filterRouteIds
            , replaceScheduledStopPointId
            , newLocatedOnInfrastructureLinkId
            , newMeasuredLocation
            , newDirection
            , newLabel
            , newValidityStart
            , newValidityEnd
            , newPriority
        );
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static GetBrokenRouteJourneyPatterns GET_BROKEN_ROUTE_JOURNEY_PATTERNS(
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
              filterRouteIds
            , replaceScheduledStopPointId
            , newLocatedOnInfrastructureLinkId
            , newMeasuredLocation
            , newDirection
            , newLabel
            , newValidityStart
            , newValidityEnd
            , newPriority
        );
    }

    /**
     * The journey patterns, i.e. the ordered lists of stops and timing points along routes: https://www.transmodel-cen.eu/model/index.htm?goto=2:3:1:813
     */
    public final fi.hsl.jore.jore4.jooq.journey_pattern.tables.JourneyPattern JOURNEY_PATTERN_ = fi.hsl.jore.jore4.jooq.journey_pattern.tables.JourneyPattern.JOURNEY_PATTERN_;

    /**
     * The table <code>journey_pattern.maximum_priority_validity_spans</code>.
     */
    public final MaximumPriorityValiditySpans MAXIMUM_PRIORITY_VALIDITY_SPANS = MaximumPriorityValiditySpans.MAXIMUM_PRIORITY_VALIDITY_SPANS;

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static Result<Record> MAXIMUM_PRIORITY_VALIDITY_SPANS(
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
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static MaximumPriorityValiditySpans MAXIMUM_PRIORITY_VALIDITY_SPANS(
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
        );
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static MaximumPriorityValiditySpans MAXIMUM_PRIORITY_VALIDITY_SPANS(
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
        );
    }

    /**
     * The scheduled stop points that form the journey pattern, in order: https://www.transmodel-cen.eu/model/index.htm?goto=2:3:1:813 . For HSL, all timing points are stops, hence journey pattern instead of service pattern.
     */
    public final ScheduledStopPointInJourneyPattern SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN = ScheduledStopPointInJourneyPattern.SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN;

    /**
     * No further instances allowed
     */
    private JourneyPattern() {
        super("journey_pattern", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            CheckInfraLinkStopRefsWithNewScheduledStopPoint.CHECK_INFRA_LINK_STOP_REFS_WITH_NEW_SCHEDULED_STOP_POINT,
            GetBrokenRouteCheckFilters.GET_BROKEN_ROUTE_CHECK_FILTERS,
            GetBrokenRouteJourneyPatterns.GET_BROKEN_ROUTE_JOURNEY_PATTERNS,
            fi.hsl.jore.jore4.jooq.journey_pattern.tables.JourneyPattern.JOURNEY_PATTERN_,
            MaximumPriorityValiditySpans.MAXIMUM_PRIORITY_VALIDITY_SPANS,
            ScheduledStopPointInJourneyPattern.SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN);
    }
}
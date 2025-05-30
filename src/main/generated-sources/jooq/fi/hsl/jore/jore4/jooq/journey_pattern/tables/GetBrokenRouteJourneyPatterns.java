/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.journey_pattern.tables;


import fi.hsl.jore.jore4.jooq.journey_pattern.JourneyPattern;

import java.time.LocalDate;
import java.util.UUID;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GetBrokenRouteJourneyPatterns extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>journey_pattern.get_broken_route_journey_patterns</code>
     */
    public static final GetBrokenRouteJourneyPatterns GET_BROKEN_ROUTE_JOURNEY_PATTERNS = new GetBrokenRouteJourneyPatterns();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column
     * <code>journey_pattern.get_broken_route_journey_patterns.journey_pattern_id</code>.
     */
    public final TableField<Record, UUID> JOURNEY_PATTERN_ID = createField(DSL.name("journey_pattern_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field(DSL.raw("public.gen_random_uuid()"), SQLDataType.UUID)), this, "");

    /**
     * The column
     * <code>journey_pattern.get_broken_route_journey_patterns.on_route_id</code>.
     */
    public final TableField<Record, UUID> ON_ROUTE_ID = createField(DSL.name("on_route_id"), SQLDataType.UUID.nullable(false), this, "");

    private GetBrokenRouteJourneyPatterns(Name alias, Table<Record> aliased) {
        this(alias, aliased, new Field[] {
            DSL.val(null, SQLDataType.UUID.array()),
            DSL.val(null, SQLDataType.UUID.defaultValue(DSL.field(DSL.raw("NULL::uuid"), SQLDataType.UUID))),
            DSL.val(null, SQLDataType.UUID.defaultValue(DSL.field(DSL.raw("NULL::uuid"), SQLDataType.UUID))),
            DSL.val(null, DefaultDataType.getDefaultDataType("\"public\".\"geography\"").defaultValue(DSL.field(DSL.raw("NULL::geography"), org.jooq.impl.SQLDataType.OTHER))),
            DSL.val(null, SQLDataType.CLOB.defaultValue(DSL.field(DSL.raw("NULL::text"), SQLDataType.CLOB))),
            DSL.val(null, SQLDataType.CLOB.defaultValue(DSL.field(DSL.raw("NULL::text"), SQLDataType.CLOB))),
            DSL.val(null, SQLDataType.LOCALDATE.defaultValue(DSL.field(DSL.raw("NULL::date"), SQLDataType.LOCALDATE))),
            DSL.val(null, SQLDataType.LOCALDATE.defaultValue(DSL.field(DSL.raw("NULL::date"), SQLDataType.LOCALDATE))),
            DSL.val(null, SQLDataType.INTEGER.defaultValue(DSL.field(DSL.raw("NULL::integer"), SQLDataType.INTEGER)))
        });
    }

    private GetBrokenRouteJourneyPatterns(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        this(alias, aliased, parameters, null);
    }

    private GetBrokenRouteJourneyPatterns(Name alias, Table<Record> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.function(), where);
    }

    /**
     * Create an aliased
     * <code>journey_pattern.get_broken_route_journey_patterns</code> table
     * reference
     */
    public GetBrokenRouteJourneyPatterns(String alias) {
        this(DSL.name(alias), GET_BROKEN_ROUTE_JOURNEY_PATTERNS);
    }

    /**
     * Create an aliased
     * <code>journey_pattern.get_broken_route_journey_patterns</code> table
     * reference
     */
    public GetBrokenRouteJourneyPatterns(Name alias) {
        this(alias, GET_BROKEN_ROUTE_JOURNEY_PATTERNS);
    }

    /**
     * Create a <code>journey_pattern.get_broken_route_journey_patterns</code>
     * table reference
     */
    public GetBrokenRouteJourneyPatterns() {
        this(DSL.name("get_broken_route_journey_patterns"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : JourneyPattern.JOURNEY_PATTERN;
    }

    @Override
    public GetBrokenRouteJourneyPatterns as(String alias) {
        return new GetBrokenRouteJourneyPatterns(DSL.name(alias), this, parameters);
    }

    @Override
    public GetBrokenRouteJourneyPatterns as(Name alias) {
        return new GetBrokenRouteJourneyPatterns(alias, this, parameters);
    }

    @Override
    public GetBrokenRouteJourneyPatterns as(Table<?> alias) {
        return new GetBrokenRouteJourneyPatterns(alias.getQualifiedName(), this, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public GetBrokenRouteJourneyPatterns rename(String name) {
        return new GetBrokenRouteJourneyPatterns(DSL.name(name), null, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public GetBrokenRouteJourneyPatterns rename(Name name) {
        return new GetBrokenRouteJourneyPatterns(name, null, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public GetBrokenRouteJourneyPatterns rename(Table<?> name) {
        return new GetBrokenRouteJourneyPatterns(name.getQualifiedName(), null, parameters);
    }

    /**
     * Call this table-valued function
     */
    public GetBrokenRouteJourneyPatterns call(
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
        GetBrokenRouteJourneyPatterns result = new GetBrokenRouteJourneyPatterns(DSL.name("get_broken_route_journey_patterns"), null, new Field[] {
            DSL.val(filterRouteIds, SQLDataType.UUID.array()),
            DSL.val(replaceScheduledStopPointId, SQLDataType.UUID.defaultValue(DSL.field(DSL.raw("NULL::uuid"), SQLDataType.UUID))),
            DSL.val(newLocatedOnInfrastructureLinkId, SQLDataType.UUID.defaultValue(DSL.field(DSL.raw("NULL::uuid"), SQLDataType.UUID))),
            DSL.val(newMeasuredLocation, DefaultDataType.getDefaultDataType("\"public\".\"geography\"").defaultValue(DSL.field(DSL.raw("NULL::geography"), org.jooq.impl.SQLDataType.OTHER))),
            DSL.val(newDirection, SQLDataType.CLOB.defaultValue(DSL.field(DSL.raw("NULL::text"), SQLDataType.CLOB))),
            DSL.val(newLabel, SQLDataType.CLOB.defaultValue(DSL.field(DSL.raw("NULL::text"), SQLDataType.CLOB))),
            DSL.val(newValidityStart, SQLDataType.LOCALDATE.defaultValue(DSL.field(DSL.raw("NULL::date"), SQLDataType.LOCALDATE))),
            DSL.val(newValidityEnd, SQLDataType.LOCALDATE.defaultValue(DSL.field(DSL.raw("NULL::date"), SQLDataType.LOCALDATE))),
            DSL.val(newPriority, SQLDataType.INTEGER.defaultValue(DSL.field(DSL.raw("NULL::integer"), SQLDataType.INTEGER)))
        });

        return aliased() ? result.as(getUnqualifiedName()) : result;
    }

    /**
     * Call this table-valued function
     */
    public GetBrokenRouteJourneyPatterns call(
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
        GetBrokenRouteJourneyPatterns result = new GetBrokenRouteJourneyPatterns(DSL.name("get_broken_route_journey_patterns"), null, new Field[] {
            filterRouteIds,
            replaceScheduledStopPointId,
            newLocatedOnInfrastructureLinkId,
            newMeasuredLocation,
            newDirection,
            newLabel,
            newValidityStart,
            newValidityEnd,
            newPriority
        });

        return aliased() ? result.as(getUnqualifiedName()) : result;
    }
}

/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.journey_pattern.tables;


import fi.hsl.jore.jore4.jooq.journey_pattern.JourneyPattern;

import java.time.LocalDate;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MaximumPriorityValiditySpans extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>journey_pattern.maximum_priority_validity_spans</code>
     */
    public static final MaximumPriorityValiditySpans MAXIMUM_PRIORITY_VALIDITY_SPANS = new MaximumPriorityValiditySpans();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column
     * <code>journey_pattern.maximum_priority_validity_spans.id</code>.
     */
    public final TableField<Record, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID, this, "");

    /**
     * The column
     * <code>journey_pattern.maximum_priority_validity_spans.validity_start</code>.
     */
    public final TableField<Record, LocalDate> VALIDITY_START = createField(DSL.name("validity_start"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column
     * <code>journey_pattern.maximum_priority_validity_spans.validity_end</code>.
     */
    public final TableField<Record, LocalDate> VALIDITY_END = createField(DSL.name("validity_end"), SQLDataType.LOCALDATE, this, "");

    private MaximumPriorityValiditySpans(Name alias, Table<Record> aliased) {
        this(alias, aliased, new Field[] {
            DSL.val(null, SQLDataType.CLOB),
            DSL.val(null, SQLDataType.CLOB.getArrayDataType()),
            DSL.val(null, SQLDataType.LOCALDATE.defaultValue(DSL.field("NULL::date", SQLDataType.LOCALDATE))),
            DSL.val(null, SQLDataType.LOCALDATE.defaultValue(DSL.field("NULL::date", SQLDataType.LOCALDATE))),
            DSL.val(null, SQLDataType.INTEGER.defaultValue(DSL.field("NULL::integer", SQLDataType.INTEGER))),
            DSL.val(null, SQLDataType.UUID.defaultValue(DSL.field("NULL::uuid", SQLDataType.UUID))),
            DSL.val(null, SQLDataType.UUID.defaultValue(DSL.field("NULL::uuid", SQLDataType.UUID))),
            DSL.val(null, SQLDataType.UUID.defaultValue(DSL.field("NULL::uuid", SQLDataType.UUID))),
            DSL.val(null, org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\"").defaultValue(DSL.field("NULL::geography", org.jooq.impl.SQLDataType.OTHER))),
            DSL.val(null, SQLDataType.CLOB.defaultValue(DSL.field("NULL::text", SQLDataType.CLOB))),
            DSL.val(null, SQLDataType.CLOB.defaultValue(DSL.field("NULL::text", SQLDataType.CLOB))),
            DSL.val(null, SQLDataType.LOCALDATE.defaultValue(DSL.field("NULL::date", SQLDataType.LOCALDATE))),
            DSL.val(null, SQLDataType.LOCALDATE.defaultValue(DSL.field("NULL::date", SQLDataType.LOCALDATE))),
            DSL.val(null, SQLDataType.INTEGER.defaultValue(DSL.field("NULL::integer", SQLDataType.INTEGER)))
        });
    }

    private MaximumPriorityValiditySpans(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.function());
    }

    /**
     * Create an aliased
     * <code>journey_pattern.maximum_priority_validity_spans</code> table
     * reference
     */
    public MaximumPriorityValiditySpans(String alias) {
        this(DSL.name(alias), MAXIMUM_PRIORITY_VALIDITY_SPANS);
    }

    /**
     * Create an aliased
     * <code>journey_pattern.maximum_priority_validity_spans</code> table
     * reference
     */
    public MaximumPriorityValiditySpans(Name alias) {
        this(alias, MAXIMUM_PRIORITY_VALIDITY_SPANS);
    }

    /**
     * Create a <code>journey_pattern.maximum_priority_validity_spans</code>
     * table reference
     */
    public MaximumPriorityValiditySpans() {
        this(DSL.name("maximum_priority_validity_spans"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : JourneyPattern.JOURNEY_PATTERN;
    }

    @Override
    public MaximumPriorityValiditySpans as(String alias) {
        return new MaximumPriorityValiditySpans(DSL.name(alias), this, parameters);
    }

    @Override
    public MaximumPriorityValiditySpans as(Name alias) {
        return new MaximumPriorityValiditySpans(alias, this, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public MaximumPriorityValiditySpans rename(String name) {
        return new MaximumPriorityValiditySpans(DSL.name(name), null, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public MaximumPriorityValiditySpans rename(Name name) {
        return new MaximumPriorityValiditySpans(name, null, parameters);
    }

    /**
     * Call this table-valued function
     */
    public MaximumPriorityValiditySpans call(
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
        MaximumPriorityValiditySpans result = new MaximumPriorityValiditySpans(DSL.name("maximum_priority_validity_spans"), null, new Field[] {
            DSL.val(entityType, SQLDataType.CLOB),
            DSL.val(filterRouteLabels, SQLDataType.CLOB.getArrayDataType()),
            DSL.val(filterValidityStart, SQLDataType.LOCALDATE.defaultValue(DSL.field("NULL::date", SQLDataType.LOCALDATE))),
            DSL.val(filterValidityEnd, SQLDataType.LOCALDATE.defaultValue(DSL.field("NULL::date", SQLDataType.LOCALDATE))),
            DSL.val(upperPriorityLimit, SQLDataType.INTEGER.defaultValue(DSL.field("NULL::integer", SQLDataType.INTEGER))),
            DSL.val(replaceScheduledStopPointId, SQLDataType.UUID.defaultValue(DSL.field("NULL::uuid", SQLDataType.UUID))),
            DSL.val(newScheduledStopPointId, SQLDataType.UUID.defaultValue(DSL.field("NULL::uuid", SQLDataType.UUID))),
            DSL.val(newLocatedOnInfrastructureLinkId, SQLDataType.UUID.defaultValue(DSL.field("NULL::uuid", SQLDataType.UUID))),
            DSL.val(newMeasuredLocation, org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\"").defaultValue(DSL.field("NULL::geography", org.jooq.impl.SQLDataType.OTHER))),
            DSL.val(newDirection, SQLDataType.CLOB.defaultValue(DSL.field("NULL::text", SQLDataType.CLOB))),
            DSL.val(newLabel, SQLDataType.CLOB.defaultValue(DSL.field("NULL::text", SQLDataType.CLOB))),
            DSL.val(newValidityStart, SQLDataType.LOCALDATE.defaultValue(DSL.field("NULL::date", SQLDataType.LOCALDATE))),
            DSL.val(newValidityEnd, SQLDataType.LOCALDATE.defaultValue(DSL.field("NULL::date", SQLDataType.LOCALDATE))),
            DSL.val(newPriority, SQLDataType.INTEGER.defaultValue(DSL.field("NULL::integer", SQLDataType.INTEGER)))
        });

        return aliased() ? result.as(getUnqualifiedName()) : result;
    }

    /**
     * Call this table-valued function
     */
    public MaximumPriorityValiditySpans call(
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
        MaximumPriorityValiditySpans result = new MaximumPriorityValiditySpans(DSL.name("maximum_priority_validity_spans"), null, new Field[] {
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
        });

        return aliased() ? result.as(getUnqualifiedName()) : result;
    }
}
/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.journey_pattern.tables;


import fi.hsl.jore.jore4.jooq.journey_pattern.JourneyPattern;

import java.util.Collection;
import java.util.UUID;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * The scheduled stop points that form the journey pattern, in order:
 * https://www.transmodel-cen.eu/model/index.htm?goto=2:3:1:813 . For HSL, all
 * timing points are stops, hence journey pattern instead of service pattern.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ScheduledStopPointInJourneyPattern extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>journey_pattern.scheduled_stop_point_in_journey_pattern</code>
     */
    public static final ScheduledStopPointInJourneyPattern SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN = new ScheduledStopPointInJourneyPattern();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column
     * <code>journey_pattern.scheduled_stop_point_in_journey_pattern.journey_pattern_id</code>.
     * The ID of the journey pattern.
     */
    public final TableField<Record, UUID> JOURNEY_PATTERN_ID = createField(DSL.name("journey_pattern_id"), SQLDataType.UUID.nullable(false), this, "The ID of the journey pattern.");

    /**
     * The column
     * <code>journey_pattern.scheduled_stop_point_in_journey_pattern.scheduled_stop_point_sequence</code>.
     * The order of the scheduled stop point within the journey pattern.
     */
    public final TableField<Record, Integer> SCHEDULED_STOP_POINT_SEQUENCE = createField(DSL.name("scheduled_stop_point_sequence"), SQLDataType.INTEGER.nullable(false), this, "The order of the scheduled stop point within the journey pattern.");

    /**
     * The column
     * <code>journey_pattern.scheduled_stop_point_in_journey_pattern.is_used_as_timing_point</code>.
     * Is this scheduled stop point used as a timing point in the journey
     * pattern?
     */
    public final TableField<Record, Boolean> IS_USED_AS_TIMING_POINT = createField(DSL.name("is_used_as_timing_point"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.field(DSL.raw("false"), SQLDataType.BOOLEAN)), this, "Is this scheduled stop point used as a timing point in the journey pattern?");

    /**
     * The column
     * <code>journey_pattern.scheduled_stop_point_in_journey_pattern.is_via_point</code>.
     * Is this scheduled stop point a via point?
     */
    public final TableField<Record, Boolean> IS_VIA_POINT = createField(DSL.name("is_via_point"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.field(DSL.raw("false"), SQLDataType.BOOLEAN)), this, "Is this scheduled stop point a via point?");

    /**
     * The column
     * <code>journey_pattern.scheduled_stop_point_in_journey_pattern.via_point_name_i18n</code>.
     */
    public final TableField<Record, JSONB> VIA_POINT_NAME_I18N = createField(DSL.name("via_point_name_i18n"), SQLDataType.JSONB, this, "");

    /**
     * The column
     * <code>journey_pattern.scheduled_stop_point_in_journey_pattern.via_point_short_name_i18n</code>.
     */
    public final TableField<Record, JSONB> VIA_POINT_SHORT_NAME_I18N = createField(DSL.name("via_point_short_name_i18n"), SQLDataType.JSONB, this, "");

    /**
     * The column
     * <code>journey_pattern.scheduled_stop_point_in_journey_pattern.scheduled_stop_point_label</code>.
     */
    public final TableField<Record, String> SCHEDULED_STOP_POINT_LABEL = createField(DSL.name("scheduled_stop_point_label"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>journey_pattern.scheduled_stop_point_in_journey_pattern.is_loading_time_allowed</code>.
     * Is adding loading time to this scheduled stop point in the journey
     * pattern allowed?
     */
    public final TableField<Record, Boolean> IS_LOADING_TIME_ALLOWED = createField(DSL.name("is_loading_time_allowed"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.field(DSL.raw("false"), SQLDataType.BOOLEAN)), this, "Is adding loading time to this scheduled stop point in the journey pattern allowed?");

    /**
     * The column
     * <code>journey_pattern.scheduled_stop_point_in_journey_pattern.is_regulated_timing_point</code>.
     * Is this stop point passing time regulated so that it cannot be passed
     * before scheduled time?
     */
    public final TableField<Record, Boolean> IS_REGULATED_TIMING_POINT = createField(DSL.name("is_regulated_timing_point"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.field(DSL.raw("false"), SQLDataType.BOOLEAN)), this, "Is this stop point passing time regulated so that it cannot be passed before scheduled time?");

    private ScheduledStopPointInJourneyPattern(Name alias, Table<Record> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private ScheduledStopPointInJourneyPattern(Name alias, Table<Record> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment("The scheduled stop points that form the journey pattern, in order: https://www.transmodel-cen.eu/model/index.htm?goto=2:3:1:813 . For HSL, all timing points are stops, hence journey pattern instead of service pattern."), TableOptions.table(), where);
    }

    /**
     * Create an aliased
     * <code>journey_pattern.scheduled_stop_point_in_journey_pattern</code>
     * table reference
     */
    public ScheduledStopPointInJourneyPattern(String alias) {
        this(DSL.name(alias), SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN);
    }

    /**
     * Create an aliased
     * <code>journey_pattern.scheduled_stop_point_in_journey_pattern</code>
     * table reference
     */
    public ScheduledStopPointInJourneyPattern(Name alias) {
        this(alias, SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN);
    }

    /**
     * Create a
     * <code>journey_pattern.scheduled_stop_point_in_journey_pattern</code>
     * table reference
     */
    public ScheduledStopPointInJourneyPattern() {
        this(DSL.name("scheduled_stop_point_in_journey_pattern"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : JourneyPattern.JOURNEY_PATTERN;
    }

    @Override
    public ScheduledStopPointInJourneyPattern as(String alias) {
        return new ScheduledStopPointInJourneyPattern(DSL.name(alias), this);
    }

    @Override
    public ScheduledStopPointInJourneyPattern as(Name alias) {
        return new ScheduledStopPointInJourneyPattern(alias, this);
    }

    @Override
    public ScheduledStopPointInJourneyPattern as(Table<?> alias) {
        return new ScheduledStopPointInJourneyPattern(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPointInJourneyPattern rename(String name) {
        return new ScheduledStopPointInJourneyPattern(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPointInJourneyPattern rename(Name name) {
        return new ScheduledStopPointInJourneyPattern(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPointInJourneyPattern rename(Table<?> name) {
        return new ScheduledStopPointInJourneyPattern(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointInJourneyPattern where(Condition condition) {
        return new ScheduledStopPointInJourneyPattern(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointInJourneyPattern where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointInJourneyPattern where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointInJourneyPattern where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScheduledStopPointInJourneyPattern where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScheduledStopPointInJourneyPattern where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScheduledStopPointInJourneyPattern where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScheduledStopPointInJourneyPattern where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointInJourneyPattern whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPointInJourneyPattern whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}

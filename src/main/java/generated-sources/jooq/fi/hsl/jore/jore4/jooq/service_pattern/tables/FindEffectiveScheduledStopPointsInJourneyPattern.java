/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.service_pattern.tables;


import fi.hsl.jore.jore4.jooq.service_pattern.ServicePattern;

import java.time.LocalDate;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.JSONB;
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
public class FindEffectiveScheduledStopPointsInJourneyPattern extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>service_pattern.find_effective_scheduled_stop_points_in_journey_pattern</code>
     */
    public static final FindEffectiveScheduledStopPointsInJourneyPattern FIND_EFFECTIVE_SCHEDULED_STOP_POINTS_IN_JOURNEY_PATTERN = new FindEffectiveScheduledStopPointsInJourneyPattern();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>service_pattern.find_effective_scheduled_stop_points_in_journey_pattern.journey_pattern_id</code>.
     */
    public final TableField<Record, UUID> JOURNEY_PATTERN_ID = createField(DSL.name("journey_pattern_id"), SQLDataType.UUID, this, "");

    /**
     * The column <code>service_pattern.find_effective_scheduled_stop_points_in_journey_pattern.scheduled_stop_point_sequence</code>.
     */
    public final TableField<Record, Integer> SCHEDULED_STOP_POINT_SEQUENCE = createField(DSL.name("scheduled_stop_point_sequence"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>service_pattern.find_effective_scheduled_stop_points_in_journey_pattern.is_used_as_timing_point</code>.
     */
    public final TableField<Record, Boolean> IS_USED_AS_TIMING_POINT = createField(DSL.name("is_used_as_timing_point"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>service_pattern.find_effective_scheduled_stop_points_in_journey_pattern.is_loading_time_allowed</code>.
     */
    public final TableField<Record, Boolean> IS_LOADING_TIME_ALLOWED = createField(DSL.name("is_loading_time_allowed"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>service_pattern.find_effective_scheduled_stop_points_in_journey_pattern.is_regulated_timing_point</code>.
     */
    public final TableField<Record, Boolean> IS_REGULATED_TIMING_POINT = createField(DSL.name("is_regulated_timing_point"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>service_pattern.find_effective_scheduled_stop_points_in_journey_pattern.is_via_point</code>.
     */
    public final TableField<Record, Boolean> IS_VIA_POINT = createField(DSL.name("is_via_point"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>service_pattern.find_effective_scheduled_stop_points_in_journey_pattern.via_point_name_i18n</code>.
     */
    public final TableField<Record, JSONB> VIA_POINT_NAME_I18N = createField(DSL.name("via_point_name_i18n"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>service_pattern.find_effective_scheduled_stop_points_in_journey_pattern.via_point_short_name_i18n</code>.
     */
    public final TableField<Record, JSONB> VIA_POINT_SHORT_NAME_I18N = createField(DSL.name("via_point_short_name_i18n"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>service_pattern.find_effective_scheduled_stop_points_in_journey_pattern.effective_scheduled_stop_point_id</code>.
     */
    public final TableField<Record, UUID> EFFECTIVE_SCHEDULED_STOP_POINT_ID = createField(DSL.name("effective_scheduled_stop_point_id"), SQLDataType.UUID, this, "");

    private FindEffectiveScheduledStopPointsInJourneyPattern(Name alias, Table<Record> aliased) {
        this(alias, aliased, new Field[3]);
    }

    private FindEffectiveScheduledStopPointsInJourneyPattern(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.function());
    }

    /**
     * Create an aliased <code>service_pattern.find_effective_scheduled_stop_points_in_journey_pattern</code> table reference
     */
    public FindEffectiveScheduledStopPointsInJourneyPattern(String alias) {
        this(DSL.name(alias), FIND_EFFECTIVE_SCHEDULED_STOP_POINTS_IN_JOURNEY_PATTERN);
    }

    /**
     * Create an aliased <code>service_pattern.find_effective_scheduled_stop_points_in_journey_pattern</code> table reference
     */
    public FindEffectiveScheduledStopPointsInJourneyPattern(Name alias) {
        this(alias, FIND_EFFECTIVE_SCHEDULED_STOP_POINTS_IN_JOURNEY_PATTERN);
    }

    /**
     * Create a <code>service_pattern.find_effective_scheduled_stop_points_in_journey_pattern</code> table reference
     */
    public FindEffectiveScheduledStopPointsInJourneyPattern() {
        this(DSL.name("find_effective_scheduled_stop_points_in_journey_pattern"), null);
    }

    @Override
    public Schema getSchema() {
        return ServicePattern.SERVICE_PATTERN;
    }

    @Override
    public FindEffectiveScheduledStopPointsInJourneyPattern as(String alias) {
        return new FindEffectiveScheduledStopPointsInJourneyPattern(DSL.name(alias), this, parameters);
    }

    @Override
    public FindEffectiveScheduledStopPointsInJourneyPattern as(Name alias) {
        return new FindEffectiveScheduledStopPointsInJourneyPattern(alias, this, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public FindEffectiveScheduledStopPointsInJourneyPattern rename(String name) {
        return new FindEffectiveScheduledStopPointsInJourneyPattern(DSL.name(name), null, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public FindEffectiveScheduledStopPointsInJourneyPattern rename(Name name) {
        return new FindEffectiveScheduledStopPointsInJourneyPattern(name, null, parameters);
    }

    /**
     * Call this table-valued function
     */
    public FindEffectiveScheduledStopPointsInJourneyPattern call(
          UUID filterJourneyPatternId
        , LocalDate observationDate
        , Boolean includeDraftStops
    ) {
        FindEffectiveScheduledStopPointsInJourneyPattern result = new FindEffectiveScheduledStopPointsInJourneyPattern(DSL.name("find_effective_scheduled_stop_points_in_journey_pattern"), null, new Field[] {
              DSL.val(filterJourneyPatternId, SQLDataType.UUID)
            , DSL.val(observationDate, SQLDataType.LOCALDATE)
            , DSL.val(includeDraftStops, SQLDataType.BOOLEAN)
        });

        return aliased() ? result.as(getUnqualifiedName()) : result;
    }

    /**
     * Call this table-valued function
     */
    public FindEffectiveScheduledStopPointsInJourneyPattern call(
          Field<UUID> filterJourneyPatternId
        , Field<LocalDate> observationDate
        , Field<Boolean> includeDraftStops
    ) {
        FindEffectiveScheduledStopPointsInJourneyPattern result = new FindEffectiveScheduledStopPointsInJourneyPattern(DSL.name("find_effective_scheduled_stop_points_in_journey_pattern"), null, new Field[] {
              filterJourneyPatternId
            , observationDate
            , includeDraftStops
        });

        return aliased() ? result.as(getUnqualifiedName()) : result;
    }
}

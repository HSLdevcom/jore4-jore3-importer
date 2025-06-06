/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.service_pattern.tables;


import fi.hsl.jore.importer.config.jooq.converter.geometry.PointBinding;
import fi.hsl.jore.jore4.jooq.service_pattern.ServicePattern;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

import org.jooq.Condition;
import org.jooq.Field;
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
import org.locationtech.jts.geom.Point;


/**
 * The scheduled stop points:
 * https://www.transmodel-cen.eu/model/index.htm?goto=2:3:4:845 . Colloquially
 * known as stops from the perspective of timetable planning.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ScheduledStopPoint extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>service_pattern.scheduled_stop_point</code>
     */
    public static final ScheduledStopPoint SCHEDULED_STOP_POINT = new ScheduledStopPoint();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column
     * <code>service_pattern.scheduled_stop_point.scheduled_stop_point_id</code>.
     * The ID of the scheduled stop point.
     */
    public final TableField<Record, UUID> SCHEDULED_STOP_POINT_ID = createField(DSL.name("scheduled_stop_point_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field(DSL.raw("public.gen_random_uuid()"), SQLDataType.UUID)), this, "The ID of the scheduled stop point.");

    /**
     * The column
     * <code>service_pattern.scheduled_stop_point.measured_location</code>. The
     * measured location describes the physical location of the stop. For some
     * stops this describes the location of the pole-mounted flag. A PostGIS
     * PointZ geography in EPSG:4326.
     */
    public final TableField<Record, Point> MEASURED_LOCATION = createField(DSL.name("measured_location"), SQLDataType.OTHER.nullable(false), this, "The measured location describes the physical location of the stop. For some stops this describes the location of the pole-mounted flag. A PostGIS PointZ geography in EPSG:4326.", new PointBinding());

    /**
     * The column
     * <code>service_pattern.scheduled_stop_point.located_on_infrastructure_link_id</code>.
     * The infrastructure link on which the stop is located.
     */
    public final TableField<Record, UUID> LOCATED_ON_INFRASTRUCTURE_LINK_ID = createField(DSL.name("located_on_infrastructure_link_id"), SQLDataType.UUID.nullable(false), this, "The infrastructure link on which the stop is located.");

    /**
     * The column <code>service_pattern.scheduled_stop_point.direction</code>.
     * The direction(s) of traffic with respect to the digitization, i.e. the
     * direction of the specified line string.
     */
    public final TableField<Record, String> DIRECTION = createField(DSL.name("direction"), SQLDataType.CLOB.nullable(false), this, "The direction(s) of traffic with respect to the digitization, i.e. the direction of the specified line string.");

    /**
     * The column <code>service_pattern.scheduled_stop_point.label</code>. The
     * label is the short code that identifies the stop to the passengers. There
     * can be at most one stop with the same label at a time. The label matches
     * the GTFS stop_code.
     */
    public final TableField<Record, String> LABEL = createField(DSL.name("label"), SQLDataType.CLOB.nullable(false), this, "The label is the short code that identifies the stop to the passengers. There can be at most one stop with the same label at a time. The label matches the GTFS stop_code.");

    /**
     * The column
     * <code>service_pattern.scheduled_stop_point.validity_start</code>. start
     * of the operating date span in the scheduled stop point's local time
     * (inclusive).
     */
    public final TableField<Record, LocalDate> VALIDITY_START = createField(DSL.name("validity_start"), SQLDataType.LOCALDATE, this, "start of the operating date span in the scheduled stop point's local time (inclusive).");

    /**
     * The column
     * <code>service_pattern.scheduled_stop_point.validity_end</code>. end of
     * the operating date span in the scheduled stop point's local time
     * (inclusive).
     */
    public final TableField<Record, LocalDate> VALIDITY_END = createField(DSL.name("validity_end"), SQLDataType.LOCALDATE, this, "end of the operating date span in the scheduled stop point's local time (inclusive).");

    /**
     * The column <code>service_pattern.scheduled_stop_point.priority</code>.
     */
    public final TableField<Record, Integer> PRIORITY = createField(DSL.name("priority"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column
     * <code>service_pattern.scheduled_stop_point.timing_place_id</code>.
     * Optional reference to a TIMING PLACE. If NULL, the SCHEDULED STOP POINT
     * is not used for timing.
     */
    public final TableField<Record, UUID> TIMING_PLACE_ID = createField(DSL.name("timing_place_id"), SQLDataType.UUID, this, "Optional reference to a TIMING PLACE. If NULL, the SCHEDULED STOP POINT is not used for timing.");

    private ScheduledStopPoint(Name alias, Table<Record> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private ScheduledStopPoint(Name alias, Table<Record> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment("The scheduled stop points: https://www.transmodel-cen.eu/model/index.htm?goto=2:3:4:845 . Colloquially known as stops from the perspective of timetable planning."), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>service_pattern.scheduled_stop_point</code> table
     * reference
     */
    public ScheduledStopPoint(String alias) {
        this(DSL.name(alias), SCHEDULED_STOP_POINT);
    }

    /**
     * Create an aliased <code>service_pattern.scheduled_stop_point</code> table
     * reference
     */
    public ScheduledStopPoint(Name alias) {
        this(alias, SCHEDULED_STOP_POINT);
    }

    /**
     * Create a <code>service_pattern.scheduled_stop_point</code> table
     * reference
     */
    public ScheduledStopPoint() {
        this(DSL.name("scheduled_stop_point"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : ServicePattern.SERVICE_PATTERN;
    }

    @Override
    public ScheduledStopPoint as(String alias) {
        return new ScheduledStopPoint(DSL.name(alias), this);
    }

    @Override
    public ScheduledStopPoint as(Name alias) {
        return new ScheduledStopPoint(alias, this);
    }

    @Override
    public ScheduledStopPoint as(Table<?> alias) {
        return new ScheduledStopPoint(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPoint rename(String name) {
        return new ScheduledStopPoint(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPoint rename(Name name) {
        return new ScheduledStopPoint(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPoint rename(Table<?> name) {
        return new ScheduledStopPoint(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPoint where(Condition condition) {
        return new ScheduledStopPoint(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPoint where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPoint where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPoint where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScheduledStopPoint where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScheduledStopPoint where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScheduledStopPoint where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScheduledStopPoint where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPoint whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScheduledStopPoint whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}

/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.service_pattern.tables;


import fi.hsl.jore.importer.config.jooq.converter.geometry.PointBinding;
import fi.hsl.jore.jore4.jooq.service_pattern.ServicePattern;

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
import org.locationtech.jts.geom.Point;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NewScheduledStopPointIfIdGiven extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>service_pattern.new_scheduled_stop_point_if_id_given</code>
     */
    public static final NewScheduledStopPointIfIdGiven NEW_SCHEDULED_STOP_POINT_IF_ID_GIVEN = new NewScheduledStopPointIfIdGiven();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column
     * <code>service_pattern.new_scheduled_stop_point_if_id_given.scheduled_stop_point_id</code>.
     */
    public final TableField<Record, UUID> SCHEDULED_STOP_POINT_ID = createField(DSL.name("scheduled_stop_point_id"), SQLDataType.UUID, this, "");

    /**
     * The column
     * <code>service_pattern.new_scheduled_stop_point_if_id_given.measured_location</code>.
     */
    public final TableField<Record, Point> MEASURED_LOCATION = createField(DSL.name("measured_location"), SQLDataType.OTHER, this, "", new PointBinding());

    /**
     * The column
     * <code>service_pattern.new_scheduled_stop_point_if_id_given.located_on_infrastructure_link_id</code>.
     */
    public final TableField<Record, UUID> LOCATED_ON_INFRASTRUCTURE_LINK_ID = createField(DSL.name("located_on_infrastructure_link_id"), SQLDataType.UUID, this, "");

    /**
     * The column
     * <code>service_pattern.new_scheduled_stop_point_if_id_given.direction</code>.
     */
    public final TableField<Record, String> DIRECTION = createField(DSL.name("direction"), SQLDataType.CLOB, this, "");

    /**
     * The column
     * <code>service_pattern.new_scheduled_stop_point_if_id_given.label</code>.
     */
    public final TableField<Record, String> LABEL = createField(DSL.name("label"), SQLDataType.CLOB, this, "");

    /**
     * The column
     * <code>service_pattern.new_scheduled_stop_point_if_id_given.validity_start</code>.
     */
    public final TableField<Record, LocalDate> VALIDITY_START = createField(DSL.name("validity_start"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column
     * <code>service_pattern.new_scheduled_stop_point_if_id_given.validity_end</code>.
     */
    public final TableField<Record, LocalDate> VALIDITY_END = createField(DSL.name("validity_end"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column
     * <code>service_pattern.new_scheduled_stop_point_if_id_given.priority</code>.
     */
    public final TableField<Record, Integer> PRIORITY = createField(DSL.name("priority"), SQLDataType.INTEGER, this, "");

    /**
     * The column
     * <code>service_pattern.new_scheduled_stop_point_if_id_given.relative_distance_from_infrastructure_link_start</code>.
     */
    public final TableField<Record, Double> RELATIVE_DISTANCE_FROM_INFRASTRUCTURE_LINK_START = createField(DSL.name("relative_distance_from_infrastructure_link_start"), SQLDataType.DOUBLE, this, "");

    private NewScheduledStopPointIfIdGiven(Name alias, Table<Record> aliased) {
        this(alias, aliased, new Field[] {
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

    private NewScheduledStopPointIfIdGiven(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        this(alias, aliased, parameters, null);
    }

    private NewScheduledStopPointIfIdGiven(Name alias, Table<Record> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.function(), where);
    }

    /**
     * Create an aliased
     * <code>service_pattern.new_scheduled_stop_point_if_id_given</code> table
     * reference
     */
    public NewScheduledStopPointIfIdGiven(String alias) {
        this(DSL.name(alias), NEW_SCHEDULED_STOP_POINT_IF_ID_GIVEN);
    }

    /**
     * Create an aliased
     * <code>service_pattern.new_scheduled_stop_point_if_id_given</code> table
     * reference
     */
    public NewScheduledStopPointIfIdGiven(Name alias) {
        this(alias, NEW_SCHEDULED_STOP_POINT_IF_ID_GIVEN);
    }

    /**
     * Create a
     * <code>service_pattern.new_scheduled_stop_point_if_id_given</code> table
     * reference
     */
    public NewScheduledStopPointIfIdGiven() {
        this(DSL.name("new_scheduled_stop_point_if_id_given"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : ServicePattern.SERVICE_PATTERN;
    }

    @Override
    public NewScheduledStopPointIfIdGiven as(String alias) {
        return new NewScheduledStopPointIfIdGiven(DSL.name(alias), this, parameters);
    }

    @Override
    public NewScheduledStopPointIfIdGiven as(Name alias) {
        return new NewScheduledStopPointIfIdGiven(alias, this, parameters);
    }

    @Override
    public NewScheduledStopPointIfIdGiven as(Table<?> alias) {
        return new NewScheduledStopPointIfIdGiven(alias.getQualifiedName(), this, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public NewScheduledStopPointIfIdGiven rename(String name) {
        return new NewScheduledStopPointIfIdGiven(DSL.name(name), null, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public NewScheduledStopPointIfIdGiven rename(Name name) {
        return new NewScheduledStopPointIfIdGiven(name, null, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public NewScheduledStopPointIfIdGiven rename(Table<?> name) {
        return new NewScheduledStopPointIfIdGiven(name.getQualifiedName(), null, parameters);
    }

    /**
     * Call this table-valued function
     */
    public NewScheduledStopPointIfIdGiven call(
          UUID newScheduledStopPointId
        , UUID newLocatedOnInfrastructureLinkId
        , Object newMeasuredLocation
        , String newDirection
        , String newLabel
        , LocalDate newValidityStart
        , LocalDate newValidityEnd
        , Integer newPriority
    ) {
        NewScheduledStopPointIfIdGiven result = new NewScheduledStopPointIfIdGiven(DSL.name("new_scheduled_stop_point_if_id_given"), null, new Field[] {
            DSL.val(newScheduledStopPointId, SQLDataType.UUID.defaultValue(DSL.field(DSL.raw("NULL::uuid"), SQLDataType.UUID))),
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
    public NewScheduledStopPointIfIdGiven call(
          Field<UUID> newScheduledStopPointId
        , Field<UUID> newLocatedOnInfrastructureLinkId
        , Field<Object> newMeasuredLocation
        , Field<String> newDirection
        , Field<String> newLabel
        , Field<LocalDate> newValidityStart
        , Field<LocalDate> newValidityEnd
        , Field<Integer> newPriority
    ) {
        NewScheduledStopPointIfIdGiven result = new NewScheduledStopPointIfIdGiven(DSL.name("new_scheduled_stop_point_if_id_given"), null, new Field[] {
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

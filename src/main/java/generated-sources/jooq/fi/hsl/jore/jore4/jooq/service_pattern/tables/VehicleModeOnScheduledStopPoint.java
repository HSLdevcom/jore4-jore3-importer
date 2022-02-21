/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.service_pattern.tables;


import fi.hsl.jore.jore4.jooq.service_pattern.ServicePattern;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
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
 * Which scheduled stop points are serviced by which vehicle modes?
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class VehicleModeOnScheduledStopPoint extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>service_pattern.vehicle_mode_on_scheduled_stop_point</code>
     */
    public static final VehicleModeOnScheduledStopPoint VEHICLE_MODE_ON_SCHEDULED_STOP_POINT = new VehicleModeOnScheduledStopPoint();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>service_pattern.vehicle_mode_on_scheduled_stop_point.scheduled_stop_point_id</code>. The scheduled stop point that is serviced by the vehicle mode.
     */
    public final TableField<Record, UUID> SCHEDULED_STOP_POINT_ID = createField(DSL.name("scheduled_stop_point_id"), SQLDataType.UUID.nullable(false), this, "The scheduled stop point that is serviced by the vehicle mode.");

    /**
     * The column <code>service_pattern.vehicle_mode_on_scheduled_stop_point.vehicle_mode</code>. The vehicle mode servicing the scheduled stop point.
     */
    public final TableField<Record, String> VEHICLE_MODE = createField(DSL.name("vehicle_mode"), SQLDataType.CLOB.nullable(false), this, "The vehicle mode servicing the scheduled stop point.");

    private VehicleModeOnScheduledStopPoint(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private VehicleModeOnScheduledStopPoint(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("Which scheduled stop points are serviced by which vehicle modes?"), TableOptions.table());
    }

    /**
     * Create an aliased <code>service_pattern.vehicle_mode_on_scheduled_stop_point</code> table reference
     */
    public VehicleModeOnScheduledStopPoint(String alias) {
        this(DSL.name(alias), VEHICLE_MODE_ON_SCHEDULED_STOP_POINT);
    }

    /**
     * Create an aliased <code>service_pattern.vehicle_mode_on_scheduled_stop_point</code> table reference
     */
    public VehicleModeOnScheduledStopPoint(Name alias) {
        this(alias, VEHICLE_MODE_ON_SCHEDULED_STOP_POINT);
    }

    /**
     * Create a <code>service_pattern.vehicle_mode_on_scheduled_stop_point</code> table reference
     */
    public VehicleModeOnScheduledStopPoint() {
        this(DSL.name("vehicle_mode_on_scheduled_stop_point"), null);
    }

    public <O extends Record> VehicleModeOnScheduledStopPoint(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, VEHICLE_MODE_ON_SCHEDULED_STOP_POINT);
    }

    @Override
    public Schema getSchema() {
        return ServicePattern.SERVICE_PATTERN;
    }

    @Override
    public VehicleModeOnScheduledStopPoint as(String alias) {
        return new VehicleModeOnScheduledStopPoint(DSL.name(alias), this);
    }

    @Override
    public VehicleModeOnScheduledStopPoint as(Name alias) {
        return new VehicleModeOnScheduledStopPoint(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public VehicleModeOnScheduledStopPoint rename(String name) {
        return new VehicleModeOnScheduledStopPoint(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public VehicleModeOnScheduledStopPoint rename(Name name) {
        return new VehicleModeOnScheduledStopPoint(name, null);
    }
}

/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.reusable_components.tables;


import fi.hsl.jore.jore4.jooq.reusable_components.Keys;
import fi.hsl.jore.jore4.jooq.reusable_components.ReusableComponents;
import fi.hsl.jore.jore4.jooq.reusable_components.tables.records.VehicleModeRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row1;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * The vehicle modes from Transmodel: https://www.transmodel-cen.eu/model/index.htm?goto=1:6:1:283
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class VehicleMode extends TableImpl<VehicleModeRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>reusable_components.vehicle_mode</code>
     */
    public static final VehicleMode VEHICLE_MODE = new VehicleMode();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<VehicleModeRecord> getRecordType() {
        return VehicleModeRecord.class;
    }

    /**
     * The column <code>reusable_components.vehicle_mode.vehicle_mode</code>. The vehicle mode from Transmodel: https://www.transmodel-cen.eu/model/index.htm?goto=1:6:1:283
     */
    public final TableField<VehicleModeRecord, String> VEHICLE_MODE_ = createField(DSL.name("vehicle_mode"), SQLDataType.CLOB.nullable(false), this, "The vehicle mode from Transmodel: https://www.transmodel-cen.eu/model/index.htm?goto=1:6:1:283");

    private VehicleMode(Name alias, Table<VehicleModeRecord> aliased) {
        this(alias, aliased, null);
    }

    private VehicleMode(Name alias, Table<VehicleModeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("The vehicle modes from Transmodel: https://www.transmodel-cen.eu/model/index.htm?goto=1:6:1:283"), TableOptions.table());
    }

    /**
     * Create an aliased <code>reusable_components.vehicle_mode</code> table reference
     */
    public VehicleMode(String alias) {
        this(DSL.name(alias), VEHICLE_MODE);
    }

    /**
     * Create an aliased <code>reusable_components.vehicle_mode</code> table reference
     */
    public VehicleMode(Name alias) {
        this(alias, VEHICLE_MODE);
    }

    /**
     * Create a <code>reusable_components.vehicle_mode</code> table reference
     */
    public VehicleMode() {
        this(DSL.name("vehicle_mode"), null);
    }

    public <O extends Record> VehicleMode(Table<O> child, ForeignKey<O, VehicleModeRecord> key) {
        super(child, key, VEHICLE_MODE);
    }

    @Override
    public Schema getSchema() {
        return ReusableComponents.REUSABLE_COMPONENTS;
    }

    @Override
    public UniqueKey<VehicleModeRecord> getPrimaryKey() {
        return Keys.VEHICLE_MODE_PKEY;
    }

    @Override
    public List<UniqueKey<VehicleModeRecord>> getKeys() {
        return Arrays.<UniqueKey<VehicleModeRecord>>asList(Keys.VEHICLE_MODE_PKEY);
    }

    @Override
    public VehicleMode as(String alias) {
        return new VehicleMode(DSL.name(alias), this);
    }

    @Override
    public VehicleMode as(Name alias) {
        return new VehicleMode(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public VehicleMode rename(String name) {
        return new VehicleMode(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public VehicleMode rename(Name name) {
        return new VehicleMode(name, null);
    }

    // -------------------------------------------------------------------------
    // Row1 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row1<String> fieldsRow() {
        return (Row1) super.fieldsRow();
    }
}

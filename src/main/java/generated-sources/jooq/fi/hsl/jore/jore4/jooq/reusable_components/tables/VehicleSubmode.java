/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.reusable_components.tables;


import fi.hsl.jore.jore4.jooq.reusable_components.ReusableComponents;

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
 * The vehicle submode, which may have implications on which infrastructure 
 * links the vehicle can traverse
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class VehicleSubmode extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>reusable_components.vehicle_submode</code>
     */
    public static final VehicleSubmode VEHICLE_SUBMODE = new VehicleSubmode();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>reusable_components.vehicle_submode.vehicle_submode</code>. The vehicle submode, which may have implications on which infrastructure links the vehicle can traverse
     */
    public final TableField<Record, String> VEHICLE_SUBMODE_ = createField(DSL.name("vehicle_submode"), SQLDataType.CLOB.nullable(false), this, "The vehicle submode, which may have implications on which infrastructure links the vehicle can traverse");

    /**
     * The column <code>reusable_components.vehicle_submode.belonging_to_vehicle_mode</code>. The vehicle mode the vehicle submode belongs to: https://www.transmodel-cen.eu/model/index.htm?goto=1:6:1:283
     */
    public final TableField<Record, String> BELONGING_TO_VEHICLE_MODE = createField(DSL.name("belonging_to_vehicle_mode"), SQLDataType.CLOB.nullable(false), this, "The vehicle mode the vehicle submode belongs to: https://www.transmodel-cen.eu/model/index.htm?goto=1:6:1:283");

    private VehicleSubmode(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private VehicleSubmode(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("The vehicle submode, which may have implications on which infrastructure links the vehicle can traverse"), TableOptions.table());
    }

    /**
     * Create an aliased <code>reusable_components.vehicle_submode</code> table reference
     */
    public VehicleSubmode(String alias) {
        this(DSL.name(alias), VEHICLE_SUBMODE);
    }

    /**
     * Create an aliased <code>reusable_components.vehicle_submode</code> table reference
     */
    public VehicleSubmode(Name alias) {
        this(alias, VEHICLE_SUBMODE);
    }

    /**
     * Create a <code>reusable_components.vehicle_submode</code> table reference
     */
    public VehicleSubmode() {
        this(DSL.name("vehicle_submode"), null);
    }

    public <O extends Record> VehicleSubmode(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, VEHICLE_SUBMODE);
    }

    @Override
    public Schema getSchema() {
        return ReusableComponents.REUSABLE_COMPONENTS;
    }

    @Override
    public VehicleSubmode as(String alias) {
        return new VehicleSubmode(DSL.name(alias), this);
    }

    @Override
    public VehicleSubmode as(Name alias) {
        return new VehicleSubmode(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public VehicleSubmode rename(String name) {
        return new VehicleSubmode(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public VehicleSubmode rename(Name name) {
        return new VehicleSubmode(name, null);
    }
}

/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.route.tables;


import fi.hsl.jore.jore4.jooq.reusable_components.tables.VehicleMode;
import fi.hsl.jore.jore4.jooq.route.Keys;
import fi.hsl.jore.jore4.jooq.route.Route;
import fi.hsl.jore.jore4.jooq.route.tables.records.LineRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * The line from Transmodel: http://www.transmodel-cen.eu/model/index.htm?goto=2:1:3:487
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Line extends TableImpl<LineRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>route.line</code>
     */
    public static final Line LINE = new Line();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<LineRecord> getRecordType() {
        return LineRecord.class;
    }

    /**
     * The column <code>route.line.line_id</code>. The ID of the line.
     */
    public final TableField<LineRecord, UUID> LINE_ID = createField(DSL.name("line_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("gen_random_uuid()", SQLDataType.UUID)), this, "The ID of the line.");

    /**
     * The column <code>route.line.name_i18n</code>. The name of the line. Placeholder for multilingual strings.
     */
    public final TableField<LineRecord, String> NAME_I18N = createField(DSL.name("name_i18n"), SQLDataType.CLOB.nullable(false), this, "The name of the line. Placeholder for multilingual strings.");

    /**
     * The column <code>route.line.short_name_i18n</code>. The shorted name of the line. Placeholder for multilingual strings.
     */
    public final TableField<LineRecord, String> SHORT_NAME_I18N = createField(DSL.name("short_name_i18n"), SQLDataType.CLOB, this, "The shorted name of the line. Placeholder for multilingual strings.");

    /**
     * The column <code>route.line.description_i18n</code>. The description of the line. Placeholder for multilingual strings.
     */
    public final TableField<LineRecord, String> DESCRIPTION_I18N = createField(DSL.name("description_i18n"), SQLDataType.CLOB, this, "The description of the line. Placeholder for multilingual strings.");

    /**
     * The column <code>route.line.primary_vehicle_mode</code>. The mode of the vehicles used as primary on the line.
     */
    public final TableField<LineRecord, String> PRIMARY_VEHICLE_MODE = createField(DSL.name("primary_vehicle_mode"), SQLDataType.CLOB.nullable(false), this, "The mode of the vehicles used as primary on the line.");

    private Line(Name alias, Table<LineRecord> aliased) {
        this(alias, aliased, null);
    }

    private Line(Name alias, Table<LineRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("The line from Transmodel: http://www.transmodel-cen.eu/model/index.htm?goto=2:1:3:487"), TableOptions.table());
    }

    /**
     * Create an aliased <code>route.line</code> table reference
     */
    public Line(String alias) {
        this(DSL.name(alias), LINE);
    }

    /**
     * Create an aliased <code>route.line</code> table reference
     */
    public Line(Name alias) {
        this(alias, LINE);
    }

    /**
     * Create a <code>route.line</code> table reference
     */
    public Line() {
        this(DSL.name("line"), null);
    }

    public <O extends Record> Line(Table<O> child, ForeignKey<O, LineRecord> key) {
        super(child, key, LINE);
    }

    @Override
    public Schema getSchema() {
        return Route.ROUTE;
    }

    @Override
    public UniqueKey<LineRecord> getPrimaryKey() {
        return Keys.LINE_PKEY;
    }

    @Override
    public List<UniqueKey<LineRecord>> getKeys() {
        return Arrays.<UniqueKey<LineRecord>>asList(Keys.LINE_PKEY);
    }

    @Override
    public List<ForeignKey<LineRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<LineRecord, ?>>asList(Keys.LINE__LINE_PRIMARY_VEHICLE_MODE_FKEY);
    }

    private transient VehicleMode _vehicleMode;

    public VehicleMode vehicleMode() {
        if (_vehicleMode == null)
            _vehicleMode = new VehicleMode(this, Keys.LINE__LINE_PRIMARY_VEHICLE_MODE_FKEY);

        return _vehicleMode;
    }

    @Override
    public Line as(String alias) {
        return new Line(DSL.name(alias), this);
    }

    @Override
    public Line as(Name alias) {
        return new Line(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Line rename(String name) {
        return new Line(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Line rename(Name name) {
        return new Line(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<UUID, String, String, String, String> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}

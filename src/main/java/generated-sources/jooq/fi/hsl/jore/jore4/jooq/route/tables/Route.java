/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.route.tables;


import java.time.LocalDate;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
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
public class Route extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>route.route</code>
     */
    public static final Route ROUTE_ = new Route();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>route.route.route_id</code>.
     */
    public final TableField<Record, UUID> ROUTE_ID = createField(DSL.name("route_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("gen_random_uuid()", SQLDataType.UUID)), this, "");

    /**
     * The column <code>route.route.description_i18n</code>.
     */
    public final TableField<Record, JSONB> DESCRIPTION_I18N = createField(DSL.name("description_i18n"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>route.route.on_line_id</code>.
     */
    public final TableField<Record, UUID> ON_LINE_ID = createField(DSL.name("on_line_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>route.route.validity_start</code>.
     */
    public final TableField<Record, LocalDate> VALIDITY_START = createField(DSL.name("validity_start"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column <code>route.route.validity_end</code>.
     */
    public final TableField<Record, LocalDate> VALIDITY_END = createField(DSL.name("validity_end"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column <code>route.route.priority</code>.
     */
    public final TableField<Record, Integer> PRIORITY = createField(DSL.name("priority"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>route.route.label</code>.
     */
    public final TableField<Record, String> LABEL = createField(DSL.name("label"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>route.route.direction</code>.
     */
    public final TableField<Record, String> DIRECTION = createField(DSL.name("direction"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>route.route.name_i18n</code>.
     */
    public final TableField<Record, JSONB> NAME_I18N = createField(DSL.name("name_i18n"), SQLDataType.JSONB.nullable(false), this, "");

    /**
     * The column <code>route.route.origin_name_i18n</code>.
     */
    public final TableField<Record, JSONB> ORIGIN_NAME_I18N = createField(DSL.name("origin_name_i18n"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>route.route.origin_short_name_i18n</code>.
     */
    public final TableField<Record, JSONB> ORIGIN_SHORT_NAME_I18N = createField(DSL.name("origin_short_name_i18n"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>route.route.destination_name_i18n</code>.
     */
    public final TableField<Record, JSONB> DESTINATION_NAME_I18N = createField(DSL.name("destination_name_i18n"), SQLDataType.JSONB, this, "");

    /**
     * The column <code>route.route.destination_short_name_i18n</code>.
     */
    public final TableField<Record, JSONB> DESTINATION_SHORT_NAME_I18N = createField(DSL.name("destination_short_name_i18n"), SQLDataType.JSONB, this, "");

    private Route(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private Route(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>route.route</code> table reference
     */
    public Route(String alias) {
        this(DSL.name(alias), ROUTE_);
    }

    /**
     * Create an aliased <code>route.route</code> table reference
     */
    public Route(Name alias) {
        this(alias, ROUTE_);
    }

    /**
     * Create a <code>route.route</code> table reference
     */
    public Route() {
        this(DSL.name("route"), null);
    }

    public <O extends Record> Route(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, ROUTE_);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : fi.hsl.jore.jore4.jooq.route.Route.ROUTE;
    }

    @Override
    public Route as(String alias) {
        return new Route(DSL.name(alias), this);
    }

    @Override
    public Route as(Name alias) {
        return new Route(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Route rename(String name) {
        return new Route(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Route rename(Name name) {
        return new Route(name, null);
    }
}

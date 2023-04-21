/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.route.tables;


import fi.hsl.jore.jore4.jooq.route.Route;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
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
 * 
 *   External identifiers for each line label.
 *   Stored in separate table because multiple line rows can have same label
 *   (if they have different priorities) and thus should have same label.
 *   Old lines (from Jore3) use numbers between 1-1999.
 *   The ids for new lines will start from 2001.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LineExternalId extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>route.line_external_id</code>
     */
    public static final LineExternalId LINE_EXTERNAL_ID = new LineExternalId();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>route.line_external_id.label</code>.
     */
    public final TableField<Record, String> LABEL = createField(DSL.name("label"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>route.line_external_id.external_id</code>.
     */
    public final TableField<Record, Short> EXTERNAL_ID = createField(DSL.name("external_id"), SQLDataType.SMALLINT.nullable(false).identity(true), this, "");

    private LineExternalId(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private LineExternalId(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("\n  External identifiers for each line label.\n  Stored in separate table because multiple line rows can have same label\n  (if they have different priorities) and thus should have same label.\n  Old lines (from Jore3) use numbers between 1-1999.\n  The ids for new lines will start from 2001."), TableOptions.table());
    }

    /**
     * Create an aliased <code>route.line_external_id</code> table reference
     */
    public LineExternalId(String alias) {
        this(DSL.name(alias), LINE_EXTERNAL_ID);
    }

    /**
     * Create an aliased <code>route.line_external_id</code> table reference
     */
    public LineExternalId(Name alias) {
        this(alias, LINE_EXTERNAL_ID);
    }

    /**
     * Create a <code>route.line_external_id</code> table reference
     */
    public LineExternalId() {
        this(DSL.name("line_external_id"), null);
    }

    public <O extends Record> LineExternalId(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, LINE_EXTERNAL_ID);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Route.ROUTE;
    }

    @Override
    public Identity<Record, Short> getIdentity() {
        return (Identity<Record, Short>) super.getIdentity();
    }

    @Override
    public LineExternalId as(String alias) {
        return new LineExternalId(DSL.name(alias), this);
    }

    @Override
    public LineExternalId as(Name alias) {
        return new LineExternalId(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public LineExternalId rename(String name) {
        return new LineExternalId(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public LineExternalId rename(Name name) {
        return new LineExternalId(name, null);
    }
}
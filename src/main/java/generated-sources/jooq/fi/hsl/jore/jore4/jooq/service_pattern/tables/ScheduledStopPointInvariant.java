/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.service_pattern.tables;


import fi.hsl.jore.jore4.jooq.service_pattern.ServicePattern;

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
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ScheduledStopPointInvariant extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>service_pattern.scheduled_stop_point_invariant</code>
     */
    public static final ScheduledStopPointInvariant SCHEDULED_STOP_POINT_INVARIANT = new ScheduledStopPointInvariant();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column
     * <code>service_pattern.scheduled_stop_point_invariant.label</code>.
     */
    public final TableField<Record, String> LABEL = createField(DSL.name("label"), SQLDataType.CLOB.nullable(false), this, "");

    private ScheduledStopPointInvariant(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private ScheduledStopPointInvariant(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased
     * <code>service_pattern.scheduled_stop_point_invariant</code> table
     * reference
     */
    public ScheduledStopPointInvariant(String alias) {
        this(DSL.name(alias), SCHEDULED_STOP_POINT_INVARIANT);
    }

    /**
     * Create an aliased
     * <code>service_pattern.scheduled_stop_point_invariant</code> table
     * reference
     */
    public ScheduledStopPointInvariant(Name alias) {
        this(alias, SCHEDULED_STOP_POINT_INVARIANT);
    }

    /**
     * Create a <code>service_pattern.scheduled_stop_point_invariant</code>
     * table reference
     */
    public ScheduledStopPointInvariant() {
        this(DSL.name("scheduled_stop_point_invariant"), null);
    }

    public <O extends Record> ScheduledStopPointInvariant(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, SCHEDULED_STOP_POINT_INVARIANT);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : ServicePattern.SERVICE_PATTERN;
    }

    @Override
    public ScheduledStopPointInvariant as(String alias) {
        return new ScheduledStopPointInvariant(DSL.name(alias), this);
    }

    @Override
    public ScheduledStopPointInvariant as(Name alias) {
        return new ScheduledStopPointInvariant(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPointInvariant rename(String name) {
        return new ScheduledStopPointInvariant(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPointInvariant rename(Name name) {
        return new ScheduledStopPointInvariant(name, null);
    }
}
/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.journey_pattern.tables;


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
 * The journey patterns, i.e. the ordered lists of stops and timing points along
 * routes: https://www.transmodel-cen.eu/model/index.htm?goto=2:3:1:813
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JourneyPattern extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>journey_pattern.journey_pattern</code>
     */
    public static final JourneyPattern JOURNEY_PATTERN_ = new JourneyPattern();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column
     * <code>journey_pattern.journey_pattern.journey_pattern_id</code>. The ID
     * of the journey pattern.
     */
    public final TableField<Record, UUID> JOURNEY_PATTERN_ID = createField(DSL.name("journey_pattern_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("gen_random_uuid()", SQLDataType.UUID)), this, "The ID of the journey pattern.");

    /**
     * The column <code>journey_pattern.journey_pattern.on_route_id</code>. The
     * ID of the route the journey pattern is on.
     */
    public final TableField<Record, UUID> ON_ROUTE_ID = createField(DSL.name("on_route_id"), SQLDataType.UUID.nullable(false), this, "The ID of the route the journey pattern is on.");

    private JourneyPattern(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private JourneyPattern(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("The journey patterns, i.e. the ordered lists of stops and timing points along routes: https://www.transmodel-cen.eu/model/index.htm?goto=2:3:1:813"), TableOptions.table());
    }

    /**
     * Create an aliased <code>journey_pattern.journey_pattern</code> table
     * reference
     */
    public JourneyPattern(String alias) {
        this(DSL.name(alias), JOURNEY_PATTERN_);
    }

    /**
     * Create an aliased <code>journey_pattern.journey_pattern</code> table
     * reference
     */
    public JourneyPattern(Name alias) {
        this(alias, JOURNEY_PATTERN_);
    }

    /**
     * Create a <code>journey_pattern.journey_pattern</code> table reference
     */
    public JourneyPattern() {
        this(DSL.name("journey_pattern"), null);
    }

    public <O extends Record> JourneyPattern(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, JOURNEY_PATTERN_);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : fi.hsl.jore.jore4.jooq.journey_pattern.JourneyPattern.JOURNEY_PATTERN;
    }

    @Override
    public JourneyPattern as(String alias) {
        return new JourneyPattern(DSL.name(alias), this);
    }

    @Override
    public JourneyPattern as(Name alias) {
        return new JourneyPattern(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public JourneyPattern rename(String name) {
        return new JourneyPattern(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public JourneyPattern rename(Name name) {
        return new JourneyPattern(name, null);
    }
}

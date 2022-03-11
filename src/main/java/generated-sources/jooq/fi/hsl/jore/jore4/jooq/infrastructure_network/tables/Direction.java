/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.infrastructure_network.tables;


import fi.hsl.jore.jore4.jooq.infrastructure_network.InfrastructureNetwork;

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
 * The direction in which an e.g. infrastructure link can be traversed
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Direction extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>infrastructure_network.direction</code>
     */
    public static final Direction DIRECTION = new Direction();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>infrastructure_network.direction.value</code>.
     */
    public final TableField<Record, String> VALUE = createField(DSL.name("value"), SQLDataType.CLOB.nullable(false), this, "");

    private Direction(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private Direction(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("The direction in which an e.g. infrastructure link can be traversed"), TableOptions.table());
    }

    /**
     * Create an aliased <code>infrastructure_network.direction</code> table
     * reference
     */
    public Direction(String alias) {
        this(DSL.name(alias), DIRECTION);
    }

    /**
     * Create an aliased <code>infrastructure_network.direction</code> table
     * reference
     */
    public Direction(Name alias) {
        this(alias, DIRECTION);
    }

    /**
     * Create a <code>infrastructure_network.direction</code> table reference
     */
    public Direction() {
        this(DSL.name("direction"), null);
    }

    public <O extends Record> Direction(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, DIRECTION);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : InfrastructureNetwork.INFRASTRUCTURE_NETWORK;
    }

    @Override
    public Direction as(String alias) {
        return new Direction(DSL.name(alias), this);
    }

    @Override
    public Direction as(Name alias) {
        return new Direction(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Direction rename(String name) {
        return new Direction(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Direction rename(Name name) {
        return new Direction(name, null);
    }
}

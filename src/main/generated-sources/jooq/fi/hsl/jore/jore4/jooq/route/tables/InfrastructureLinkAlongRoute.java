/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.route.tables;


import fi.hsl.jore.jore4.jooq.route.Route;

import java.util.Collection;
import java.util.UUID;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * The infrastructure links along which the routes are defined.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureLinkAlongRoute extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>route.infrastructure_link_along_route</code>
     */
    public static final InfrastructureLinkAlongRoute INFRASTRUCTURE_LINK_ALONG_ROUTE = new InfrastructureLinkAlongRoute();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>route.infrastructure_link_along_route.route_id</code>.
     * The ID of the route.
     */
    public final TableField<Record, UUID> ROUTE_ID = createField(DSL.name("route_id"), SQLDataType.UUID.nullable(false), this, "The ID of the route.");

    /**
     * The column
     * <code>route.infrastructure_link_along_route.infrastructure_link_id</code>.
     * The ID of the infrastructure link.
     */
    public final TableField<Record, UUID> INFRASTRUCTURE_LINK_ID = createField(DSL.name("infrastructure_link_id"), SQLDataType.UUID.nullable(false), this, "The ID of the infrastructure link.");

    /**
     * The column
     * <code>route.infrastructure_link_along_route.infrastructure_link_sequence</code>.
     * The order of the infrastructure link within the journey pattern.
     */
    public final TableField<Record, Integer> INFRASTRUCTURE_LINK_SEQUENCE = createField(DSL.name("infrastructure_link_sequence"), SQLDataType.INTEGER.nullable(false), this, "The order of the infrastructure link within the journey pattern.");

    /**
     * The column
     * <code>route.infrastructure_link_along_route.is_traversal_forwards</code>.
     * Is the infrastructure link traversed in the direction of its linestring?
     */
    public final TableField<Record, Boolean> IS_TRAVERSAL_FORWARDS = createField(DSL.name("is_traversal_forwards"), SQLDataType.BOOLEAN.nullable(false), this, "Is the infrastructure link traversed in the direction of its linestring?");

    private InfrastructureLinkAlongRoute(Name alias, Table<Record> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private InfrastructureLinkAlongRoute(Name alias, Table<Record> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment("The infrastructure links along which the routes are defined."), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>route.infrastructure_link_along_route</code>
     * table reference
     */
    public InfrastructureLinkAlongRoute(String alias) {
        this(DSL.name(alias), INFRASTRUCTURE_LINK_ALONG_ROUTE);
    }

    /**
     * Create an aliased <code>route.infrastructure_link_along_route</code>
     * table reference
     */
    public InfrastructureLinkAlongRoute(Name alias) {
        this(alias, INFRASTRUCTURE_LINK_ALONG_ROUTE);
    }

    /**
     * Create a <code>route.infrastructure_link_along_route</code> table
     * reference
     */
    public InfrastructureLinkAlongRoute() {
        this(DSL.name("infrastructure_link_along_route"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Route.ROUTE;
    }

    @Override
    public InfrastructureLinkAlongRoute as(String alias) {
        return new InfrastructureLinkAlongRoute(DSL.name(alias), this);
    }

    @Override
    public InfrastructureLinkAlongRoute as(Name alias) {
        return new InfrastructureLinkAlongRoute(alias, this);
    }

    @Override
    public InfrastructureLinkAlongRoute as(Table<?> alias) {
        return new InfrastructureLinkAlongRoute(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinkAlongRoute rename(String name) {
        return new InfrastructureLinkAlongRoute(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinkAlongRoute rename(Name name) {
        return new InfrastructureLinkAlongRoute(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinkAlongRoute rename(Table<?> name) {
        return new InfrastructureLinkAlongRoute(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinkAlongRoute where(Condition condition) {
        return new InfrastructureLinkAlongRoute(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinkAlongRoute where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinkAlongRoute where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinkAlongRoute where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public InfrastructureLinkAlongRoute where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public InfrastructureLinkAlongRoute where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public InfrastructureLinkAlongRoute where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public InfrastructureLinkAlongRoute where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinkAlongRoute whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinkAlongRoute whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}

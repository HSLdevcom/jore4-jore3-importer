/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.network.Keys;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLines.NetworkLinesPath;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirections.NetworkRouteDirectionsPath;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRoutesRecord;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.InverseForeignKey;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Path;
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
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkRoutes extends TableImpl<NetworkRoutesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.network_routes</code>
     */
    public static final NetworkRoutes NETWORK_ROUTES = new NetworkRoutes();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkRoutesRecord> getRecordType() {
        return NetworkRoutesRecord.class;
    }

    /**
     * The column <code>network.network_routes.network_route_id</code>.
     */
    public final TableField<NetworkRoutesRecord, UUID> NETWORK_ROUTE_ID = createField(DSL.name("network_route_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field(DSL.raw("gen_random_uuid()"), SQLDataType.UUID)), this, "");

    /**
     * The column <code>network.network_routes.network_line_id</code>.
     */
    public final TableField<NetworkRoutesRecord, UUID> NETWORK_LINE_ID = createField(DSL.name("network_line_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>network.network_routes.network_route_ext_id</code>.
     */
    public final TableField<NetworkRoutesRecord, String> NETWORK_ROUTE_EXT_ID = createField(DSL.name("network_route_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_routes.network_route_number</code>.
     */
    public final TableField<NetworkRoutesRecord, String> NETWORK_ROUTE_NUMBER = createField(DSL.name("network_route_number"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_routes.network_route_name</code>.
     */
    public final TableField<NetworkRoutesRecord, JSONB> NETWORK_ROUTE_NAME = createField(DSL.name("network_route_name"), SQLDataType.JSONB.nullable(false), this, "");

    /**
     * The column <code>network.network_routes.network_route_sys_period</code>.
     */
    public final TableField<NetworkRoutesRecord, TimeRange> NETWORK_ROUTE_SYS_PERIOD = createField(DSL.name("network_route_sys_period"), DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\"").nullable(false).defaultValue(DSL.field(DSL.raw("tstzrange(CURRENT_TIMESTAMP, NULL::timestamp with time zone)"), org.jooq.impl.SQLDataType.OTHER)), this, "", new TimeRangeBinding());

    /**
     * The column
     * <code>network.network_routes.network_route_hidden_variant</code>.
     */
    public final TableField<NetworkRoutesRecord, Short> NETWORK_ROUTE_HIDDEN_VARIANT = createField(DSL.name("network_route_hidden_variant"), SQLDataType.SMALLINT, this, "");

    /**
     * The column
     * <code>network.network_routes.network_route_legacy_hsl_municipality_code</code>.
     */
    public final TableField<NetworkRoutesRecord, String> NETWORK_ROUTE_LEGACY_HSL_MUNICIPALITY_CODE = createField(DSL.name("network_route_legacy_hsl_municipality_code"), SQLDataType.CLOB, this, "");

    private NetworkRoutes(Name alias, Table<NetworkRoutesRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private NetworkRoutes(Name alias, Table<NetworkRoutesRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>network.network_routes</code> table reference
     */
    public NetworkRoutes(String alias) {
        this(DSL.name(alias), NETWORK_ROUTES);
    }

    /**
     * Create an aliased <code>network.network_routes</code> table reference
     */
    public NetworkRoutes(Name alias) {
        this(alias, NETWORK_ROUTES);
    }

    /**
     * Create a <code>network.network_routes</code> table reference
     */
    public NetworkRoutes() {
        this(DSL.name("network_routes"), null);
    }

    public <O extends Record> NetworkRoutes(Table<O> path, ForeignKey<O, NetworkRoutesRecord> childPath, InverseForeignKey<O, NetworkRoutesRecord> parentPath) {
        super(path, childPath, parentPath, NETWORK_ROUTES);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class NetworkRoutesPath extends NetworkRoutes implements Path<NetworkRoutesRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> NetworkRoutesPath(Table<O> path, ForeignKey<O, NetworkRoutesRecord> childPath, InverseForeignKey<O, NetworkRoutesRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private NetworkRoutesPath(Name alias, Table<NetworkRoutesRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public NetworkRoutesPath as(String alias) {
            return new NetworkRoutesPath(DSL.name(alias), this);
        }

        @Override
        public NetworkRoutesPath as(Name alias) {
            return new NetworkRoutesPath(alias, this);
        }

        @Override
        public NetworkRoutesPath as(Table<?> alias) {
            return new NetworkRoutesPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Network.NETWORK;
    }

    @Override
    public UniqueKey<NetworkRoutesRecord> getPrimaryKey() {
        return Keys.NETWORK_ROUTES_PKEY;
    }

    @Override
    public List<ForeignKey<NetworkRoutesRecord, ?>> getReferences() {
        return Arrays.asList(Keys.NETWORK_ROUTES__NETWORK_ROUTES_NETWORK_LINE_ID_FKEY);
    }

    private transient NetworkLinesPath _networkLines;

    /**
     * Get the implicit join path to the <code>network.network_lines</code>
     * table.
     */
    public NetworkLinesPath networkLines() {
        if (_networkLines == null)
            _networkLines = new NetworkLinesPath(this, Keys.NETWORK_ROUTES__NETWORK_ROUTES_NETWORK_LINE_ID_FKEY, null);

        return _networkLines;
    }

    private transient NetworkRouteDirectionsPath _networkRouteDirections;

    /**
     * Get the implicit to-many join path to the
     * <code>network.network_route_directions</code> table
     */
    public NetworkRouteDirectionsPath networkRouteDirections() {
        if (_networkRouteDirections == null)
            _networkRouteDirections = new NetworkRouteDirectionsPath(this, null, Keys.NETWORK_ROUTE_DIRECTIONS__NETWORK_ROUTE_DIRECTIONS_NETWORK_ROUTE_ID_FKEY.getInverseKey());

        return _networkRouteDirections;
    }

    @Override
    public NetworkRoutes as(String alias) {
        return new NetworkRoutes(DSL.name(alias), this);
    }

    @Override
    public NetworkRoutes as(Name alias) {
        return new NetworkRoutes(alias, this);
    }

    @Override
    public NetworkRoutes as(Table<?> alias) {
        return new NetworkRoutes(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutes rename(String name) {
        return new NetworkRoutes(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutes rename(Name name) {
        return new NetworkRoutes(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRoutes rename(Table<?> name) {
        return new NetworkRoutes(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRoutes where(Condition condition) {
        return new NetworkRoutes(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRoutes where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRoutes where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRoutes where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkRoutes where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkRoutes where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkRoutes where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkRoutes where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRoutes whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkRoutes whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}

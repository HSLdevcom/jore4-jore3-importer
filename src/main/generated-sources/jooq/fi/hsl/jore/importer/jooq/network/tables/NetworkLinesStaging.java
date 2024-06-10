/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNetworkTypes.InfrastructureNetworkTypesPath;
import fi.hsl.jore.importer.jooq.network.Keys;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkLinesStagingRecord;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.InverseForeignKey;
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
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkLinesStaging extends TableImpl<NetworkLinesStagingRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.network_lines_staging</code>
     */
    public static final NetworkLinesStaging NETWORK_LINES_STAGING = new NetworkLinesStaging();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkLinesStagingRecord> getRecordType() {
        return NetworkLinesStagingRecord.class;
    }

    /**
     * The column
     * <code>network.network_lines_staging.network_line_ext_id</code>.
     */
    public final TableField<NetworkLinesStagingRecord, String> NETWORK_LINE_EXT_ID = createField(DSL.name("network_line_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>network.network_lines_staging.network_line_number</code>.
     */
    public final TableField<NetworkLinesStagingRecord, String> NETWORK_LINE_NUMBER = createField(DSL.name("network_line_number"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>network.network_lines_staging.infrastructure_network_type</code>.
     */
    public final TableField<NetworkLinesStagingRecord, String> INFRASTRUCTURE_NETWORK_TYPE = createField(DSL.name("infrastructure_network_type"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>network.network_lines_staging.network_line_type_of_line</code>.
     */
    public final TableField<NetworkLinesStagingRecord, String> NETWORK_LINE_TYPE_OF_LINE = createField(DSL.name("network_line_type_of_line"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>network.network_lines_staging.network_line_legacy_hsl_municipality_code</code>.
     */
    public final TableField<NetworkLinesStagingRecord, String> NETWORK_LINE_LEGACY_HSL_MUNICIPALITY_CODE = createField(DSL.name("network_line_legacy_hsl_municipality_code"), SQLDataType.CLOB, this, "");

    private NetworkLinesStaging(Name alias, Table<NetworkLinesStagingRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private NetworkLinesStaging(Name alias, Table<NetworkLinesStagingRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>network.network_lines_staging</code> table
     * reference
     */
    public NetworkLinesStaging(String alias) {
        this(DSL.name(alias), NETWORK_LINES_STAGING);
    }

    /**
     * Create an aliased <code>network.network_lines_staging</code> table
     * reference
     */
    public NetworkLinesStaging(Name alias) {
        this(alias, NETWORK_LINES_STAGING);
    }

    /**
     * Create a <code>network.network_lines_staging</code> table reference
     */
    public NetworkLinesStaging() {
        this(DSL.name("network_lines_staging"), null);
    }

    public <O extends Record> NetworkLinesStaging(Table<O> path, ForeignKey<O, NetworkLinesStagingRecord> childPath, InverseForeignKey<O, NetworkLinesStagingRecord> parentPath) {
        super(path, childPath, parentPath, NETWORK_LINES_STAGING);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class NetworkLinesStagingPath extends NetworkLinesStaging implements Path<NetworkLinesStagingRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> NetworkLinesStagingPath(Table<O> path, ForeignKey<O, NetworkLinesStagingRecord> childPath, InverseForeignKey<O, NetworkLinesStagingRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private NetworkLinesStagingPath(Name alias, Table<NetworkLinesStagingRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public NetworkLinesStagingPath as(String alias) {
            return new NetworkLinesStagingPath(DSL.name(alias), this);
        }

        @Override
        public NetworkLinesStagingPath as(Name alias) {
            return new NetworkLinesStagingPath(alias, this);
        }

        @Override
        public NetworkLinesStagingPath as(Table<?> alias) {
            return new NetworkLinesStagingPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Network.NETWORK;
    }

    @Override
    public UniqueKey<NetworkLinesStagingRecord> getPrimaryKey() {
        return Keys.NETWORK_LINES_STAGING_PKEY;
    }

    @Override
    public List<ForeignKey<NetworkLinesStagingRecord, ?>> getReferences() {
        return Arrays.asList(Keys.NETWORK_LINES_STAGING__NETWORK_LINES_STAGING_INFRASTRUCTURE_NETWORK_TYPE_FKEY);
    }

    private transient InfrastructureNetworkTypesPath _infrastructureNetworkTypes;

    /**
     * Get the implicit join path to the
     * <code>infrastructure_network.infrastructure_network_types</code> table.
     */
    public InfrastructureNetworkTypesPath infrastructureNetworkTypes() {
        if (_infrastructureNetworkTypes == null)
            _infrastructureNetworkTypes = new InfrastructureNetworkTypesPath(this, Keys.NETWORK_LINES_STAGING__NETWORK_LINES_STAGING_INFRASTRUCTURE_NETWORK_TYPE_FKEY, null);

        return _infrastructureNetworkTypes;
    }

    @Override
    public NetworkLinesStaging as(String alias) {
        return new NetworkLinesStaging(DSL.name(alias), this);
    }

    @Override
    public NetworkLinesStaging as(Name alias) {
        return new NetworkLinesStaging(alias, this);
    }

    @Override
    public NetworkLinesStaging as(Table<?> alias) {
        return new NetworkLinesStaging(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkLinesStaging rename(String name) {
        return new NetworkLinesStaging(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkLinesStaging rename(Name name) {
        return new NetworkLinesStaging(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkLinesStaging rename(Table<?> name) {
        return new NetworkLinesStaging(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkLinesStaging where(Condition condition) {
        return new NetworkLinesStaging(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkLinesStaging where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkLinesStaging where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkLinesStaging where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkLinesStaging where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkLinesStaging where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkLinesStaging where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkLinesStaging where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkLinesStaging whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkLinesStaging whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}

/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables;


import fi.hsl.jore.importer.config.jooq.converter.geometry.LineStringBinding;
import fi.hsl.jore.importer.jooq.infrastructure_network.InfrastructureNetwork;
import fi.hsl.jore.importer.jooq.infrastructure_network.Keys;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNetworkTypes.InfrastructureNetworkTypesPath;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureLinksStagingRecord;

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
import org.locationtech.jts.geom.LineString;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureLinksStaging extends TableImpl<InfrastructureLinksStagingRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>infrastructure_network.infrastructure_links_staging</code>
     */
    public static final InfrastructureLinksStaging INFRASTRUCTURE_LINKS_STAGING = new InfrastructureLinksStaging();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InfrastructureLinksStagingRecord> getRecordType() {
        return InfrastructureLinksStagingRecord.class;
    }

    /**
     * The column
     * <code>infrastructure_network.infrastructure_links_staging.infrastructure_link_ext_id</code>.
     */
    public final TableField<InfrastructureLinksStagingRecord, String> INFRASTRUCTURE_LINK_EXT_ID = createField(DSL.name("infrastructure_link_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>infrastructure_network.infrastructure_links_staging.infrastructure_link_geog</code>.
     */
    public final TableField<InfrastructureLinksStagingRecord, LineString> INFRASTRUCTURE_LINK_GEOG = createField(DSL.name("infrastructure_link_geog"), SQLDataType.OTHER.nullable(false), this, "", new LineStringBinding());

    /**
     * The column
     * <code>infrastructure_network.infrastructure_links_staging.infrastructure_network_type</code>.
     */
    public final TableField<InfrastructureLinksStagingRecord, String> INFRASTRUCTURE_NETWORK_TYPE = createField(DSL.name("infrastructure_network_type"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>infrastructure_network.infrastructure_links_staging.infrastructure_link_start_node_ext_id</code>.
     */
    public final TableField<InfrastructureLinksStagingRecord, String> INFRASTRUCTURE_LINK_START_NODE_EXT_ID = createField(DSL.name("infrastructure_link_start_node_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>infrastructure_network.infrastructure_links_staging.infrastructure_link_end_node_ext_id</code>.
     */
    public final TableField<InfrastructureLinksStagingRecord, String> INFRASTRUCTURE_LINK_END_NODE_EXT_ID = createField(DSL.name("infrastructure_link_end_node_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    private InfrastructureLinksStaging(Name alias, Table<InfrastructureLinksStagingRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private InfrastructureLinksStaging(Name alias, Table<InfrastructureLinksStagingRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased
     * <code>infrastructure_network.infrastructure_links_staging</code> table
     * reference
     */
    public InfrastructureLinksStaging(String alias) {
        this(DSL.name(alias), INFRASTRUCTURE_LINKS_STAGING);
    }

    /**
     * Create an aliased
     * <code>infrastructure_network.infrastructure_links_staging</code> table
     * reference
     */
    public InfrastructureLinksStaging(Name alias) {
        this(alias, INFRASTRUCTURE_LINKS_STAGING);
    }

    /**
     * Create a <code>infrastructure_network.infrastructure_links_staging</code>
     * table reference
     */
    public InfrastructureLinksStaging() {
        this(DSL.name("infrastructure_links_staging"), null);
    }

    public <O extends Record> InfrastructureLinksStaging(Table<O> path, ForeignKey<O, InfrastructureLinksStagingRecord> childPath, InverseForeignKey<O, InfrastructureLinksStagingRecord> parentPath) {
        super(path, childPath, parentPath, INFRASTRUCTURE_LINKS_STAGING);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class InfrastructureLinksStagingPath extends InfrastructureLinksStaging implements Path<InfrastructureLinksStagingRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> InfrastructureLinksStagingPath(Table<O> path, ForeignKey<O, InfrastructureLinksStagingRecord> childPath, InverseForeignKey<O, InfrastructureLinksStagingRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private InfrastructureLinksStagingPath(Name alias, Table<InfrastructureLinksStagingRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public InfrastructureLinksStagingPath as(String alias) {
            return new InfrastructureLinksStagingPath(DSL.name(alias), this);
        }

        @Override
        public InfrastructureLinksStagingPath as(Name alias) {
            return new InfrastructureLinksStagingPath(alias, this);
        }

        @Override
        public InfrastructureLinksStagingPath as(Table<?> alias) {
            return new InfrastructureLinksStagingPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : InfrastructureNetwork.INFRASTRUCTURE_NETWORK;
    }

    @Override
    public UniqueKey<InfrastructureLinksStagingRecord> getPrimaryKey() {
        return Keys.INFRASTRUCTURE_LINKS_STAGING_PKEY;
    }

    @Override
    public List<ForeignKey<InfrastructureLinksStagingRecord, ?>> getReferences() {
        return Arrays.asList(Keys.INFRASTRUCTURE_LINKS_STAGING__INFRASTRUCTURE_LINKS_STAGING_INFRASTRUCTURE_NETWORK_TYPE_FKEY);
    }

    private transient InfrastructureNetworkTypesPath _infrastructureNetworkTypes;

    /**
     * Get the implicit join path to the
     * <code>infrastructure_network.infrastructure_network_types</code> table.
     */
    public InfrastructureNetworkTypesPath infrastructureNetworkTypes() {
        if (_infrastructureNetworkTypes == null)
            _infrastructureNetworkTypes = new InfrastructureNetworkTypesPath(this, Keys.INFRASTRUCTURE_LINKS_STAGING__INFRASTRUCTURE_LINKS_STAGING_INFRASTRUCTURE_NETWORK_TYPE_FKEY, null);

        return _infrastructureNetworkTypes;
    }

    @Override
    public InfrastructureLinksStaging as(String alias) {
        return new InfrastructureLinksStaging(DSL.name(alias), this);
    }

    @Override
    public InfrastructureLinksStaging as(Name alias) {
        return new InfrastructureLinksStaging(alias, this);
    }

    @Override
    public InfrastructureLinksStaging as(Table<?> alias) {
        return new InfrastructureLinksStaging(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinksStaging rename(String name) {
        return new InfrastructureLinksStaging(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinksStaging rename(Name name) {
        return new InfrastructureLinksStaging(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinksStaging rename(Table<?> name) {
        return new InfrastructureLinksStaging(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinksStaging where(Condition condition) {
        return new InfrastructureLinksStaging(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinksStaging where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinksStaging where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinksStaging where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public InfrastructureLinksStaging where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public InfrastructureLinksStaging where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public InfrastructureLinksStaging where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public InfrastructureLinksStaging where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinksStaging whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinksStaging whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}

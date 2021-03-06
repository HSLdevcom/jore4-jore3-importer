/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables;


import fi.hsl.jore.importer.jooq.infrastructure_network.InfrastructureNetwork;
import fi.hsl.jore.importer.jooq.infrastructure_network.Keys;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureNetworkTypesRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row1;
import org.jooq.Schema;
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
public class InfrastructureNetworkTypes extends TableImpl<InfrastructureNetworkTypesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>infrastructure_network.infrastructure_network_types</code>
     */
    public static final InfrastructureNetworkTypes INFRASTRUCTURE_NETWORK_TYPES = new InfrastructureNetworkTypes();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InfrastructureNetworkTypesRecord> getRecordType() {
        return InfrastructureNetworkTypesRecord.class;
    }

    /**
     * The column <code>infrastructure_network.infrastructure_network_types.infrastructure_network_type</code>.
     */
    public final TableField<InfrastructureNetworkTypesRecord, String> INFRASTRUCTURE_NETWORK_TYPE = createField(DSL.name("infrastructure_network_type"), SQLDataType.CLOB.nullable(false), this, "");

    private InfrastructureNetworkTypes(Name alias, Table<InfrastructureNetworkTypesRecord> aliased) {
        this(alias, aliased, null);
    }

    private InfrastructureNetworkTypes(Name alias, Table<InfrastructureNetworkTypesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>infrastructure_network.infrastructure_network_types</code> table reference
     */
    public InfrastructureNetworkTypes(String alias) {
        this(DSL.name(alias), INFRASTRUCTURE_NETWORK_TYPES);
    }

    /**
     * Create an aliased <code>infrastructure_network.infrastructure_network_types</code> table reference
     */
    public InfrastructureNetworkTypes(Name alias) {
        this(alias, INFRASTRUCTURE_NETWORK_TYPES);
    }

    /**
     * Create a <code>infrastructure_network.infrastructure_network_types</code> table reference
     */
    public InfrastructureNetworkTypes() {
        this(DSL.name("infrastructure_network_types"), null);
    }

    public <O extends Record> InfrastructureNetworkTypes(Table<O> child, ForeignKey<O, InfrastructureNetworkTypesRecord> key) {
        super(child, key, INFRASTRUCTURE_NETWORK_TYPES);
    }

    @Override
    public Schema getSchema() {
        return InfrastructureNetwork.INFRASTRUCTURE_NETWORK;
    }

    @Override
    public UniqueKey<InfrastructureNetworkTypesRecord> getPrimaryKey() {
        return Keys.INFRASTRUCTURE_NETWORK_TYPES_PKEY;
    }

    @Override
    public List<UniqueKey<InfrastructureNetworkTypesRecord>> getKeys() {
        return Arrays.<UniqueKey<InfrastructureNetworkTypesRecord>>asList(Keys.INFRASTRUCTURE_NETWORK_TYPES_PKEY);
    }

    @Override
    public InfrastructureNetworkTypes as(String alias) {
        return new InfrastructureNetworkTypes(DSL.name(alias), this);
    }

    @Override
    public InfrastructureNetworkTypes as(Name alias) {
        return new InfrastructureNetworkTypes(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureNetworkTypes rename(String name) {
        return new InfrastructureNetworkTypes(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureNetworkTypes rename(Name name) {
        return new InfrastructureNetworkTypes(name, null);
    }

    // -------------------------------------------------------------------------
    // Row1 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row1<String> fieldsRow() {
        return (Row1) super.fieldsRow();
    }
}

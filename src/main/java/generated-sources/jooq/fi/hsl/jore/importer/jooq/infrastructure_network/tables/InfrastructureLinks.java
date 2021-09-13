/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables;


import fi.hsl.jore.importer.config.jooq.converter.geometry.LineStringBinding;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.infrastructure_network.InfrastructureNetwork;
import fi.hsl.jore.importer.jooq.infrastructure_network.Keys;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureLinksRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row7;
import org.jooq.Schema;
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
public class InfrastructureLinks extends TableImpl<InfrastructureLinksRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>infrastructure_network.infrastructure_links</code>
     */
    public static final InfrastructureLinks INFRASTRUCTURE_LINKS = new InfrastructureLinks();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InfrastructureLinksRecord> getRecordType() {
        return InfrastructureLinksRecord.class;
    }

    /**
     * The column <code>infrastructure_network.infrastructure_links.infrastructure_link_id</code>.
     */
    public final TableField<InfrastructureLinksRecord, UUID> INFRASTRUCTURE_LINK_ID = createField(DSL.name("infrastructure_link_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("gen_random_uuid()", SQLDataType.UUID)), this, "");

    /**
     * The column <code>infrastructure_network.infrastructure_links.infrastructure_link_ext_id</code>.
     */
    public final TableField<InfrastructureLinksRecord, String> INFRASTRUCTURE_LINK_EXT_ID = createField(DSL.name("infrastructure_link_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>infrastructure_network.infrastructure_links.infrastructure_link_geog</code>.
     */
    public final TableField<InfrastructureLinksRecord, LineString> INFRASTRUCTURE_LINK_GEOG = createField(DSL.name("infrastructure_link_geog"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\"").nullable(false), this, "", new LineStringBinding());

    /**
     * The column <code>infrastructure_network.infrastructure_links.infrastructure_network_type</code>.
     */
    public final TableField<InfrastructureLinksRecord, String> INFRASTRUCTURE_NETWORK_TYPE = createField(DSL.name("infrastructure_network_type"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>infrastructure_network.infrastructure_links.infrastructure_link_sys_period</code>.
     */
    public final TableField<InfrastructureLinksRecord, TimeRange> INFRASTRUCTURE_LINK_SYS_PERIOD = createField(DSL.name("infrastructure_link_sys_period"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\"").nullable(false).defaultValue(DSL.field("tstzrange(CURRENT_TIMESTAMP, NULL::timestamp with time zone)", org.jooq.impl.SQLDataType.OTHER)), this, "", new TimeRangeBinding());

    /**
     * The column <code>infrastructure_network.infrastructure_links.infrastructure_link_start_node</code>.
     */
    public final TableField<InfrastructureLinksRecord, UUID> INFRASTRUCTURE_LINK_START_NODE = createField(DSL.name("infrastructure_link_start_node"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>infrastructure_network.infrastructure_links.infrastructure_link_end_node</code>.
     */
    public final TableField<InfrastructureLinksRecord, UUID> INFRASTRUCTURE_LINK_END_NODE = createField(DSL.name("infrastructure_link_end_node"), SQLDataType.UUID.nullable(false), this, "");

    private InfrastructureLinks(Name alias, Table<InfrastructureLinksRecord> aliased) {
        this(alias, aliased, null);
    }

    private InfrastructureLinks(Name alias, Table<InfrastructureLinksRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>infrastructure_network.infrastructure_links</code> table reference
     */
    public InfrastructureLinks(String alias) {
        this(DSL.name(alias), INFRASTRUCTURE_LINKS);
    }

    /**
     * Create an aliased <code>infrastructure_network.infrastructure_links</code> table reference
     */
    public InfrastructureLinks(Name alias) {
        this(alias, INFRASTRUCTURE_LINKS);
    }

    /**
     * Create a <code>infrastructure_network.infrastructure_links</code> table reference
     */
    public InfrastructureLinks() {
        this(DSL.name("infrastructure_links"), null);
    }

    public <O extends Record> InfrastructureLinks(Table<O> child, ForeignKey<O, InfrastructureLinksRecord> key) {
        super(child, key, INFRASTRUCTURE_LINKS);
    }

    @Override
    public Schema getSchema() {
        return InfrastructureNetwork.INFRASTRUCTURE_NETWORK;
    }

    @Override
    public UniqueKey<InfrastructureLinksRecord> getPrimaryKey() {
        return Keys.INFRASTRUCTURE_LINKS_PKEY;
    }

    @Override
    public List<UniqueKey<InfrastructureLinksRecord>> getKeys() {
        return Arrays.<UniqueKey<InfrastructureLinksRecord>>asList(Keys.INFRASTRUCTURE_LINKS_PKEY);
    }

    @Override
    public List<ForeignKey<InfrastructureLinksRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<InfrastructureLinksRecord, ?>>asList(Keys.INFRASTRUCTURE_LINKS__INFRASTRUCTURE_LINKS_INFRASTRUCTURE_NETWORK_TYPE_FKEY, Keys.INFRASTRUCTURE_LINKS__INFRASTRUCTURE_LINKS_INFRASTRUCTURE_LINK_START_NODE_FKEY, Keys.INFRASTRUCTURE_LINKS__INFRASTRUCTURE_LINKS_INFRASTRUCTURE_LINK_END_NODE_FKEY);
    }

    private transient InfrastructureNetworkTypes _infrastructureNetworkTypes;
    private transient InfrastructureNodes _infrastructureLinksInfrastructureLinkStartNodeFkey;
    private transient InfrastructureNodes _infrastructureLinksInfrastructureLinkEndNodeFkey;

    public InfrastructureNetworkTypes infrastructureNetworkTypes() {
        if (_infrastructureNetworkTypes == null)
            _infrastructureNetworkTypes = new InfrastructureNetworkTypes(this, Keys.INFRASTRUCTURE_LINKS__INFRASTRUCTURE_LINKS_INFRASTRUCTURE_NETWORK_TYPE_FKEY);

        return _infrastructureNetworkTypes;
    }

    public InfrastructureNodes infrastructureLinksInfrastructureLinkStartNodeFkey() {
        if (_infrastructureLinksInfrastructureLinkStartNodeFkey == null)
            _infrastructureLinksInfrastructureLinkStartNodeFkey = new InfrastructureNodes(this, Keys.INFRASTRUCTURE_LINKS__INFRASTRUCTURE_LINKS_INFRASTRUCTURE_LINK_START_NODE_FKEY);

        return _infrastructureLinksInfrastructureLinkStartNodeFkey;
    }

    public InfrastructureNodes infrastructureLinksInfrastructureLinkEndNodeFkey() {
        if (_infrastructureLinksInfrastructureLinkEndNodeFkey == null)
            _infrastructureLinksInfrastructureLinkEndNodeFkey = new InfrastructureNodes(this, Keys.INFRASTRUCTURE_LINKS__INFRASTRUCTURE_LINKS_INFRASTRUCTURE_LINK_END_NODE_FKEY);

        return _infrastructureLinksInfrastructureLinkEndNodeFkey;
    }

    @Override
    public InfrastructureLinks as(String alias) {
        return new InfrastructureLinks(DSL.name(alias), this);
    }

    @Override
    public InfrastructureLinks as(Name alias) {
        return new InfrastructureLinks(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinks rename(String name) {
        return new InfrastructureLinks(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinks rename(Name name) {
        return new InfrastructureLinks(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<UUID, String, LineString, String, TimeRange, UUID, UUID> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}

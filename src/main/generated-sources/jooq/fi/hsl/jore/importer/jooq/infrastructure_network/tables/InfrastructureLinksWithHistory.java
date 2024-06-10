/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables;


import fi.hsl.jore.importer.config.jooq.converter.geometry.LineStringBinding;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.infrastructure_network.InfrastructureNetwork;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureLinksWithHistoryRecord;

import java.util.Collection;
import java.util.UUID;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.locationtech.jts.geom.LineString;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureLinksWithHistory extends TableImpl<InfrastructureLinksWithHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>infrastructure_network.infrastructure_links_with_history</code>
     */
    public static final InfrastructureLinksWithHistory INFRASTRUCTURE_LINKS_WITH_HISTORY = new InfrastructureLinksWithHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InfrastructureLinksWithHistoryRecord> getRecordType() {
        return InfrastructureLinksWithHistoryRecord.class;
    }

    /**
     * The column
     * <code>infrastructure_network.infrastructure_links_with_history.infrastructure_link_id</code>.
     */
    public final TableField<InfrastructureLinksWithHistoryRecord, UUID> INFRASTRUCTURE_LINK_ID = createField(DSL.name("infrastructure_link_id"), SQLDataType.UUID, this, "");

    /**
     * The column
     * <code>infrastructure_network.infrastructure_links_with_history.infrastructure_link_ext_id</code>.
     */
    public final TableField<InfrastructureLinksWithHistoryRecord, String> INFRASTRUCTURE_LINK_EXT_ID = createField(DSL.name("infrastructure_link_ext_id"), SQLDataType.CLOB, this, "");

    /**
     * The column
     * <code>infrastructure_network.infrastructure_links_with_history.infrastructure_link_geog</code>.
     */
    public final TableField<InfrastructureLinksWithHistoryRecord, LineString> INFRASTRUCTURE_LINK_GEOG = createField(DSL.name("infrastructure_link_geog"), SQLDataType.OTHER, this, "", new LineStringBinding());

    /**
     * The column
     * <code>infrastructure_network.infrastructure_links_with_history.infrastructure_network_type</code>.
     */
    public final TableField<InfrastructureLinksWithHistoryRecord, String> INFRASTRUCTURE_NETWORK_TYPE = createField(DSL.name("infrastructure_network_type"), SQLDataType.CLOB, this, "");

    /**
     * The column
     * <code>infrastructure_network.infrastructure_links_with_history.infrastructure_link_sys_period</code>.
     */
    public final TableField<InfrastructureLinksWithHistoryRecord, TimeRange> INFRASTRUCTURE_LINK_SYS_PERIOD = createField(DSL.name("infrastructure_link_sys_period"), DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\""), this, "", new TimeRangeBinding());

    /**
     * The column
     * <code>infrastructure_network.infrastructure_links_with_history.infrastructure_link_start_node</code>.
     */
    public final TableField<InfrastructureLinksWithHistoryRecord, UUID> INFRASTRUCTURE_LINK_START_NODE = createField(DSL.name("infrastructure_link_start_node"), SQLDataType.UUID, this, "");

    /**
     * The column
     * <code>infrastructure_network.infrastructure_links_with_history.infrastructure_link_end_node</code>.
     */
    public final TableField<InfrastructureLinksWithHistoryRecord, UUID> INFRASTRUCTURE_LINK_END_NODE = createField(DSL.name("infrastructure_link_end_node"), SQLDataType.UUID, this, "");

    private InfrastructureLinksWithHistory(Name alias, Table<InfrastructureLinksWithHistoryRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private InfrastructureLinksWithHistory(Name alias, Table<InfrastructureLinksWithHistoryRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view("""
         create view "infrastructure_links_with_history" as  SELECT infrastructure_links.infrastructure_link_id,
            infrastructure_links.infrastructure_link_ext_id,
            infrastructure_links.infrastructure_link_geog,
            infrastructure_links.infrastructure_network_type,
            infrastructure_links.infrastructure_link_sys_period,
            infrastructure_links.infrastructure_link_start_node,
            infrastructure_links.infrastructure_link_end_node
           FROM infrastructure_network.infrastructure_links
        UNION ALL
         SELECT infrastructure_links_history.infrastructure_link_id,
            infrastructure_links_history.infrastructure_link_ext_id,
            infrastructure_links_history.infrastructure_link_geog,
            infrastructure_links_history.infrastructure_network_type,
            infrastructure_links_history.infrastructure_link_sys_period,
            infrastructure_links_history.infrastructure_link_start_node,
            infrastructure_links_history.infrastructure_link_end_node
           FROM infrastructure_network.infrastructure_links_history;
        """), where);
    }

    /**
     * Create an aliased
     * <code>infrastructure_network.infrastructure_links_with_history</code>
     * table reference
     */
    public InfrastructureLinksWithHistory(String alias) {
        this(DSL.name(alias), INFRASTRUCTURE_LINKS_WITH_HISTORY);
    }

    /**
     * Create an aliased
     * <code>infrastructure_network.infrastructure_links_with_history</code>
     * table reference
     */
    public InfrastructureLinksWithHistory(Name alias) {
        this(alias, INFRASTRUCTURE_LINKS_WITH_HISTORY);
    }

    /**
     * Create a
     * <code>infrastructure_network.infrastructure_links_with_history</code>
     * table reference
     */
    public InfrastructureLinksWithHistory() {
        this(DSL.name("infrastructure_links_with_history"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : InfrastructureNetwork.INFRASTRUCTURE_NETWORK;
    }

    @Override
    public InfrastructureLinksWithHistory as(String alias) {
        return new InfrastructureLinksWithHistory(DSL.name(alias), this);
    }

    @Override
    public InfrastructureLinksWithHistory as(Name alias) {
        return new InfrastructureLinksWithHistory(alias, this);
    }

    @Override
    public InfrastructureLinksWithHistory as(Table<?> alias) {
        return new InfrastructureLinksWithHistory(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinksWithHistory rename(String name) {
        return new InfrastructureLinksWithHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinksWithHistory rename(Name name) {
        return new InfrastructureLinksWithHistory(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinksWithHistory rename(Table<?> name) {
        return new InfrastructureLinksWithHistory(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinksWithHistory where(Condition condition) {
        return new InfrastructureLinksWithHistory(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinksWithHistory where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinksWithHistory where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinksWithHistory where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public InfrastructureLinksWithHistory where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public InfrastructureLinksWithHistory where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public InfrastructureLinksWithHistory where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public InfrastructureLinksWithHistory where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinksWithHistory whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinksWithHistory whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}

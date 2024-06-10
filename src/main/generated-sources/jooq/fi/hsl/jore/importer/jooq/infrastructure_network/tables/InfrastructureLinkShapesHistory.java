/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables;


import fi.hsl.jore.importer.config.jooq.converter.geometry.LineStringBinding;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.infrastructure_network.InfrastructureNetwork;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureLinkShapesHistoryRecord;

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
public class InfrastructureLinkShapesHistory extends TableImpl<InfrastructureLinkShapesHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>infrastructure_network.infrastructure_link_shapes_history</code>
     */
    public static final InfrastructureLinkShapesHistory INFRASTRUCTURE_LINK_SHAPES_HISTORY = new InfrastructureLinkShapesHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InfrastructureLinkShapesHistoryRecord> getRecordType() {
        return InfrastructureLinkShapesHistoryRecord.class;
    }

    /**
     * The column
     * <code>infrastructure_network.infrastructure_link_shapes_history.infrastructure_link_shape_id</code>.
     */
    public final TableField<InfrastructureLinkShapesHistoryRecord, UUID> INFRASTRUCTURE_LINK_SHAPE_ID = createField(DSL.name("infrastructure_link_shape_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column
     * <code>infrastructure_network.infrastructure_link_shapes_history.infrastructure_link_ext_id</code>.
     */
    public final TableField<InfrastructureLinkShapesHistoryRecord, String> INFRASTRUCTURE_LINK_EXT_ID = createField(DSL.name("infrastructure_link_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>infrastructure_network.infrastructure_link_shapes_history.infrastructure_link_id</code>.
     */
    public final TableField<InfrastructureLinkShapesHistoryRecord, UUID> INFRASTRUCTURE_LINK_ID = createField(DSL.name("infrastructure_link_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column
     * <code>infrastructure_network.infrastructure_link_shapes_history.infrastructure_link_shape</code>.
     */
    public final TableField<InfrastructureLinkShapesHistoryRecord, LineString> INFRASTRUCTURE_LINK_SHAPE = createField(DSL.name("infrastructure_link_shape"), SQLDataType.OTHER.nullable(false), this, "", new LineStringBinding());

    /**
     * The column
     * <code>infrastructure_network.infrastructure_link_shapes_history.infrastructure_link_shape_sys_period</code>.
     */
    public final TableField<InfrastructureLinkShapesHistoryRecord, TimeRange> INFRASTRUCTURE_LINK_SHAPE_SYS_PERIOD = createField(DSL.name("infrastructure_link_shape_sys_period"), DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\"").nullable(false), this, "", new TimeRangeBinding());

    private InfrastructureLinkShapesHistory(Name alias, Table<InfrastructureLinkShapesHistoryRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private InfrastructureLinkShapesHistory(Name alias, Table<InfrastructureLinkShapesHistoryRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased
     * <code>infrastructure_network.infrastructure_link_shapes_history</code>
     * table reference
     */
    public InfrastructureLinkShapesHistory(String alias) {
        this(DSL.name(alias), INFRASTRUCTURE_LINK_SHAPES_HISTORY);
    }

    /**
     * Create an aliased
     * <code>infrastructure_network.infrastructure_link_shapes_history</code>
     * table reference
     */
    public InfrastructureLinkShapesHistory(Name alias) {
        this(alias, INFRASTRUCTURE_LINK_SHAPES_HISTORY);
    }

    /**
     * Create a
     * <code>infrastructure_network.infrastructure_link_shapes_history</code>
     * table reference
     */
    public InfrastructureLinkShapesHistory() {
        this(DSL.name("infrastructure_link_shapes_history"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : InfrastructureNetwork.INFRASTRUCTURE_NETWORK;
    }

    @Override
    public InfrastructureLinkShapesHistory as(String alias) {
        return new InfrastructureLinkShapesHistory(DSL.name(alias), this);
    }

    @Override
    public InfrastructureLinkShapesHistory as(Name alias) {
        return new InfrastructureLinkShapesHistory(alias, this);
    }

    @Override
    public InfrastructureLinkShapesHistory as(Table<?> alias) {
        return new InfrastructureLinkShapesHistory(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinkShapesHistory rename(String name) {
        return new InfrastructureLinkShapesHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinkShapesHistory rename(Name name) {
        return new InfrastructureLinkShapesHistory(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLinkShapesHistory rename(Table<?> name) {
        return new InfrastructureLinkShapesHistory(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinkShapesHistory where(Condition condition) {
        return new InfrastructureLinkShapesHistory(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinkShapesHistory where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinkShapesHistory where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinkShapesHistory where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public InfrastructureLinkShapesHistory where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public InfrastructureLinkShapesHistory where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public InfrastructureLinkShapesHistory where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public InfrastructureLinkShapesHistory where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinkShapesHistory whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public InfrastructureLinkShapesHistory whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}

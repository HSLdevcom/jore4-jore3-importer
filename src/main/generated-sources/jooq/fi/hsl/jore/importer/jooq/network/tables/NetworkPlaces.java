/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRangeBinding;
import fi.hsl.jore.importer.jooq.network.Keys;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPoints.ScheduledStopPointsPath;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkPlacesRecord;

import java.util.Collection;
import java.util.UUID;

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
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkPlaces extends TableImpl<NetworkPlacesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.network_places</code>
     */
    public static final NetworkPlaces NETWORK_PLACES = new NetworkPlaces();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkPlacesRecord> getRecordType() {
        return NetworkPlacesRecord.class;
    }

    /**
     * The column <code>network.network_places.network_place_id</code>.
     */
    public final TableField<NetworkPlacesRecord, UUID> NETWORK_PLACE_ID = createField(DSL.name("network_place_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field(DSL.raw("gen_random_uuid()"), SQLDataType.UUID)), this, "");

    /**
     * The column <code>network.network_places.network_place_ext_id</code>.
     */
    public final TableField<NetworkPlacesRecord, String> NETWORK_PLACE_EXT_ID = createField(DSL.name("network_place_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_places.network_place_name</code>.
     */
    public final TableField<NetworkPlacesRecord, String> NETWORK_PLACE_NAME = createField(DSL.name("network_place_name"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_places.network_place_sys_period</code>.
     */
    public final TableField<NetworkPlacesRecord, TimeRange> NETWORK_PLACE_SYS_PERIOD = createField(DSL.name("network_place_sys_period"), DefaultDataType.getDefaultDataType("\"pg_catalog\".\"tstzrange\"").nullable(false).defaultValue(DSL.field(DSL.raw("tstzrange(CURRENT_TIMESTAMP, NULL::timestamp with time zone)"), org.jooq.impl.SQLDataType.OTHER)), this, "", new TimeRangeBinding());

    private NetworkPlaces(Name alias, Table<NetworkPlacesRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private NetworkPlaces(Name alias, Table<NetworkPlacesRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>network.network_places</code> table reference
     */
    public NetworkPlaces(String alias) {
        this(DSL.name(alias), NETWORK_PLACES);
    }

    /**
     * Create an aliased <code>network.network_places</code> table reference
     */
    public NetworkPlaces(Name alias) {
        this(alias, NETWORK_PLACES);
    }

    /**
     * Create a <code>network.network_places</code> table reference
     */
    public NetworkPlaces() {
        this(DSL.name("network_places"), null);
    }

    public <O extends Record> NetworkPlaces(Table<O> path, ForeignKey<O, NetworkPlacesRecord> childPath, InverseForeignKey<O, NetworkPlacesRecord> parentPath) {
        super(path, childPath, parentPath, NETWORK_PLACES);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class NetworkPlacesPath extends NetworkPlaces implements Path<NetworkPlacesRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> NetworkPlacesPath(Table<O> path, ForeignKey<O, NetworkPlacesRecord> childPath, InverseForeignKey<O, NetworkPlacesRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private NetworkPlacesPath(Name alias, Table<NetworkPlacesRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public NetworkPlacesPath as(String alias) {
            return new NetworkPlacesPath(DSL.name(alias), this);
        }

        @Override
        public NetworkPlacesPath as(Name alias) {
            return new NetworkPlacesPath(alias, this);
        }

        @Override
        public NetworkPlacesPath as(Table<?> alias) {
            return new NetworkPlacesPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Network.NETWORK;
    }

    @Override
    public UniqueKey<NetworkPlacesRecord> getPrimaryKey() {
        return Keys.NETWORK_PLACES_PKEY;
    }

    private transient ScheduledStopPointsPath _scheduledStopPoints;

    /**
     * Get the implicit to-many join path to the
     * <code>network.scheduled_stop_points</code> table
     */
    public ScheduledStopPointsPath scheduledStopPoints() {
        if (_scheduledStopPoints == null)
            _scheduledStopPoints = new ScheduledStopPointsPath(this, null, Keys.SCHEDULED_STOP_POINTS__SCHEDULED_STOP_POINTS_NETWORK_PLACE_ID_FKEY.getInverseKey());

        return _scheduledStopPoints;
    }

    @Override
    public NetworkPlaces as(String alias) {
        return new NetworkPlaces(DSL.name(alias), this);
    }

    @Override
    public NetworkPlaces as(Name alias) {
        return new NetworkPlaces(alias, this);
    }

    @Override
    public NetworkPlaces as(Table<?> alias) {
        return new NetworkPlaces(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkPlaces rename(String name) {
        return new NetworkPlaces(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkPlaces rename(Name name) {
        return new NetworkPlaces(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkPlaces rename(Table<?> name) {
        return new NetworkPlaces(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkPlaces where(Condition condition) {
        return new NetworkPlaces(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkPlaces where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkPlaces where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkPlaces where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkPlaces where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkPlaces where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkPlaces where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public NetworkPlaces where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkPlaces whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public NetworkPlaces whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}

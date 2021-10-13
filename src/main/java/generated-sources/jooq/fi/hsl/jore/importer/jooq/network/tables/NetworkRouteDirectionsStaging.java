/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRangeBinding;
import fi.hsl.jore.importer.jooq.network.Keys;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRouteDirectionsStagingRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row9;
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
public class NetworkRouteDirectionsStaging extends TableImpl<NetworkRouteDirectionsStagingRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.network_route_directions_staging</code>
     */
    public static final NetworkRouteDirectionsStaging NETWORK_ROUTE_DIRECTIONS_STAGING = new NetworkRouteDirectionsStaging();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkRouteDirectionsStagingRecord> getRecordType() {
        return NetworkRouteDirectionsStagingRecord.class;
    }

    /**
     * The column <code>network.network_route_directions_staging.network_route_direction_ext_id</code>.
     */
    public final TableField<NetworkRouteDirectionsStagingRecord, String> NETWORK_ROUTE_DIRECTION_EXT_ID = createField(DSL.name("network_route_direction_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_route_directions_staging.network_route_ext_id</code>.
     */
    public final TableField<NetworkRouteDirectionsStagingRecord, String> NETWORK_ROUTE_EXT_ID = createField(DSL.name("network_route_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_route_directions_staging.network_route_direction_type</code>.
     */
    public final TableField<NetworkRouteDirectionsStagingRecord, String> NETWORK_ROUTE_DIRECTION_TYPE = createField(DSL.name("network_route_direction_type"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_route_directions_staging.network_route_direction_length</code>.
     */
    public final TableField<NetworkRouteDirectionsStagingRecord, Integer> NETWORK_ROUTE_DIRECTION_LENGTH = createField(DSL.name("network_route_direction_length"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>network.network_route_directions_staging.network_route_direction_name</code>.
     */
    public final TableField<NetworkRouteDirectionsStagingRecord, JSONB> NETWORK_ROUTE_DIRECTION_NAME = createField(DSL.name("network_route_direction_name"), SQLDataType.JSONB.nullable(false), this, "");

    /**
     * The column <code>network.network_route_directions_staging.network_route_direction_name_short</code>.
     */
    public final TableField<NetworkRouteDirectionsStagingRecord, JSONB> NETWORK_ROUTE_DIRECTION_NAME_SHORT = createField(DSL.name("network_route_direction_name_short"), SQLDataType.JSONB.nullable(false), this, "");

    /**
     * The column <code>network.network_route_directions_staging.network_route_direction_origin</code>.
     */
    public final TableField<NetworkRouteDirectionsStagingRecord, JSONB> NETWORK_ROUTE_DIRECTION_ORIGIN = createField(DSL.name("network_route_direction_origin"), SQLDataType.JSONB.nullable(false), this, "");

    /**
     * The column <code>network.network_route_directions_staging.network_route_direction_destination</code>.
     */
    public final TableField<NetworkRouteDirectionsStagingRecord, JSONB> NETWORK_ROUTE_DIRECTION_DESTINATION = createField(DSL.name("network_route_direction_destination"), SQLDataType.JSONB.nullable(false), this, "");

    /**
     * The column <code>network.network_route_directions_staging.network_route_direction_valid_date_range</code>.
     */
    public final TableField<NetworkRouteDirectionsStagingRecord, DateRange> NETWORK_ROUTE_DIRECTION_VALID_DATE_RANGE = createField(DSL.name("network_route_direction_valid_date_range"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"daterange\"").nullable(false), this, "", new DateRangeBinding());

    private NetworkRouteDirectionsStaging(Name alias, Table<NetworkRouteDirectionsStagingRecord> aliased) {
        this(alias, aliased, null);
    }

    private NetworkRouteDirectionsStaging(Name alias, Table<NetworkRouteDirectionsStagingRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>network.network_route_directions_staging</code> table reference
     */
    public NetworkRouteDirectionsStaging(String alias) {
        this(DSL.name(alias), NETWORK_ROUTE_DIRECTIONS_STAGING);
    }

    /**
     * Create an aliased <code>network.network_route_directions_staging</code> table reference
     */
    public NetworkRouteDirectionsStaging(Name alias) {
        this(alias, NETWORK_ROUTE_DIRECTIONS_STAGING);
    }

    /**
     * Create a <code>network.network_route_directions_staging</code> table reference
     */
    public NetworkRouteDirectionsStaging() {
        this(DSL.name("network_route_directions_staging"), null);
    }

    public <O extends Record> NetworkRouteDirectionsStaging(Table<O> child, ForeignKey<O, NetworkRouteDirectionsStagingRecord> key) {
        super(child, key, NETWORK_ROUTE_DIRECTIONS_STAGING);
    }

    @Override
    public Schema getSchema() {
        return Network.NETWORK;
    }

    @Override
    public UniqueKey<NetworkRouteDirectionsStagingRecord> getPrimaryKey() {
        return Keys.NETWORK_ROUTE_DIRECTIONS_STAGING_PKEY;
    }

    @Override
    public List<UniqueKey<NetworkRouteDirectionsStagingRecord>> getKeys() {
        return Arrays.<UniqueKey<NetworkRouteDirectionsStagingRecord>>asList(Keys.NETWORK_ROUTE_DIRECTIONS_STAGING_PKEY);
    }

    @Override
    public List<ForeignKey<NetworkRouteDirectionsStagingRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<NetworkRouteDirectionsStagingRecord, ?>>asList(Keys.NETWORK_ROUTE_DIRECTIONS_STAGING__NETWORK_ROUTE_DIRECTIONS_STAG_NETWORK_ROUTE_DIRECTION_TYPE_FKEY);
    }

    private transient NetworkDirectionTypes _networkDirectionTypes;

    public NetworkDirectionTypes networkDirectionTypes() {
        if (_networkDirectionTypes == null)
            _networkDirectionTypes = new NetworkDirectionTypes(this, Keys.NETWORK_ROUTE_DIRECTIONS_STAGING__NETWORK_ROUTE_DIRECTIONS_STAG_NETWORK_ROUTE_DIRECTION_TYPE_FKEY);

        return _networkDirectionTypes;
    }

    @Override
    public NetworkRouteDirectionsStaging as(String alias) {
        return new NetworkRouteDirectionsStaging(DSL.name(alias), this);
    }

    @Override
    public NetworkRouteDirectionsStaging as(Name alias) {
        return new NetworkRouteDirectionsStaging(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRouteDirectionsStaging rename(String name) {
        return new NetworkRouteDirectionsStaging(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkRouteDirectionsStaging rename(Name name) {
        return new NetworkRouteDirectionsStaging(name, null);
    }

    // -------------------------------------------------------------------------
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<String, String, String, Integer, JSONB, JSONB, JSONB, JSONB, DateRange> fieldsRow() {
        return (Row9) super.fieldsRow();
    }
}
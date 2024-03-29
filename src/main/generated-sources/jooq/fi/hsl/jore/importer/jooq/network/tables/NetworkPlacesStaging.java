/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.jooq.network.Keys;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkPlacesStagingRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
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
public class NetworkPlacesStaging extends TableImpl<NetworkPlacesStagingRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.network_places_staging</code>
     */
    public static final NetworkPlacesStaging NETWORK_PLACES_STAGING = new NetworkPlacesStaging();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NetworkPlacesStagingRecord> getRecordType() {
        return NetworkPlacesStagingRecord.class;
    }

    /**
     * The column <code>network.network_places_staging.network_place_ext_id</code>.
     */
    public final TableField<NetworkPlacesStagingRecord, String> NETWORK_PLACE_EXT_ID = createField(DSL.name("network_place_ext_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>network.network_places_staging.network_place_name</code>.
     */
    public final TableField<NetworkPlacesStagingRecord, String> NETWORK_PLACE_NAME = createField(DSL.name("network_place_name"), SQLDataType.CLOB.nullable(false), this, "");

    private NetworkPlacesStaging(Name alias, Table<NetworkPlacesStagingRecord> aliased) {
        this(alias, aliased, null);
    }

    private NetworkPlacesStaging(Name alias, Table<NetworkPlacesStagingRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>network.network_places_staging</code> table reference
     */
    public NetworkPlacesStaging(String alias) {
        this(DSL.name(alias), NETWORK_PLACES_STAGING);
    }

    /**
     * Create an aliased <code>network.network_places_staging</code> table reference
     */
    public NetworkPlacesStaging(Name alias) {
        this(alias, NETWORK_PLACES_STAGING);
    }

    /**
     * Create a <code>network.network_places_staging</code> table reference
     */
    public NetworkPlacesStaging() {
        this(DSL.name("network_places_staging"), null);
    }

    public <O extends Record> NetworkPlacesStaging(Table<O> child, ForeignKey<O, NetworkPlacesStagingRecord> key) {
        super(child, key, NETWORK_PLACES_STAGING);
    }

    @Override
    public Schema getSchema() {
        return Network.NETWORK;
    }

    @Override
    public UniqueKey<NetworkPlacesStagingRecord> getPrimaryKey() {
        return Keys.NETWORK_PLACES_STAGING_PKEY;
    }

    @Override
    public List<UniqueKey<NetworkPlacesStagingRecord>> getKeys() {
        return Arrays.<UniqueKey<NetworkPlacesStagingRecord>>asList(Keys.NETWORK_PLACES_STAGING_PKEY);
    }

    @Override
    public NetworkPlacesStaging as(String alias) {
        return new NetworkPlacesStaging(DSL.name(alias), this);
    }

    @Override
    public NetworkPlacesStaging as(Name alias) {
        return new NetworkPlacesStaging(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkPlacesStaging rename(String name) {
        return new NetworkPlacesStaging(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NetworkPlacesStaging rename(Name name) {
        return new NetworkPlacesStaging(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<String, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}

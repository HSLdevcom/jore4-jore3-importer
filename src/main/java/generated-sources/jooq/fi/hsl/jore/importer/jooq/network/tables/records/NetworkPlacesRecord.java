/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.network.tables.NetworkPlaces;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkPlacesRecord extends UpdatableRecordImpl<NetworkPlacesRecord> implements Record4<UUID, String, String, TimeRange> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>network.network_places.network_place_id</code>.
     */
    public void setNetworkPlaceId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>network.network_places.network_place_id</code>.
     */
    public UUID getNetworkPlaceId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>network.network_places.network_place_ext_id</code>.
     */
    public void setNetworkPlaceExtId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>network.network_places.network_place_ext_id</code>.
     */
    public String getNetworkPlaceExtId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>network.network_places.network_place_name</code>.
     */
    public void setNetworkPlaceName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>network.network_places.network_place_name</code>.
     */
    public String getNetworkPlaceName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>network.network_places.network_place_sys_period</code>.
     */
    public void setNetworkPlaceSysPeriod(TimeRange value) {
        set(3, value);
    }

    /**
     * Getter for <code>network.network_places.network_place_sys_period</code>.
     */
    public TimeRange getNetworkPlaceSysPeriod() {
        return (TimeRange) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<UUID, String, String, TimeRange> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<UUID, String, String, TimeRange> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return NetworkPlaces.NETWORK_PLACES.NETWORK_PLACE_ID;
    }

    @Override
    public Field<String> field2() {
        return NetworkPlaces.NETWORK_PLACES.NETWORK_PLACE_EXT_ID;
    }

    @Override
    public Field<String> field3() {
        return NetworkPlaces.NETWORK_PLACES.NETWORK_PLACE_NAME;
    }

    @Override
    public Field<TimeRange> field4() {
        return NetworkPlaces.NETWORK_PLACES.NETWORK_PLACE_SYS_PERIOD;
    }

    @Override
    public UUID component1() {
        return getNetworkPlaceId();
    }

    @Override
    public String component2() {
        return getNetworkPlaceExtId();
    }

    @Override
    public String component3() {
        return getNetworkPlaceName();
    }

    @Override
    public TimeRange component4() {
        return getNetworkPlaceSysPeriod();
    }

    @Override
    public UUID value1() {
        return getNetworkPlaceId();
    }

    @Override
    public String value2() {
        return getNetworkPlaceExtId();
    }

    @Override
    public String value3() {
        return getNetworkPlaceName();
    }

    @Override
    public TimeRange value4() {
        return getNetworkPlaceSysPeriod();
    }

    @Override
    public NetworkPlacesRecord value1(UUID value) {
        setNetworkPlaceId(value);
        return this;
    }

    @Override
    public NetworkPlacesRecord value2(String value) {
        setNetworkPlaceExtId(value);
        return this;
    }

    @Override
    public NetworkPlacesRecord value3(String value) {
        setNetworkPlaceName(value);
        return this;
    }

    @Override
    public NetworkPlacesRecord value4(TimeRange value) {
        setNetworkPlaceSysPeriod(value);
        return this;
    }

    @Override
    public NetworkPlacesRecord values(UUID value1, String value2, String value3, TimeRange value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached NetworkPlacesRecord
     */
    public NetworkPlacesRecord() {
        super(NetworkPlaces.NETWORK_PLACES);
    }

    /**
     * Create a detached, initialised NetworkPlacesRecord
     */
    public NetworkPlacesRecord(UUID networkPlaceId, String networkPlaceExtId, String networkPlaceName, TimeRange networkPlaceSysPeriod) {
        super(NetworkPlaces.NETWORK_PLACES);

        setNetworkPlaceId(networkPlaceId);
        setNetworkPlaceExtId(networkPlaceExtId);
        setNetworkPlaceName(networkPlaceName);
        setNetworkPlaceSysPeriod(networkPlaceSysPeriod);
    }
}

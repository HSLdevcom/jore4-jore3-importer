/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables.records;


import fi.hsl.jore.importer.jooq.network.tables.NetworkDirectionTypes;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NetworkDirectionTypesRecord extends UpdatableRecordImpl<NetworkDirectionTypesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>network.network_direction_types.network_direction_type</code>.
     */
    public void setNetworkDirectionType(String value) {
        set(0, value);
    }

    /**
     * Getter for
     * <code>network.network_direction_types.network_direction_type</code>.
     */
    public String getNetworkDirectionType() {
        return (String) get(0);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached NetworkDirectionTypesRecord
     */
    public NetworkDirectionTypesRecord() {
        super(NetworkDirectionTypes.NETWORK_DIRECTION_TYPES);
    }

    /**
     * Create a detached, initialised NetworkDirectionTypesRecord
     */
    public NetworkDirectionTypesRecord(String networkDirectionType) {
        super(NetworkDirectionTypes.NETWORK_DIRECTION_TYPES);

        setNetworkDirectionType(networkDirectionType);
        resetChangedOnNotNull();
    }
}

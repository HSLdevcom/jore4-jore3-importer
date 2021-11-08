/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.infrastructure_network.tables.records;


import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.ExternalSource;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Row1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * An external source from which infrastructure network parts are imported
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ExternalSourceRecord extends UpdatableRecordImpl<ExternalSourceRecord> implements Record1<String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>infrastructure_network.external_source.value</code>.
     */
    public void setValue(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>infrastructure_network.external_source.value</code>.
     */
    public String getValue() {
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
    // Record1 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row1<String> fieldsRow() {
        return (Row1) super.fieldsRow();
    }

    @Override
    public Row1<String> valuesRow() {
        return (Row1) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return ExternalSource.EXTERNAL_SOURCE.VALUE;
    }

    @Override
    public String component1() {
        return getValue();
    }

    @Override
    public String value1() {
        return getValue();
    }

    @Override
    public ExternalSourceRecord value1(String value) {
        setValue(value);
        return this;
    }

    @Override
    public ExternalSourceRecord values(String value1) {
        value1(value1);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ExternalSourceRecord
     */
    public ExternalSourceRecord() {
        super(ExternalSource.EXTERNAL_SOURCE);
    }

    /**
     * Create a detached, initialised ExternalSourceRecord
     */
    public ExternalSourceRecord(String value) {
        super(ExternalSource.EXTERNAL_SOURCE);

        setValue(value);
    }
}

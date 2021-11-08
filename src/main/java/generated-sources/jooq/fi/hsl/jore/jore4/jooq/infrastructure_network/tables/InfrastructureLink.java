/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.infrastructure_network.tables;


import fi.hsl.jore.importer.config.jooq.converter.geometry.LineStringBinding;
import fi.hsl.jore.jore4.jooq.infrastructure_network.InfrastructureNetwork;
import fi.hsl.jore.jore4.jooq.infrastructure_network.Keys;
import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.records.InfrastructureLinkRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row6;
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
 * The infrastructure links, e.g. road or rail elements: https://www.transmodel-cen.eu/model/index.htm?goto=2:1:1:1:453
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureLink extends TableImpl<InfrastructureLinkRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>infrastructure_network.infrastructure_link</code>
     */
    public static final InfrastructureLink INFRASTRUCTURE_LINK = new InfrastructureLink();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InfrastructureLinkRecord> getRecordType() {
        return InfrastructureLinkRecord.class;
    }

    /**
     * The column <code>infrastructure_network.infrastructure_link.infrastructure_link_id</code>. The ID of the infrastructure link.
     */
    public final TableField<InfrastructureLinkRecord, UUID> INFRASTRUCTURE_LINK_ID = createField(DSL.name("infrastructure_link_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("gen_random_uuid()", SQLDataType.UUID)), this, "The ID of the infrastructure link.");

    /**
     * The column <code>infrastructure_network.infrastructure_link.direction</code>. The direction(s) of traffic with respect to the digitization, i.e. the direction of the specified line string.
     */
    public final TableField<InfrastructureLinkRecord, String> DIRECTION = createField(DSL.name("direction"), SQLDataType.CLOB.nullable(false), this, "The direction(s) of traffic with respect to the digitization, i.e. the direction of the specified line string.");

    /**
     * The column <code>infrastructure_network.infrastructure_link.shape</code>. A PostGIS LinestringZ geography in EPSG:4326 describing the infrastructure link.
     */
    public final TableField<InfrastructureLinkRecord, LineString> SHAPE = createField(DSL.name("shape"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\"").nullable(false), this, "A PostGIS LinestringZ geography in EPSG:4326 describing the infrastructure link.", new LineStringBinding());

    /**
     * The column <code>infrastructure_network.infrastructure_link.estimated_length_in_metres</code>. The estimated length of the infrastructure link in metres.
     */
    public final TableField<InfrastructureLinkRecord, Double> ESTIMATED_LENGTH_IN_METRES = createField(DSL.name("estimated_length_in_metres"), SQLDataType.DOUBLE, this, "The estimated length of the infrastructure link in metres.");

    /**
     * The column <code>infrastructure_network.infrastructure_link.external_link_id</code>.
     */
    public final TableField<InfrastructureLinkRecord, String> EXTERNAL_LINK_ID = createField(DSL.name("external_link_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>infrastructure_network.infrastructure_link.external_link_source</code>.
     */
    public final TableField<InfrastructureLinkRecord, String> EXTERNAL_LINK_SOURCE = createField(DSL.name("external_link_source"), SQLDataType.CLOB.nullable(false), this, "");

    private InfrastructureLink(Name alias, Table<InfrastructureLinkRecord> aliased) {
        this(alias, aliased, null);
    }

    private InfrastructureLink(Name alias, Table<InfrastructureLinkRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("The infrastructure links, e.g. road or rail elements: https://www.transmodel-cen.eu/model/index.htm?goto=2:1:1:1:453"), TableOptions.table());
    }

    /**
     * Create an aliased <code>infrastructure_network.infrastructure_link</code> table reference
     */
    public InfrastructureLink(String alias) {
        this(DSL.name(alias), INFRASTRUCTURE_LINK);
    }

    /**
     * Create an aliased <code>infrastructure_network.infrastructure_link</code> table reference
     */
    public InfrastructureLink(Name alias) {
        this(alias, INFRASTRUCTURE_LINK);
    }

    /**
     * Create a <code>infrastructure_network.infrastructure_link</code> table reference
     */
    public InfrastructureLink() {
        this(DSL.name("infrastructure_link"), null);
    }

    public <O extends Record> InfrastructureLink(Table<O> child, ForeignKey<O, InfrastructureLinkRecord> key) {
        super(child, key, INFRASTRUCTURE_LINK);
    }

    @Override
    public Schema getSchema() {
        return InfrastructureNetwork.INFRASTRUCTURE_NETWORK;
    }

    @Override
    public UniqueKey<InfrastructureLinkRecord> getPrimaryKey() {
        return Keys.INFRASTRUCTURE_LINK_PKEY;
    }

    @Override
    public List<UniqueKey<InfrastructureLinkRecord>> getKeys() {
        return Arrays.<UniqueKey<InfrastructureLinkRecord>>asList(Keys.INFRASTRUCTURE_LINK_PKEY);
    }

    @Override
    public List<ForeignKey<InfrastructureLinkRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<InfrastructureLinkRecord, ?>>asList(Keys.INFRASTRUCTURE_LINK__INFRASTRUCTURE_LINK_DIRECTION_FKEY, Keys.INFRASTRUCTURE_LINK__INFRASTRUCTURE_LINK_EXTERNAL_LINK_SOURCE_FKEY);
    }

    private transient Direction _direction;
    private transient ExternalSource _externalSource;

    public Direction direction() {
        if (_direction == null)
            _direction = new Direction(this, Keys.INFRASTRUCTURE_LINK__INFRASTRUCTURE_LINK_DIRECTION_FKEY);

        return _direction;
    }

    public ExternalSource externalSource() {
        if (_externalSource == null)
            _externalSource = new ExternalSource(this, Keys.INFRASTRUCTURE_LINK__INFRASTRUCTURE_LINK_EXTERNAL_LINK_SOURCE_FKEY);

        return _externalSource;
    }

    @Override
    public InfrastructureLink as(String alias) {
        return new InfrastructureLink(DSL.name(alias), this);
    }

    @Override
    public InfrastructureLink as(Name alias) {
        return new InfrastructureLink(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLink rename(String name) {
        return new InfrastructureLink(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public InfrastructureLink rename(Name name) {
        return new InfrastructureLink(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<UUID, String, LineString, Double, String, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}

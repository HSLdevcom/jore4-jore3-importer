/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.infrastructure_network.tables;


import fi.hsl.jore.importer.config.jooq.converter.geometry.LineStringBinding;
import fi.hsl.jore.jore4.jooq.infrastructure_network.InfrastructureNetwork;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.locationtech.jts.geom.LineString;


/**
 * The infrastructure links, e.g. road or rail elements: https://www.transmodel-cen.eu/model/index.htm?goto=2:1:1:1:453
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureLink extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>infrastructure_network.infrastructure_link</code>
     */
    public static final InfrastructureLink INFRASTRUCTURE_LINK = new InfrastructureLink();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>infrastructure_network.infrastructure_link.infrastructure_link_id</code>. The ID of the infrastructure link.
     */
    public final TableField<Record, UUID> INFRASTRUCTURE_LINK_ID = createField(DSL.name("infrastructure_link_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("gen_random_uuid()", SQLDataType.UUID)), this, "The ID of the infrastructure link.");

    /**
     * The column <code>infrastructure_network.infrastructure_link.direction</code>. The direction(s) of traffic with respect to the digitization, i.e. the direction of the specified line string.
     */
    public final TableField<Record, String> DIRECTION = createField(DSL.name("direction"), SQLDataType.CLOB.nullable(false), this, "The direction(s) of traffic with respect to the digitization, i.e. the direction of the specified line string.");

    /**
     * The column <code>infrastructure_network.infrastructure_link.shape</code>. A PostGIS LinestringZ geography in EPSG:4326 describing the infrastructure link.
     */
    public final TableField<Record, LineString> SHAPE = createField(DSL.name("shape"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\"").nullable(false), this, "A PostGIS LinestringZ geography in EPSG:4326 describing the infrastructure link.", new LineStringBinding());

    /**
     * The column <code>infrastructure_network.infrastructure_link.estimated_length_in_metres</code>. The estimated length of the infrastructure link in metres.
     */
    public final TableField<Record, Double> ESTIMATED_LENGTH_IN_METRES = createField(DSL.name("estimated_length_in_metres"), SQLDataType.DOUBLE, this, "The estimated length of the infrastructure link in metres.");

    /**
     * The column <code>infrastructure_network.infrastructure_link.external_link_id</code>.
     */
    public final TableField<Record, String> EXTERNAL_LINK_ID = createField(DSL.name("external_link_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>infrastructure_network.infrastructure_link.external_link_source</code>.
     */
    public final TableField<Record, String> EXTERNAL_LINK_SOURCE = createField(DSL.name("external_link_source"), SQLDataType.CLOB.nullable(false), this, "");

    private InfrastructureLink(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private InfrastructureLink(Name alias, Table<Record> aliased, Field<?>[] parameters) {
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

    public <O extends Record> InfrastructureLink(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, INFRASTRUCTURE_LINK);
    }

    @Override
    public Schema getSchema() {
        return InfrastructureNetwork.INFRASTRUCTURE_NETWORK;
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
}

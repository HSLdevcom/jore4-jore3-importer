/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.infrastructure_network.tables;


import fi.hsl.jore.importer.config.jooq.converter.geometry.LineStringBinding;
import fi.hsl.jore.jore4.jooq.infrastructure_network.InfrastructureNetwork;
import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.records.ResolvePointToClosestLinkRecord;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Row6;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.locationtech.jts.geom.LineString;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ResolvePointToClosestLink extends TableImpl<ResolvePointToClosestLinkRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>infrastructure_network.resolve_point_to_closest_link</code>
     */
    public static final ResolvePointToClosestLink RESOLVE_POINT_TO_CLOSEST_LINK = new ResolvePointToClosestLink();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ResolvePointToClosestLinkRecord> getRecordType() {
        return ResolvePointToClosestLinkRecord.class;
    }

    /**
     * The column <code>infrastructure_network.resolve_point_to_closest_link.infrastructure_link_id</code>.
     */
    public final TableField<ResolvePointToClosestLinkRecord, UUID> INFRASTRUCTURE_LINK_ID = createField(DSL.name("infrastructure_link_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("gen_random_uuid()", SQLDataType.UUID)), this, "");

    /**
     * The column <code>infrastructure_network.resolve_point_to_closest_link.direction</code>.
     */
    public final TableField<ResolvePointToClosestLinkRecord, String> DIRECTION = createField(DSL.name("direction"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>infrastructure_network.resolve_point_to_closest_link.shape</code>.
     */
    public final TableField<ResolvePointToClosestLinkRecord, LineString> SHAPE = createField(DSL.name("shape"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\"").nullable(false), this, "", new LineStringBinding());

    /**
     * The column <code>infrastructure_network.resolve_point_to_closest_link.estimated_length_in_metres</code>.
     */
    public final TableField<ResolvePointToClosestLinkRecord, Double> ESTIMATED_LENGTH_IN_METRES = createField(DSL.name("estimated_length_in_metres"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>infrastructure_network.resolve_point_to_closest_link.external_link_id</code>.
     */
    public final TableField<ResolvePointToClosestLinkRecord, String> EXTERNAL_LINK_ID = createField(DSL.name("external_link_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>infrastructure_network.resolve_point_to_closest_link.external_link_source</code>.
     */
    public final TableField<ResolvePointToClosestLinkRecord, String> EXTERNAL_LINK_SOURCE = createField(DSL.name("external_link_source"), SQLDataType.CLOB.nullable(false), this, "");

    private ResolvePointToClosestLink(Name alias, Table<ResolvePointToClosestLinkRecord> aliased) {
        this(alias, aliased, new Field[1]);
    }

    private ResolvePointToClosestLink(Name alias, Table<ResolvePointToClosestLinkRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.function());
    }

    /**
     * Create an aliased <code>infrastructure_network.resolve_point_to_closest_link</code> table reference
     */
    public ResolvePointToClosestLink(String alias) {
        this(DSL.name(alias), RESOLVE_POINT_TO_CLOSEST_LINK);
    }

    /**
     * Create an aliased <code>infrastructure_network.resolve_point_to_closest_link</code> table reference
     */
    public ResolvePointToClosestLink(Name alias) {
        this(alias, RESOLVE_POINT_TO_CLOSEST_LINK);
    }

    /**
     * Create a <code>infrastructure_network.resolve_point_to_closest_link</code> table reference
     */
    public ResolvePointToClosestLink() {
        this(DSL.name("resolve_point_to_closest_link"), null);
    }

    @Override
    public Schema getSchema() {
        return InfrastructureNetwork.INFRASTRUCTURE_NETWORK;
    }

    @Override
    public ResolvePointToClosestLink as(String alias) {
        return new ResolvePointToClosestLink(DSL.name(alias), this, parameters);
    }

    @Override
    public ResolvePointToClosestLink as(Name alias) {
        return new ResolvePointToClosestLink(alias, this, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public ResolvePointToClosestLink rename(String name) {
        return new ResolvePointToClosestLink(DSL.name(name), null, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public ResolvePointToClosestLink rename(Name name) {
        return new ResolvePointToClosestLink(name, null, parameters);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<UUID, String, LineString, Double, String, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * Call this table-valued function
     */
    public ResolvePointToClosestLink call(
          Object geog
    ) {
        ResolvePointToClosestLink result = new ResolvePointToClosestLink(DSL.name("resolve_point_to_closest_link"), null, new Field[] {
              DSL.val(geog, org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\""))
        });

        return aliased() ? result.as(getUnqualifiedName()) : result;
    }

    /**
     * Call this table-valued function
     */
    public ResolvePointToClosestLink call(
          Field<Object> geog
    ) {
        ResolvePointToClosestLink result = new ResolvePointToClosestLink(DSL.name("resolve_point_to_closest_link"), null, new Field[] {
              geog
        });

        return aliased() ? result.as(getUnqualifiedName()) : result;
    }
}
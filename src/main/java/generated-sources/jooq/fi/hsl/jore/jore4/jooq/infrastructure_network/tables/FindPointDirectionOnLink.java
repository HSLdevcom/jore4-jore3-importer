/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.infrastructure_network.tables;


import fi.hsl.jore.jore4.jooq.infrastructure_network.InfrastructureNetwork;
import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.records.FindPointDirectionOnLinkRecord;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Row1;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FindPointDirectionOnLink extends TableImpl<FindPointDirectionOnLinkRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>infrastructure_network.find_point_direction_on_link</code>
     */
    public static final FindPointDirectionOnLink FIND_POINT_DIRECTION_ON_LINK = new FindPointDirectionOnLink();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<FindPointDirectionOnLinkRecord> getRecordType() {
        return FindPointDirectionOnLinkRecord.class;
    }

    /**
     * The column <code>infrastructure_network.find_point_direction_on_link.value</code>.
     */
    public final TableField<FindPointDirectionOnLinkRecord, String> VALUE = createField(DSL.name("value"), SQLDataType.CLOB.nullable(false), this, "");

    private FindPointDirectionOnLink(Name alias, Table<FindPointDirectionOnLinkRecord> aliased) {
        this(alias, aliased, new Field[3]);
    }

    private FindPointDirectionOnLink(Name alias, Table<FindPointDirectionOnLinkRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.function());
    }

    /**
     * Create an aliased <code>infrastructure_network.find_point_direction_on_link</code> table reference
     */
    public FindPointDirectionOnLink(String alias) {
        this(DSL.name(alias), FIND_POINT_DIRECTION_ON_LINK);
    }

    /**
     * Create an aliased <code>infrastructure_network.find_point_direction_on_link</code> table reference
     */
    public FindPointDirectionOnLink(Name alias) {
        this(alias, FIND_POINT_DIRECTION_ON_LINK);
    }

    /**
     * Create a <code>infrastructure_network.find_point_direction_on_link</code> table reference
     */
    public FindPointDirectionOnLink() {
        this(DSL.name("find_point_direction_on_link"), null);
    }

    @Override
    public Schema getSchema() {
        return InfrastructureNetwork.INFRASTRUCTURE_NETWORK;
    }

    @Override
    public FindPointDirectionOnLink as(String alias) {
        return new FindPointDirectionOnLink(DSL.name(alias), this, parameters);
    }

    @Override
    public FindPointDirectionOnLink as(Name alias) {
        return new FindPointDirectionOnLink(alias, this, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public FindPointDirectionOnLink rename(String name) {
        return new FindPointDirectionOnLink(DSL.name(name), null, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public FindPointDirectionOnLink rename(Name name) {
        return new FindPointDirectionOnLink(name, null, parameters);
    }

    // -------------------------------------------------------------------------
    // Row1 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row1<String> fieldsRow() {
        return (Row1) super.fieldsRow();
    }

    /**
     * Call this table-valued function
     */
    public FindPointDirectionOnLink call(
          Object pointOfInterest
        , UUID infrastructureLinkUuid
        , Double pointMaxDistanceInMeters
    ) {
        FindPointDirectionOnLink result = new FindPointDirectionOnLink(DSL.name("find_point_direction_on_link"), null, new Field[] {
              DSL.val(pointOfInterest, org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\""))
            , DSL.val(infrastructureLinkUuid, SQLDataType.UUID)
            , DSL.val(pointMaxDistanceInMeters, SQLDataType.DOUBLE.defaultValue(DSL.field("50.0", SQLDataType.DOUBLE)))
        });

        return aliased() ? result.as(getUnqualifiedName()) : result;
    }

    /**
     * Call this table-valued function
     */
    public FindPointDirectionOnLink call(
          Field<Object> pointOfInterest
        , Field<UUID> infrastructureLinkUuid
        , Field<Double> pointMaxDistanceInMeters
    ) {
        FindPointDirectionOnLink result = new FindPointDirectionOnLink(DSL.name("find_point_direction_on_link"), null, new Field[] {
              pointOfInterest
            , infrastructureLinkUuid
            , pointMaxDistanceInMeters
        });

        return aliased() ? result.as(getUnqualifiedName()) : result;
    }
}
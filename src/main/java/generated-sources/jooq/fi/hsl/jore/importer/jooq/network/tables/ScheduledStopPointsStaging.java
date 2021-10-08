/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network.tables;


import fi.hsl.jore.importer.config.jooq.converter.geometry.PointBinding;
import fi.hsl.jore.importer.jooq.network.Keys;
import fi.hsl.jore.importer.jooq.network.Network;
import fi.hsl.jore.importer.jooq.network.tables.records.ScheduledStopPointsStagingRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.locationtech.jts.geom.Point;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ScheduledStopPointsStaging extends TableImpl<ScheduledStopPointsStagingRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network.scheduled_stop_points_staging</code>
     */
    public static final ScheduledStopPointsStaging SCHEDULED_STOP_POINTS_STAGING = new ScheduledStopPointsStaging();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ScheduledStopPointsStagingRecord> getRecordType() {
        return ScheduledStopPointsStagingRecord.class;
    }

    /**
     * The column <code>network.scheduled_stop_points_staging.scheduled_stop_point_ext_id</code>.
     */
    public final TableField<ScheduledStopPointsStagingRecord, String> SCHEDULED_STOP_POINT_EXT_ID = createField(DSL.name("scheduled_stop_point_ext_id"), SQLDataType.VARCHAR(7).nullable(false), this, "");

    /**
     * The column <code>network.scheduled_stop_points_staging.scheduled_stop_point_location</code>.
     */
    public final TableField<ScheduledStopPointsStagingRecord, Point> SCHEDULED_STOP_POINT_LOCATION = createField(DSL.name("scheduled_stop_point_location"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geometry\"").nullable(false), this, "", new PointBinding());

    /**
     * The column <code>network.scheduled_stop_points_staging.scheduled_stop_point_name</code>.
     */
    public final TableField<ScheduledStopPointsStagingRecord, JSONB> SCHEDULED_STOP_POINT_NAME = createField(DSL.name("scheduled_stop_point_name"), SQLDataType.JSONB.nullable(false), this, "");

    private ScheduledStopPointsStaging(Name alias, Table<ScheduledStopPointsStagingRecord> aliased) {
        this(alias, aliased, null);
    }

    private ScheduledStopPointsStaging(Name alias, Table<ScheduledStopPointsStagingRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>network.scheduled_stop_points_staging</code> table reference
     */
    public ScheduledStopPointsStaging(String alias) {
        this(DSL.name(alias), SCHEDULED_STOP_POINTS_STAGING);
    }

    /**
     * Create an aliased <code>network.scheduled_stop_points_staging</code> table reference
     */
    public ScheduledStopPointsStaging(Name alias) {
        this(alias, SCHEDULED_STOP_POINTS_STAGING);
    }

    /**
     * Create a <code>network.scheduled_stop_points_staging</code> table reference
     */
    public ScheduledStopPointsStaging() {
        this(DSL.name("scheduled_stop_points_staging"), null);
    }

    public <O extends Record> ScheduledStopPointsStaging(Table<O> child, ForeignKey<O, ScheduledStopPointsStagingRecord> key) {
        super(child, key, SCHEDULED_STOP_POINTS_STAGING);
    }

    @Override
    public Schema getSchema() {
        return Network.NETWORK;
    }

    @Override
    public UniqueKey<ScheduledStopPointsStagingRecord> getPrimaryKey() {
        return Keys.SCHEDULED_STOP_POINTS_STAGING_PKEY;
    }

    @Override
    public List<UniqueKey<ScheduledStopPointsStagingRecord>> getKeys() {
        return Arrays.<UniqueKey<ScheduledStopPointsStagingRecord>>asList(Keys.SCHEDULED_STOP_POINTS_STAGING_PKEY);
    }

    @Override
    public ScheduledStopPointsStaging as(String alias) {
        return new ScheduledStopPointsStaging(DSL.name(alias), this);
    }

    @Override
    public ScheduledStopPointsStaging as(Name alias) {
        return new ScheduledStopPointsStaging(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPointsStaging rename(String name) {
        return new ScheduledStopPointsStaging(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPointsStaging rename(Name name) {
        return new ScheduledStopPointsStaging(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<String, Point, JSONB> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}

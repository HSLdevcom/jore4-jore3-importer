/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.internal_service_pattern.tables;


import fi.hsl.jore.importer.config.jooq.converter.geometry.PointBinding;
import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.Direction;
import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.InfrastructureLink;
import fi.hsl.jore.jore4.jooq.internal_service_pattern.InternalServicePattern;
import fi.hsl.jore.jore4.jooq.internal_service_pattern.Keys;
import fi.hsl.jore.jore4.jooq.internal_service_pattern.tables.records.ScheduledStopPointRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
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
public class ScheduledStopPoint extends TableImpl<ScheduledStopPointRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>internal_service_pattern.scheduled_stop_point</code>
     */
    public static final ScheduledStopPoint SCHEDULED_STOP_POINT = new ScheduledStopPoint();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ScheduledStopPointRecord> getRecordType() {
        return ScheduledStopPointRecord.class;
    }

    /**
     * The column <code>internal_service_pattern.scheduled_stop_point.scheduled_stop_point_id</code>.
     */
    public final TableField<ScheduledStopPointRecord, UUID> SCHEDULED_STOP_POINT_ID = createField(DSL.name("scheduled_stop_point_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("gen_random_uuid()", SQLDataType.UUID)), this, "");

    /**
     * The column <code>internal_service_pattern.scheduled_stop_point.measured_location</code>.
     */
    public final TableField<ScheduledStopPointRecord, Point> MEASURED_LOCATION = createField(DSL.name("measured_location"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"public\".\"geography\"").nullable(false), this, "", new PointBinding());

    /**
     * The column <code>internal_service_pattern.scheduled_stop_point.located_on_infrastructure_link_id</code>.
     */
    public final TableField<ScheduledStopPointRecord, UUID> LOCATED_ON_INFRASTRUCTURE_LINK_ID = createField(DSL.name("located_on_infrastructure_link_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>internal_service_pattern.scheduled_stop_point.direction</code>.
     */
    public final TableField<ScheduledStopPointRecord, String> DIRECTION = createField(DSL.name("direction"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>internal_service_pattern.scheduled_stop_point.label</code>.
     */
    public final TableField<ScheduledStopPointRecord, String> LABEL = createField(DSL.name("label"), SQLDataType.CLOB.nullable(false), this, "");

    private ScheduledStopPoint(Name alias, Table<ScheduledStopPointRecord> aliased) {
        this(alias, aliased, null);
    }

    private ScheduledStopPoint(Name alias, Table<ScheduledStopPointRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>internal_service_pattern.scheduled_stop_point</code> table reference
     */
    public ScheduledStopPoint(String alias) {
        this(DSL.name(alias), SCHEDULED_STOP_POINT);
    }

    /**
     * Create an aliased <code>internal_service_pattern.scheduled_stop_point</code> table reference
     */
    public ScheduledStopPoint(Name alias) {
        this(alias, SCHEDULED_STOP_POINT);
    }

    /**
     * Create a <code>internal_service_pattern.scheduled_stop_point</code> table reference
     */
    public ScheduledStopPoint() {
        this(DSL.name("scheduled_stop_point"), null);
    }

    public <O extends Record> ScheduledStopPoint(Table<O> child, ForeignKey<O, ScheduledStopPointRecord> key) {
        super(child, key, SCHEDULED_STOP_POINT);
    }

    @Override
    public Schema getSchema() {
        return InternalServicePattern.INTERNAL_SERVICE_PATTERN;
    }

    @Override
    public UniqueKey<ScheduledStopPointRecord> getPrimaryKey() {
        return Keys.SCHEDULED_STOP_POINT_PKEY;
    }

    @Override
    public List<UniqueKey<ScheduledStopPointRecord>> getKeys() {
        return Arrays.<UniqueKey<ScheduledStopPointRecord>>asList(Keys.SCHEDULED_STOP_POINT_PKEY);
    }

    @Override
    public List<ForeignKey<ScheduledStopPointRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ScheduledStopPointRecord, ?>>asList(Keys.SCHEDULED_STOP_POINT__SCHEDULED_STOP_POINT_LOCATED_ON_INFRASTRUCTURE_LINK_ID_FKEY, Keys.SCHEDULED_STOP_POINT__SCHEDULED_STOP_POINT_DIRECTION_FKEY);
    }

    private transient InfrastructureLink _infrastructureLink;
    private transient Direction _direction;

    public InfrastructureLink infrastructureLink() {
        if (_infrastructureLink == null)
            _infrastructureLink = new InfrastructureLink(this, Keys.SCHEDULED_STOP_POINT__SCHEDULED_STOP_POINT_LOCATED_ON_INFRASTRUCTURE_LINK_ID_FKEY);

        return _infrastructureLink;
    }

    public Direction direction() {
        if (_direction == null)
            _direction = new Direction(this, Keys.SCHEDULED_STOP_POINT__SCHEDULED_STOP_POINT_DIRECTION_FKEY);

        return _direction;
    }

    @Override
    public ScheduledStopPoint as(String alias) {
        return new ScheduledStopPoint(DSL.name(alias), this);
    }

    @Override
    public ScheduledStopPoint as(Name alias) {
        return new ScheduledStopPoint(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPoint rename(String name) {
        return new ScheduledStopPoint(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScheduledStopPoint rename(Name name) {
        return new ScheduledStopPoint(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<UUID, Point, UUID, String, String> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
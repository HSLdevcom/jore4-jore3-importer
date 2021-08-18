/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinkShapesWithHistory;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.TableRecordImpl;
import org.locationtech.jts.geom.LineString;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureLinkShapesWithHistoryRecord extends TableRecordImpl<InfrastructureLinkShapesWithHistoryRecord> implements Record5<UUID, String, UUID, LineString, TimeRange> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>infrastructure_network.infrastructure_link_shapes_with_history.infrastructure_link_shape_id</code>.
     */
    public void setInfrastructureLinkShapeId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>infrastructure_network.infrastructure_link_shapes_with_history.infrastructure_link_shape_id</code>.
     */
    public UUID getInfrastructureLinkShapeId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>infrastructure_network.infrastructure_link_shapes_with_history.infrastructure_link_ext_id</code>.
     */
    public void setInfrastructureLinkExtId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>infrastructure_network.infrastructure_link_shapes_with_history.infrastructure_link_ext_id</code>.
     */
    public String getInfrastructureLinkExtId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>infrastructure_network.infrastructure_link_shapes_with_history.infrastructure_link_link_id</code>.
     */
    public void setInfrastructureLinkLinkId(UUID value) {
        set(2, value);
    }

    /**
     * Getter for <code>infrastructure_network.infrastructure_link_shapes_with_history.infrastructure_link_link_id</code>.
     */
    public UUID getInfrastructureLinkLinkId() {
        return (UUID) get(2);
    }

    /**
     * Setter for <code>infrastructure_network.infrastructure_link_shapes_with_history.infrastructure_link_shape</code>.
     */
    public void setInfrastructureLinkShape(LineString value) {
        set(3, value);
    }

    /**
     * Getter for <code>infrastructure_network.infrastructure_link_shapes_with_history.infrastructure_link_shape</code>.
     */
    public LineString getInfrastructureLinkShape() {
        return (LineString) get(3);
    }

    /**
     * Setter for <code>infrastructure_network.infrastructure_link_shapes_with_history.infrastructure_link_shape_sys_period</code>.
     */
    public void setInfrastructureLinkShapeSysPeriod(TimeRange value) {
        set(4, value);
    }

    /**
     * Getter for <code>infrastructure_network.infrastructure_link_shapes_with_history.infrastructure_link_shape_sys_period</code>.
     */
    public TimeRange getInfrastructureLinkShapeSysPeriod() {
        return (TimeRange) get(4);
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<UUID, String, UUID, LineString, TimeRange> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<UUID, String, UUID, LineString, TimeRange> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return InfrastructureLinkShapesWithHistory.INFRASTRUCTURE_LINK_SHAPES_WITH_HISTORY.INFRASTRUCTURE_LINK_SHAPE_ID;
    }

    @Override
    public Field<String> field2() {
        return InfrastructureLinkShapesWithHistory.INFRASTRUCTURE_LINK_SHAPES_WITH_HISTORY.INFRASTRUCTURE_LINK_EXT_ID;
    }

    @Override
    public Field<UUID> field3() {
        return InfrastructureLinkShapesWithHistory.INFRASTRUCTURE_LINK_SHAPES_WITH_HISTORY.INFRASTRUCTURE_LINK_LINK_ID;
    }

    @Override
    public Field<LineString> field4() {
        return InfrastructureLinkShapesWithHistory.INFRASTRUCTURE_LINK_SHAPES_WITH_HISTORY.INFRASTRUCTURE_LINK_SHAPE;
    }

    @Override
    public Field<TimeRange> field5() {
        return InfrastructureLinkShapesWithHistory.INFRASTRUCTURE_LINK_SHAPES_WITH_HISTORY.INFRASTRUCTURE_LINK_SHAPE_SYS_PERIOD;
    }

    @Override
    public UUID component1() {
        return getInfrastructureLinkShapeId();
    }

    @Override
    public String component2() {
        return getInfrastructureLinkExtId();
    }

    @Override
    public UUID component3() {
        return getInfrastructureLinkLinkId();
    }

    @Override
    public LineString component4() {
        return getInfrastructureLinkShape();
    }

    @Override
    public TimeRange component5() {
        return getInfrastructureLinkShapeSysPeriod();
    }

    @Override
    public UUID value1() {
        return getInfrastructureLinkShapeId();
    }

    @Override
    public String value2() {
        return getInfrastructureLinkExtId();
    }

    @Override
    public UUID value3() {
        return getInfrastructureLinkLinkId();
    }

    @Override
    public LineString value4() {
        return getInfrastructureLinkShape();
    }

    @Override
    public TimeRange value5() {
        return getInfrastructureLinkShapeSysPeriod();
    }

    @Override
    public InfrastructureLinkShapesWithHistoryRecord value1(UUID value) {
        setInfrastructureLinkShapeId(value);
        return this;
    }

    @Override
    public InfrastructureLinkShapesWithHistoryRecord value2(String value) {
        setInfrastructureLinkExtId(value);
        return this;
    }

    @Override
    public InfrastructureLinkShapesWithHistoryRecord value3(UUID value) {
        setInfrastructureLinkLinkId(value);
        return this;
    }

    @Override
    public InfrastructureLinkShapesWithHistoryRecord value4(LineString value) {
        setInfrastructureLinkShape(value);
        return this;
    }

    @Override
    public InfrastructureLinkShapesWithHistoryRecord value5(TimeRange value) {
        setInfrastructureLinkShapeSysPeriod(value);
        return this;
    }

    @Override
    public InfrastructureLinkShapesWithHistoryRecord values(UUID value1, String value2, UUID value3, LineString value4, TimeRange value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached InfrastructureLinkShapesWithHistoryRecord
     */
    public InfrastructureLinkShapesWithHistoryRecord() {
        super(InfrastructureLinkShapesWithHistory.INFRASTRUCTURE_LINK_SHAPES_WITH_HISTORY);
    }

    /**
     * Create a detached, initialised InfrastructureLinkShapesWithHistoryRecord
     */
    public InfrastructureLinkShapesWithHistoryRecord(UUID infrastructureLinkShapeId, String infrastructureLinkExtId, UUID infrastructureLinkLinkId, LineString infrastructureLinkShape, TimeRange infrastructureLinkShapeSysPeriod) {
        super(InfrastructureLinkShapesWithHistory.INFRASTRUCTURE_LINK_SHAPES_WITH_HISTORY);

        setInfrastructureLinkShapeId(infrastructureLinkShapeId);
        setInfrastructureLinkExtId(infrastructureLinkExtId);
        setInfrastructureLinkLinkId(infrastructureLinkLinkId);
        setInfrastructureLinkShape(infrastructureLinkShape);
        setInfrastructureLinkShapeSysPeriod(infrastructureLinkShapeSysPeriod);
    }
}

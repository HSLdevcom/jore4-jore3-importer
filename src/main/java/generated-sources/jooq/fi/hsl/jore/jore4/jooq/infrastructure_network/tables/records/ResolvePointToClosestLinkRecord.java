/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.infrastructure_network.tables.records;


import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.ResolvePointToClosestLink;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.TableRecordImpl;
import org.locationtech.jts.geom.LineString;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ResolvePointToClosestLinkRecord extends TableRecordImpl<ResolvePointToClosestLinkRecord> implements Record6<UUID, String, LineString, Double, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>infrastructure_network.resolve_point_to_closest_link.infrastructure_link_id</code>.
     */
    public void setInfrastructureLinkId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>infrastructure_network.resolve_point_to_closest_link.infrastructure_link_id</code>.
     */
    public UUID getInfrastructureLinkId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>infrastructure_network.resolve_point_to_closest_link.direction</code>.
     */
    public void setDirection(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>infrastructure_network.resolve_point_to_closest_link.direction</code>.
     */
    public String getDirection() {
        return (String) get(1);
    }

    /**
     * Setter for <code>infrastructure_network.resolve_point_to_closest_link.shape</code>.
     */
    public void setShape(LineString value) {
        set(2, value);
    }

    /**
     * Getter for <code>infrastructure_network.resolve_point_to_closest_link.shape</code>.
     */
    public LineString getShape() {
        return (LineString) get(2);
    }

    /**
     * Setter for <code>infrastructure_network.resolve_point_to_closest_link.estimated_length_in_metres</code>.
     */
    public void setEstimatedLengthInMetres(Double value) {
        set(3, value);
    }

    /**
     * Getter for <code>infrastructure_network.resolve_point_to_closest_link.estimated_length_in_metres</code>.
     */
    public Double getEstimatedLengthInMetres() {
        return (Double) get(3);
    }

    /**
     * Setter for <code>infrastructure_network.resolve_point_to_closest_link.external_link_id</code>.
     */
    public void setExternalLinkId(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>infrastructure_network.resolve_point_to_closest_link.external_link_id</code>.
     */
    public String getExternalLinkId() {
        return (String) get(4);
    }

    /**
     * Setter for <code>infrastructure_network.resolve_point_to_closest_link.external_link_source</code>.
     */
    public void setExternalLinkSource(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>infrastructure_network.resolve_point_to_closest_link.external_link_source</code>.
     */
    public String getExternalLinkSource() {
        return (String) get(5);
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<UUID, String, LineString, Double, String, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<UUID, String, LineString, Double, String, String> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return ResolvePointToClosestLink.RESOLVE_POINT_TO_CLOSEST_LINK.INFRASTRUCTURE_LINK_ID;
    }

    @Override
    public Field<String> field2() {
        return ResolvePointToClosestLink.RESOLVE_POINT_TO_CLOSEST_LINK.DIRECTION;
    }

    @Override
    public Field<LineString> field3() {
        return ResolvePointToClosestLink.RESOLVE_POINT_TO_CLOSEST_LINK.SHAPE;
    }

    @Override
    public Field<Double> field4() {
        return ResolvePointToClosestLink.RESOLVE_POINT_TO_CLOSEST_LINK.ESTIMATED_LENGTH_IN_METRES;
    }

    @Override
    public Field<String> field5() {
        return ResolvePointToClosestLink.RESOLVE_POINT_TO_CLOSEST_LINK.EXTERNAL_LINK_ID;
    }

    @Override
    public Field<String> field6() {
        return ResolvePointToClosestLink.RESOLVE_POINT_TO_CLOSEST_LINK.EXTERNAL_LINK_SOURCE;
    }

    @Override
    public UUID component1() {
        return getInfrastructureLinkId();
    }

    @Override
    public String component2() {
        return getDirection();
    }

    @Override
    public LineString component3() {
        return getShape();
    }

    @Override
    public Double component4() {
        return getEstimatedLengthInMetres();
    }

    @Override
    public String component5() {
        return getExternalLinkId();
    }

    @Override
    public String component6() {
        return getExternalLinkSource();
    }

    @Override
    public UUID value1() {
        return getInfrastructureLinkId();
    }

    @Override
    public String value2() {
        return getDirection();
    }

    @Override
    public LineString value3() {
        return getShape();
    }

    @Override
    public Double value4() {
        return getEstimatedLengthInMetres();
    }

    @Override
    public String value5() {
        return getExternalLinkId();
    }

    @Override
    public String value6() {
        return getExternalLinkSource();
    }

    @Override
    public ResolvePointToClosestLinkRecord value1(UUID value) {
        setInfrastructureLinkId(value);
        return this;
    }

    @Override
    public ResolvePointToClosestLinkRecord value2(String value) {
        setDirection(value);
        return this;
    }

    @Override
    public ResolvePointToClosestLinkRecord value3(LineString value) {
        setShape(value);
        return this;
    }

    @Override
    public ResolvePointToClosestLinkRecord value4(Double value) {
        setEstimatedLengthInMetres(value);
        return this;
    }

    @Override
    public ResolvePointToClosestLinkRecord value5(String value) {
        setExternalLinkId(value);
        return this;
    }

    @Override
    public ResolvePointToClosestLinkRecord value6(String value) {
        setExternalLinkSource(value);
        return this;
    }

    @Override
    public ResolvePointToClosestLinkRecord values(UUID value1, String value2, LineString value3, Double value4, String value5, String value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ResolvePointToClosestLinkRecord
     */
    public ResolvePointToClosestLinkRecord() {
        super(ResolvePointToClosestLink.RESOLVE_POINT_TO_CLOSEST_LINK);
    }

    /**
     * Create a detached, initialised ResolvePointToClosestLinkRecord
     */
    public ResolvePointToClosestLinkRecord(UUID infrastructureLinkId, String direction, LineString shape, Double estimatedLengthInMetres, String externalLinkId, String externalLinkSource) {
        super(ResolvePointToClosestLink.RESOLVE_POINT_TO_CLOSEST_LINK);

        setInfrastructureLinkId(infrastructureLinkId);
        setDirection(direction);
        setShape(shape);
        setEstimatedLengthInMetres(estimatedLengthInMetres);
        setExternalLinkId(externalLinkId);
        setExternalLinkSource(externalLinkSource);
    }
}

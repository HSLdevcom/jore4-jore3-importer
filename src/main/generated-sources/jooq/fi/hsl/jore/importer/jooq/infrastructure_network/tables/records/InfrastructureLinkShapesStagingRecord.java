/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables.records;


import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinkShapesStaging;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;
import org.locationtech.jts.geom.LineString;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureLinkShapesStagingRecord extends UpdatableRecordImpl<InfrastructureLinkShapesStagingRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_link_shapes_staging.infrastructure_link_ext_id</code>.
     */
    public void setInfrastructureLinkExtId(String value) {
        set(0, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_link_shapes_staging.infrastructure_link_ext_id</code>.
     */
    public String getInfrastructureLinkExtId() {
        return (String) get(0);
    }

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_link_shapes_staging.infrastructure_link_shape</code>.
     */
    public void setInfrastructureLinkShape(LineString value) {
        set(1, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_link_shapes_staging.infrastructure_link_shape</code>.
     */
    public LineString getInfrastructureLinkShape() {
        return (LineString) get(1);
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
     * Create a detached InfrastructureLinkShapesStagingRecord
     */
    public InfrastructureLinkShapesStagingRecord() {
        super(InfrastructureLinkShapesStaging.INFRASTRUCTURE_LINK_SHAPES_STAGING);
    }

    /**
     * Create a detached, initialised InfrastructureLinkShapesStagingRecord
     */
    public InfrastructureLinkShapesStagingRecord(String infrastructureLinkExtId, LineString infrastructureLinkShape) {
        super(InfrastructureLinkShapesStaging.INFRASTRUCTURE_LINK_SHAPES_STAGING);

        setInfrastructureLinkExtId(infrastructureLinkExtId);
        setInfrastructureLinkShape(infrastructureLinkShape);
        resetChangedOnNotNull();
    }
}

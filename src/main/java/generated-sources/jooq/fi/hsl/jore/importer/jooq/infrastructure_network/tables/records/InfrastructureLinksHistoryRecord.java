/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.infrastructure_network.tables.records;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinksHistory;

import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.TableRecordImpl;
import org.locationtech.jts.geom.LineString;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureLinksHistoryRecord extends TableRecordImpl<InfrastructureLinksHistoryRecord> implements Record7<UUID, String, LineString, String, TimeRange, UUID, UUID> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_links_history.infrastructure_link_id</code>.
     */
    public void setInfrastructureLinkId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_links_history.infrastructure_link_id</code>.
     */
    public UUID getInfrastructureLinkId() {
        return (UUID) get(0);
    }

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_links_history.infrastructure_link_ext_id</code>.
     */
    public void setInfrastructureLinkExtId(String value) {
        set(1, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_links_history.infrastructure_link_ext_id</code>.
     */
    public String getInfrastructureLinkExtId() {
        return (String) get(1);
    }

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_links_history.infrastructure_link_geog</code>.
     */
    public void setInfrastructureLinkGeog(LineString value) {
        set(2, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_links_history.infrastructure_link_geog</code>.
     */
    public LineString getInfrastructureLinkGeog() {
        return (LineString) get(2);
    }

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_links_history.infrastructure_network_type</code>.
     */
    public void setInfrastructureNetworkType(String value) {
        set(3, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_links_history.infrastructure_network_type</code>.
     */
    public String getInfrastructureNetworkType() {
        return (String) get(3);
    }

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_links_history.infrastructure_link_sys_period</code>.
     */
    public void setInfrastructureLinkSysPeriod(TimeRange value) {
        set(4, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_links_history.infrastructure_link_sys_period</code>.
     */
    public TimeRange getInfrastructureLinkSysPeriod() {
        return (TimeRange) get(4);
    }

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_links_history.infrastructure_link_start_node</code>.
     */
    public void setInfrastructureLinkStartNode(UUID value) {
        set(5, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_links_history.infrastructure_link_start_node</code>.
     */
    public UUID getInfrastructureLinkStartNode() {
        return (UUID) get(5);
    }

    /**
     * Setter for
     * <code>infrastructure_network.infrastructure_links_history.infrastructure_link_end_node</code>.
     */
    public void setInfrastructureLinkEndNode(UUID value) {
        set(6, value);
    }

    /**
     * Getter for
     * <code>infrastructure_network.infrastructure_links_history.infrastructure_link_end_node</code>.
     */
    public UUID getInfrastructureLinkEndNode() {
        return (UUID) get(6);
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<UUID, String, LineString, String, TimeRange, UUID, UUID> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<UUID, String, LineString, String, TimeRange, UUID, UUID> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return InfrastructureLinksHistory.INFRASTRUCTURE_LINKS_HISTORY.INFRASTRUCTURE_LINK_ID;
    }

    @Override
    public Field<String> field2() {
        return InfrastructureLinksHistory.INFRASTRUCTURE_LINKS_HISTORY.INFRASTRUCTURE_LINK_EXT_ID;
    }

    @Override
    public Field<LineString> field3() {
        return InfrastructureLinksHistory.INFRASTRUCTURE_LINKS_HISTORY.INFRASTRUCTURE_LINK_GEOG;
    }

    @Override
    public Field<String> field4() {
        return InfrastructureLinksHistory.INFRASTRUCTURE_LINKS_HISTORY.INFRASTRUCTURE_NETWORK_TYPE;
    }

    @Override
    public Field<TimeRange> field5() {
        return InfrastructureLinksHistory.INFRASTRUCTURE_LINKS_HISTORY.INFRASTRUCTURE_LINK_SYS_PERIOD;
    }

    @Override
    public Field<UUID> field6() {
        return InfrastructureLinksHistory.INFRASTRUCTURE_LINKS_HISTORY.INFRASTRUCTURE_LINK_START_NODE;
    }

    @Override
    public Field<UUID> field7() {
        return InfrastructureLinksHistory.INFRASTRUCTURE_LINKS_HISTORY.INFRASTRUCTURE_LINK_END_NODE;
    }

    @Override
    public UUID component1() {
        return getInfrastructureLinkId();
    }

    @Override
    public String component2() {
        return getInfrastructureLinkExtId();
    }

    @Override
    public LineString component3() {
        return getInfrastructureLinkGeog();
    }

    @Override
    public String component4() {
        return getInfrastructureNetworkType();
    }

    @Override
    public TimeRange component5() {
        return getInfrastructureLinkSysPeriod();
    }

    @Override
    public UUID component6() {
        return getInfrastructureLinkStartNode();
    }

    @Override
    public UUID component7() {
        return getInfrastructureLinkEndNode();
    }

    @Override
    public UUID value1() {
        return getInfrastructureLinkId();
    }

    @Override
    public String value2() {
        return getInfrastructureLinkExtId();
    }

    @Override
    public LineString value3() {
        return getInfrastructureLinkGeog();
    }

    @Override
    public String value4() {
        return getInfrastructureNetworkType();
    }

    @Override
    public TimeRange value5() {
        return getInfrastructureLinkSysPeriod();
    }

    @Override
    public UUID value6() {
        return getInfrastructureLinkStartNode();
    }

    @Override
    public UUID value7() {
        return getInfrastructureLinkEndNode();
    }

    @Override
    public InfrastructureLinksHistoryRecord value1(UUID value) {
        setInfrastructureLinkId(value);
        return this;
    }

    @Override
    public InfrastructureLinksHistoryRecord value2(String value) {
        setInfrastructureLinkExtId(value);
        return this;
    }

    @Override
    public InfrastructureLinksHistoryRecord value3(LineString value) {
        setInfrastructureLinkGeog(value);
        return this;
    }

    @Override
    public InfrastructureLinksHistoryRecord value4(String value) {
        setInfrastructureNetworkType(value);
        return this;
    }

    @Override
    public InfrastructureLinksHistoryRecord value5(TimeRange value) {
        setInfrastructureLinkSysPeriod(value);
        return this;
    }

    @Override
    public InfrastructureLinksHistoryRecord value6(UUID value) {
        setInfrastructureLinkStartNode(value);
        return this;
    }

    @Override
    public InfrastructureLinksHistoryRecord value7(UUID value) {
        setInfrastructureLinkEndNode(value);
        return this;
    }

    @Override
    public InfrastructureLinksHistoryRecord values(UUID value1, String value2, LineString value3, String value4, TimeRange value5, UUID value6, UUID value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached InfrastructureLinksHistoryRecord
     */
    public InfrastructureLinksHistoryRecord() {
        super(InfrastructureLinksHistory.INFRASTRUCTURE_LINKS_HISTORY);
    }

    /**
     * Create a detached, initialised InfrastructureLinksHistoryRecord
     */
    public InfrastructureLinksHistoryRecord(UUID infrastructureLinkId, String infrastructureLinkExtId, LineString infrastructureLinkGeog, String infrastructureNetworkType, TimeRange infrastructureLinkSysPeriod, UUID infrastructureLinkStartNode, UUID infrastructureLinkEndNode) {
        super(InfrastructureLinksHistory.INFRASTRUCTURE_LINKS_HISTORY);

        setInfrastructureLinkId(infrastructureLinkId);
        setInfrastructureLinkExtId(infrastructureLinkExtId);
        setInfrastructureLinkGeog(infrastructureLinkGeog);
        setInfrastructureNetworkType(infrastructureNetworkType);
        setInfrastructureLinkSysPeriod(infrastructureLinkSysPeriod);
        setInfrastructureLinkStartNode(infrastructureLinkStartNode);
        setInfrastructureLinkEndNode(infrastructureLinkEndNode);
    }
}

package fi.hsl.jore.importer.feature.network.route_point.repository;

import static fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodes.INFRASTRUCTURE_NODES;
import static fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirections.NETWORK_ROUTE_DIRECTIONS;
import static fi.hsl.jore.importer.jooq.network.tables.NetworkRoutePoints.NETWORK_ROUTE_POINTS;
import static fi.hsl.jore.importer.jooq.network.tables.NetworkRouteStopPoints.NETWORK_ROUTE_STOP_POINTS;
import static fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPoints.SCHEDULED_STOP_POINTS;
import static org.jooq.impl.DSL.length;

import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRoutePoint;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodes;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirections;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutePoints;
import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPoints;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RoutePointExportRepository implements IRoutePointExportRepository {
    static final InfrastructureNodes IN = INFRASTRUCTURE_NODES;
    static final NetworkRouteDirections NRD = NETWORK_ROUTE_DIRECTIONS;
    static final NetworkRoutePoints NRP = NETWORK_ROUTE_POINTS;
    static final ScheduledStopPoints SSP = SCHEDULED_STOP_POINTS;

    private final DSLContext db;

    @Autowired
    public RoutePointExportRepository(@Qualifier("importerDsl") final DSLContext db) {
        this.db = db;
    }

    @Transactional(readOnly = true)
    @Override
    public io.vavr.collection.List<ImporterRoutePoint> findImporterRoutePointsByRouteDirectionId(
            final UUID routeDirectionId) {
        final Stream<ImporterRoutePoint> pointStream = db.select(
                        NRD.NETWORK_ROUTE_JORE4_ID,
                        SSP.SCHEDULED_STOP_POINT_ELY_NUMBER,
                        SSP.SCHEDULED_STOP_POINT_SHORT_ID,
                        NRP.NETWORK_ROUTE_POINT_ORDER,
                        IN.INFRASTRUCTURE_NODE_LOCATION,
                        IN.INFRASTRUCTURE_NODE_PROJECTED_LOCATION,
                        IN.INFRASTRUCTURE_NODE_TYPE)
                .from(NRD)
                .join(NRP)
                .on(NRP.NETWORK_ROUTE_DIRECTION_ID.eq(NRD.NETWORK_ROUTE_DIRECTION_ID))
                .leftJoin(NETWORK_ROUTE_STOP_POINTS)
                .on(NETWORK_ROUTE_STOP_POINTS.NETWORK_ROUTE_POINT_ID.eq(NRP.NETWORK_ROUTE_POINT_ID))
                .join(IN)
                .on(IN.INFRASTRUCTURE_NODE_ID.eq(NRP.INFRASTRUCTURE_NODE))
                .leftJoin(SSP)
                .on(SSP.INFRASTRUCTURE_NODE_ID.eq(IN.INFRASTRUCTURE_NODE_ID))
                .where(getFindImporterRoutePointsByRouteDirectionIdWhereConditions(routeDirectionId))
                .orderBy(NRP.NETWORK_ROUTE_POINT_ORDER)
                .fetchStream()
                .map(RoutePointExportRepository::mapRecordToImporterRoutePoint);

        return io.vavr.collection.List.ofAll(pointStream);
    }

    private Condition getFindImporterRoutePointsByRouteDirectionIdWhereConditions(final UUID routeDirectionId) {
        return DSL.and(
                NRD.NETWORK_ROUTE_DIRECTION_ID.eq(routeDirectionId),
                NRD.NETWORK_ROUTE_JORE4_ID.isNotNull(),
                IN.INFRASTRUCTURE_NODE_TYPE
                        .eq(NodeType.STOP.value())
                        .and(SSP.SCHEDULED_STOP_POINT_ELY_NUMBER
                                .isNotNull()
                                .and(length(SSP.SCHEDULED_STOP_POINT_SHORT_ID).gt(4)))
                        .or(IN.INFRASTRUCTURE_NODE_TYPE.notEqual(NodeType.STOP.value())));
    }

    private static ImporterRoutePoint mapRecordToImporterRoutePoint(final org.jooq.Record record) {
        return ImporterRoutePoint.from(
                record.get(IN.INFRASTRUCTURE_NODE_LOCATION),
                record.get(NRP.NETWORK_ROUTE_POINT_ORDER),
                Optional.ofNullable(record.get(IN.INFRASTRUCTURE_NODE_PROJECTED_LOCATION)),
                NodeType.of(record.get(IN.INFRASTRUCTURE_NODE_TYPE)),
                Optional.ofNullable(record.get(SSP.SCHEDULED_STOP_POINT_ELY_NUMBER)),
                Optional.ofNullable(record.get(SSP.SCHEDULED_STOP_POINT_SHORT_ID)));
    }
}

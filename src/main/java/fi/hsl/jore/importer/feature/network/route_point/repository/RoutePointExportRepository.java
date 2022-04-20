package fi.hsl.jore.importer.feature.network.route_point.repository;

import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.network.route_point.dto.ExportableRoutePoint;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodes.INFRASTRUCTURE_NODES;
import static fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirections.NETWORK_ROUTE_DIRECTIONS;
import static fi.hsl.jore.importer.jooq.network.tables.NetworkRoutePoints.NETWORK_ROUTE_POINTS;
import static fi.hsl.jore.importer.jooq.network.tables.NetworkRouteStopPoints.NETWORK_ROUTE_STOP_POINTS;
import static fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPoints.SCHEDULED_STOP_POINTS;

@Repository
public class RoutePointExportRepository implements IRoutePointExportRepository {

    private final DSLContext db;

    @Autowired
    public RoutePointExportRepository(@Qualifier("importerDsl") final DSLContext db) {
        this.db = db;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExportableRoutePoint> findExportableRoutePointsByRouteDirectionId(final UUID routeDirectionId) {
        return Stream.ofAll(db.select(NETWORK_ROUTE_DIRECTIONS.NETWORK_ROUTE_TRANSMODEL_ID,
                        SCHEDULED_STOP_POINTS.SCHEDULED_STOP_POINT_ELY_NUMBER,
                        SCHEDULED_STOP_POINTS.SCHEDULED_STOP_POINT_SHORT_ID,
                        NETWORK_ROUTE_POINTS.NETWORK_ROUTE_POINT_ORDER,
                        INFRASTRUCTURE_NODES.INFRASTRUCTURE_NODE_LOCATION,
                        INFRASTRUCTURE_NODES.INFRASTRUCTURE_NODE_PROJECTED_LOCATION,
                        INFRASTRUCTURE_NODES.INFRASTRUCTURE_NODE_TYPE
                )
                .from(NETWORK_ROUTE_DIRECTIONS)
                .join(NETWORK_ROUTE_POINTS)
                .on(NETWORK_ROUTE_POINTS.NETWORK_ROUTE_DIRECTION_ID.eq(NETWORK_ROUTE_DIRECTIONS.NETWORK_ROUTE_DIRECTION_ID))
                .leftJoin(NETWORK_ROUTE_STOP_POINTS)
                .on(NETWORK_ROUTE_STOP_POINTS.NETWORK_ROUTE_POINT_ID.eq(NETWORK_ROUTE_POINTS.NETWORK_ROUTE_POINT_ID))
                .leftJoin(SCHEDULED_STOP_POINTS)
                .on(SCHEDULED_STOP_POINTS.SCHEDULED_STOP_POINT_EXT_ID.eq(
                        DSL.splitPart(NETWORK_ROUTE_STOP_POINTS.NETWORK_ROUTE_STOP_POINT_EXT_ID, "-", 2)
                ))
                .join(INFRASTRUCTURE_NODES)
                .on(INFRASTRUCTURE_NODES.INFRASTRUCTURE_NODE_ID.eq(NETWORK_ROUTE_POINTS.INFRASTRUCTURE_NODE))
                .where(
                        NETWORK_ROUTE_DIRECTIONS.NETWORK_ROUTE_DIRECTION_ID.eq(routeDirectionId),
                        NETWORK_ROUTE_DIRECTIONS.NETWORK_ROUTE_TRANSMODEL_ID.isNotNull()
                )
                .orderBy(NETWORK_ROUTE_POINTS.NETWORK_ROUTE_POINT_ORDER)
                .fetchStream()
                .map(record -> ExportableRoutePoint.from(
                        record.component5(),
                        record.component4(),
                        Optional.ofNullable(record.component6()),
                        NodeType.of(record.component7()),
                        Optional.ofNullable(record.component2()),
                        Optional.ofNullable(record.component3())
                )))
                .toList();
    }
}
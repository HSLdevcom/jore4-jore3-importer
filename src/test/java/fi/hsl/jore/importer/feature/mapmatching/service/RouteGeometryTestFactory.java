package fi.hsl.jore.importer.feature.mapmatching.service;

import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRouteGeometry;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRoutePoint;
import fi.hsl.jore.importer.util.GeometryUtil;
import io.vavr.collection.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

import java.util.Optional;
import java.util.UUID;

public class RouteGeometryTestFactory {

    public static final UUID ROUTE_DIRECTION_ID = UUID.fromString("0269ee59-730b-499a-8cf4-bef6f7923b3c");
    public static final String ROUTE_DIRECTION_EXT_ID = "1001-2-20211004";
    public static final UUID ROUTE_TRANSMODEL_ID = UUID.randomUUID();

    public static final double REQUEST_ROUTE_GEOMETRY_START_X = 5;
    public static final double REQUEST_ROUTE_GEOMETRY_START_Y = 6;
    public static final double REQUEST_ROUTE_GEOMETRY_END_X = 10;
    public static final double REQUEST_ROUTE_GEOMETRY_END_Y = 11;

    public static final double REQUEST_ROUTE_JUNCTION_POINT_X = 3.212;
    public static final double REQUEST_ROUTE_JUNCTION_POINT_Y = 1.2334;

    public static final Long REQUEST_STOP_POINT_ELY_NUMBER = 123567L;
    public static final String REQUEST_STOP_POINT_SHORT_ID = "H1234";

    public static final double REQUEST_ROUTE_STOP_POINT_POINT_X = 4.5533;
    public static final double REQUEST_ROUTE_STOP_POINT_POINT_Y = 10.3343;

    public static final double REQUEST_ROUTE_STOP_POINT_PROJECTED_POINT_X = 5.604;
    public static final double REQUEST_ROUTE_STOP_POINT_PROJECTED_POINT_Y = 12.053;
    
    /**
     * Prevent instantiation.
     */
    private RouteGeometryTestFactory() {}

    public static ImporterRouteGeometry createRouteGeometry() {
        final CoordinateSequence coordinates = new CoordinateArraySequence(new Coordinate[]{
                new Coordinate(REQUEST_ROUTE_GEOMETRY_START_X, REQUEST_ROUTE_GEOMETRY_START_Y),
                new Coordinate(REQUEST_ROUTE_GEOMETRY_END_X, REQUEST_ROUTE_GEOMETRY_END_Y)
        });
        final LineString routeLineString = new LineString(coordinates,
                GeometryUtil.factoryForSrid(GeometryUtil.SRID_WGS84)
        );

        return ImporterRouteGeometry.from(routeLineString,
                ROUTE_DIRECTION_ID,
                ROUTE_DIRECTION_EXT_ID,
                ROUTE_TRANSMODEL_ID
        );
    }

    public static List<ImporterRoutePoint> createRoutePoints() {


        return List.of(
                createStopPoint(),
                createJunctionPoint()
        );
    }

    private static ImporterRoutePoint createStopPoint() {
        final CoordinateSequence locationCoordinates = new CoordinateArraySequence(new Coordinate[]{
                new Coordinate(REQUEST_ROUTE_STOP_POINT_POINT_X, REQUEST_ROUTE_STOP_POINT_POINT_Y)
        });
        final Point location = new Point(
                locationCoordinates,
                GeometryUtil.factoryForSrid(GeometryUtil.SRID_WGS84)
        );

        final CoordinateSequence projectedLocationCoordinates = new CoordinateArraySequence(new Coordinate[]{
                new Coordinate(REQUEST_ROUTE_STOP_POINT_PROJECTED_POINT_X, REQUEST_ROUTE_STOP_POINT_PROJECTED_POINT_Y)
        });
        final Point projectedLocation = new Point(
                projectedLocationCoordinates,
                GeometryUtil.factoryForSrid(GeometryUtil.SRID_WGS84)
        );

        return ImporterRoutePoint.from(
                location,
                1,
                Optional.of(projectedLocation),
                NodeType.STOP,
                Optional.of(REQUEST_STOP_POINT_ELY_NUMBER),
                Optional.of(REQUEST_STOP_POINT_SHORT_ID)
        );
    }

    private static ImporterRoutePoint createJunctionPoint() {
        final CoordinateSequence locationCoordinates = new CoordinateArraySequence(new Coordinate[]{
                new Coordinate(REQUEST_ROUTE_JUNCTION_POINT_X, REQUEST_ROUTE_JUNCTION_POINT_Y)
        });
        final Point location = new Point(
                locationCoordinates,
                GeometryUtil.factoryForSrid(GeometryUtil.SRID_WGS84)
        );

        return ImporterRoutePoint.from(location,
                2,
                Optional.empty(),
                NodeType.CROSSROADS,
                Optional.empty(),
                Optional.empty()
        );
    }
}

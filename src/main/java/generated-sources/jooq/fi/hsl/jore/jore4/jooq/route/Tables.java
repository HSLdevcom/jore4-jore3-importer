/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.route;


import fi.hsl.jore.jore4.jooq.route.tables.Direction;
import fi.hsl.jore.jore4.jooq.route.tables.InfrastructureLinkAlongRoute;
import fi.hsl.jore.jore4.jooq.route.tables.Line;
import fi.hsl.jore.jore4.jooq.route.tables.Route;


/**
 * Convenience access to all tables in route.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The route directions from Transmodel:
     * https://www.transmodel-cen.eu/model/index.htm?goto=2:1:3:480
     */
    public static final Direction DIRECTION = Direction.DIRECTION;

    /**
     * The infrastructure links along which the routes are defined.
     */
    public static final InfrastructureLinkAlongRoute INFRASTRUCTURE_LINK_ALONG_ROUTE = InfrastructureLinkAlongRoute.INFRASTRUCTURE_LINK_ALONG_ROUTE;

    /**
     * The line from Transmodel:
     * http://www.transmodel-cen.eu/model/index.htm?goto=2:1:3:487
     */
    public static final Line LINE = Line.LINE;

    /**
     * The routes from Transmodel:
     * https://www.transmodel-cen.eu/model/index.htm?goto=2:1:3:483
     */
    public static final Route ROUTE_ = Route.ROUTE_;
}

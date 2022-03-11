/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.route;


import fi.hsl.jore.jore4.jooq.DefaultCatalog;
import fi.hsl.jore.jore4.jooq.route.tables.Direction;
import fi.hsl.jore.jore4.jooq.route.tables.InfrastructureLinkAlongRoute;
import fi.hsl.jore.jore4.jooq.route.tables.Line;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Route extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>route</code>
     */
    public static final Route ROUTE = new Route();

    /**
     * The route directions from Transmodel:
     * https://www.transmodel-cen.eu/model/index.htm?goto=2:1:3:480
     */
    public final Direction DIRECTION = Direction.DIRECTION;

    /**
     * The infrastructure links along which the routes are defined.
     */
    public final InfrastructureLinkAlongRoute INFRASTRUCTURE_LINK_ALONG_ROUTE = InfrastructureLinkAlongRoute.INFRASTRUCTURE_LINK_ALONG_ROUTE;

    /**
     * The line from Transmodel:
     * http://www.transmodel-cen.eu/model/index.htm?goto=2:1:3:487
     */
    public final Line LINE = Line.LINE;

    /**
     * The routes from Transmodel:
     * https://www.transmodel-cen.eu/model/index.htm?goto=2:1:3:483
     */
    public final fi.hsl.jore.jore4.jooq.route.tables.Route ROUTE_ = fi.hsl.jore.jore4.jooq.route.tables.Route.ROUTE_;

    /**
     * No further instances allowed
     */
    private Route() {
        super("route", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Direction.DIRECTION,
            InfrastructureLinkAlongRoute.INFRASTRUCTURE_LINK_ALONG_ROUTE,
            Line.LINE,
            fi.hsl.jore.jore4.jooq.route.tables.Route.ROUTE_
        );
    }
}

/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.importer.jooq.network;


import fi.hsl.jore.importer.jooq.DefaultCatalog;
import fi.hsl.jore.importer.jooq.network.tables.NetworkDirectionTypes;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLineHeaders;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLineHeadersHistory;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLineHeadersStaging;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLineHeadersWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLines;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLinesHistory;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLinesStaging;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLinesWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.NetworkPlaces;
import fi.hsl.jore.importer.jooq.network.tables.NetworkPlacesHistory;
import fi.hsl.jore.importer.jooq.network.tables.NetworkPlacesStaging;
import fi.hsl.jore.importer.jooq.network.tables.NetworkPlacesWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirections;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirectionsHistory;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirectionsStaging;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirectionsWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteLinks;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteLinksHistory;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteLinksStaging;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteLinksWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutePoints;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutePointsHistory;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutePointsStaging;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutePointsWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteStopPoints;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteStopPointsHistory;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteStopPointsStaging;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRouteStopPointsWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutes;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutesHistory;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutesStaging;
import fi.hsl.jore.importer.jooq.network.tables.NetworkRoutesWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPoints;
import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPointsHistory;
import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPointsStaging;
import fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPointsWithHistory;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Network extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>network</code>
     */
    public static final Network NETWORK = new Network();

    /**
     * The table <code>network.network_direction_types</code>.
     */
    public final NetworkDirectionTypes NETWORK_DIRECTION_TYPES = NetworkDirectionTypes.NETWORK_DIRECTION_TYPES;

    /**
     * The table <code>network.network_line_headers</code>.
     */
    public final NetworkLineHeaders NETWORK_LINE_HEADERS = NetworkLineHeaders.NETWORK_LINE_HEADERS;

    /**
     * The table <code>network.network_line_headers_history</code>.
     */
    public final NetworkLineHeadersHistory NETWORK_LINE_HEADERS_HISTORY = NetworkLineHeadersHistory.NETWORK_LINE_HEADERS_HISTORY;

    /**
     * The table <code>network.network_line_headers_staging</code>.
     */
    public final NetworkLineHeadersStaging NETWORK_LINE_HEADERS_STAGING = NetworkLineHeadersStaging.NETWORK_LINE_HEADERS_STAGING;

    /**
     * The table <code>network.network_line_headers_with_history</code>.
     */
    public final NetworkLineHeadersWithHistory NETWORK_LINE_HEADERS_WITH_HISTORY = NetworkLineHeadersWithHistory.NETWORK_LINE_HEADERS_WITH_HISTORY;

    /**
     * The table <code>network.network_lines</code>.
     */
    public final NetworkLines NETWORK_LINES = NetworkLines.NETWORK_LINES;

    /**
     * The table <code>network.network_lines_history</code>.
     */
    public final NetworkLinesHistory NETWORK_LINES_HISTORY = NetworkLinesHistory.NETWORK_LINES_HISTORY;

    /**
     * The table <code>network.network_lines_staging</code>.
     */
    public final NetworkLinesStaging NETWORK_LINES_STAGING = NetworkLinesStaging.NETWORK_LINES_STAGING;

    /**
     * The table <code>network.network_lines_with_history</code>.
     */
    public final NetworkLinesWithHistory NETWORK_LINES_WITH_HISTORY = NetworkLinesWithHistory.NETWORK_LINES_WITH_HISTORY;

    /**
     * The table <code>network.network_places</code>.
     */
    public final NetworkPlaces NETWORK_PLACES = NetworkPlaces.NETWORK_PLACES;

    /**
     * The table <code>network.network_places_history</code>.
     */
    public final NetworkPlacesHistory NETWORK_PLACES_HISTORY = NetworkPlacesHistory.NETWORK_PLACES_HISTORY;

    /**
     * The table <code>network.network_places_staging</code>.
     */
    public final NetworkPlacesStaging NETWORK_PLACES_STAGING = NetworkPlacesStaging.NETWORK_PLACES_STAGING;

    /**
     * The table <code>network.network_places_with_history</code>.
     */
    public final NetworkPlacesWithHistory NETWORK_PLACES_WITH_HISTORY = NetworkPlacesWithHistory.NETWORK_PLACES_WITH_HISTORY;

    /**
     * The table <code>network.network_route_directions</code>.
     */
    public final NetworkRouteDirections NETWORK_ROUTE_DIRECTIONS = NetworkRouteDirections.NETWORK_ROUTE_DIRECTIONS;

    /**
     * The table <code>network.network_route_directions_history</code>.
     */
    public final NetworkRouteDirectionsHistory NETWORK_ROUTE_DIRECTIONS_HISTORY = NetworkRouteDirectionsHistory.NETWORK_ROUTE_DIRECTIONS_HISTORY;

    /**
     * The table <code>network.network_route_directions_staging</code>.
     */
    public final NetworkRouteDirectionsStaging NETWORK_ROUTE_DIRECTIONS_STAGING = NetworkRouteDirectionsStaging.NETWORK_ROUTE_DIRECTIONS_STAGING;

    /**
     * The table <code>network.network_route_directions_with_history</code>.
     */
    public final NetworkRouteDirectionsWithHistory NETWORK_ROUTE_DIRECTIONS_WITH_HISTORY = NetworkRouteDirectionsWithHistory.NETWORK_ROUTE_DIRECTIONS_WITH_HISTORY;

    /**
     * The table <code>network.network_route_links</code>.
     */
    public final NetworkRouteLinks NETWORK_ROUTE_LINKS = NetworkRouteLinks.NETWORK_ROUTE_LINKS;

    /**
     * The table <code>network.network_route_links_history</code>.
     */
    public final NetworkRouteLinksHistory NETWORK_ROUTE_LINKS_HISTORY = NetworkRouteLinksHistory.NETWORK_ROUTE_LINKS_HISTORY;

    /**
     * The table <code>network.network_route_links_staging</code>.
     */
    public final NetworkRouteLinksStaging NETWORK_ROUTE_LINKS_STAGING = NetworkRouteLinksStaging.NETWORK_ROUTE_LINKS_STAGING;

    /**
     * The table <code>network.network_route_links_with_history</code>.
     */
    public final NetworkRouteLinksWithHistory NETWORK_ROUTE_LINKS_WITH_HISTORY = NetworkRouteLinksWithHistory.NETWORK_ROUTE_LINKS_WITH_HISTORY;

    /**
     * The table <code>network.network_route_points</code>.
     */
    public final NetworkRoutePoints NETWORK_ROUTE_POINTS = NetworkRoutePoints.NETWORK_ROUTE_POINTS;

    /**
     * The table <code>network.network_route_points_history</code>.
     */
    public final NetworkRoutePointsHistory NETWORK_ROUTE_POINTS_HISTORY = NetworkRoutePointsHistory.NETWORK_ROUTE_POINTS_HISTORY;

    /**
     * The table <code>network.network_route_points_staging</code>.
     */
    public final NetworkRoutePointsStaging NETWORK_ROUTE_POINTS_STAGING = NetworkRoutePointsStaging.NETWORK_ROUTE_POINTS_STAGING;

    /**
     * The table <code>network.network_route_points_with_history</code>.
     */
    public final NetworkRoutePointsWithHistory NETWORK_ROUTE_POINTS_WITH_HISTORY = NetworkRoutePointsWithHistory.NETWORK_ROUTE_POINTS_WITH_HISTORY;

    /**
     * The table <code>network.network_route_stop_points</code>.
     */
    public final NetworkRouteStopPoints NETWORK_ROUTE_STOP_POINTS = NetworkRouteStopPoints.NETWORK_ROUTE_STOP_POINTS;

    /**
     * The table <code>network.network_route_stop_points_history</code>.
     */
    public final NetworkRouteStopPointsHistory NETWORK_ROUTE_STOP_POINTS_HISTORY = NetworkRouteStopPointsHistory.NETWORK_ROUTE_STOP_POINTS_HISTORY;

    /**
     * The table <code>network.network_route_stop_points_staging</code>.
     */
    public final NetworkRouteStopPointsStaging NETWORK_ROUTE_STOP_POINTS_STAGING = NetworkRouteStopPointsStaging.NETWORK_ROUTE_STOP_POINTS_STAGING;

    /**
     * The table <code>network.network_route_stop_points_with_history</code>.
     */
    public final NetworkRouteStopPointsWithHistory NETWORK_ROUTE_STOP_POINTS_WITH_HISTORY = NetworkRouteStopPointsWithHistory.NETWORK_ROUTE_STOP_POINTS_WITH_HISTORY;

    /**
     * The table <code>network.network_routes</code>.
     */
    public final NetworkRoutes NETWORK_ROUTES = NetworkRoutes.NETWORK_ROUTES;

    /**
     * The table <code>network.network_routes_history</code>.
     */
    public final NetworkRoutesHistory NETWORK_ROUTES_HISTORY = NetworkRoutesHistory.NETWORK_ROUTES_HISTORY;

    /**
     * The table <code>network.network_routes_staging</code>.
     */
    public final NetworkRoutesStaging NETWORK_ROUTES_STAGING = NetworkRoutesStaging.NETWORK_ROUTES_STAGING;

    /**
     * The table <code>network.network_routes_with_history</code>.
     */
    public final NetworkRoutesWithHistory NETWORK_ROUTES_WITH_HISTORY = NetworkRoutesWithHistory.NETWORK_ROUTES_WITH_HISTORY;

    /**
     * The table <code>network.scheduled_stop_points</code>.
     */
    public final ScheduledStopPoints SCHEDULED_STOP_POINTS = ScheduledStopPoints.SCHEDULED_STOP_POINTS;

    /**
     * The table <code>network.scheduled_stop_points_history</code>.
     */
    public final ScheduledStopPointsHistory SCHEDULED_STOP_POINTS_HISTORY = ScheduledStopPointsHistory.SCHEDULED_STOP_POINTS_HISTORY;

    /**
     * The table <code>network.scheduled_stop_points_staging</code>.
     */
    public final ScheduledStopPointsStaging SCHEDULED_STOP_POINTS_STAGING = ScheduledStopPointsStaging.SCHEDULED_STOP_POINTS_STAGING;

    /**
     * The table <code>network.scheduled_stop_points_with_history</code>.
     */
    public final ScheduledStopPointsWithHistory SCHEDULED_STOP_POINTS_WITH_HISTORY = ScheduledStopPointsWithHistory.SCHEDULED_STOP_POINTS_WITH_HISTORY;

    /**
     * No further instances allowed
     */
    private Network() {
        super("network", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            NetworkDirectionTypes.NETWORK_DIRECTION_TYPES,
            NetworkLineHeaders.NETWORK_LINE_HEADERS,
            NetworkLineHeadersHistory.NETWORK_LINE_HEADERS_HISTORY,
            NetworkLineHeadersStaging.NETWORK_LINE_HEADERS_STAGING,
            NetworkLineHeadersWithHistory.NETWORK_LINE_HEADERS_WITH_HISTORY,
            NetworkLines.NETWORK_LINES,
            NetworkLinesHistory.NETWORK_LINES_HISTORY,
            NetworkLinesStaging.NETWORK_LINES_STAGING,
            NetworkLinesWithHistory.NETWORK_LINES_WITH_HISTORY,
            NetworkPlaces.NETWORK_PLACES,
            NetworkPlacesHistory.NETWORK_PLACES_HISTORY,
            NetworkPlacesStaging.NETWORK_PLACES_STAGING,
            NetworkPlacesWithHistory.NETWORK_PLACES_WITH_HISTORY,
            NetworkRouteDirections.NETWORK_ROUTE_DIRECTIONS,
            NetworkRouteDirectionsHistory.NETWORK_ROUTE_DIRECTIONS_HISTORY,
            NetworkRouteDirectionsStaging.NETWORK_ROUTE_DIRECTIONS_STAGING,
            NetworkRouteDirectionsWithHistory.NETWORK_ROUTE_DIRECTIONS_WITH_HISTORY,
            NetworkRouteLinks.NETWORK_ROUTE_LINKS,
            NetworkRouteLinksHistory.NETWORK_ROUTE_LINKS_HISTORY,
            NetworkRouteLinksStaging.NETWORK_ROUTE_LINKS_STAGING,
            NetworkRouteLinksWithHistory.NETWORK_ROUTE_LINKS_WITH_HISTORY,
            NetworkRoutePoints.NETWORK_ROUTE_POINTS,
            NetworkRoutePointsHistory.NETWORK_ROUTE_POINTS_HISTORY,
            NetworkRoutePointsStaging.NETWORK_ROUTE_POINTS_STAGING,
            NetworkRoutePointsWithHistory.NETWORK_ROUTE_POINTS_WITH_HISTORY,
            NetworkRouteStopPoints.NETWORK_ROUTE_STOP_POINTS,
            NetworkRouteStopPointsHistory.NETWORK_ROUTE_STOP_POINTS_HISTORY,
            NetworkRouteStopPointsStaging.NETWORK_ROUTE_STOP_POINTS_STAGING,
            NetworkRouteStopPointsWithHistory.NETWORK_ROUTE_STOP_POINTS_WITH_HISTORY,
            NetworkRoutes.NETWORK_ROUTES,
            NetworkRoutesHistory.NETWORK_ROUTES_HISTORY,
            NetworkRoutesStaging.NETWORK_ROUTES_STAGING,
            NetworkRoutesWithHistory.NETWORK_ROUTES_WITH_HISTORY,
            ScheduledStopPoints.SCHEDULED_STOP_POINTS,
            ScheduledStopPointsHistory.SCHEDULED_STOP_POINTS_HISTORY,
            ScheduledStopPointsStaging.SCHEDULED_STOP_POINTS_STAGING,
            ScheduledStopPointsWithHistory.SCHEDULED_STOP_POINTS_WITH_HISTORY
        );
    }
}

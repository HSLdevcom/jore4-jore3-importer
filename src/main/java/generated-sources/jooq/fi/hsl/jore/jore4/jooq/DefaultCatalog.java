/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq;


import fi.hsl.jore.jore4.jooq.deleted.Deleted;
import fi.hsl.jore.jore4.jooq.infrastructure_network.InfrastructureNetwork;
import fi.hsl.jore.jore4.jooq.internal_route.InternalRoute;
import fi.hsl.jore.jore4.jooq.internal_service_pattern.InternalServicePattern;
import fi.hsl.jore.jore4.jooq.journey_pattern.JourneyPattern;
import fi.hsl.jore.jore4.jooq.reusable_components.ReusableComponents;
import fi.hsl.jore.jore4.jooq.route.Route;
import fi.hsl.jore.jore4.jooq.service_pattern.ServicePattern;

import java.util.Arrays;
import java.util.List;

import org.jooq.Schema;
import org.jooq.impl.CatalogImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DefaultCatalog extends CatalogImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_CATALOG</code>
     */
    public static final DefaultCatalog DEFAULT_CATALOG = new DefaultCatalog();

    /**
     * The schema <code>deleted</code>.
     */
    public final Deleted DELETED = Deleted.DELETED;

    /**
     * The schema <code>infrastructure_network</code>.
     */
    public final InfrastructureNetwork INFRASTRUCTURE_NETWORK = InfrastructureNetwork.INFRASTRUCTURE_NETWORK;

    /**
     * The schema <code>internal_route</code>.
     */
    public final InternalRoute INTERNAL_ROUTE = InternalRoute.INTERNAL_ROUTE;

    /**
     * The schema <code>internal_service_pattern</code>.
     */
    public final InternalServicePattern INTERNAL_SERVICE_PATTERN = InternalServicePattern.INTERNAL_SERVICE_PATTERN;

    /**
     * The schema <code>journey_pattern</code>.
     */
    public final JourneyPattern JOURNEY_PATTERN = JourneyPattern.JOURNEY_PATTERN;

    /**
     * The schema <code>reusable_components</code>.
     */
    public final ReusableComponents REUSABLE_COMPONENTS = ReusableComponents.REUSABLE_COMPONENTS;

    /**
     * The schema <code>route</code>.
     */
    public final Route ROUTE = Route.ROUTE;

    /**
     * The schema <code>service_pattern</code>.
     */
    public final ServicePattern SERVICE_PATTERN = ServicePattern.SERVICE_PATTERN;

    /**
     * No further instances allowed
     */
    private DefaultCatalog() {
        super("");
    }

    @Override
    public final List<Schema> getSchemas() {
        return Arrays.asList(
            Deleted.DELETED,
            InfrastructureNetwork.INFRASTRUCTURE_NETWORK,
            InternalRoute.INTERNAL_ROUTE,
            InternalServicePattern.INTERNAL_SERVICE_PATTERN,
            JourneyPattern.JOURNEY_PATTERN,
            ReusableComponents.REUSABLE_COMPONENTS,
            Route.ROUTE,
            ServicePattern.SERVICE_PATTERN
        );
    }
}

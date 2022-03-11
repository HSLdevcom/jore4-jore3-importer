/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.internal_route;


import fi.hsl.jore.jore4.jooq.DefaultCatalog;
import fi.hsl.jore.jore4.jooq.internal_route.tables.Route;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InternalRoute extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>internal_route</code>
     */
    public static final InternalRoute INTERNAL_ROUTE = new InternalRoute();

    /**
     * The table <code>internal_route.route</code>.
     */
    public final Route ROUTE = Route.ROUTE;

    /**
     * No further instances allowed
     */
    private InternalRoute() {
        super("internal_route", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Route.ROUTE
        );
    }
}

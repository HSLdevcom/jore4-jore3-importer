/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.deleted;


import fi.hsl.jore.jore4.jooq.DefaultCatalog;

import org.jooq.Catalog;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Deleted extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>deleted</code>
     */
    public static final Deleted DELETED = new Deleted();

    /**
     * No further instances allowed
     */
    private Deleted() {
        super("deleted", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }
}

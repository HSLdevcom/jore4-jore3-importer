/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.infrastructure_network;


import fi.hsl.jore.jore4.jooq.DefaultCatalog;
import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.Direction;
import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.ExternalSource;
import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.InfrastructureLink;
import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.ResolvePointToClosestLink;
import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.VehicleSubmodeOnInfrastructureLink;
import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.records.ResolvePointToClosestLinkRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InfrastructureNetwork extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>infrastructure_network</code>
     */
    public static final InfrastructureNetwork INFRASTRUCTURE_NETWORK = new InfrastructureNetwork();

    /**
     * The direction in which an e.g. infrastructure link can be traversed
     */
    public final Direction DIRECTION = Direction.DIRECTION;

    /**
     * An external source from which infrastructure network parts are imported
     */
    public final ExternalSource EXTERNAL_SOURCE = ExternalSource.EXTERNAL_SOURCE;

    /**
     * The infrastructure links, e.g. road or rail elements: https://www.transmodel-cen.eu/model/index.htm?goto=2:1:1:1:453
     */
    public final InfrastructureLink INFRASTRUCTURE_LINK = InfrastructureLink.INFRASTRUCTURE_LINK;

    /**
     * The table <code>infrastructure_network.resolve_point_to_closest_link</code>.
     */
    public final ResolvePointToClosestLink RESOLVE_POINT_TO_CLOSEST_LINK = ResolvePointToClosestLink.RESOLVE_POINT_TO_CLOSEST_LINK;

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static Result<ResolvePointToClosestLinkRecord> RESOLVE_POINT_TO_CLOSEST_LINK(
          Configuration configuration
        , Object geog
    ) {
        return configuration.dsl().selectFrom(fi.hsl.jore.jore4.jooq.infrastructure_network.tables.ResolvePointToClosestLink.RESOLVE_POINT_TO_CLOSEST_LINK.call(
              geog
        )).fetch();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static ResolvePointToClosestLink RESOLVE_POINT_TO_CLOSEST_LINK(
          Object geog
    ) {
        return fi.hsl.jore.jore4.jooq.infrastructure_network.tables.ResolvePointToClosestLink.RESOLVE_POINT_TO_CLOSEST_LINK.call(
              geog
        );
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static ResolvePointToClosestLink RESOLVE_POINT_TO_CLOSEST_LINK(
          Field<Object> geog
    ) {
        return fi.hsl.jore.jore4.jooq.infrastructure_network.tables.ResolvePointToClosestLink.RESOLVE_POINT_TO_CLOSEST_LINK.call(
              geog
        );
    }

    /**
     * Which infrastructure links are safely traversed by which vehicle submodes?
     */
    public final VehicleSubmodeOnInfrastructureLink VEHICLE_SUBMODE_ON_INFRASTRUCTURE_LINK = VehicleSubmodeOnInfrastructureLink.VEHICLE_SUBMODE_ON_INFRASTRUCTURE_LINK;

    /**
     * No further instances allowed
     */
    private InfrastructureNetwork() {
        super("infrastructure_network", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            Direction.DIRECTION,
            ExternalSource.EXTERNAL_SOURCE,
            InfrastructureLink.INFRASTRUCTURE_LINK,
            ResolvePointToClosestLink.RESOLVE_POINT_TO_CLOSEST_LINK,
            VehicleSubmodeOnInfrastructureLink.VEHICLE_SUBMODE_ON_INFRASTRUCTURE_LINK);
    }
}

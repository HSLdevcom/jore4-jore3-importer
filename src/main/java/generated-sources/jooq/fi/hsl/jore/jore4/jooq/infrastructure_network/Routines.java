/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.infrastructure_network;


import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.FindPointDirectionOnLink;
import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.ResolvePointToClosestLink;
import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.records.FindPointDirectionOnLinkRecord;
import fi.hsl.jore.jore4.jooq.infrastructure_network.tables.records.ResolvePointToClosestLinkRecord;

import java.util.UUID;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Result;


/**
 * Convenience access to all stored procedures and functions in infrastructure_network.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Routines {

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static Result<FindPointDirectionOnLinkRecord> findPointDirectionOnLink(
          Configuration configuration
        , Object pointOfInterest
        , UUID infrastructureLinkUuid
        , Double pointMaxDistanceInMeters
    ) {
        return configuration.dsl().selectFrom(fi.hsl.jore.jore4.jooq.infrastructure_network.tables.FindPointDirectionOnLink.FIND_POINT_DIRECTION_ON_LINK.call(
              pointOfInterest
            , infrastructureLinkUuid
            , pointMaxDistanceInMeters
        )).fetch();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static FindPointDirectionOnLink findPointDirectionOnLink(
          Object pointOfInterest
        , UUID infrastructureLinkUuid
        , Double pointMaxDistanceInMeters
    ) {
        return fi.hsl.jore.jore4.jooq.infrastructure_network.tables.FindPointDirectionOnLink.FIND_POINT_DIRECTION_ON_LINK.call(
              pointOfInterest
            , infrastructureLinkUuid
            , pointMaxDistanceInMeters
        );
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static FindPointDirectionOnLink findPointDirectionOnLink(
          Field<Object> pointOfInterest
        , Field<UUID> infrastructureLinkUuid
        , Field<Double> pointMaxDistanceInMeters
    ) {
        return fi.hsl.jore.jore4.jooq.infrastructure_network.tables.FindPointDirectionOnLink.FIND_POINT_DIRECTION_ON_LINK.call(
              pointOfInterest
            , infrastructureLinkUuid
            , pointMaxDistanceInMeters
        );
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify how this type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public static Result<ResolvePointToClosestLinkRecord> resolvePointToClosestLink(
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
    public static ResolvePointToClosestLink resolvePointToClosestLink(
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
    public static ResolvePointToClosestLink resolvePointToClosestLink(
          Field<Object> geog
    ) {
        return fi.hsl.jore.jore4.jooq.infrastructure_network.tables.ResolvePointToClosestLink.RESOLVE_POINT_TO_CLOSEST_LINK.call(
              geog
        );
    }
}

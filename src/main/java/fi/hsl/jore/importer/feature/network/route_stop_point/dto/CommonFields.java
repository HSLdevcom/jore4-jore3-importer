package fi.hsl.jore.importer.feature.network.route_stop_point.dto;

import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;

import java.util.Optional;

public interface CommonFields
        extends IHasExternalId {

    // 0-indexed
    int orderNumber();

    boolean hastusStopPoint();

    Optional<Integer> timetableColumn();
}

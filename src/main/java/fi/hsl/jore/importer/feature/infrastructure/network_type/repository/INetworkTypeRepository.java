package fi.hsl.jore.importer.feature.infrastructure.network_type.repository;

import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;

public interface INetworkTypeRepository {
    void createIfMissing(NetworkType type);
}

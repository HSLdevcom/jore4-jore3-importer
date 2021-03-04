package fi.hsl.jore.importer.feature.infrastructure.network_type.repository;

import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.generated.NetworkTypePK;

public interface INetworkTypeRepository {
    NetworkTypePK findOrCreate(NetworkType type);
}

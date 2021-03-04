package fi.hsl.jore.importer.feature.infrastructure.network_type.repository;

import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.generated.NetworkTypePK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNetworkTypes;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Repository
public class NetworkTypeRepository
        implements INetworkTypeRepository {

    private static final InfrastructureNetworkTypes TYPES = InfrastructureNetworkTypes.INFRASTRUCTURE_NETWORK_TYPES;

    private final DSLContext db;

    @Autowired
    public NetworkTypeRepository(final DSLContext db) {
        this.db = db;
    }

    @Transactional
    public NetworkTypePK findOrCreate(final NetworkType type) {
        db.insertInto(TYPES)
          .columns(TYPES.INFRASTRUCTURE_NETWORK_TYPE_NAME)
          .values(type.getLabel())
          .onConflictDoNothing()
          .execute();

        return NetworkTypePK.of(
                Objects.requireNonNull(
                        db.select(TYPES.INFRASTRUCTURE_NETWORK_TYPE_ID)
                          .from(TYPES)
                          .where(TYPES.INFRASTRUCTURE_NETWORK_TYPE_NAME.eq(type.getLabel()))
                          .fetchOne(TYPES.INFRASTRUCTURE_NETWORK_TYPE_ID)
                )
        );
    }
}
